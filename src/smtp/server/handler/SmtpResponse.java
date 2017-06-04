package smtp.server.handler;

public class SmtpResponse {
	public static final String READY = "220 Service ready";
	public static final String OKAY = "250 OK";
	public static final String DATA_START = "354 Start mail input; end with <CRLF>.<CRLF>";
	public static final String DATA_RECEIVED = "1000 Data received";
	public static final String QUIT = "221 Closing transmission channel";
	public static final String ERROR_SYNTAX = "500 Syntax error, command unrecognized";
	public static final String ERROR_ARGUMENT = "501 Syntax error in parameters or arguments";
	public static final String ERROR_NO_MATCH = "550 Mailbox not found";
	public static final String ERROR_WRONG_HOST = "550 Mail undeliverable - Wrong Host";
	public static final String ERROR_MAIL_NOT_COMPLETE = "1001 Sender or recipient not set; restart the procedure";
}
