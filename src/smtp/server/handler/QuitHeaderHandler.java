package smtp.server.handler;

import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class QuitHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
		return SmtpResponse.QUIT;
	}

}
