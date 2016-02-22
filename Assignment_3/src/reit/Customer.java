package reit;

/**
 * Created by airbag on 12/9/14.
 */
class Customer {

    // fields
    enum VandalismType { Arbitrary, Fixed, None }
    private final VandalismType vandalismType;
    private final String name;
    private final int minDamage;
    private final int maxDamage;

    Customer(String name, VandalismType vandalismType, int minDamage, int maxDamage) {
        this.name = name;
        this.vandalismType = vandalismType;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    double vandalize() {

        double damage = 0.5; // default wear&tear value

        switch (vandalismType) {

            case Arbitrary: // generate random number in minDamage - maxDamage range
                damage = minDamage + Math.random() * (maxDamage - minDamage);
                break;

            case Fixed: // calc average
                damage = (minDamage + maxDamage) / 2;
                break;

            case None: // leave default value unchanged
                break;
        }
        return damage;
    }

    String getName() {
        return name;
    }

    public String toString() {
        return "[Name=" + name +
                "][Vandalism=" + vandalismType.toString() +
                "][MinDamage=" + minDamage +
                "][MaxDamage=" + maxDamage +
                "]";
    }




}
