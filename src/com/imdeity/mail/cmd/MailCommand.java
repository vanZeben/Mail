package com.imdeity.mail.cmd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.mail.Mail;
import com.imdeity.mail.object.Language;
import com.imdeity.mail.sql.MailSQL;
import com.imdeity.mail.util.ChatTools;

public class MailCommand implements CommandExecutor {

	private Mail plugin = null;

	public MailCommand(Mail instance) {
		plugin = instance;
	}

	private void sendCommands(Player player) {
		List<String> output = new ArrayList<String>();
		output.add(Language.getMailCommandHeader());
		if (player.hasPermission("mail.admin")) {
			output.add(this.formatCommands("/mail <player>", "Checks Inbox."));
			output.add(this.formatCommands("/mail write [player] [message]",
					"Sends a message to the specified person."));
			output.add(this.formatCommands("/mail read <player> [num]",
					"Opens up the specified message."));
			output.add(this.formatCommands("/mail delete [num/*]",
					"Removes the specified message from your inbox."));
			output.add(this.formatCommands("/mail reload",
					"Reloads the language and config files"));
		} else {
			output.add(this.formatCommands("/mail", "Checks Inbox."));
			output.add(this.formatCommands("/mail write [player] [message]",
					"Sends a message to the specified person."));
			output.add(this.formatCommands("/mail read [num]",
					"Opens up the specified message."));
			output.add(this.formatCommands("/mail delete [num/*]",
					"Removes the specified message from your inbox."));
		}
		output.add(this.formatCommands("/mail info", "Plugin Information"));
		for (String msg : output) {
			ChatTools.formatAndSend(msg, player);
		}
	}

	private String formatCommands(String command, String explanation) {
		String tmp = Language.getMailCommandMessage()
				.replaceAll("%command", command)
				.replaceAll("%explanation", explanation);
		return tmp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String args[]) {
		if (Mail.hasError) {
			sender.sendMessage("[Mail] Config is not set up correctly. Commands will not work until this is fixed");
			return false;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			try {
				parseCommand(player, args);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public void parseCommand(Player player, String[] split) throws SQLException {
		if (split.length == 0) {
			checkMail(player);
		} else if (split[0].equalsIgnoreCase("help")
				|| split[0].equalsIgnoreCase("?")) {
			sendCommands(player);
		} else if (split[0].equalsIgnoreCase("read")
				|| split[0].equalsIgnoreCase("r")) {
			readCommand(player, split);
		} else if (split[0].equalsIgnoreCase("delete")
				|| split[0].equalsIgnoreCase("d")) {
			closeCommand(player, split);
		} else if (split[0].equalsIgnoreCase("write")
				|| split[0].equalsIgnoreCase("w")
				|| split[0].equalsIgnoreCase("send")
				|| split[0].equalsIgnoreCase("s")) {
			writeCommand(player, split);
		} else if (split[0].equalsIgnoreCase("reload")) {
			Mail.mail.reloadConfig();
			Mail.mail.sendPlayerMessage(player,
					"Reloaded Config.yml and Language.yml");
		} else if (split[0].equalsIgnoreCase("info")) {
			ArrayList<String> out = new ArrayList<String>();
			out.add("&3-----[&bMail Information&3]-----");
			out.add("&3#&0-&b###&0-");
			out.add("&3&0--&b#&0--&b#    &3Developed by: &bvanZeben");
			out.add("&3#&0-&b#&0--&b#");
			out.add("&3#&0-&b#&0--&b#    &3\'The affordable courier.\'");
			out.add("&3#&0-&b###&0-");
			for (String s : out) {
				ChatTools.formatAndSend(s, player);
			}
		} else if (split.length == 1) {
			checkMail(player, split[0]);
		} else {
			help(player);
		}
	}

	private void checkMail(Player player) {
		if (player.hasPermission("mail.player.read")) {
			MailSQL.getAllMail(player);
		}
	}

	private void checkMail(Player player, String playername) {
		if (player.hasPermission("mail.admin")) {
			MailSQL.getAllMail(player, playername);
		}
	}

	private void readCommand(Player player, String[] split) {
		if (split.length == 2) {
			if (player.hasPermission("mail.player.read")) {

				int id = 0;
				try {
					id = Integer.parseInt(split[1]);
				} catch (NumberFormatException ex) {
					this.help(player);
				}
				MailSQL.getSpecificMail(player, id);
			} else {
				plugin.sendPlayerMessage(player,
						Language.getMailErrorPermission());
			}
		} else if (split.length == 3) {
			if (player.hasPermission("mail.admin")) {
				String playername = split[1];
				int id = 0;
				try {
					id = Integer.parseInt(split[2]);
				} catch (NumberFormatException ex) {
					this.help(player);
				}
				MailSQL.getSpecificMail(player, playername, id);
			} else {
				plugin.sendPlayerMessage(player,
						Language.getMailErrorPermission());
			}
		} else {
			help(player);
		}

	}

	private void closeCommand(Player player, String[] split)
			throws SQLException {
		if (player.hasPermission("mail.player.delete")) {
			if (split.length == 2) {
				int id = 0;
				if (split[1].equalsIgnoreCase("*")) {
					MailSQL.setAllClosedMail(player);
					return;
				}
				try {
					id = Integer.parseInt(split[1]);
				} catch (NumberFormatException ex) {
					help(player);
				}
				MailSQL.setClosedMail(player, id);
			} else {
				help(player);
			}
		} else {
			plugin.sendPlayerMessage(player, Language.getMailErrorPermission());
		}
	}

	private void writeCommand(Player player, String[] split)
			throws SQLException {
		if (player.hasPermission("mail.player.write")) {
			if (split.length < 3) {
				this.help(player);
				return;
			}
			if (split.length <= 5) {
				plugin.sendPlayerMessage(
						player,
						Language.getMailNotLongEnough().replaceAll(
								"%messageLength", "3"));
				return;
			}
			String sender = player.getName();
			String receiver = split[1];
			String message = "";
			for (int i = 2; i <= split.length - 1; i++) {
				if (i == 2)
					message += split[i];
				else
					message += " " + split[i];
			}
			MailSQL.sendMail(sender, receiver, message);
			plugin.sendPlayerMessage(
					player,
					Language.getMailSent()
							.replaceAll("%messageReceiver", receiver)
							.replaceAll("%messageMessage", message));
		} else {
			plugin.sendPlayerMessage(player, Language.getMailErrorPermission());
		}
	}

	private void help(Player player) {
		plugin.sendPlayerMessage(player, Language.getMailErrorHelp());
	}

}
