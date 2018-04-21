package com.github.pocketkid2.report;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class GUI {

	// Main GUI menu
	public static Inventory main;
	// Report GUI menu
	public static Inventory report;

	public static void createMainMenu(ReportPlugin pl, Player p) {
		int reportCount = pl.getReportManager().read().size();
		int finalSize = 9;
		// If we need more than one line
		if ((reportCount - 7) > 0) {
			reportCount -= 7;

			// Find how many lines will be filled
			int lines = reportCount / 9;
			// Add one if we need a partial line
			if ((reportCount % 9) > 0) {
				lines++;
			}

			// Now add to finalSize
			finalSize += (lines * 9);
		}

		// Error check
		if (finalSize > 54) {
			p.sendMessage(ChatColor.RED + "Error: too many lines for menu");
			return;
		}

		main = Bukkit.createInventory(p, finalSize, "bReport menu");

		main.setItem(0, GUI.createItemWithName(Material.BARRIER, "Exit menu"));

		int index = 2;
		for (Report report : pl.getReportManager().read()) {
			main.setItem(index, GUI.createItemWithName(Material.BEACON, report.getId() + " " + Bukkit.getOfflinePlayer(report.getReporter()).getName()));
			index++;
		}
	}

	public static void createReportMenu(ReportPlugin pl, Player p, Report r) {
		int commentCount = r.getComments().size();
		int finalSize = 9;
		if ((commentCount - 2) > 0) {
			commentCount -= 2;

			int lines = commentCount / 9;
			if ((commentCount % 9) > 0) {
				lines++;
			}
			finalSize += (lines * 9);
		}

		// Error check
		if (finalSize > 54) {
			p.sendMessage(ChatColor.RED + "Error: too many lines for menu");
			return;
		}

		report = Bukkit.createInventory(p, finalSize, "Report menu for " + r.getId());

		report.setItem(0, GUI.createItemWithName(Material.BARRIER, "Back"));

		report.setItem(2, GUI.createItemWithNameAndColor(Material.STAINED_GLASS_PANE, "Resolve Report", DyeColor.LIME));
		report.setItem(3, GUI.createItemWithNameAndColor(Material.STAINED_GLASS_PANE, "Unresolve Report", DyeColor.YELLOW));
		report.setItem(4, GUI.createItemWithName(Material.COMMAND, "Teleport to report location"));
		report.setItem(5, GUI.createItemWithNameAndLore(Material.BOOK, "Report Description", r.getReportMessage()));

		int index = 7;
		for (Comment s : r.getComments()) {
			report.setItem(index, GUI.createItemWithNameAndLore(Material.PAPER, "Comment " + (index - 5), String.format(ChatColor.GRAY + "%s: %s", Bukkit.getOfflinePlayer(s.getCommenter()).getName(), s.getMessage())));
			index++;
		}
	}

	/**
	 * Helper function
	 *
	 * @param mat
	 * @param name
	 * @return
	 */
	public static ItemStack createItemWithName(Material mat, String name) {
		ItemStack stack = new ItemStack(mat);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Helper function
	 *
	 * @param mat
	 * @param name
	 * @return
	 */
	public static ItemStack createItemWithNameAndLore(Material mat, String name, String lore) {
		ItemStack stack = new ItemStack(mat);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Helper function
	 *
	 * @param mat
	 * @param name
	 * @return
	 */
	public static ItemStack createItemWithNameAndColor(Material mat, String name, DyeColor col) {
		Dye dye = new Dye();
		dye.setColor(col);
		ItemStack stack = dye.toItemStack(1);
		stack.setType(mat);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}
}

/**
 * Green glass pane resolves the report - DARK OR LIGHT???
 *
 * yellow glass pane unresolves the report if the report was resolved while
 * still in the report menu
 *
 * a commandblock to tp to the report location
 *
 * a book to read the report in the lore of the book or if possible is in a
 * written book to read the report
 *
 * another book to read the report comments if it has any
 *
 *
 *
 */