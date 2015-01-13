package Server;

import java.io.IOException;

/**
 * Created by gal on 1/9/2015.
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server testServer = new Server();
        Thread server = new Thread(testServer);
        server.run();
    }
}
