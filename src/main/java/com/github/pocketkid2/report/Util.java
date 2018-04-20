package com.github.pocketkid2.report;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Util {

	public static String locToString(Location loc) {
		return String.format("%s,%s,%s,%s,%s,%s", loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName(), loc.getPitch(), loc.getYaw());
	}

	public static Location stringToLoc(String str) {
		String[] a = str.split(",");
		double x = Double.parseDouble(a[0]);
		double y = Double.parseDouble(a[1]);
		double z = Double.parseDouble(a[2]);
		World world = Bukkit.getWorld(a[3]);
		float yaw = Float.parseFloat(a[5]);
		float pitch = Float.parseFloat(a[4]);
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static String durationFormat(Duration d) {
		List<String> s = new ArrayList<String>();
		long days = d.toDays();
		long hours = d.minusDays(days).toHours();
		long minutes = d.minusDays(days).minusHours(hours).toMinutes();
		if (days > 0) {
			s.add(String.format("%s days", days));
		}
		if (hours > 0) {
			s.add(String.format("%s hours", hours));
		}
		if (minutes > 0) {
			s.add(String.format("%s min", minutes));
		}
		return String.join(" ", s);
	}
}
