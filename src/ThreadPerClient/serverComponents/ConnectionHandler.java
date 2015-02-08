package ThreadPerClient.serverComponents;

import java.io.*;
import java.net.Socket;

import ThreadPerClient.application.WhatsAppApplication;
import protocol.ServerProtocol;
import protocol_whatsapp.WhatsAppProtocol;
import tokenizer.Tokenizer;
import tokenizer_http.*;
import tokenizer_whatsapp.WhatsAppMessage;
import tokenizer_whatsapp.WhatsAppTokenizer;

public class ConnectionHandler<T> implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	Socket clientSocket;
	ServerProtocol<T> protocol;
	Tokenizer<T> tokenizer;

	public ConnectionHandler(Socket acceptedSocket, ServerProtocol<T> p, Tokenizer<T> t, WhatsAppApplication app)
	{
		in = null;
		out = null;
		clientSocket = acceptedSocket;
		protocol = p;
		tokenizer = t;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
		((WhatsAppProtocol) protocol).addApp(app);
	}

	public void run() {


		try {
			initialize();
		} catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} catch (IOException e) {
			System.out.println("Error in I/O");

			System.out.println("Connection closed - bye bye...");
			close();

		}
	}

	public void process() throws IOException
	{
		T msg;

		while ((msg = tokenizer.nextMessage()) != null)
		{
			if(protocol.isEnd(msg)){
				//if the user has been naughty and exit before logout.
				close();
				break;
			}
			else {
				T response = protocol.processMessage(msg);

				if (response instanceof HttpResponseMessage) {
					System.out.println(response.toString());
				} else {
					WhatsAppMessage whatsAppMessage;
					//check what kind of request we got in response- POST or GET?
					if (response instanceof HttpPostRequest) {
						whatsAppMessage = ((WhatsAppTokenizer) tokenizer).nextMessage((HttpPostRequest) response);

					} else {
						whatsAppMessage = ((WhatsAppTokenizer) tokenizer).nextMessage((HttpGetRequest) response);
					}
					HttpResponseMessage responseMessage = ((WhatsAppProtocol) protocol).processMessage(whatsAppMessage);
					System.out.println(responseMessage.toString());
				}
			}
		}
		clientSocket.close();
		//
		System.out.println("Client at" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " commited exit, Handler is done.");
		//
	}

	// Starts listening
	public void initialize() throws IOException
	{
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"), true);
		tokenizer.addInputStream(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		System.out.println("I/O initialized");
	}

	// Closes the connection
	public void close()
	{
		try {
			if (tokenizer.isAlive())//Handle this in tokenizer
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}

			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception in closing I/O");
		}
	}

}