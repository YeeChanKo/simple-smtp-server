package smtp.server.handler;

import smtp.server.Mail;

public class QuitHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
		return SmtpResponse.QUIT;
	}

}
