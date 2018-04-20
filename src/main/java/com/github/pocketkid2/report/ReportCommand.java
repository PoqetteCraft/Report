package com.github.pocketkid2.report;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class ReportCommand implements CommandExecutor {

	private ReportPlugin plugin;

	public ReportCommand(ReportPlugin plugin) {
		this.plugin = plugin;
	}

	public String[] shifted(String[] args) {
		String[] a2 = new String[args.length - 1];
		System.arraycopy(args, 1, a2, 0, args.length - 1);
		return a2;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Sender must be a player
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.MUST_BE_PLAYER);
			return true;
		}
		Player player = (Player) sender;

		// Check for command aliases
		if (label.equalsIgnoreCase("read")) {
			read(player, args);
		} else if (label.equalsIgnoreCase("resolve")) {
			resolve(player, args);
		} else if (label.equalsIgnoreCase("unresolve")) {
			unresolve(player, args);
		} else if (label.equalsIgnoreCase("comment")) {
			comment(player, args);
		} else if (label.equalsIgnoreCase("gotoreport")) {
			teleport(player, args);
		} else if (label.equalsIgnoreCase("rgui")) {
			gui(player);
		} else {
			if (args.length == 0) {
				// Not enough arguments! Display stuff
				PluginDescriptionFile pdf = plugin.getDescription();
				player.sendMessage(pdf.getFullName());
				player.sendMessage(Messages.REPORT_FILE_HELP);
			} else {
				// Read the argument
				if (args[0].equalsIgnoreCase("file")) {
					file(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("read")) {
					read(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("resolve")) {
					resolve(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("unresolve")) {
					unresolve(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("comment")) {
					comment(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("teleport")) {
					teleport(player, shifted(args));
				} else if (args[0].equalsIgnoreCase("gui")) {
					gui(player);
				} else {
					file(player, args);
				}
			}
		}
		return true;
	}

	private void gui(Player player) {
		// Check permission
		if (!(player.hasPermission("report.gui"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}
		player.sendMessage("Not implemented yet!");
	}

	private void teleport(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.teleport"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		if (args.length > 1) {
			try {
				int id = Integer.parseInt(args[0]);
				Report r = plugin.getReportManager().read(id);
				if (r == null) {
					player.sendMessage(Messages.NO_REPORT);
				} else {
					player.teleport(r.getReportLoc());
				}
			} catch (NumberFormatException e) {
				player.sendMessage(Messages.INVALID_ID);
			}
		} else {
			player.sendMessage(Messages.ID_REQUIRED);
		}
	}

	private void comment(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.comment"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		if (args.length > 1) {
			try {
				int id = Integer.parseInt(args[0]);
				plugin.getReportManager().comment(id, player, String.join(" ", shifted(args)));
				player.sendMessage(String.format(Messages.COMMENT_SUCCESSFUL, id));
			} catch (NumberFormatException e) {
				player.sendMessage(Messages.INVALID_ID);
			}
		} else {
			player.sendMessage(Messages.ID_REQUIRED);
		}
	}

	private void unresolve(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.unresolve"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		if (args.length > 1) {
			try {
				int id = Integer.parseInt(args[0]);
				plugin.getReportManager().unresolve(id);
				player.sendMessage(String.format(Messages.UNRESOLVE_SUCCESSFUL, id));
			} catch (NumberFormatException e) {
				player.sendMessage(Messages.INVALID_ID);
			}
		} else {
			player.sendMessage(Messages.ID_REQUIRED);
		}
	}

	private void resolve(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.resolve"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		if (args.length > 1) {
			try {
				int id = Integer.parseInt(args[0]);
				plugin.getReportManager().resolve(id, player, String.join(" ", shifted(args)));
				player.sendMessage(String.format(Messages.RESOLVE_SUCCESSFUL, id));
			} catch (NumberFormatException e) {
				player.sendMessage(Messages.INVALID_ID);
			}
		} else {
			player.sendMessage(Messages.ID_REQUIRED);
		}
	}

	private void read(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.read"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		// Check if there is an id
		if (args.length > 1) {
			try {
				int id = Integer.parseInt(args[0]);
				Report r = plugin.getReportManager().read(id);
				if (r == null) {
					player.sendMessage(Messages.NO_REPORT);
				} else {
					player.sendMessage(String.format(Messages.REPORT_TITLE, r.getId(), Bukkit.getOfflinePlayer(r.getReporter()), Util.durationFormat(Duration.between(r.getReportTime().toLocalDateTime(), LocalDateTime.now()))));
					player.sendMessage(String.format(Messages.REPORT_DETAILS, r.getReportMessage()));
					if (r.isResolved()) {
						player.sendMessage(String.format(Messages.REPORT_RESOLVED, Bukkit.getOfflinePlayer(r.getResolver()).getName(), Util.durationFormat(Duration.between(r.getResolveTime().toLocalDateTime(), LocalDateTime.now()))));
					} else {
						player.sendMessage(Messages.REPORT_UNRESOLVED);
					}
					if (r.getComments().isEmpty()) {
						player.sendMessage(Messages.REPORT_NO_COMMENTS);
					} else {
						player.sendMessage(String.format(Messages.REPORT_HAS_COMMENTS, r.getComments().size()));
						for (Comment c : r.getComments()) {
							player.sendMessage(String.format(Messages.REPORT_COMMENT, Bukkit.getOfflinePlayer(c.getCommenter()).getName(), c.getMessage()));
						}
					}
				}

			} catch (NumberFormatException e) {
				player.sendMessage(Messages.INVALID_ID);
			}
		} else {
			List<Report> reports = plugin.getReportManager().read();
			int size = reports.size();
			if (size > 0) {
				player.sendMessage(String.format(Messages.REPORT_COUNT, size));
				for (Report r : reports) {
					player.sendMessage(String.format(Messages.REPORT_TITLE, r.getId(), Bukkit.getOfflinePlayer(r.getReporter()), Util.durationFormat(Duration.between(r.getReportTime().toLocalDateTime(), LocalDateTime.now()))));
				}
			} else {
				player.sendMessage(Messages.NO_REPORTS);
			}
		}
	}

	private void file(Player player, String[] args) {
		// Check permission
		if (!(player.hasPermission("report.file"))) {
			player.sendMessage(Messages.NO_PERM);
			return;
		}

		// Check that there are enough arguments
		if (args.length < plugin.getSettings().getMinWords()) {
			player.sendMessage(Messages.REPORT_TOO_SHORT);
			return;
		}

		String message = String.join(" ", args);

		// Check that there are no reports like this
		for (Report r : plugin.getReportManager().read(player)) {
			if ((r.isResolved() == false) && (Levenshtein.distance(r.getReportMessage(), message) < plugin.getSettings().getMinDistance())) {
				player.sendMessage(Messages.REPORT_TOO_SIMILAR);
				return;
			}
		}

		// File the report
		plugin.getReportManager().report(player, message);
		player.sendMessage(Messages.REPORT_FILED);
	}

}
