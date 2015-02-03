package ThreadPerClient.serverComponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import ThreadPerClient.application.WhatsAppApplication;
import protocol.ServerProtocolFactory;
import protocol_whatsapp.WhatsAppProtocolFactory;
import tokenizer.TokenizerFactory;
import tokenizer_whatsapp.WhatsAppTokenizerFactory;

public class MultipleClientProtocolServer<T> implements Runnable {
	private ServerSocket serverSocket;
	private int listenPort;
	private ServerProtocolFactory<T> _protocolFactory;
	private TokenizerFactory<T> _tokenizerFactory;
	private WhatsAppApplication _app;
	private BufferedReader serverAdminConsole;
	private String consoleLine;

	public MultipleClientProtocolServer(int port, ServerProtocolFactory<T> protocolFactory, TokenizerFactory<T> tokenizerFactory)
	{
		serverSocket = null;
		listenPort = port;
		_protocolFactory = protocolFactory;
		_tokenizerFactory = tokenizerFactory;
		_app =  new WhatsAppApplication();
		serverAdminConsole = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenPort);
			serverSocket.setSoTimeout(1000);
			System.out.println("Listening...");
			System.out.println("Admin console is ready...");
		}
		catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (true)
		{
			try {
				if(serverAdminConsole.ready()){
					if((consoleLine = serverAdminConsole.readLine()).equals("exit")) {
						close();
						break;
					}
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ConnectionHandler newConnection = new ConnectionHandler(serverSocket.accept(), _protocolFactory.create(),_tokenizerFactory.create(),_app);
				new Thread(newConnection).start();
			}
			catch (IOException e)
			{
				if(e instanceof SocketTimeoutException){
				}
				else{
					System.out.println("Failed to accept on port " + listenPort);
				}
			}
		}
		System.out.println("server done");
	}


	// Closes the connection
	public void close() throws IOException
	{
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException
	{
		// Get port
		int port = Integer.decode("126").intValue();

		MultipleClientProtocolServer server = new MultipleClientProtocolServer(port, new WhatsAppProtocolFactory(), new WhatsAppTokenizerFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Server stopped");
		}

		System.out.println("server finished, exiting program");


	}
}

