package com.github.pocketkid2.report;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ReportCommand implements CommandExecutor {

	private ReportPlugin plugin;

	public ReportCommand(ReportPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Not enough arguments!");
			return false;
		} else {
			if (args[0].equalsIgnoreCase("file")) {
				if (sender.hasPermission("report.file")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if ((args.length - 1) < plugin.getSettings().getMinWords()) {
							player.sendMessage("Report must be at least " + plugin.getSettings().getMinWords() + " words long!");
						} else {
							String[] parts = new String[args.length - 1];
							System.arraycopy(args, 1, parts, 0, parts.length);
							String message = String.join(" ", parts);
							// File report
							plugin.getReportManager().report(player, message);
							player.sendMessage("Report has been filed!");
						}
					} else {
						sender.sendMessage("You must be a player to file a report!");
					}
				} else {
					sender.sendMessage("You don't have permission for that!");
				}
			} else if (args[0].equalsIgnoreCase("read")) {
				if (sender.hasPermission("report.read")) {
					if (args.length == 1) {
						// List all reports
						List<Report> reports = plugin.getReportManager().read();
						sender.sendMessage(String.format("Unresolved reports (%d): ", reports.size()));
						for (Report r : reports) {
							sender.sendMessage(String.format("ID: %d, Playername: %s", r.getId(), Bukkit.getOfflinePlayer(r.getFiler()).getName()));
						}
					} else if (args.length == 2) {
						// Grab a certain report
						Report report = null;
						try {
							report = plugin.getReportManager().read(Integer.parseInt(args[1]));
						} catch (NumberFormatException e) {
							sender.sendMessage(String.format("Invalid id '%s'", args[1]));
							return false;
						}
						if (report == null) {
							sender.sendMessage("There are no reports with that ID!");
						} else {
							sender.sendMessage("Report ID: " + report.getId());
							sender.sendMessage("Reported by: " + Bukkit.getOfflinePlayer(report.getFiler()).getName());
							sender.sendMessage("Details: " + report.getReportMessage());
							sender.sendMessage("Current status: " + (report.isResolved() ? (ChatColor.GREEN + "resolved") : (ChatColor.RED + "unresolved")));
						}
					} else {
						sender.sendMessage("Too many arguments!");
						return false;
					}
				} else {
					sender.sendMessage("You don't have permission for that!");
				}
			} else if (args[0].equalsIgnoreCase("resolve")) {
				if (sender.hasPermission("report.resolve")) {
					if (args.length == 1) {
						sender.sendMessage("You must provide a report ID!");
					} else if (args.length > 2) {
						// TODO admin resolve comments
						sender.sendMessage("Too many arguments!");
						return false;
					} else {
						// Resolve the report
						int id;
						try {
							id = Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							sender.sendMessage("Invalid ID " + args[1]);
							return true;
						}
						if (plugin.getReportManager().read(id) == null) {
							sender.sendMessage("There is no report with that ID!");
						} else {
							plugin.getReportManager().resolve(id);
							sender.sendMessage("Report with ID " + id + " has been resolved!");
						}
					}
				} else {
					sender.sendMessage("You don't have permission for that!");
				}
			} else {
				sender.sendMessage("Not implemented yet!");
			}
			return true;
		}
	}

}
