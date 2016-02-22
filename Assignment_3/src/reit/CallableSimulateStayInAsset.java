package reit;

import java.util.concurrent.Callable;

/**
 * CallableSimulateStayInAsset:
 * This is a Callable wrapper for Customer class. It's purpose is to simulate the duration of stay and damage
 * inflicted by a single Customer.
 * @version 1.0
 *
 */
class CallableSimulateStayInAsset implements Callable {

    // fields

    private final RentalRequest rentalRequest;
    private final Customer customer;

    CallableSimulateStayInAsset(RentalRequest rentalRequest, Customer customer) {
        this.rentalRequest = rentalRequest;
        this.customer = customer;
    }

    @Override
    public Object call() throws Exception {
        Thread.sleep(rentalRequest.stay()); // simulate stay
        return retrieveDamagePercentage();

    }

    // retrieve damage inflicted percentage,
    // calculated by customer.
    private Double retrieveDamagePercentage() {
        return customer.vandalize();
    }
}
