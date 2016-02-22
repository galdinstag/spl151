package reit;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by airbag on 12/8/14.
 */
public class Management {

    // fields
    public static final Logger logger = Logger.getLogger(Management.class.getName());
    public static final int DAYS_TO_MILLISECONDS = 24000;
    public static final int SEC_TO_MILL = 1000;

    private final Warehouse warehouse;
    private final Assets assets;

    private final HashMap<String, ArrayList<RepairMaterialInformation>> repairMaterialInformationMap;
    private final HashMap<String, ArrayList<RepairToolInformation>> repairToolInformationMap;

    private Statistics statistics;
    private final Semaphore reportSemaphore;
    private CyclicBarrier clerkShiftBarrier;
    private final AtomicInteger nUnhandledRequests;
    private int nUnhandledRequestsPerShift;
    private final Object beginNewShift;
    private final BlockingQueue<RentalRequest> rentalRequests;

    private final Vector<CustomerGroupDetails> customers;
    private final Vector<ClerkDetails> clerks;

    private int nMaintenanceWorkers;

    public Management(Warehouse warehouse) { // public constructor
        this.warehouse = warehouse;
        this.assets = new Assets();
        this.rentalRequests = new LinkedBlockingDeque<RentalRequest>();
        this.clerks = new Vector<ClerkDetails>();
        this.repairToolInformationMap = new HashMap<String, ArrayList<RepairToolInformation>>();
        this.repairMaterialInformationMap = new HashMap<String, ArrayList<RepairMaterialInformation>>();
        this.customers = new Vector<CustomerGroupDetails>();
        this.nUnhandledRequests = new AtomicInteger(0);
        this.nUnhandledRequestsPerShift = 0;
        this.nMaintenanceWorkers = 0;
        this.beginNewShift = new Object();
        this.reportSemaphore = new Semaphore(0);
        statistics = new Statistics(warehouse);
    }

    public void simulate() { // main simulation loop

        init();
        runCustomers();
        runClerks();

        while (nUnhandledRequests.get() > 0) {
            waitForReports(); // waiting until management acquires all damageReports
            updateDamage(); // iterate over acquired damage reports, update damage.
            beginMaintenanceShift(); // call maintenance
            beginNewShift(); // notify clerks a new shift has begun.
            System.out.println(statistics.toString()); //print statistics at the end of all shifts
        }
    }

    /**
     *
     * @param income = the income got from a single rental and should be summed with the rest of the bling-blings.
     */
    public void addIncomeToStatistics(int income){ statistics.addIncome(income);    }

    /**
     *
     * @param id - the id of the request fulfilled.
     * @param request - the request fulfilled.
     */
    public void addFulfilledRequestToStatistics(String id, RentalRequest request){ statistics.addFulfilledRequest(id,request);}

    public void addClerk(String name, Location location) {
        clerks.add(new ClerkDetails(name, location));
    }

    /**
     * @param name
     * @param type
     * @param size
     * @param location
     * @param cost
     * @return
     */
    public Asset addAsset (String name, String type, int size, Location location, int cost) {
        Asset asset = new Asset(name, type, location, cost, size);
        assets.addAsset(asset);
        return asset;
    }

    public CustomerGroupDetails addCustomerGroup(String groupManager) {
        CustomerGroupDetails customerGroupDetails = new CustomerGroupDetails(groupManager);
        customers.add(customerGroupDetails);
        return customerGroupDetails;
    }

    public void addNewContentRepairInformation(String content) {
        repairToolInformationMap.put(content, new ArrayList<RepairToolInformation>());
        repairMaterialInformationMap.put(content, new ArrayList<RepairMaterialInformation>());

    }

    public void addItemRepairMaterialInformation(String content,
                                                 String repairMaterialName,
                                                 int quantity) {
        repairMaterialInformationMap.get(content).add(new RepairMaterialInformation(repairMaterialName, quantity));
    }

    public void addItemRepairToolInformation(String content,
                                             String repairToolName,
                                             int quantity) {
        repairToolInformationMap.get(content).add(new RepairToolInformation(repairToolName, quantity));
    }


    public void setTotalNumberOfRentalRequests(int nRentalRequests) {
        this.nUnhandledRequests.set(nRentalRequests);
        this.nUnhandledRequestsPerShift = nRentalRequests;
    }

