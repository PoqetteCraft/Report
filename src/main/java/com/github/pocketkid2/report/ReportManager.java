package com.github.pocketkid2.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.pocketkid2.database.Database;

public class ReportManager {

	private static final String CREATE_PRIMARY_TABLE = "CREATE TABLE IF NOT EXISTS reports (id INTEGER PRIMARY KEY AUTO_INCREMENT, resolved BOOLEAN DEFAULT false, reporter CHAR(36), reportTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, reportLoc TEXT, reportMessage TEXT, resolver CHAR(36), resolveTime TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP);";

	private static final String CREATE_COMMENT_TABLE = "CREATE TABLE IF NOT EXISTS report_comments (id INTEGER, commenter CHAR(36), comment TEXT);";

	private static final String INSERT_REPORT = "INSERT INTO reports (reporter, reportLoc, reportMessage) VALUES (?, ?, ?);";

	private static final String INSERT_COMMENT = "INSERT INTO report_comments (id, commenter, comment) VALUES (?, ?, ?);";

	private static final String UPDATE_RESOLVE = "UPDATE reports SET resolved = true, resolver = ?, resolveTime = CURRENT_TIMESTAMP WHERE id = ?;";

	private static final String UPDATE_UNRESOLVE = "UPDATE reports SET resolved = false where id = ?;";

	private static final String SELECT_UNRESOLVED = "SELECT * FROM reports WHERE resolved = false;";

	private static final String SELECT_REPORT_BY_ID = "SELECT * FROM reports WHERE id = ?;";

	private static final String SELECT_COMMENT_BY_ID = "SELECT * FROM report_comments WHERE id = ?;";

	private static final String SELECT_REPORT_BY_PLAYER = "SELECT * FROM reports WHERE reporter = ?;";

	private PreparedStatement insertReport;
	private PreparedStatement insertComment;
	private PreparedStatement updateResolve;
	private PreparedStatement updateUnresolve;
	private PreparedStatement selectUnresolved;
	private PreparedStatement selectReportById;
	private PreparedStatement selectCommentById;
	private PreparedStatement selectReportByPlayer;

	private ReportPlugin plugin;

	public ReportManager(ReportPlugin plugin) {
		this.plugin = plugin;
	}

	protected void initStatements() {
		insertReport = Database.prepare(INSERT_REPORT);
		insertComment = Database.prepare(INSERT_COMMENT);
		updateResolve = Database.prepare(UPDATE_RESOLVE);
		updateUnresolve = Database.prepare(UPDATE_UNRESOLVE);
		selectUnresolved = Database.prepare(SELECT_UNRESOLVED);
		selectReportById = Database.prepare(SELECT_REPORT_BY_ID);
		selectCommentById = Database.prepare(SELECT_COMMENT_BY_ID);
		selectReportByPlayer = Database.prepare(SELECT_REPORT_BY_PLAYER);
	}

