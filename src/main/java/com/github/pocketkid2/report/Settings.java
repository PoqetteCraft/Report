package com.github.pocketkid2.report;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
	private final int minDistance;

	private final int minWords;

	public Settings(FileConfiguration config) {

		minDistance = config.getInt("minimum.distance");

		minWords = config.getInt("minimum.words");
	}

	/**
	 * @return the minDistance
	 */
	public int getMinDistance() {
		return minDistance;
	}

	/**
	 * @return the minWords
	 */
	public int getMinWords() {
		return minWords;
	}

}
