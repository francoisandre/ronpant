package fr.gouv.education.sirhen.gin.gestiondescontratsdesnontitulaires.fait;

import java.util.List;

public class Message {
	public static final int HELLO = 0;
	public static final int GOODBYE = 1;

	public String message;

	private int status;

	public Message() {

	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(final int status) {
		this.status = status;
	}

	public static Message doSomething(final Message message) {
		return message;
	}

	public boolean isSomething(final String msg, final List < Object > list) {
		list.add(this);
		return this.message.equals(msg);
	}
}