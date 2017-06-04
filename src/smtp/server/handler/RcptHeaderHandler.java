package smtp.server.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smtp.server.Main;
import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class RcptHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
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
}
