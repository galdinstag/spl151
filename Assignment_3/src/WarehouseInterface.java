/**
 * Created by gal on 12/8/2014.
 */

/**
 * A warehouse is an object designed to obtain different tools and materials (types of objects),
 * that are stored in internal data structors.
 * You can add tools and materials, rent or release a tool and take a material.
 *
 * @author Eugene Lerman and Gal Dinstag
 */
public interface WarehouseInterface {

    /**
     * add a repair tool to the warehouse data structor (this is a command)
     * @param toolName
     *                  a String
     * @param quantity
     *                  an int
     * @pre: none
     * @post: the data structor contains the tool
     */
    void addRepairTool(String toolName, int quantity);

    /**
     * add a repair material to the warehouse data structor (this is a command)
     * @param materialName
     *                      a String
     * @param quantity
     *                      an int
     * @pre: none
     * @post: the data structor contains the material
     */
    void addRepairMaterial(String materialName, int quantity);

    /**
     * decrease the amount of the tools from "toolName" in the container by "quantity" (this is a command)
     * @param toolName
     *                  a String
     * @param quantity
     *                  an int
     * @pre: "toolName" exist in warehouse (one of his data-structors)
     * @post: "toolName".currentQuantity = "toolName".currentQuantity - quantity
     */
    void rentATool(String toolName, int quantity);

    /**
     * decrease the amount of materials from "materialName" in the container by "quantity" (this is a command)
     *
     * @param materialName
     *                      a String
     * @param quantity
     *                      an int
     * @pre: "materialName" exists in warehouse (one of his data-structors)
     * @post: "materialName".currentQuantity = "materialName".currentQuantity - quantity
     */
    void takeMaterial(String materialName, int quantity);

    /**
     * increase the amount of the tools from "toolName" in the container by "quantity" (this is a command)
     * @param toolName
     *                  a String
     * @param quantity
     *                  an int
     * @pre: "toolName" exist in warehouse (one of his data-structors)
     * @post: "toolName".currentQuantity = "toolName".currentQuantity + quantity
     */
    void releaseTool(String toolName, int quantity);
}
