package com.imdeity.mail.cmds.mail;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.deityapi.exception.NegativeMoneyException;
import com.imdeity.mail.MailConfigHelper;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;
import com.imdeity.mail.object.MailManager;
import com.imdeity.mail.object.MailPlayer;

public class MailWriteCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) { return false; }
        double cost = MailMain.plugin.config.getDouble(MailConfigHelper.MAIL_COST_WRITE);
        if (!DeityAPI.getAPI().getEconAPI().canPay(player.getName(), cost)) {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_FUNDS).replaceAll("%cost", Matcher.quoteReplacement(cost + "")));
            return true;
        }
        String sender = player.getName();
        String receiver = args[0];
        args = DeityAPI.getAPI().getUtilAPI().getStringUtils().remFirstArg(args);
        if (args.length < 3) {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_LENGTH).replaceAll("%messageLength", "3"));
            return true;
        }
        String message = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args, " ");
        MailPlayer mPlayer = MailManager.getMailPlayer(sender);
        MailMain.plugin.chat.sendPlayerMessage(player, "Sending message...");
        new MailWrite(player, receiver, message, mPlayer, cost);
        return true;
    }
    
    public class MailWrite implements Runnable {
        private Player player;
        private String receiver;
        private String message;
        private MailPlayer mPlayer;
        private double cost;
        
        public MailWrite(Player player, String receiver, String message, MailPlayer mPlayer, double cost) {
            this.player = player;
            this.receiver = receiver;
            this.message = message;
            this.mPlayer = mPlayer;
            this.cost = cost;
            MailMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(MailMain.plugin, this);
        }
        
        public void run() {
            if (MailManager.containsCloseMessage(player.getName(), receiver, message)) {
                MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_CLOSE_MATCH));
                return;
            }
            mPlayer.sendMail(receiver, message);
            if (cost > 0) {
                try {
                    DeityAPI.getAPI().getEconAPI().send(player.getName(), cost, "Mail - Write");
                } catch (NegativeMoneyException e) {
                    e.printStackTrace();
                }
            }
            MailMain.plugin.chat.sendPlayerMessage(player,
                    MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_SENT).replaceAll("%messageReceiver", Matcher.quoteReplacement(receiver)).replaceAll("%messageMessage", Matcher.quoteReplacement(message)).replaceAll("%cost", Matcher.quoteReplacement(cost + "")));
        }
    }
}
