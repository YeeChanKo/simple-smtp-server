package smtp.server.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smtp.server.Main;
import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class HeaderHandler {

	@SmtpHandler({ "HELO", "EHLO" })
	public String helo(String line, Mail mail) {
		return SmtpResponse.OKAY;
	}

	@SmtpHandler("MAIL")
	public String mail(String line, Mail mail) {
		// remove white space because of java regex limitation
		line = line.replaceAll(" ", "");
		Pattern p = Pattern.compile("MAILFROM:<(.*?@.*?)>");
		Matcher m = p.matcher(line);

		if (m.find()) {
			String sender = m.group(1);
			mail.setSender(sender);
			return SmtpResponse.OKAY;
		} else {
			return SmtpResponse.ERROR_ARGUMENT;
		}
	}

	@SmtpHandler("RCPT")
	public String rcpt(String line, Mail mail) {
		// remove white space because of java regex limitation
		line = line.replaceAll(" ", "");
		Pattern p = Pattern.compile("RCPTTO:<(.*?)@(.*?)>");
		Matcher m = p.matcher(line);

		if (m.find()) {
			String recipientUser = m.group(1);
			String recipientHost = m.group(2);
			String recipient = recipientUser + "@" + recipientHost;

			if (!recipientHost.equals(Main.SERVER_DOMAIN_NAME)
					&& !recipientHost.equals(Main.SERVER_IP)) {
				return SmtpResponse.ERROR_WRONG_HOST;
			}

			// TODO: check existing user list here
			if (recipientUser == null || recipientUser.isEmpty()) {
				return SmtpResponse.ERROR_NO_MATCH;
			}

			mail.addRecipient(recipient);
			return SmtpResponse.OKAY;

		} else {
			return SmtpResponse.ERROR_ARGUMENT;
		}
	}

	@SmtpHandler("DATA")
	public String data(String line, Mail mail) {
		mail.setDataFlag(true);
		return SmtpResponse.DATA_START;
	}

	@SmtpHandler("QUIT")
	public String quit(String line, Mail mail) {
		return SmtpResponse.QUIT;
	}
}
