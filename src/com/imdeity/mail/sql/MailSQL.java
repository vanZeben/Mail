package com.imdeity.mail.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.imdeity.mail.Mail;
import com.imdeity.mail.object.Language;
import com.imdeity.mail.object.MailObject;

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
			Mail.mail.sendPlayerMessage(player, Language.getInboxEmpty());
			Mail.mail.sendPlayerMessage(player, Language.getMailErrorHelp());
			return;
		} else {
			Mail.mail.sendPlayerMessage(player, "&c--[Your Inbox]--");
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
				for (String s : mail.toShortString()) {
					Mail.mail.sendPlayerMessage(player, s);
				}

			}
		}
	}

	public static void getAllMail(Player player, String playername) {
		String sql = "SELECT id, sender, receiver, message FROM "
				+ Mail.database.tableName("mail") + " WHERE `receiver` = '"
				+ playername + "' AND `read` = '0';";
		HashMap<Integer, ArrayList<String>> result = Mail.database.Read(sql);
		if (result.isEmpty()) {
			Mail.mail.sendPlayerMessage(player, Language.getInboxEmptyOther()
					.replaceAll("%player", playername));
			Mail.mail.sendPlayerMessage(player, Language.getMailErrorHelp());
			return;
		} else {
			Mail.mail.sendPlayerMessage(player, "&c--[" + playername
					+ "'s Inbox]--");
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
				for (String s : mail.toShortString()) {
					Mail.mail.sendPlayerMessage(player, s);
				}
			}
		}
	}

	public static void getSpecificMail(Player player, int id) {
		MailObject tmpMail = getMail(player.getName(), id);
		if (tmpMail == null) {
			Mail.mail.sendPlayerMessage(player,
					Language.getMailErrorInvalidSelected());
			return;
		} else {
			for (String s : tmpMail.toLongString()) {
				Mail.mail.sendPlayerMessage(player, s);
			}
		}
	}

	public static void getSpecificMail(Player player, String playername, int id) {
		MailObject tmpMail = getMail(playername, id);
		if (tmpMail == null) {
			Mail.mail.sendPlayerMessage(player,
					Language.getMailErrorInvalidSelected());
			return;
		} else {
			for (String s : tmpMail.toLongString()) {
				Mail.mail.sendPlayerMessage(player, s);
			}
		}
	}

	private static MailObject getMail(String player, int offset) {
		String sql = "SELECT @rownum:=@rownum+1 AS rownum, id, sender, message, send_date FROM "
				+ Mail.database.tableName("mail")
				+ ", (SELECT @rownum:=0) r WHERE `receiver` = '"
				+ player
				+ "' AND `read` = '0';";
		HashMap<Integer, ArrayList<Object>> result = Mail.database.Read2(sql);
		if (result != null && !result.isEmpty() && result.size() >= offset) {
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
			Mail.mail.sendPlayerMessage(player,
					Language.getMailErrorInvalidSelected());
			return;
		}
		String sql = "UPDATE " + Mail.database.tableName("mail")
				+ " SET `read` = '1' WHERE id = '" + tmpMail.getId() + "';";
		Mail.database.Write(sql);
		for (String line : tmpMail.preformReplacement(Language
				.getMailDeleteSingular().replaceAll("%messageNumber",
						tmpMail.getIndex() + ""))) {
			Mail.mail.sendPlayerMessage(player, line);
		}
	}

	public static void setAllClosedMail(Player player) throws SQLException {
		String sql = "UPDATE " + Mail.database.tableName("mail")
				+ " SET `read` = '1' WHERE `receiver` = '" + player.getName()
				+ "';";
		Mail.database.Write(sql);
		Mail.mail.sendPlayerMessage(player, Language.getMailDeletePlural());
	}

	public static void sendUnreadCount(String playername) {
		Player player = Mail.mail.getPlayer(playername);
		if (player.isOnline()) {
			int unread = MailSQL.getUnreadCount(player.getName());
			if (unread > 0)
				if (unread == 1)
					Mail.mail.sendPlayerMessage(
							player,
							Language.getInboxNewSingular().replaceAll(
									"%numMessages", "" + unread));
				else
					Mail.mail.sendPlayerMessage(
							player,
							Language.getInboxNewPlural().replaceAll(
									"%numMessages", "" + unread));
			else
				Mail.mail.sendPlayerMessage(player, Language.getInboxEmpty());
		}
	}
}
