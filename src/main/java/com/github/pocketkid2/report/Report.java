package com.github.pocketkid2.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

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
	private final UUID reporter;
	private final String reportMessage;
	private final Timestamp reportTime;
	private final Location reportLoc;

	// Working conditions
	private final int id;
	private final boolean resolved;
	private final List<Comment> comments;

	// Final conditions
	private final UUID resolver;
	private final Timestamp resolveTime;

	/**
	 * @param reporter
	 * @param reportMessage
	 * @param reportTime
	 * @param reportLoc
	 * @param id
	 * @param resolved
	 * @param resolver
	 * @param resolveMessage
	 * @param resolveTime
	 * @param resolveLoc
	 */
	public Report(UUID reporter, String reportMessage, Timestamp reportTime, Location reportLoc, int id, boolean resolved, UUID resolver, Timestamp resolveTime) {
		this.reporter = reporter;
		this.reportMessage = reportMessage;
		this.reportTime = reportTime;
		this.reportLoc = reportLoc;
		this.id = id;
		this.resolved = resolved;
		comments = new ArrayList<Comment>();
		this.resolver = resolver;
		this.resolveTime = resolveTime;
	}

	/**
	 * @return the reporter
	 */
	public UUID getReporter() {
		return reporter;
	}

	/**
	 * @return the reportMessage
	 */
	public String getReportMessage() {
		return reportMessage;
	}

	/**
	 * @return the reportTime
	 */
	public Timestamp getReportTime() {
		return reportTime;
	}

	/**
	 * @return the reportLoc
	 */
	public Location getReportLoc() {
		return reportLoc;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the resolved
	 */
	public boolean isResolved() {
		return resolved;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @return the resolver
	 */
	public UUID getResolver() {
		return resolver;
	}

	/**
	 * @return the resolveTime
	 */
	public Timestamp getResolveTime() {
		return resolveTime;
	}

	public void addComment(Comment s) {
		comments.add(s);
	}

	public void addComments(List<Comment> c) {
		comments.addAll(c);
	}

}
