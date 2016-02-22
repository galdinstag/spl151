package reit;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Assets:
 * This class holds a collection of Asset objects, providing methods for searching suitable assets,
 * submitting damageReports & retrieving a collection of damaged assets.
 * @version 1.0
 *
 */
class Assets {

    private final ArrayList<Asset> assets;
    private final BlockingQueue<DamageReport> damageReports;
    private final Object assetLock;

    Assets() {
        this.assets = new ArrayList<Asset>();
        assetLock = new Object();
        this.damageReports = new LinkedBlockingQueue<DamageReport>();
    }

    void addAsset(Asset asset) {
        assets.add(asset);
    }

    /**
     * @param rentalRequest - RentalRequest object that holds the requirements for Asset.
     * @param clerk - ClerkDetails object representing the clerk calling this method.
     * @return Asset object fitting requirements specified in rental request.
     */
    Asset find(RentalRequest rentalRequest, ClerkDetails clerk) {
        boolean assetFound = false;
        Asset suitableAsset = null;
        synchronized (assetLock) {
            while (!assetFound) { // while no suitable asset has been found
                for (Asset asset : assets) { // look for assets
                    if (rentalRequest.isSuitable(asset)) { // if suitable asset has been found
                        assetFound = true;
                        return asset; // TODO: UGLY HACK.
                    }
                }
                try { // no available assets have been found
                    Management.logger.info(clerk.getName() + " couldn't find a suitable asset. WAITING...");
                    assetLock.wait(); // so we wait.
                    Management.logger.info("NOTIFY ALL CLERKS! AN ASSET HAS BEEN VACATED!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return suitableAsset;
    }

    /**
     * @param damageReport - DamageReport object to be submitted.
     */
    void submitDamageReport(DamageReport damageReport) { // adds another damageReport to
        damageReports.add(damageReport);
        synchronized (assetLock) {
            try {
                assetLock.notifyAll(); // let everyone know a new asset is now available
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Asset> getDamagedAssets() {
        ArrayList<Asset> damagedProperty = new ArrayList<Asset>();

        for (Asset asset : assets) {
            if (asset.isWrecked())
                damagedProperty.add(asset);
        }
        return damagedProperty;
    }

    /**
     * applyDamage:
     * This method applies damage to Assets that have an active DamageReport.
     */
    void applyDamage() { // iterate over damage reports, inflict damage
        for (DamageReport damageReport : damageReports) {
            damageReport.inflictDamage();
        }

        damageReports.clear();
    }


    /**
     *
     * @return - A string containing toString() of all the assets that belongs to Asset
     */
    public String toString() {
        StringBuilder output = new StringBuilder("::Asset Report::\n");

        for (Asset asset : assets) {
            output.append(asset).append("\n");
        }
        return output.toString();
    }
}