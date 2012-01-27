package com.imdeity.mail.cmd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.mail.Mail;
import com.imdeity.mail.sql.MailSQL;
import com.imdeity.mail.util.ChatTools;

public class MailCommand implements CommandExecutor {

    private static final List<String> output = new ArrayList<String>();

    static {
        output.add(ChatTools.formatTitle("Mail"));
        output.add(ChatTools.formatCommand("", "/mail", "", "Checks Inbox."));
        output.add(ChatTools.formatCommand("", "/mail write",
                "[player] [message]",
                "Sends a message to the specified person."));
        output.add(ChatTools.formatCommand("", "/mail", "read [num]",
                "Opens up the specified message."));
        output.add(ChatTools.formatCommand("", "/mail", "delete [num/*]",
                "Removes the specified message from your inbox."));
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
            for (String o : output)
                player.sendMessage(o);
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
        } else {
            help(player);
        }
    }

    private void checkMail(Player player) {
        if (player.hasPermission("mail.read")) {
            MailSQL.getAllMail(player);
        }
    }

    private void readCommand(Player player, String[] split) {
        if (player.hasPermission("mail.read")) {
            if (split.length == 2) {
                int id = 0;
                try {
                    id = Integer.parseInt(split[1]);
                } catch (NumberFormatException ex) {
                    help(player);
                }
                MailSQL.getSpecificMail(player, id);
            } else {
                help(player);
            }
        } else {
            warn(player, "You dont have permission to perform this action.");
        }
    }

    private void closeCommand(Player player, String[] split)
            throws SQLException {
        if (player.hasPermission("mail.delete")) {
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
            warn(player, "You dont have permission to perform this action.");
        }
    }

    private void writeCommand(Player player, String[] split)
            throws SQLException {
        if (player.hasPermission("mail.write")) {
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
            ChatTools.formatAndSend(
                    "<option><green>Your message has been sent to " + receiver
                            + ".", "Mail", player);
        } else {
            warn(player, "You dont have permission to perform this action.");
        }
    }

    private void help(Player player) {
        ChatTools.formatAndSend(
                "<option><yellow>Invalid Syntax, use \"/mail ?\" for help.",
                "Mail", player);
    }

    private void warn(Player player, String msg) {
        ChatTools.formatAndSend("<option><red>" + msg, "Mail", player);
    }
}
