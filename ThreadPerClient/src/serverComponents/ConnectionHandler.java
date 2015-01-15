package serverComponents;

import java.io.*;
import java.net.Socket;

import application.WhatsAppApplication;
import protocol.ServerProtocol;
import protocol_whatsapp.WhatsAppProtocol;
import tokenizer.Tokenizer;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpRequestMessage;
import tokenizer_http.HttpRequestType;
import tokenizer_http.HttpResponseMessage;
import tokenizer_whatsapp.WhatsAppMessage;
import tokenizer_whatsapp.WhatsAppTokenizer;

public class ConnectionHandler<T> implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	Socket clientSocket;
	ServerProtocol<T> protocol;
	Tokenizer<T> tokenizer;
	private WhatsAppApplication _app;

	public ConnectionHandler(Socket acceptedSocket, ServerProtocol<T> p, Tokenizer<T> t, WhatsAppApplication app)
	{
		in = null;
		out = null;
		clientSocket = acceptedSocket;
		protocol = p;
		tokenizer = t;
		_app = app;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}

	public void run()
	{


		try {
			initialize();
		}
		catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} 
		catch (IOException e) {
			System.out.println("Error in I/O");
		} 

		System.out.println("Connection closed - bye bye...");
		close();

	}

	public void process() throws IOException
	{
		T msg;

		while ((msg = tokenizer.nextMessage()) != null)
		{
			System.out.println("Received \"" + msg + "\" from client");
			T response = protocol.processMessage(msg);
			if(response instanceof HttpResponseMessage){
				System.out.println(response);
			}
			else{
				WhatsAppMessage whatsAppMessage = ((WhatsAppTokenizer) tokenizer).nextMessage((HttpRequestMessage) response);

			}






			/**

			if (response != null)
			{
				out.println(response);
			}

			if (protocol.isEnd(msg))
			{
				break;
			}
			*/



		}
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