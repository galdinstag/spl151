package reit;

/**
 * Created by airbag on 12/9/14.
 */
class DamageReport {

    private final Asset asset;
    private final double damagePercent;

    DamageReport(Asset asset, double totalDamage) {
        this.asset = asset;
        this.damagePercent = totalDamage;
    }

    void inflictDamage() {
        asset.updateDamage(damagePercent);
    }

    public String toString() {
        return "[Asset=" + asset + "][Damage=" + damagePercent + "]";
    }
}
