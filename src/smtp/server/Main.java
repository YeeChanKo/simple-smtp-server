package smtp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import smtp.server.constant.SmtpHeader;
import smtp.server.handler.DataHeaderHandler;
import smtp.server.handler.SmtpHandler;
import smtp.server.handler.HeloHeaderHandler;
import smtp.server.handler.MailHeaderHandler;
import smtp.server.handler.QuitHeaderHandler;
import smtp.server.handler.RcptHeaderHandler;

public class Main {

	public static final String SERVER_DOMAIN_NAME = "test.com";
	public static final String SERVER_IP = "200.200.200.200";
	public static final int SERVER_PORT = 8000; // 587

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
			System.out.println("SMTP Server Started");

			// TODO: refactor using annotation
			// create all handler methods in one header handler class
			Map<SmtpHeader, SmtpHandler> headerHandlers = new HashMap<>();
			headerHandlers.put(SmtpHeader.HELO, new HeloHeaderHandler());
			headerHandlers.put(SmtpHeader.EHLO, new HeloHeaderHandler());
			headerHandlers.put(SmtpHeader.MAIL, new MailHeaderHandler());
			headerHandlers.put(SmtpHeader.RCPT, new RcptHeaderHandler());
			headerHandlers.put(SmtpHeader.DATA, new DataHeaderHandler());
			headerHandlers.put(SmtpHeader.QUIT, new QuitHeaderHandler());

			while (true) {
				Socket socket = serverSocket.accept();
				new SocketThread(socket, headerHandlers).start();
			}
		}
	}
}