    public void setNumberOfMaintenanceWorkers(int nMaintenanceWorkers) {
        this.nMaintenanceWorkers = nMaintenanceWorkers;
    }

    public void submitDamageReport(DamageReport damageReport) {
        assets.submitDamageReport(damageReport); // submit report to Assets.
        reportSemaphore.release(1);
    }

    public void addRentalRequest(RentalRequest rentalRequest) {
        rentalRequests.add(rentalRequest);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("reit.Management: \n");

        for (CustomerGroupDetails customerGroupDetails : customers) {
            stringBuilder.append(customerGroupDetails).append("\n");
        }

        for (ClerkDetails clerkDetails : clerks) {
            stringBuilder.append(clerkDetails).append("\n");
        }

        for (SortedMap.Entry<String, ArrayList<RepairToolInformation>> entry : repairToolInformationMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" \tTools: [");
            for (RepairToolInformation repairToolInformation: entry.getValue()) {
                stringBuilder.append("<").append(repairToolInformation.getName()).append(",").append(repairToolInformation.getQuantity()).append(">");
            }
            stringBuilder.append("]\n");
        }
        return stringBuilder.toString();
    }

    private void init() {
        reportSemaphore.release(nUnhandledRequests.get()); // initialize reportSemaphore to # of rental requests
        nUnhandledRequestsPerShift = nUnhandledRequests.get(); // before the simulation begins, these are equal
    }

    private void runCustomers() {
        logger.info("MANAGEMENT: Initializing reit.Customer Groups...");
        for (CustomerGroupDetails customerGroup : customers) {
            new Thread(new RunnableCustomerGroupManager(this, customerGroup)).start();
        }
    }

    private void runClerks() {
        // ensuring we'll wait for all clerks to end their shift
        clerkShiftBarrier = new CyclicBarrier(clerks.size() + 1); // +1 for management
        logger.info("MANAGEMENT: Initializing RunnableClerks...");
        for (ClerkDetails clerk : clerks) {
            new Thread(new RunnableClerk(clerk, rentalRequests,
                    clerkShiftBarrier, assets,
                    nUnhandledRequests, reportSemaphore,
                    beginNewShift)).start();
        }
    }

    private void beginMaintenanceShift() {
        logger.info("MANAGEMENT: Maintenance shift begins.. Retrieving damaged asset list.");
        ArrayList<Asset> damagedAssets = assets.getDamagedAssets();
        // initialize latch. when this reaches zero, maintenance work is done.
        CountDownLatch maintenanceShiftLatch = new CountDownLatch(damagedAssets.size());

        ExecutorService maintenanceExecutor = Executors.newFixedThreadPool(nMaintenanceWorkers);
        logger.info("MANAGEMENT: Executing maintenance worker threads..");
        for (Asset asset : damagedAssets) {
            maintenanceExecutor.execute(new RunnableMaintenanceRequest(asset,
                    repairToolInformationMap,
                    repairMaterialInformationMap,
                    warehouse,
                    maintenanceShiftLatch));
        }

        try {
            maintenanceShiftLatch.await(); // waiting for maintenance workers to finish repairs

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        maintenanceExecutor.shutdown();
        logger.info("MAINTENANCE: Done! let's hit the bar!");
        // done. let's hit the bar!
    }

    private void updateDamage() {
        assets.applyDamage();
    }

    private void beginNewShift() {
        synchronized (beginNewShift) {
            logger.info("MANAGEMENT: CALLING ALL CLERKS!! NEW SHIFT HAS BEGUN!");
            beginNewShift.notifyAll();
        }
    }

    private void waitForReports() {

        try {
            clerkShiftBarrier.await(); // waiting for all clerks to finish their shift.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        // all clerks are off-duty.
        // draining the semaphore will provide us with the # of reports we should expect to be submitted.
        // immediately acquiring the drained amount will guarantee that only once all reports have been submitted,
        // maintenance will be called upon.

        int semaphoreCount = reportSemaphore.drainPermits();
        try {
            reportSemaphore.acquire(nUnhandledRequestsPerShift - semaphoreCount); // essentially emptying the semaphore

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nUnhandledRequestsPerShift = nUnhandledRequests.get();
        reportSemaphore.release(nUnhandledRequestsPerShift); // filling it up again, before clerks' next shift begins
    }

}
