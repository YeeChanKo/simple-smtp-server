package smtp.server.handler;

import smtp.server.model.Mail;

public interface SmtpHandler {
	public abstract String handle(String line, Mail mail);
}
