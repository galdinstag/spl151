package reit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Created by airbag on 12/9/14.
 */
public class CustomerGroupDetails {

    private final String groupManagerName;
    private final Queue<RentalRequest> rentalRequests;
    private final Vector<Customer> customers;


    public CustomerGroupDetails(String groupManagerName) {
        this.groupManagerName = groupManagerName;
        this.customers = new Vector<Customer>();
        this.rentalRequests = new LinkedList<RentalRequest>();
    }

    public void addCustomer(String name, String vandalismType, int minDamage, int maxDamage) {
        customers.add(new Customer(name,
                Customer.VandalismType.valueOf(vandalismType),
                minDamage,
                maxDamage));
    }

    public void addRentalRequest(String id, String type, int size, int duration) {
        rentalRequests.add(new RentalRequest(id, type, size, duration));

    }

    public RentalRequest pullRentalRequest() {
        if (!rentalRequests.isEmpty())
            return rentalRequests.remove();
        return null;
    }

    boolean isRequestsLeft() {
        return !rentalRequests.isEmpty();
    }

    Iterator<Customer> customerIterator() {
        return customers.iterator();
    }

    int numOfCustomers() {
        return customers.size();

    }

    String getName() {
        return "[" + groupManagerName + "]";
    }

    public String toString() {
        return "[" + groupManagerName + "]";
    }

}
