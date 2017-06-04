package smtp.server.handler;

import smtp.server.Mail;

public interface SmtpHandler {
	public abstract String handle(String line, Mail mail);
}
