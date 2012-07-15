package com.imdeity.mail.object;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import com.imdeity.mail.util.HumanTime;
import com.imdeity.mail.util.StringMgmt;

public class MailObject {

	private int id, index = 0;
	private String sender, receiver, message = "";
	private Date sendDate = null;

	public MailObject(int id, int index, String sender, String receiver,
			String message) {
		this.id = id;
		this.index = index;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}

	public MailObject(int id, int index, String sender, String receiver,
			String message, Date sendDate) {
		this.id = id;
		this.index = index;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.sendDate = sendDate;
	}

	public int getId() {
		return this.id;
	}

	public int getIndex() {
		return this.index;
	}

	public String getSender() {
		return this.sender;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public String getMessage() {
		return this.message;
	}

	public String[] toShortString() {
		return this.preformReplacement(Language.getMailShortMessage());
	}

	public String[] toLongString() {
		return this.preformReplacement(Language.getMailLongMessage());
	}

	public String getSendDate() {
		return (this.sendDate != null ? this.timeApproxToDate(sendDate)
				: "Not Available");
	}

	public String timeApproxToDate(Date date) {
		long relativeTime = date.getTime()
				- Calendar.getInstance().getTimeInMillis();

		return HumanTime.approximately(relativeTime) + " ago";
	}

	public String[] preformReplacement(String msg) {
		msg = msg
				.replaceAll("%messageId", this.getId() + "")
				.replaceAll("%messageLocalIndex", this.getIndex() + "")
				.replaceAll("%messageSender", Matcher.quoteReplacement(this.getSender()))
				.replaceAll("%messageLongMessage", Matcher.quoteReplacement(this.message))
				.replaceAll("%messageShortMessage",
						Matcher.quoteReplacement(StringMgmt.maxLength(this.message, 30)))
				.replaceAll("%messageReceiver", Matcher.quoteReplacement(this.getReceiver()))
				.replaceAll("%messageSendDate", this.getSendDate());

		String[] tmpMsg = msg.split("%newline");
		return tmpMsg;
	}
}