	protected void initTables() {
		try (Statement s = Database.create()) {
			s.executeUpdate(CREATE_PRIMARY_TABLE);
			s.executeUpdate(CREATE_COMMENT_TABLE);
		} catch (SQLException e) {
			plugin.getLogger().severe("Error creating tables!");
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a new report into the database
	 *
	 * @param player
	 *            The player who filed a report
	 * @param message
	 *            The message of the report
	 */
	public void report(Player player, String message) {
		try {
			insertReport.setString(1, player.getUniqueId().toString());
			insertReport.setString(2, Util.locToString(player.getLocation()));
			insertReport.setString(3, message);
			insertReport.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe("Error creating new report for player " + player.getName() + " with message " + message);
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a new comment into the database
	 *
	 * @param id
	 *            The report ID to comment on
	 * @param player
	 *            The player who commented
	 * @param message
	 *            The comment message
	 */
	public void comment(int id, Player player, String message) {
		try {
			insertComment.setInt(1, id);
			insertComment.setString(2, player.getUniqueId().toString());
			insertComment.setString(3, message);
			insertComment.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe("Error commenting on report " + id + " by player " + player.getName());
			e.printStackTrace();
		}
	}

	/**
	 * Resolves a report
	 *
	 * @param id
	 *            The ID of the report to resolve
	 * @param player
	 *            The player that resolved the report
	 * @param message
	 *            The optional resolve message
	 */
	public void resolve(int id, Player player, String message) {
		try {
			updateResolve.setString(1, player.getUniqueId().toString());
			updateResolve.setInt(2, id);
			updateResolve.executeUpdate();
			if ((message != null) && (message.length() > 1)) {
				comment(id, player, message);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Error resolving report " + id + " by player " + player.getName());
			e.printStackTrace();
		}
	}

	public void unresolve(int id, Player player, String message) {
		try {
			updateUnresolve.setInt(1, id);
			updateUnresolve.executeUpdate();
			if ((message != null) && (message.length() > 1)) {
				comment(id, player, message);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Error unresolving report " + id);
			e.printStackTrace();
		}
	}

	/**
	 * Grabs all unread reports (resolved = false) from the database
	 *
	 * @return All unresolved reports, parsed into a List
	 */
	public List<Report> read() {
		List<Report> reports = new ArrayList<Report>();

		try (ResultSet rs = selectUnresolved.executeQuery()) {
			while (rs.next()) {
				Report report = parse(rs);
				report.addComments(getComments(report.getId()));
				reports.add(report);
			}

		} catch (SQLException e) {
			plugin.getLogger().severe("Error reading reports");
			e.printStackTrace();
		}

		return reports;
	}

	public List<Report> read(Player player) {
		List<Report> reports = new ArrayList<Report>();

		try {
			selectReportByPlayer.setString(1, player.getUniqueId().toString());
		} catch (SQLException e) {
			plugin.getLogger().severe("Error loading select report by player statement for player " + player.getName());
			e.printStackTrace();
		}

		try (ResultSet rs = selectReportByPlayer.executeQuery()) {
			while (rs.next()) {
				Report report = parse(rs);
				report.addComments(getComments(report.getId()));
				reports.add(report);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Error reading reports");
			e.printStackTrace();
		}
		return reports;
	}

	/**
	 * Grabs a report by it's ID
	 *
	 * @param id
	 *            The ID of the report to grab
	 * @return The report parsed, or null if no report by that ID exists
	 */
	public Report read(int id) {
		Report report = null;

		try {
			selectReportById.setInt(1, id);
		} catch (SQLException e) {
			plugin.getLogger().severe("Error creating select by ID message");
			e.printStackTrace();
		}

		try (ResultSet rs = selectReportById.executeQuery()) {
			if (!rs.next()) {
				return null;
			}
			report = parse(rs);
			report.addComments(getComments(report.getId()));
		} catch (SQLException e) {
			plugin.getLogger().severe("Error reading report with ID " + id);
			e.printStackTrace();
		}

		return report;
	}

	public List<Comment> getComments(int id) {
		List<Comment> list = new ArrayList<Comment>();
		try {
			selectCommentById.setInt(1, id);
		} catch (SQLException e1) {
			plugin.getLogger().severe("Error preparing comment statement for id " + id);
			e1.printStackTrace();
		}
		try (ResultSet comments = selectCommentById.executeQuery()) {
			while (comments.next()) {
				UUID uuid = UUID.fromString(comments.getString("commenter"));
				String message = comments.getString("comment");

				list.add(new Comment(message, uuid));
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Error reading comments with ID " + id);
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * Attempts to parse the current full row into a report which can be used by the
	 * other classes
	 */
	private Report parse(ResultSet rs) {
		// Initial conditions
		try {
			int id = rs.getInt("id");
			boolean resolved = rs.getBoolean("resolved");

			UUID reporter = UUID.fromString(rs.getString("reporter"));
			String reportMessage = rs.getString("reportMessage");
			Timestamp reportTime = rs.getTimestamp("reportTime");
			Location reportLoc = Util.stringToLoc(rs.getString("reportLoc"));

			String s = rs.getString("resolver");
			UUID resolver = null;
			if (s != null) {
				resolver = UUID.fromString(s);
			}
			Timestamp resolveTime = rs.getTimestamp("resolveTime");
			return new Report(reporter, reportMessage, reportTime, reportLoc, id, resolved, resolver, resolveTime);

		} catch (SQLException e) {
			plugin.getLogger().severe("Error reading report (no comments)");
			e.printStackTrace();
		}
		return null;
	}

}
