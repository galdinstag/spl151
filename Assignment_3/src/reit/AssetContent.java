package reit;

/**
 * AssetContent:
 * This class represents a single asset content object.
 * @version 1.0
 */
class AssetContent {
    private final int DAMAGE_THRESHOLD = 65;
    private final String name;
    private double health;
    private final double repairCostMultiplier;

    /**
     * Constructor
     *
     * @param contentName
     * @param repairCostMultiplier
     */
    AssetContent(String contentName, double repairCostMultiplier) {
        name = contentName;
        health = 100;
        this.repairCostMultiplier = repairCostMultiplier;
    }

    /**
     *
     * @param percentage- the precentage of
     */
    void breakAsset(double percentage) {
        health -= percentage;
    }

    /**
     * "heal" the content
     */
    void fix() {
        health = 100;
    }

    /**
     *
     * @return - sleep time to fix the content
     */
    double timeToFix() {
        return (100 - health) * repairCostMultiplier;
    }

    /**
     *
     * @return - is the content broken?
     */
    boolean isBroken() {
        return health <= DAMAGE_THRESHOLD;
    }

    /**
     *
     * @return - content name
     */
    String getName() {
        return name;
    }

    /**
     *
     * @return - string that reflects the state of the content
     */
    public String toString() {
        return "[Content=" + name + "][Health=" + health + "][RepairMultiplier=" + repairCostMultiplier + "]";
    }
}