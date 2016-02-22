package reit;

/**
 * RepairToolInformation:
 * A class that represents needed tool and its quantity in order to
 * repair one content.
 *
 * Created by airbag on 12/9/14.
 */
class RepairToolInformation implements Comparable<RepairToolInformation> {

    // fields
    private String name;
    private int quantity;

    /**
     * Constructor
     *
     * @param name
     * @param quantity
     */
    RepairToolInformation(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * @return - the tool's name
     */
    String getName() {
        return name;
    }

    /**
     * @return - the quantity of the tool's needed in order to repair the content
     */
    int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param o
     * @return - Does the tool comes before the other material? (lexicographically)
     */
    @Override
    public int compareTo(RepairToolInformation o) {
        if (name.compareTo(o.name) == 0) {
            return quantity - o.quantity;
        }
        return name.compareTo(o.name);
    }
}
