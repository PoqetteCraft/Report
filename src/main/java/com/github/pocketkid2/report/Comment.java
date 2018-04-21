package com.github.pocketkid2.report;

import java.util.UUID;

public class Comment {

	private String message;
	private UUID commenter;

	/**
	 * @param message
	 * @param commenter
	 * @param time
	 */
	public Comment(String message, UUID commenter) {
		this.message = message;
		this.commenter = commenter;
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

}
