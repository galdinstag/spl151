package reit;

/**
 * RepairMaterialInformation:
 *
 * A class that represents needed material and its quantity in order to
 * repair one content.
 *
 * Created by airbag on 12/9/14.
 */
class RepairMaterialInformation implements Comparable<RepairMaterialInformation> {

    private String name;
    private int quantity;

    /**
     * Constructor
     *
     * @param name
     * @param quantity
     */
    RepairMaterialInformation(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * @return - The material's name
     */
    String getName() {
        return name;
    }

    /**
     * @return - the quantity of the material's needed in order to repair the content
     */
    int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param o
     * @return - Does the material comes before the other material? (lexicographically)
     */
    @Override
    public int compareTo(RepairMaterialInformation o) {
        if (name.compareTo(o.name) == 0) {
            return quantity - o.quantity;
        }
        return name.compareTo(o.name);
    }
}
