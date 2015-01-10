package Client;

import Server.Server;

import java.io.IOException;

/**
 * Created by gal on 1/10/2015.
 */
public class ClientMain {
    public static void main(String[] args) throws IOException {
        Client testclient = new Client();
        Thread client = new Thread(testclient);
        client.run();
    }
}
