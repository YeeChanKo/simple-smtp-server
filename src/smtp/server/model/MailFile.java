package smtp.server.model;

import java.io.Serializable;
import java.util.List;

public class MailFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sender;
	private List<String> recipients;
	private String data;

	public MailFile(String sender, List<String> recipients, String data) {
		super();
		this.sender = sender;
		this.recipients = recipients;
		this.data = data;
	}
}
