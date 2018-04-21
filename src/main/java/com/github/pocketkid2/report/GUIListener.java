package com.github.pocketkid2.report;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

	private ReportPlugin plugin;

	public GUIListener(ReportPlugin pl) {
		plugin = pl;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			Inventory inv = event.getInventory();
			ItemStack stack = event.getCurrentItem();

			if (inv.getTitle().equalsIgnoreCase("Report menu")) {
				if (stack.getType() == Material.BARRIER) {
					event.setCancelled(true);
					player.closeInventory();
					return; // EXIT
				}
				if (stack.getType() == Material.BEACON) {
					String[] strings = stack.getItemMeta().getDisplayName().split(" ");
					Report r = plugin.getReportManager().read(Integer.parseInt(strings[0]));
					if (r != null) {
						event.setCancelled(true);
						GUI.createReportMenu(plugin, player, r);
						player.openInventory(GUI.report);
						return; // ENTER SUBMENU
					}
				}
			} else if (inv.getTitle().startsWith("Report menu for ")) {
				if (stack.getType() == Material.BARRIER) {
					GUI.createMainMenu(plugin, player);
					event.setCancelled(true);
					player.openInventory(GUI.main);
					return; // BACK
				}
				if (stack.getType() == Material.COMMAND) {
					String[] strings = inv.getTitle().split(" ");
					Report r = plugin.getReportManager().read(Integer.parseInt(strings[3]));
					if (r != null) {
						event.setCancelled(true);
						player.closeInventory();
						player.teleport(r.getReportLoc());
						return; // TELEPORT
					}
				}
				if ((stack.getType() == Material.BOOK) || (stack.getType() == Material.PAPER)) {
					event.setCancelled(true);
					return; // NO MOVING STUFF
				}
				if (stack.getType() == Material.STAINED_GLASS_PANE) {
					String name = stack.getItemMeta().getDisplayName();
					if (name.equals("Resolve Report")) {
						String[] strings = inv.getTitle().split(" ");
						Report r = plugin.getReportManager().read(Integer.parseInt(strings[3]));
						if (r != null) {
							if (r.isResolved() == false) {
								plugin.getReportManager().resolve(r.getId(), player, null);
								player.sendMessage(ChatColor.GREEN + "Report resolved!");
							} else {
								player.sendMessage(ChatColor.RED + "Report already resolved!");
							}
							event.setCancelled(true);
							return; // RESOLVE
						}
					}
					if (name.equals("Unresolve Report")) {
						String[] strings = inv.getTitle().split(" ");
						Report r = plugin.getReportManager().read(Integer.parseInt(strings[3]));
						if (r != null) {
							if (r.isResolved() == true) {
								plugin.getReportManager().unresolve(r.getId(), player, null);
								player.sendMessage(ChatColor.GREEN + "Report unresolved!");
							} else {
								player.sendMessage(ChatColor.RED + "Report already unresolved!");
							}
							event.setCancelled(true);
							return; // UNRESOLVE
						}
					}

				}
			}

		}
	}

}
