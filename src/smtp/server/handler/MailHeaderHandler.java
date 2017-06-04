package smtp.server.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smtp.server.constant.SmtpResponse;
import smtp.server.model.Mail;

public class MailHeaderHandler implements SmtpHandler {

	@Override
	public String handle(String line, Mail mail) {
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

}
