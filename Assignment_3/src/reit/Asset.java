package reit;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Asset:
 * This class represents an Asset object, with mandatory fields, as defined in the assignment documentation.
 * @version 1.0
 *
 */
public class Asset {

    private final String name;
    private final String type;
    private final Location location;
    private final ArrayList<AssetContent> assetContentContainer;
    private enum Status {AVAILABLE, BOOKED, OCCUPIED, UNAVAILABLE}
    private Status status;
    private final int costPerNight;
    private final int size;

    /**
     * main constructor, build in the parsing stage and stored in Assets
     * @param name
     * @param type
     * @param location
     * @param costPerNight
     * @param size
     */
    Asset(String name, String type, Location location,
                 int costPerNight, int size) {
        this.name = name;
        this.type = type;
        this.location = location;
        assetContentContainer = new ArrayList<AssetContent>();
        status = Status.AVAILABLE;
        this.costPerNight = costPerNight;
        this.size = size;
    }

    /**
     * adding contents to the contents ArrayList
     * @param name
     * @param repairMultiplier
     */
    public void addContent(String name, double repairMultiplier) {
        assetContentContainer.add(new AssetContent(name, repairMultiplier));
    }

    /**
     *
     * @return - is the asset wracked?
     */
    boolean isWrecked() {
        return (status == Status.UNAVAILABLE);
    }

    /**
     * Raise the damage to all Asset's contents following the rent
     * @param percentage - the percentage of damaged need to be done to each content
     */
    synchronized void updateDamage(double percentage) {

        for (AssetContent assetContent : assetContentContainer) {
            assetContent.breakAsset(percentage);
        }
        checkHealth();
    }

    /**
     * 1)Fixes all of the contents (raise health to 100)
     * 2)Marks the asset as "AVAILABLE"
     */
    synchronized void repairAsset() {
        for (AssetContent assetContent : assetContentContainer) {
            assetContent.fix();
        }
        status = Status.AVAILABLE; // making asset available
        Management.logger.info(getName() + " has been repaired.");
    }

    /**
     * Calculates the total sleep time needed to fix all contents.
     * @return - the sleep time.
     */
    long timeToFix() {
        double timeToFix = 0;

        for (AssetContent assetContent : assetContentContainer) {
            timeToFix += assetContent.timeToFix();
        }

        return Math.round(timeToFix);
    }

    /**
     * After contents has been wracked, checks if the asset's still habitable, if not marks it as "UNAVAILABLE"
     */
    private void checkHealth() {
        boolean stillAvailable = true;
        ListIterator<AssetContent> it = assetContentContainer.listIterator();

        while (it.hasNext() && stillAvailable) {
            if (it.next().isBroken()) {
                status = Status.UNAVAILABLE;
                stillAvailable = false;
            }
        }
    }

    /**
     * @return - A string that represents the damaged contents in the Asset
     */
    String whatsDamaged() {
        String damagedGoods = "";

        for (AssetContent assetContent : assetContentContainer) {
            damagedGoods += assetContent.getName() + ",";
        }
        if (damagedGoods.length() > 0) {
            damagedGoods = damagedGoods.substring(0, damagedGoods.length() - 1); // remove pesky trailing comma
        }
        return damagedGoods;
    }

    /**
     * Chackes if the asset is suitable for a given rental request
     * @param type
     * @param size
     * @return - well, is the asset suitable or not?
     */
    boolean isSuitable(String type, int size) {
        return (this.type.equals(type) &&
                this.size >= size &&
                this.status.equals(Status.AVAILABLE));
    }

    /**
     * Calculates the distance the clerk has to walk to get to the asset
     * @param location
     * @return - the distance of the asset from the clerk
     */
    long distanceToClerk(Location location) {
        return (Math.round(this.location.calculateDistance(location)));
    }

    /**
     * Change the status of the asset to "BOOKED" (means the clerk just found this asset and the group can enter)
     */
    synchronized void book() {
        status = Status.BOOKED;
    }

    /**
     * Change the status of the asset to "OCCUPIED" (means a group is about to enter)
     */
    synchronized void occupy() {
        status = Status.OCCUPIED;
    }

    /**
     * Change the status of the asset to "AVAILABLE" (means the rental ended and the house was fixed)
     */
    synchronized void vacate() {
        status = Status.AVAILABLE;
        Management.logger.info(getName() + " has been vacated.");
    }

    /**
     *
     * @return - A string representing all the contents of the asset
     */
    String listContent() {
        StringBuilder contentList = new StringBuilder();

        for (AssetContent content : assetContentContainer) {
            contentList.append(content).append("\n");
        }

        return contentList.toString();
    }

    /**
     *
     * @return - Asset's name
     */
    String getName() {
        return "[" + name + "]";
    }

    int getCostPerNight(){ return costPerNight;}

    /**
     *
     * @return - A string representing the state of the asset
     */
    public String toString() {
        return "[Name=" + name + "][Type=" + type + "][Size=" + size + "][Status=" + status.toString() + "]";
    }
}