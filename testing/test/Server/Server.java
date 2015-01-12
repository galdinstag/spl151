package Server;

import protocol.ServerProtocol;
import protocol_http.HttpProtocol;
import protocol_http.HttpProtocolFactory;
import tokenizer.Tokenizer;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpTokenizer;
import tokenizer_http.HttpTokenizerFactory;
import tokenizer_http.HttpTokenizer;

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
    public HttpTokenizerFactory _factory;
    public Tokenizer _tokenizer;
    public HttpProtocolFactory _Pfactory;
    public ServerProtocol _Pprotocol;
    public Socket client;
    public InputStreamReader in;

    @Override
    public void run() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("127.0.0.1", 126));
            System.out.println("listening...");

            _factory = new HttpTokenizerFactory();
            _tokenizer = _factory.create();
            _Pfactory = new HttpProtocolFactory();
            _Pprotocol = _Pfactory.create();
            client = server.accept();
            System.out.println("recived connection");

            in = new InputStreamReader(client.getInputStream(), "UTF-8");
            _tokenizer.addInputStream(in);
            System.out.println("tokenizer set");

            while(_tokenizer.isAlive()){
                System.out.println("trying to read next token");
                HttpMessage message = (HttpMessage)_tokenizer.nextMessage();
                HttpMessage ans = ((HttpProtocol)_Pprotocol).processMessage(message);
                System.out.println(ans.toString());
            }
            System.out.println("finished, i'm outahere");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
