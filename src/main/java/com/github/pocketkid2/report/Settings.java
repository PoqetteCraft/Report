package com.github.pocketkid2.report;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

	private final int minDistance;
	private final int maxLength;
	private final int minWords;

	public Settings(FileConfiguration config) {
		minDistance = config.getInt("min-distance");
		maxLength = config.getInt("max-length");
		minWords = config.getInt("min-words");
	}

	public int getMinDistance() {
		return minDistance;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getMinWords() {
		return minWords;
	}

}
