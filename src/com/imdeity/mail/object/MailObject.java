package com.imdeity.mail.object;

import java.util.Calendar;
import java.util.Date;

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

	public String toShortString() {
		String message = "";
		message = "<gray>[" + this.index + "]<white> " + this.sender
				+ ":<gray> " + StringMgmt.maxLength(this.message, 30);
		return message;
	}

	public String toLongString() {
		String message = "";
		message = "<gray>["
				+ this.index
				+ "]<white> "
				+ this.sender
				+ ":<gray> "
				+ this.message
				+ " <darkgray>["
				+ (this.sendDate != null ? this.timeApproxToDate(sendDate)
						: "Not Available") + "]";
		return message;
	}

	public String timeApproxToDate(Date date) {
		long relativeTime = date.getTime()
				- Calendar.getInstance().getTimeInMillis();

		return HumanTime.approximately(relativeTime) + " ago";
	}

}
