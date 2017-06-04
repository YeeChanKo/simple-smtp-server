package smtp.server.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smtp.server.constant.SmtpResponse;

public class Mail {

	private static final String dirPath = "/Users/viz/MailReceived/";

	private boolean dataFlag = false; // default value is false

	private String sender;
	private String dataContent;
	private StringBuilder dataBuilder = new StringBuilder();
	private List<String> recipients = new ArrayList<>();

	public void addRecipient(String recipient) {
		recipients.add(recipient);
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setDataFlag(boolean dataFlag) {
		this.dataFlag = dataFlag;
	}

	public boolean getDataFlag() {
		return dataFlag;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void appendDataWithNewLine(String data) {
		dataBuilder.append(data).append(System.lineSeparator());
	}

	public String flushDataAndSave() {
		// check if sender and recipients exist
		// if not, clear string builder and notify client to restart
		if (sender == null || recipients.isEmpty()) {
			dataBuilder.setLength(0);
			return SmtpResponse.ERROR_MAIL_NOT_COMPLETE;
		}

		// flush data and save into dataContent
		dataContent = dataBuilder.toString();

		// create data string with specified format
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		String date = dateFormat.format(new Date());

		// create file and write serialized map object
		try (FileOutputStream fout = new FileOutputStream(dirPath + date);
				ObjectOutput out = new ObjectOutputStream(fout);) {
			Map<String, Object> mail = new HashMap<>();
			mail.put("sender", sender);
			mail.put("recipients", recipients);
			mail.put("data", dataContent);
			out.writeObject(mail);
			out.flush();
			return SmtpResponse.OKAY;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public String toString() {
		return "Mail [dataFlag=" + dataFlag + ", sender=" + sender
				+ ", recipients=" + recipients + "]";
	}
}
