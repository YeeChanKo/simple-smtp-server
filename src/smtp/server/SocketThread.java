package smtp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import smtp.server.handler.DataBodyHandler;
import smtp.server.handler.SmtpHandler;
import smtp.server.handler.SmtpHeader;
import smtp.server.handler.SmtpResponse;

public class SocketThread extends Thread {

	private Socket socket;
	private Map<SmtpHeader, SmtpHandler> headerHandlers;

	public SocketThread(Socket socket,
			Map<SmtpHeader, SmtpHandler> headerHandlers) {
		this.socket = socket;
		this.headerHandlers = headerHandlers;
	}

	public void run() {
		// initialize stream reader & writer
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		// send ready message to client
		out.println(SmtpResponse.READY);
		// log client connection
		System.out.println(String.format("%s:%s CONNECTED!",
				socket.getInetAddress().getHostAddress(), socket.getPort()));

		// initialize a mail instance
		Mail mail = new Mail();

		// read line by line from stream
		while (true) {
			// check if client disconnected
			try {
				in.mark(100); // 100 chars read ahead limit
				if (in.read() == -1) {
					System.out.println(
							String.format("%s:%s CONNECTION CLOSED BY CLIENT!",
									socket.getInetAddress().getHostAddress(),
									socket.getPort()));
					return; // end of thread
				} else {
					in.reset();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}

			// read input
			String input = null;
			try {
				input = in.readLine();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}

			// execute handler based on input
			String response = executeHandler(input, mail);
			out.println(response);

			// close connection if header is QUIT
			if (response.equals(SmtpResponse.QUIT)) {
				try {
					socket.close();
					return; // end of thread
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}

			// log result
			System.out.println(String.format("%s:%s - %s",
					socket.getInetAddress().getHostAddress(), socket.getPort(),
					mail.toString()));
		}
	}

	public String executeHandler(String line, Mail mail) {
		if (mail.getDataFlag() == false) {
			// check if line is null or empty
			if (line == null || line.isEmpty() || line.length() < 4) {
				return SmtpResponse.ERROR_SYNTAX;
			}

			// retrieve header
			String header = line.substring(0, 4);

			// check if header is recognizable
			if (!isValidSmtpHeader(header)) {
				return SmtpResponse.ERROR_SYNTAX;
			}

			SmtpHandler handler = headerHandlers
					.get(SmtpHeader.valueOf(header));
			return handler.handle(line, mail);

		} else {
			// if client is sending data
			// TODO: put this in handler map as well
			return new DataBodyHandler().handle(line, mail);
		}
	}

	public boolean isValidSmtpHeader(String test) {
		for (SmtpHeader header : SmtpHeader.values()) {
			if (header.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}
