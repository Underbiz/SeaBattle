package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.Fleet;
import client.Ship;
import client.Shot;
import network.TCPConnection;
import network.TCPConnectionListener;

public class Server implements TCPConnectionListener {

	private int MAX_CONNECTIONS = 2;
	private static int port;
	boolean gameOver;
	String player_1;
	String player_2;

	public static void main(String[] args) throws IOException {
		new Server(7777);
	}

	private final ArrayList<TCPConnection> connections = new ArrayList<>();

	public Server(int port) throws IOException {
		this.port = port;
		System.out.println("Server is ready for connetions..");
		try (ServerSocket serverSocket = new ServerSocket(port, MAX_CONNECTIONS)) {
			while (true) {
				try {

					Socket socket = serverSocket.accept();

					new TCPConnection(this, socket);
					

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	

	@Override
	public synchronized void onConnectionReady(TCPConnection tcpConnection) {

		connections.add(tcpConnection);

		sendToAllConnections("Client connected: " + tcpConnection);
		System.out.println(connections.size());
	}

	@Override
	public void onReceiveSrting(TCPConnection tcpConnection, String value) {
		
		if(value.startsWith("msg")) {
			sendToAllConnections(value);
		}else if(value.startsWith("fleet")){
			if(tcpConnection == connections.get(0)) {
				player_1 = value;
				sendToEachOther(value, 1);
			}else if(tcpConnection == connections.get(1)) {
				player_2 = value;
				sendToEachOther(value, 0);
			}
			
			
		}else if(value.startsWith("shot")) {
			if(tcpConnection == connections.get(0)) {
				sendToEachOther(value, 1);
			}else if(tcpConnection == connections.get(1)) {
				sendToEachOther(value, 0);
			}
		}
			else if(value.equals("gameOver")) {
			sendToAllConnections(value);
			
		}else if(value.equals("active") || value.equals("inactive")) {
			if(tcpConnection == connections.get(0)) {
				sendToEachOther(value, 1);
			}else if(tcpConnection == connections.get(1)) {
				sendToEachOther(value, 0);
			}
		}
			else {
			System.out.println(value);
		}
		

	}

	@Override
	public synchronized void onDisconnect(TCPConnection tcpConnection) {
		connections.remove(tcpConnection);
		sendToAllConnections("Client disconnected: " + tcpConnection);
		if(connections.size() == 0) {
			System.exit(0);
		}
		System.out.println(connections.size());

	}

	@Override
	public synchronized void onExeption(TCPConnection tcpConnection, Exception e) {
		System.out.println("TCPConnection exception " + e);

	}

	private void sendToEachOther(String fleet, int value) {

		connections.get(value).sendString(fleet);
		System.out.println("Enviamos " + value + " a connection #" + (value + 1));

	}

	private void sendToAllConnections(String value) {
		final int cnt = connections.size();
		for (int i = 0; i < cnt; i++) {
			connections.get(i).sendString(value);
		}
	}

	
	

}
