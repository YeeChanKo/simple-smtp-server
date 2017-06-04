package smtp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import smtp.server.constant.SmtpResponse;
import smtp.server.handler.HandlerMapper;
import smtp.server.model.Mail;

public class SocketThread extends Thread {

	private Socket socket;
	private HandlerMapper hm;

	public SocketThread(Socket socket, HandlerMapper hm) {
		this.socket = socket;
		this.hm = hm;
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
				in.mark(10);
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
			if (!response.equals(SmtpResponse.DATA_RECEIVED)) {
				out.println(response);
			} else {
				System.out.println(response);
			}

			// close connection if header is QUIT
			if (response.equals(SmtpResponse.QUIT)) {
				try {
					socket.close();
					System.out.println(
							String.format("%s:%s CONNECTION CLOSED BY SERVER!",
									socket.getInetAddress().getHostAddress(),
									socket.getPort()));
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
			// check if line is null or empty or too short for header
			if (line == null || line.isEmpty() || line.length() < 4) {
				return SmtpResponse.ERROR_SYNTAX;
			}

			// retrieve header
			String header = line.substring(0, 4);

			// check if header is recognizable
			if (!hm.isValidKey(header)) {
				return SmtpResponse.ERROR_SYNTAX;
			}

			return hm.executeMethod(header, line, mail);

		} else {
			// if client is sending data
			return hm.executeMethod("DATA_BODY", line, mail);
		}
	}
}
