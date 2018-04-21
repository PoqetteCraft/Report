package com.github.pocketkid2.report;

import org.bukkit.ChatColor;

public interface Messages {

	String MUST_BE_PLAYER = ChatColor.RED + "You must be a player!";
	String REPORT_FILE_HELP = "Type /report file <message> to file a report";
	String NO_PERM = ChatColor.RED + "You don't have permission for that!";
	String REPORT_TOO_SHORT = ChatColor.RED + "Your report is too short! Please be more descriptive!";
	String REPORT_TOO_SIMILAR = ChatColor.RED + "Your report is too similar to another one of yours!";
	String REPORT_FILED = "Your report has been filed! Please wait for an admin to respond.";
	String INVALID_ID = ChatColor.RED + "Invalid report ID!";
	String NO_REPORT = ChatColor.RED + "No report with that ID exists!";
	String REPORT_TITLE = "Report ID %d: by player %s filed %s";
	String REPORT_DETAILS = "Details: %s";
	String REPORT_RESOLVED = "Status: " + ChatColor.GREEN + "resolved" + ChatColor.RESET + " by %s %s";
	String REPORT_UNRESOLVED = "Status: " + ChatColor.RED + "unresolved" + ChatColor.RESET;
	String REPORT_NO_COMMENTS = "There are currently no comments";
	String REPORT_HAS_COMMENTS = "There are %d comments:";
	String REPORT_COMMENT = ChatColor.GRAY + "%s: %s";
	String REPORT_COUNT = "There are %d unresolved reports: ";
	String NO_REPORTS = "There are no unresolved reports at this time!";
	String ID_REQUIRED = "You must provide a report ID";
	String UNRESOLVE_SUCCESSFUL = "Unresolved report %d!";
	String RESOLVE_SUCCESSFUL = "Resolved report %d!";
	String COMMENT_SUCCESSFUL = "Comment posted to report ID %d";

}
