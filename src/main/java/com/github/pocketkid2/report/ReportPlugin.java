package com.github.pocketkid2.report;

import com.github.pocketkid2.database.Database;
import com.github.pocketkid2.database.DatabasePlugin;

public class ReportPlugin extends DatabasePlugin {

	@Override
	public void onEnable() {
		Database.register(this);
		getLogger().info("Done!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Done!");
	}
}
