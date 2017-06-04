package smtp.server.handler;

import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class DataHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
		mail.setDataFlag(true);
		return SmtpResponse.DATA_START;
	}
}
