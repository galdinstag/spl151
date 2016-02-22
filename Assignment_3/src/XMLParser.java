import reit.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Eugene on 27/12/2014.
 */
public final class XMLParser {

    private XMLParser() {
    }

    private static XMLStreamReader initializeReader(String xmlPath)
            throws FileNotFoundException, XMLStreamException { // helper method to parse XMLStreamReader object
        // initialize parser
        XMLInputFactory factory = XMLInputFactory.newInstance();

        return factory.createXMLStreamReader(new FileInputStream(xmlPath));
    }

    private static Location parseLocation (XMLStreamReader reader) { // helper method to parse Location object
        return new Location(Integer.parseInt(reader.getAttributeValue(0)),
                            Integer.parseInt(reader.getAttributeValue(1)));
    }

    public static Management parseInitialData(String initialDataXmlPath)
            throws FileNotFoundException, XMLStreamException {

        Warehouse warehouse = new Warehouse();
        Management management = new Management(warehouse);
        String name = "";
        int quantity = 0;

        XMLStreamReader reader = initializeReader(initialDataXmlPath);
        String elementContent = "";

        while (reader.hasNext()) {

            int event = reader.next();

            switch (event) {

                case XMLStreamConstants.START_ELEMENT:

                    String startElement = reader.getLocalName();

                    if ("Location".equals(startElement)) {
                        management.addClerk(name, parseLocation(reader));

                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    elementContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:

                    String endElement = reader.getLocalName();

                    if ("Tool".equals(endElement)) {
                        warehouse.addRepairTool(name, quantity);

                    }
                    if ("Material".equals(endElement)) {
                        warehouse.addRepairMaterial(name, quantity);

                    }
                    if ("Name".equals(endElement)) {
                        name = elementContent;

                    }
                    if ("Quantity".equals(endElement)) {
                        quantity = Integer.parseInt(elementContent);

                    }
                    if ("NumberOfMaintenancePersons".equals(endElement)) {
                        management.setNumberOfMaintenanceWorkers(Integer.parseInt(elementContent));

                    }
                    if ("TotalNumberOfRentalRequests".equals(endElement)) {
                        management.setTotalNumberOfRentalRequests(Integer.parseInt(elementContent));

                    }
                    break;
            }
        }
        return management;
    }


    public static void parseAssets(String assetsXmlPath, Management management)
            throws FileNotFoundException, XMLStreamException {

        String name = "";
        String type = "";
        int size = 0;
        int cost = 0;
        double repairMultiplier = 0;
        Location location = null;
        Asset asset = null;

        XMLStreamReader reader = initializeReader(assetsXmlPath);
        String elementContent = "";

        while (reader.hasNext()) {

            int event = reader.next();

            switch (event) {

                case XMLStreamConstants.START_ELEMENT:
                    String startElement = reader.getLocalName();

                    if ("Location".equals(startElement)) {
                        location = parseLocation(reader);

                    }
                    if ("AssetContents".equals(startElement)) {
                        asset = management.addAsset(name, type, size, location, cost);

                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    elementContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String endElement = reader.getLocalName();

                    if ("Name".equals(endElement)) {
                        name = elementContent;

                    }
                    if ("Type".equals(endElement)) {
                        type = elementContent;

                    }
                    if ("Size".equals(endElement)) {
                        size = Integer.parseInt(elementContent);

                    }
                    if ("CostPerNight".equals(endElement)) {
                        cost = Integer.parseInt(elementContent);

                    }
                    if ("RepairMultiplier".equals(endElement)) {
                        repairMultiplier = Double.parseDouble(elementContent);

                    }
                    if ("AssetContent".equals(endElement)) {
                        asset.addContent(name, repairMultiplier);

                    }
                    break;
            }
        }
    }

    public static void parseContentRepairDetails (String contentRepairDetailsXmlPath, Management management)
            throws FileNotFoundException, XMLStreamException {

        String contentName = "";
        String repairName = "";
        int quantity = 0;

        // initialize XMLStreamReader
        XMLStreamReader reader = initializeReader(contentRepairDetailsXmlPath);
        String elementContent  = "";

        while (reader.hasNext()) {

            int event = reader.next();

            switch (event) {

                case XMLStreamConstants.START_ELEMENT:
                    String startElement = reader.getLocalName();

                    if (startElement.equals("Tools")) {
                        contentName = repairName;
                        management.addNewContentRepairInformation(contentName);

                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    elementContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String endElement = reader.getLocalName();

                    if (endElement.equals("Name")) {
                        repairName = elementContent;

                    }
                    if (endElement.equals("Quantity")) {
                        quantity = Integer.parseInt(elementContent);

                    }
                    if (endElement.equals("Tool")) {
                        management.addItemRepairToolInformation(contentName, repairName, quantity);

                    }
                    if (endElement.equals("Material")) {
                        management.addItemRepairMaterialInformation(contentName, repairName, quantity);

                    }
                    break;
            }
        }

    }

    public static void parseCustomerGroups (String customerGroupsXmlPath, Management management)
            throws FileNotFoundException, XMLStreamException {

        // customer fields
        String name = "";
        String vandalismType = "";
        int minDamage = 0;
        int maxDamage = 0;

        CustomerGroupDetails customerGroupDetails = null;

        // rentalRequest fields
        String id = "";
        String type = "";
        int size = 0;
        int duration = 0;

        // initialize XMLStreamReader
        XMLStreamReader reader = initializeReader(customerGroupsXmlPath);
        String elementContent  = "";

        while (reader.hasNext()) {

            int event = reader.next();

            switch (event) {

                case XMLStreamConstants.START_ELEMENT:
                    String startElement = reader.getLocalName();

                    if (startElement.equals("Request")) {
                        id = reader.getAttributeValue(null, "id");

                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    elementContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String endElement = reader.getLocalName();

                    if (endElement.equals("GroupManagerName")) { // adding CustomerGroupDetails to Management
                        customerGroupDetails = management.addCustomerGroup(elementContent);

                    }
                    if (endElement.equals("Name")) {
                        name = elementContent;

                    }
                    if (endElement.equals("Vandalism")) {
                        vandalismType = elementContent;

                    }
                    if (endElement.equals("MinimumDamage")) {
                        minDamage = Integer.parseInt(elementContent);

                    }
                    if (endElement.equals("MaximumDamage")) {
                        maxDamage = Integer.parseInt(elementContent);

                    }
                    if (endElement.equals("Customer")) { // adding new CustomerDetails object to CustomerGroup
                        customerGroupDetails.addCustomer(id, vandalismType, minDamage, maxDamage);

                    }
                    if (endElement.equals("Type")) {
                        type = elementContent;

                    }
                    if (endElement.equals("Size")) {
                        size = Integer.parseInt(elementContent);

                    }
                    if (endElement.equals("Duration")) {
                        duration = Integer.parseInt(elementContent);

                    }
                    if (endElement.equals("Request")) { // adding new RentalRequest to CustomerGroup
                        customerGroupDetails.addRentalRequest(name, type, size, duration);

                    }
                    break;
            }
        }
    }
}
