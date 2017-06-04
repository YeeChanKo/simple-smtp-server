package smtp.server.handler;

import smtp.server.Mail;

public class DataBodyHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
		if (line.equals(".")) {
			mail.setDataFlag(false);
			return mail.flushDataAndSave();
		}

		mail.appendDataWithNewLine(line);
		return SmtpResponse.DATA_RECEIVED;
	}
}
