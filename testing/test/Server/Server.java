package Server;

import tokenizer.TokenizerImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gal on 1/10/2015.
 */
public class Server implements Runnable{
   public ServerSocket server;
    @Override
    public void run() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("127.0.0.1", 126));
            System.out.println("listening...");
            TokenizerImpl _tokenizer = new TokenizerImpl();
            Socket client = null;
            client = server.accept();
            System.out.println("recived connection");
            InputStreamReader in = null;
            in = new InputStreamReader(client.getInputStream(), "UTF-8");
            _tokenizer.addInputStream(in);
            System.out.println("tokenizer set");
            while(_tokenizer.isAlive()){
                System.out.println("trying to read next token");
                _tokenizer.nextMessage();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
