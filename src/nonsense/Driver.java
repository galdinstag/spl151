package nonsense;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * For testing purposes.
 */
public class Driver {

    // main function. testing.
    public static void main(String[] args) throws Exception {

        //String host = args[0];
        String host = "www.cs.bgu.ac.il";
        Socket lp = new Socket(host, 80);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(lp.getOutputStream(), "UTF-8"), true);
        out.print ("GET / HTTP/1.0\r\n" +
                "Host: " + host+ "\r\n" +
                "\r\n");
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(lp.getInputStream(),"UTF-8"));
        String msg = in.readLine();
        while (msg != null) {
            System.out.println(msg);
            msg = in.readLine();
        }

        out.close();
        in.close();
        lp.close();
    }
}

