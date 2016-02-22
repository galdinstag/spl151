package reit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Statistics:
 * This class keeps simulator's given-time statistics about:
 * 1) how much money was gained soo far
 * 2) amount of tools and materials used soo far
 * 3) fulfilled rental requests and their information
 *
 * to our enjoyment
 *
 * Created by airbag on 12/9/14.
 */
public class Statistics {
    private Warehouse warehouse;
    private AtomicInteger MoneyGained;
    private HashMap<String,RentalRequest> rentalRequestsFulfilled;

    /**
     * default constructor
     * @param warehouse - the main warehouse of the program which we who's statistics we'll need to print statistics
     */
    public Statistics(Warehouse warehouse){
        this.warehouse = warehouse;
        rentalRequestsFulfilled = new HashMap<String, RentalRequest>();
        MoneyGained = new AtomicInteger(0);
    }

    /**
     *
     * @param id- the id of the request fulfilled
     * @param request- the request fulfilled
     */
    public synchronized void addFulfilledRequest(String id, RentalRequest request) {
        rentalRequestsFulfilled.put(id,request);
    }


    /**
     *
     * @return- StringBulder of fulfilled requests and their information
     */
    public StringBuilder showFulfilledRentalRequests(){
        StringBuilder requests = new StringBuilder();
        for(Map.Entry<String,RentalRequest> entry : rentalRequestsFulfilled.entrySet()){
            requests.append("\n" + "request id: " + entry.getKey() + " was fulfilled by the asset: " + entry.getValue().getAssetName() + "\n");
        }
        return requests;
    }

    /**
     *
     * @param income- the income needed to sum for the total income of the program
     */
    public void addIncome(int income){
        MoneyGained.addAndGet(income);
    }

    /**
     * prints the current: 1) total money gained during the simulator
     *                     2) the amount of tools and materials used so far in the simulator
     *                     3) the information of the requests fulfilled so far
     */


    public String toString(){
        StringBuilder currentStatistics = new StringBuilder();
        currentStatistics.append("Current income summed is " + MoneyGained.toString() + "NIS" + "\n\n");
        currentStatistics.append(warehouse.WarehouseStatistics());
        currentStatistics.append(showFulfilledRentalRequests());

        return new String(currentStatistics);
    }
}
