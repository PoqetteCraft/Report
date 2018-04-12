package com.github.pocketkid2.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.pocketkid2.database.Database;

public class ReportManager {

	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS reports (id INTEGER PRIMARY KEY AUTO_INCREMENT, uuid CHAR(36), report VARCHAR(%d), resolved BOOLEAN DEFAULT false);";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS reports;";
	private static final String INSERT_REPORT = "INSERT INTO reports (uuid, report) VALUES (?, ?);";
	private static final String UPDATE_RESOLVE = "UPDATE reports SET resolved = true WHERE id = ?;";
	private static final String SELECT_UNRESOLVED = "SELECT * FROM reports WHERE resolved = false;";
	private static final String SELECT_BY_ID = "SELECT * FROM reports WHERE id = ?;";

	private PreparedStatement insertReport;
	private PreparedStatement updateResolve;
	private PreparedStatement selectUnresolved;
	private PreparedStatement selectById;

	private ReportPlugin plugin;

	public ReportManager(ReportPlugin plugin) {
		this.plugin = plugin;
	}

	protected void initStatements() {
		insertReport = Database.prepare(INSERT_REPORT);
		updateResolve = Database.prepare(UPDATE_RESOLVE);
		selectUnresolved = Database.prepare(SELECT_UNRESOLVED);
		selectById = Database.prepare(SELECT_BY_ID);
	}

	protected void initTable() {
		try (Statement s = Database.create()) {
			s.executeUpdate(String.format(CREATE_TABLE, plugin.getSettings().getMaxLength()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void removeTable() {
		try (Statement s = Database.create()) {
			s.executeUpdate(DROP_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			insertReport.setString(2, message);
			insertReport.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Resolves an existing report
	 *
	 * @param id
	 *            The ID of the report to resolve
	 */
	public void resolve(int id) {
		try {
			updateResolve.setInt(1, id);
			updateResolve.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
				reports.add(parse(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			selectById.setInt(1, id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (ResultSet rs = selectById.executeQuery()) {
			if (rs.next()) {
				report = parse(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return report;
	}

	private Report parse(ResultSet rs) {
		try {
			int id = rs.getInt(1);
			UUID uuid = UUID.fromString(rs.getString(2));
			String report = rs.getString(3);
			boolean resolved = rs.getBoolean(4);
			return new Report(id, uuid, report, resolved);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
