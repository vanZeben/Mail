package com.imdeity.mail.cmds.mail;

import java.util.ArrayList;
import java.util.List;
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

public class MailReadCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        MailPlayer mPlayer = MailManager.getMailPlayer(player.getName());
        List<String[]> output = new ArrayList<String[]>();
        if (args.length < 1) { return false; }
        double cost = MailMain.plugin.config.getDouble(MailConfigHelper.MAIL_COST_READ);
        if (cost > 0 && DeityAPI.getAPI().isEconAPIOnline() && !DeityAPI.getAPI().getEconAPI().canPay(player.getName(), cost)) {
            MailMain.plugin.chat.sendPlayerMessage(
                    player,
                    MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_FUNDS).replaceAll("%cost",
                            Matcher.quoteReplacement(cost + "")));
            return true;
        }
        int shownIndex = 0;
        int actualIndex = 0;
        try {
            shownIndex = Integer.parseInt(args[0]);
            actualIndex = shownIndex - 1;
        } catch (NumberFormatException e) {
            return false;
        }
        if (actualIndex < 0) {
            actualIndex = 0;
        }
        MailType lastType = MailInboxCommand.lastInboxMailType.get(player.getName().toLowerCase());
        if (lastType == null) {
            lastType = MailType.UNREAD;
        }
        if (actualIndex >= mPlayer.getAllMail(lastType).size()) {
            MailMain.plugin.chat.sendPlayerMessage(player,
                    MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_MAIL));
            return true;
        }
        Mail mail = mPlayer.getAllMail(lastType).get(actualIndex);
        if (lastType == MailType.UNREAD) {
            mPlayer.setMailType(mail.getId(), MailType.READ);
        }
        if (cost > 0 && DeityAPI.getAPI().isEconAPIOnline()) {
            try {
                DeityAPI.getAPI().getEconAPI().send(player.getName(), cost, "Mail - Read");
            } catch (NegativeMoneyException e) {
                e.printStackTrace();
                return true;
            }
        }
        output.add(mail.toLongString(shownIndex));
        if (output != null && output.size() > 0) {
            for (String[] sOutput : output) {
                for (String s : sOutput) {
                    MailMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
                }
            }
        }
        return true;
    }
}
