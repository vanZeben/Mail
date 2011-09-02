package com.imdeity.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.config.Configuration;

@SuppressWarnings("unused")
public class Settings {
    private static Configuration config;
    private static Configuration language;
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

    /*
     * Functions to pull data from the config and language files
     */
    private static String[] parseString(String str) {
        return parseSingleLineString(str).split("@");
    }

    private static String parseSingleLineString(String str) {
        return str.replaceAll("&", "\u00A7");
    }

    private static Boolean getBoolean(String root) {
        return config.getBoolean(root.toLowerCase(), true);
    }

    private static Double getDouble(String root) {
        return config.getDouble(root.toLowerCase(), 0);
    }

    private static Integer getInt(String root) {
        return config.getInt(root.toLowerCase(), 0);
    }

    private static Long getLong(String root) {
        return Long.parseLong(getString(root).trim());
    }

    /*
     * Public Functions to read data from the Configuration and Language data
     */
    private static String getString(String root) {
        return config.getString(root.toLowerCase());
    }

    private static String getLangString(String root) {
        return parseSingleLineString(language.getString(root.toLowerCase()));
    }

    /*
     * Read a comma delimited string into an Integer list
     */
    private static List<Integer> getIntArr(String root) {

        String[] strArray = getString(root.toLowerCase()).split(",");
        List<Integer> list = new ArrayList<Integer>();
        if (strArray != null) {
            for (int ctr = 0; ctr < strArray.length; ctr++)
                if (strArray[ctr] != null)
                    list.add(Integer.parseInt(strArray[ctr].trim()));
        }
        return list;
    }

    /*
     * Read a comma delimited string into a trimmed list.
     */
    private static List<String> getStrArr(String root) {

        String[] strArray = getString(root.toLowerCase()).split(",");
        List<String> list = new ArrayList<String>();
        if (strArray != null) {
            for (int ctr = 0; ctr < strArray.length; ctr++)
                if (strArray[ctr] != null)
                    list.add(strArray[ctr].trim());
        }
        return list;
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

    ////////////////////////

}