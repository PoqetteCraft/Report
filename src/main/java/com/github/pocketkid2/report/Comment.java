package com.github.pocketkid2.report;

import java.sql.Timestamp;
import java.util.UUID;

public class Comment {

	private String message;
	private UUID commenter;
	private Timestamp time;

	/**
	 * @param message
	 * @param commenter
	 * @param time
	 */
	public Comment(String message, UUID commenter, Timestamp time) {
		this.message = message;
		this.commenter = commenter;
		this.time = time;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the commenter
	 */
	public UUID getCommenter() {
		return commenter;
	}

	/**
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}

}
