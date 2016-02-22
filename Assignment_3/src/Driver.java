import reit.Management;

/**
 * Created by airbag on 12/9/14.
 */
class Driver {

    public static void main(String[] args) throws Exception {

        // parse XML
        Management management = XMLParser.parseInitialData(args[0]);
        XMLParser.parseContentRepairDetails(args[1], management);
        XMLParser.parseAssets(args[2], management);
        XMLParser.parseCustomerGroups(args[3], management);


        System.out.println("Finished parsing files.");
        management.simulate();
        System.out.println("Simulation Complete");

    }
}
