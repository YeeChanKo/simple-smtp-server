package smtp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import smtp.server.handler.HandlerMapper;

public class Main {

	public static final String SERVER_DOMAIN_NAME = "test.com";
	public static final String SERVER_IP = "200.200.200.200";
	public static final int SERVER_PORT = 8000; // 587

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
			System.out.println("SMTP Server Started");

			HandlerMapper hm = new HandlerMapper("smtp.server.handler");
			hm.initialize();

			while (true) {
				Socket socket = serverSocket.accept();
				new SocketThread(socket, hm).start();
			}
		}
	}
}
