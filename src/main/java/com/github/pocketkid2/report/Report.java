package com.github.pocketkid2.report;

import java.util.UUID;

/**
 * An unmutable class representing a retrieved Report from the database. Each
 * instance of Report cooresponds to a single Report and row in the database.
 * For speed and simplicity, modification should be done through ReportManager
 * to directly access the database.
 *
 * @author Adam
 *
 */
public class Report {

	// Initial conditions
	private final UUID filer;
	private final String reportMessage;
	// TODO file time and location

	// Working conditions
	private final int id;
	private final boolean resolved;
	// TODO comments

	// Final conditions
	// TODO resolution location, time, uuid, and optional message

	/**
	 * Constructor for reading from database
	 *
	 * @param uuid
	 *            The filer UUID from the database
	 * @param message
	 *            The message string from the database
	 * @param resolved
	 *            The resolution status from the database
	 * @param id
	 *            The int (primary key) from the database
	 */
	public Report(int id, UUID uuid, String message, boolean resolved) {
		filer = uuid;
		reportMessage = message;
		this.resolved = resolved;
		this.id = id;
	}

	public UUID getFiler() {
		return filer;
	}

	public String getReportMessage() {
		return reportMessage;
	}

	public int getId() {
		return id;
	}

	public boolean isResolved() {
		return resolved;
	}

}
