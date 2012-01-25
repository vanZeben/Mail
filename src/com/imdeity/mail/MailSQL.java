package com.imdeity.mail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

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
		String sql = "SELECT @rownum:=@rownum+1 AS rownum, id, sender, receiver, message FROM "
				+ Mail.database.tableName("mail")
				+ ", (SELECT @rownum:=0) r WHERE `receiver` = '"
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
				int index = Integer.parseInt(result.get(i).get(0));
				int id = Integer.parseInt(result.get(i).get(1));
				String sender = result.get(i).get(2);
				String receiver = result.get(i).get(3);
				String message = result.get(i).get(4);
				MailObject mail = new MailObject(id, index, sender, receiver,
						message);
				ChatTools.formatAndSend("<option>" + mail.toShortString(),
						"Mail", player);
			}
		}
	}

	public static void getSpecificMail(Player player, int id) {
		MailObject tmpMail = getMail(player, id);
		if (tmpMail == null) {
			ChatTools
					.formatAndSend("<option><gray>That message doesn't exist.",
							"Mail", player);
		} else {
			ChatTools.formatAndSend("<option>" + tmpMail.toLongString(),
					"Mail", player);
		}
	}

	private static MailObject getMail(Player player, int id) {
		String sql = "SELECT @rownum:=@rownum+1 AS rownum, id, sender, receiver, message FROM "
				+ Mail.database.tableName("mail")
				+ ", (SELECT @rownum:=0) r WHERE `receiver` = '"
				+ player.getName().toLowerCase() + "' AND `read` = '0';";
		HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql);
		if (!result.isEmpty() && result.size() >= id) {
			int index = Integer.parseInt(result.get(id).get(0));
			int id1 = Integer.parseInt(result.get(id).get(1));
			String sender = result.get(id).get(2);
			String receiver = result.get(id).get(3);
			String message = result.get(id).get(4);
			MailObject mail = new MailObject(id1, index, sender, receiver,
					message);
			return mail;
		}
		return null;
	}

	public static void setClosedMail(Player player, int id) throws SQLException {
		MailObject tmpMail = getMail(player, id);
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
}
