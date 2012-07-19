package com.imdeity.mail.cmds.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.deityapi.exception.NegativeMoneyException;
import com.imdeity.mail.MailConfigHelper;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;
import com.imdeity.mail.object.Mail;
import com.imdeity.mail.object.MailManager;
import com.imdeity.mail.object.MailPlayer;
import com.imdeity.mail.object.MailType;

public class MailInboxCommand extends DeityCommandReceiver {
    
    public static Map<String, MailType> lastInboxMailType = new HashMap<String, MailType>();
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        MailPlayer mPlayer = MailManager.getMailPlayer(player.getName());
        List<String[]> output = new ArrayList<String[]>();
        MailType type = null;
        double cost = MailMain.plugin.config.getDouble(MailConfigHelper.MAIL_COST_INBOX);
        if (!DeityAPI.getAPI().getEconAPI().canPay(player.getName(), cost)) {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_FUNDS).replaceAll("%cost", Matcher.quoteReplacement(cost + "")));
            return true;
        }
        if (args.length < 1) {
            if (mPlayer.getUnreadMail() == null) {
                MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_EMPTY_GENERAL));
                return true;
            }
            for (int i = 0; i < mPlayer.getUnreadMail().size(); i++) {
                Mail m = mPlayer.getUnreadMail().get(i);
                output.add(m.toShortString(i + 1));
            }
        } else {
            type = MailType.getFromString(args[0]);
            if (type == null) { return false; }
            if (mPlayer.getAllMail(type) == null) {
                MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_EMPTY_GENERAL));
                return true;
            }
            for (int i = 0; i < mPlayer.getAllMail(type).size(); i++) {
                Mail m = mPlayer.getAllMail(type).get(i);
                output.add(m.toShortString(i + 1));
            }
        }
        if (cost > 0) {
            try {
                DeityAPI.getAPI().getEconAPI().send(player.getName(), cost, "Mail - Inbox");
            } catch (NegativeMoneyException e) {
                e.printStackTrace();
                return true;
            }
        }
        if (output != null && output.size() > 0) {
            lastInboxMailType.put(player.getName().toLowerCase(), type);
            if (type == MailType.READ) {
                MailMain.plugin.chat.sendPlayerMessageNoHeader(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_MESSAGE_HEADER_READ));
            } else {
                MailMain.plugin.chat.sendPlayerMessageNoHeader(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_MESSAGE_HEADER_UNREAD));
            }
            for (String[] sOutput : output) {
                for (String s : sOutput) {
                    MailMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
                }
            }
        } else {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_EMPTY_GENERAL));
            return true;
        }
        return true;
    }
}
