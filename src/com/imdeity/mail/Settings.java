package com.imdeity.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.util.config.Configuration;

import com.imdeity.mail.util.FileMgmt;

@SuppressWarnings("deprecation")
public class Settings {

	private static Configuration config;
	private Mail plugin = null;

	public Settings(Mail instance) {
		this.plugin = instance;
	}

	public boolean loadSettings(String name, String location) {
		try {
			FileMgmt.checkFolders(new String[] { getRootFolder(),
					getRootFolder() + FileMgmt.fileSeparator() + "" });
			loadConfig(getRootFolder() + FileMgmt.fileSeparator() + name,
					location);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	private static void loadConfig(String filepath, String defaultRes)
			throws IOException {
		File file = FileMgmt.CheckYMLexists(filepath, defaultRes);
		if (file != null) {

			// read the config.yml into memory
			config = new Configuration(file);
			config.load();
			file = null;
		}
	}

	private static Integer getInt(String root) {
		return config.getInt(root.toLowerCase(), 0);
	}

	/*
	 * Public Functions to read data from the Configuration and Language data
	 */
	private static String getString(String root) {
		return config.getString(root.toLowerCase());
	}

	/*
	 * Sets a property and saves the Configuration
	 */
	public static void setProperty(String root, Object value) {
		config.setProperty(root.toLowerCase(), value);
		config.save();
	}

	public String getRootFolder() {
		if (this != null)
			return plugin.getDataFolder().getPath();
		else
			return "";
	}

	// /////////////////////////////////

	public static String getMySQLServerAddress() {
		return getString("mysql.server.ADDRESS");
	}

	public static int getMySQLServerPort() {
		return getInt("mysql.server.PORT");
	}

	public static String getMySQLUsername() {
		return getString("mysql.server.USERNAME");
	}

	public static String getMySQLPassword() {
		return getString("mysql.server.PASSWORD");
	}

	public static String getMySQLDatabaseName() {
		return getString("mysql.database.NAME");
	}

	public static String getMySQLDatabaseTablePrefix() {
		return getString("mysql.database.TABLE_PREFIX");
	}

	// //////////////////////

}