package Server;

/**
 * Created by gal on 1/10/2015.
 */
/**
public class Server implements Runnable{
    public ServerSocket server;
    public WhatsAppTokenizerFactory _Tfactory;
    public Tokenizer _tokenizer;
    public ServerProtocolFactory _Pfactory;
    public WhatsAppProtocol _protocol;
    public Socket client;
    public InputStreamReader in;
    public WhatsAppApplication app;

    /
    @Override
    public void run() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("127.0.0.1", 126));
            System.out.println("listening...");

            app = new WhatsAppApplication();
            _Tfactory = new WhatsAppTokenizerFactory();
            _tokenizer = _Tfactory.create();
            _Pfactory = new WhatsAppProtocolFactory();
            _protocol = new WhatsAppProtocol();
            client = server.accept();
            System.out.println("recived connection");

            in = new InputStreamReader(client.getInputStream(), "UTF-8");
            _tokenizer.addInputStream(in);
            System.out.println("tokenizer set");

            while(_tokenizer.isAlive()){
                System.out.println("trying to read next token");
                HttpMessage message = (HttpMessage)_tokenizer.nextMessage();
                HttpMessage ans = _protocol.processMessage(message);
                WhatsAppMessage whatsAppMessage = _tokenizer.nextMessage((HttpRequestMessage)ans);
                HttpResponseMessage responseMessage = _protocol.processMessage(whatsAppMessage);
                System.out.println(responseMessage.toString());
            }
            System.out.println("finished, i'm outahere");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
*/