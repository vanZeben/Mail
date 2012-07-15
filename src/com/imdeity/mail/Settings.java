package com.imdeity.mail;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings {

	private YamlConfiguration config = null;
	private Plugin plugin;

	public Settings(Mail mail) {
		plugin = mail;
	}

	public void loadDefaults() {
		File tmpconfig = new File(plugin.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(tmpconfig);
		if (!config.contains("mysql.server.address"))
			config.set("mysql.server.address", "localhost");
		if (!config.contains("mysql.server.port"))
			config.set("mysql.server.port", 3306);
		if (!config.contains("mysql.database.name"))
			config.set("mysql.database.name", "kingdoms");
		if (!config.contains("mysql.database.username"))
			config.set("mysql.database.username", "root");
		if (!config.contains("mysql.database.password"))
			config.set("mysql.database.password", "root");
		if (!config.contains("mysql.database.table_prefix"))
			config.set("mysql.database.table_prefix", "mail_");
		this.save();
	}

	public void save() {
		try {
			this.config.save(new File(plugin.getDataFolder(),"config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMySQLServerAddress() {
		return config.getString("mysql.server.address");
	}

	public int getMySQLServerPort() {
		return config.getInt("mysql.server.port");
	}

	public String getMySQLDatabaseName() {
		return config.getString("mysql.database.name");
	}

	public String getMySQLDatabaseUsername() {
		return config.getString("mysql.database.username");
	}

	public String getMySQLDatabasePassword() {
		return config.getString("mysql.database.password");
	}

	public String getMySQLDatabaseTablePrefix() {
		return config.getString("mysql.database.table_prefix");
	}
}