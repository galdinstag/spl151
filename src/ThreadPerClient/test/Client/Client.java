package ThreadPerClient.test.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by gal on 1/10/2015.
 */
public class Client implements Runnable {
    Socket clientSocket;
    @Override
    public void run() {
        System.out.println("running client");
        try {
            clientSocket = new Socket("127.0.0.1",126);
            System.out.println("client connected");
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            OutputStreamWriter osw = new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8");
            String line;
            System.out.println("print something");
            StringBuilder sb = new StringBuilder();
                while((line = bf.readLine()) != null){
                    sb.append(line);
                    System.out.println("print something");
                    osw.write(line);
                    osw.flush();
                }
            System.out.println(sb.indexOf("\n"));
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
