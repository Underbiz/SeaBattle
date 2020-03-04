package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import client.Fleet;
import client.Ship;
import client.Shot;

public class TCPConnection {

	private final Socket socket;
	private final Thread rxThread;
	private final TCPConnectionListener eventListener;
	private final BufferedWriter out;
	private final BufferedReader in;

	public TCPConnection(TCPConnectionListener eventListener, String IpAddr, int port) throws IOException {
		this(eventListener, new Socket(IpAddr, port));

	}

	public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
		this.eventListener = eventListener;
		this.socket = socket;

		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

		rxThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					eventListener.onConnectionReady(TCPConnection.this);

					while (!rxThread.isInterrupted()) {

						String value = in.readLine();

						eventListener.onReceiveSrting(TCPConnection.this, value);

					}

				} catch (IOException e) {
					e.printStackTrace();

					eventListener.onExeption(TCPConnection.this, e);
				} finally {
					eventListener.onDisconnect(TCPConnection.this);
				}

			}
		});
		rxThread.start();

	}

	public synchronized void sendString(String value) {
		if (value.startsWith("msg")) {
			System.out.println("Enviamos msg : " + value);
		} else if (value.startsWith("fleet")) {
			System.out.println("Enviamos fleet : " + value);
		} else if (value.startsWith("shot")) {
			System.out.println("Enviamos shot : " + value);
		} else if (value.startsWith("activ") || value.startsWith("inactiv")) {
			System.out.println("Enviamos status! : " + value);
		}

		try {
			out.write(value + "\n");
			out.flush();
			if (value.startsWith("msg")) {
				System.out.println("MSG enviado! : " + value);
			} else if (value.startsWith("fleet")) {
				System.out.println("FLEET enviado! : " + value);
			} else if (value.startsWith("shot")) {
				System.out.println("SHOT enviado! : " + value);
			} else if (value.startsWith("activ") || value.startsWith("inactiv")) {
				System.out.println("status enviado! : " + value);
			}
		} catch (IOException e) {
			eventListener.onExeption(TCPConnection.this, e);
			disconnect();
		}
	}

	public synchronized void disconnect() {
		rxThread.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			eventListener.onExeption(TCPConnection.this, e);
		}
	}

	@Override
	public String toString() {
		return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
	}

}
