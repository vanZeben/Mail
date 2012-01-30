package com.imdeity.mail.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.imdeity.mail.Mail;
import com.imdeity.mail.object.MailObject;
import com.imdeity.mail.util.ChatTools;

public class MailSQL {
	public Mail plugin;

	public MailSQL(Mail instance) {
		this.plugin = instance;
	}

	public static int getUnreadCount(String playername) {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM " + Mail.database.tableName("mail")
				+ " WHERE `receiver` = '" + playername + "' AND `read` = '0';";
		HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql);
		count = Integer.parseInt(result.get(1).get(0));
		return count;
	}

	// public static int getIndex(String playerName, String message) {
	// String sql = "SELECT `id` FROM " + Mail.database.tableName("mail")
	// + " WHERE `receiver` = '" + playerName + "' AND `message` = ?;";
	// HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql,
	// message);
	// int index = Integer.parseInt(result.get(1).get(0));
	// return index;
	// }

	public static boolean sendMail(String sender, String receiver,
			String message) throws SQLException {
		String sql = "INSERT INTO " + Mail.database.tableName("mail") + " ("
				+ "`sender`, `receiver`, `message`" + ") values (?,?,?);";
		Mail.database.Write(sql, sender, receiver, message);
		Mail.mail.notifyReceiver(receiver);
		return true;
	}

	public static void getAllMail(Player player) {
		String sql = "SELECT id, sender, receiver, message FROM "
				+ Mail.database.tableName("mail") + " WHERE `receiver` = '"
				+ player.getName().toLowerCase() + "' AND `read` = '0';";
		HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql);
		if (result.isEmpty()) {
			ChatTools.formatAndSend("<option><gray>Nothing in your inbox.",
					"Mail", player);
			ChatTools.formatAndSend(
					"<option><yellow>Use \"/mail ?\" for command help.",
					"Mail", player);
			return;
		} else {
			for (int i = 1; i <= result.size(); i++) {
				if (i >= 15) {
					return;
				}
				int index = i;
				int id = Integer.parseInt(result.get(i).get(0));
				String sender = result.get(i).get(1);
				String receiver = result.get(i).get(2);
				String message = result.get(i).get(3);
				MailObject mail = new MailObject(id, index, sender, receiver,
						message);
				ChatTools.formatAndSend("<option>" + mail.toShortString(),
						"Mail", player);
			}
		}
	}

	public static void getAllMail(Player player, String playername) {
		String sql = "SELECT id, sender, receiver, message FROM "
				+ Mail.database.tableName("mail") + " WHERE `receiver` = '"
				+ playername + "' AND `read` = '0';";
		HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql);
		if (result.isEmpty()) {
			ChatTools.formatAndSend("<option><gray>Nothing in " + playername
					+ "'s inbox.", "Mail", player);
			ChatTools.formatAndSend(
					"<option><yellow>Use \"/mail ?\" for command help.",
					"Mail", player);
			return;
		} else {
			for (int i = 1; i <= result.size(); i++) {
				if (i >= 15) {
					return;
				}
				int index = i;
				int id = Integer.parseInt(result.get(i).get(0));
				String sender = result.get(i).get(1);
				String receiver = result.get(i).get(2);
				String message = result.get(i).get(3);
				MailObject mail = new MailObject(id, index, sender, receiver,
						message);
				ChatTools.formatAndSend("<option>" + mail.toShortString(),
						"Mail", player);
			}
		}
	}

	public static void getSpecificMail(Player player, int id) {
		MailObject tmpMail = getMail(player.getName(), id);
		if (tmpMail == null) {
			ChatTools
					.formatAndSend("<option><gray>That message doesn't exist.",
							"Mail", player);
		} else {
			ChatTools.formatAndSend("<option>" + tmpMail.toLongString(),
					"Mail", player);
		}
	}

	public static void getSpecificMail(Player player, String playername, int id) {
		MailObject tmpMail = getMail(playername, id);
		if (tmpMail == null) {
			ChatTools
					.formatAndSend("<option><gray>That message doesn't exist.",
							"Mail", player);
		} else {
			ChatTools.formatAndSend("<option>" + tmpMail.toLongString(),
					"Mail", player);
		}
	}

	private static MailObject getMail(String player, int offset) {
		String sql = "SELECT @rownum:=@rownum+1 AS rownum, id, sender, message, send_date FROM "
				+ Mail.database.tableName("mail")
				+ ", (SELECT @rownum:=0) r WHERE `receiver` = '"
				+ player
				+ "' AND `read` = '0';";
		HashMap<Integer, ArrayList<Object>> result = Mail.database.Read2(sql);
		if (!result.isEmpty() && result.size() >= offset) {
			int index = Integer.parseInt("" + result.get(offset).get(0));
			int id = Integer.parseInt("" + result.get(offset).get(1));
			String sender = "" + result.get(offset).get(2);
			String receiver = player;
			String message = "" + result.get(offset).get(3);
			Date sendDate = null;
			try {
				sendDate = (Date) result.get(offset).get(4);
			} catch (Exception ex) {

			}
			MailObject mail = new MailObject(id, index, sender, receiver,
					message, sendDate);
			return mail;
		}
		return null;
	}

	public static void setClosedMail(Player player, int id) throws SQLException {
		MailObject tmpMail = getMail(player.getName(), id);
		if (tmpMail == null) {
			ChatTools.formatAndSend("<option><red>That message doesn't exist.",
					"Mail", player);
			return;
		}
		String sql = "UPDATE " + Mail.database.tableName("mail")
				+ " SET `read` = '1' WHERE id = '" + tmpMail.getId() + "';";
		Mail.database.Write(sql);
		ChatTools.formatAndSend("<option>Successfully deleted that message.",
				"Mail", player);

	}

	public static void setAllClosedMail(Player player) throws SQLException {
		String sql = "UPDATE " + Mail.database.tableName("mail")
				+ " SET `read` = '1' WHERE `receiver` = '" + player.getName()
				+ "';";
		Mail.database.Write(sql);
		ChatTools.formatAndSend("<option>Successfully deleted your mail.",
				"Mail", player);
	}

	public static void sendUnreadCount(String playername) {
		Player player = Mail.mail.getPlayer(playername);
		if (player.isOnline()) {
			int unread = MailSQL.getUnreadCount(player.getName());
			if (unread > 0)
				if (unread == 1)
					ChatTools
							.formatAndSend(
									("<option><white>You have <yellow>"
											+ unread + "<white> unread message in your inbox."),
									"Mail", player);
				else
					ChatTools
							.formatAndSend(
									("<option><white>You have <yellow>"
											+ unread + "<white> unread messages in your inbox."),
									"Mail", player);
			else
				ChatTools.formatAndSend(
						("<option><gray>Nothing in your inbox."), "Mail",
						player);
		}
	}
}
