package com.imdeity.mail;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.mail.cmd.MailCommand;
import com.imdeity.mail.event.MailPlayerListener;
import com.imdeity.mail.sql.MailSQL;
import com.imdeity.mail.sql.MySQLConnection;
import com.imdeity.mail.util.ChatTools;

public class Mail extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public static Mail mail = null;
	public static MySQLConnection database = null;
	public static boolean hasError = false;
	private Settings settings = null;

	@Override
	public void onDisable() {
		out("Disabled");
	}

	@Override
	public void onEnable() {
		settings = new Settings(this);
		settings.loadSettings("config.yml", "/config.yml");
		Mail.mail = this;

		getCommand("mail").setExecutor(new MailCommand());

		try {
			setupDatabase();
		} catch (Exception ex) {
			out("Database is set up incorrectly. Please configure the config.yml before procedeing");
			hasError = true;
		}
		if (!hasError) {
			getServer().getPluginManager().registerEvents(
					new MailPlayerListener(this), this);
		}
		out("Enabled");

	}

	public void setupDatabase() throws Exception {
		database = new MySQLConnection();
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

	public static void sendMailToPlayer(String sender, String receiver,
			String message) throws SQLException {
		MailSQL.sendMail(sender, receiver, message);
	}

	public static void sendUnreadMailCountMessage(String player) {
		MailSQL.sendUnreadCount(player);
	}
}
