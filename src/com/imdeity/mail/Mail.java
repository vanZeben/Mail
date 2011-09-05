package com.imdeity.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Mail extends JavaPlugin {

    public final Logger log = Logger.getLogger("Minecraft");
    public static MySQLConnection database = null;
    public static PermissionHandler permissions = null;
    public static ArrayList<MailObject> mailCache = new ArrayList<MailObject>();
    private boolean hasRegistered = false;
    private Settings settings = null;

    @Override
    public void onDisable() {
        out("Disabled");

    }

    @Override
    public void onEnable() {
        if (!this.hasRegistered) {
            this.hasRegistered = true;
            
            settings = new Settings(this);
            settings.loadSettings("config.yml", "/config.yml");
            
            getCommand("mail").setExecutor(new MailCommand(this));
            getServer().getPluginManager().registerEvent(
                    Event.Type.PLAYER_JOIN, new MailPlayerListener(this),
                    Event.Priority.High, this);
            
            database = new MySQLConnection();
            database.createDatabaseTables();
            checkPlugins();
        }
        out("Enabled");
    }

    private void checkPlugins() {
        List<String> using = new ArrayList<String>();

        Plugin test = getServer().getPluginManager().getPlugin("Permissions");
        if (test != null) {
            permissions = ((Permissions) test).getHandler();
            using.add("Permissions");
        }
        if (using.size() > 0)
            out("Using: " + StringMgmt.join(using, ", ") + ".");
    }

    public Player getPlayer(String playername) {
        Player player = null;
        for (Player checkPlayer : this.getServer().getOnlinePlayers()) {
            if (checkPlayer.getName().equalsIgnoreCase(playername)) {
                player = checkPlayer;
                return player;
            }
        }
        return null;
    }

    public void notifyReceiver(String playername) {
        Player receiver = this.getPlayer(playername);
        if (receiver != null) {
            ChatTools.formatAndSend("<option><green>You have got a message!",
                    "Mail", receiver);
            ChatTools.formatAndSend(
                    "<option><gray>Use /mail to see your inbox.", "Mail",
                    receiver);
        }
    }

    public void out(String message) {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] " + message);
    }
}
