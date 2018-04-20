package com.github.pocketkid2.report;

import com.github.pocketkid2.database.Database;
import com.github.pocketkid2.database.DatabasePlugin;

public class ReportPlugin extends DatabasePlugin {

	private ReportManager manager;

	private Settings settings;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		settings = new Settings(getConfig());
		getCommand("report").setExecutor(new ReportCommand(this));
		manager = new ReportManager(this);

		if (Database.register(this)) {
			manager.initStatements();
			manager.initTables();
			getLogger().info("Done!");
		} else {
			getLogger().warning("Finished registering but currently disabled. Plugin will come online when the database connection is re-established.");
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Done!");
	}

	public ReportManager getReportManager() {
		return manager;
	}

	public Settings getSettings() {
		return settings;
	}
}
