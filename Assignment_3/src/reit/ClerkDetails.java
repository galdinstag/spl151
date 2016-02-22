package reit;

/**
 * ClerkDetails:
 * This is a Callable wrapper for Customer class. It's purpose is to simulate the duration of stay and damage
 * inflicted by a single Customer.
 * @version 1.0
 *
 */
class ClerkDetails {

    private final String name;
    private final Location location;

    /**
     * Constructor
     *
     * @param name
     * @param location
     */
    ClerkDetails(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    /**
     * Returns the distance between the clerk and a given asset (calculated by the asset)
     * @param asset - the given asset
     * @return - the distance between the clerk and the asset
     */
    long distanceToTravel(Asset asset) {
        return asset.distanceToClerk(location);
    }

    /**
     *
     * @return - dudes name
     */
    String getName() {
        return "[" + name + "]";
    }

    /**
     *
     * @return - A string representing the clerk and his location
     */
    public String toString() {
        return "[name=" + name + "][Location=" + location + "]";
    }
}
