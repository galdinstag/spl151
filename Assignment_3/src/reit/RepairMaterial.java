package reit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RepairMaterial:
 *
 * A class that represents single material type (and it's quantity in the Warehouse)
 * That will be needed in order to fix contents
 *
 * Created by gal on 12/8/2014.
 */
class RepairMaterial implements Comparable<RepairMaterial> {
    private String materialName;
    private Semaphore quantity;
    private AtomicInteger totalAcquired;


    /**
     * Contractor
     *
     * @param materialName
     * @param quantity
     */
    RepairMaterial(String materialName, int quantity) {
        this.materialName = materialName;
        this.quantity = new Semaphore(quantity);
        totalAcquired = new AtomicInteger(0);

    }

    /**
     * Simulates taking a number of material objets from the warehouse
     * @param quantity - Number of material objects needed
     */
    void acquireMaterial (int quantity) {
        try {
            this.quantity.acquire(quantity);
            totalAcquired.addAndGet(quantity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return - The material name (dha...)
     */
    String getName() {
        return materialName;
    }

    /**
     *
     * @return - The total amount of acquires made soo far during the run of the simulator
     */
    public int getTotalAcquired(){ return totalAcquired.get();}

    /**
     *
     * @return - Number of objects still available in the warehouse
     */
    int getQuantity() {
        return quantity.availablePermits();
    }


    @Override
    /**
     * @return - Does the material comes before the other material? (lexicographically)
     */
    public int compareTo(RepairMaterial o) {
        return materialName.compareTo(o.materialName);
    }
}
