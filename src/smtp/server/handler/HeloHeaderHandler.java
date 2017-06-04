package smtp.server.handler;

import smtp.server.Mail;

public class HeloHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
		return SmtpResponse.OKAY;
	}

}
