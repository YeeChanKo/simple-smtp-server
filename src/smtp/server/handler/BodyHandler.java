package smtp.server.handler;

import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class BodyHandler {

	@SmtpHandler("DATA_BODY")
	public String data(String line, Mail mail) {
		if (line.equals(".")) {
			mail.setDataFlag(false);
			return mail.flushDataAndSave();
		}

		mail.appendDataWithNewLine(line);
		return SmtpResponse.DATA_RECEIVED;
	}
}
