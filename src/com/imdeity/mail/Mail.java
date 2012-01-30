package com.imdeity.mail;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.mail.cmd.MailCommand;
import com.imdeity.mail.event.MailPlayerListener;
import com.imdeity.mail.object.Language;
import com.imdeity.mail.sql.MailSQL;
import com.imdeity.mail.sql.MySQLConnection;
import com.imdeity.mail.util.ChatTools;

public class Mail extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public static Mail mail = null;
	public static MySQLConnection database = null;
	public static boolean hasError = false;
	private Settings settings = null;
	public Language language = null;

	@Override
	public void onDisable() {
		language.save();
		out("Disabled");
	}

	@Override
	public void onEnable() {
		this.reloadConfigs();
		Mail.mail = this;
		getCommand("mail").setExecutor(new MailCommand(this));
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

	public void reloadConfigs() {
		this.language = null;
		this.settings = null;
		this.language = new Language();
		this.language.loadDefaults();
		this.settings = new Settings(this);
		this.settings.loadSettings("config.yml", "/config.yml");
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
			this.sendPlayerMessage(receiver, Language.getInboxNew());
			this.sendPlayerMessage(receiver, Language.getInboxCheck());
		}
	}

	public void sendPlayerMessage(Player player, String message) {
		ChatTools.formatAndSend(Language.getHeader() + message, player);
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
