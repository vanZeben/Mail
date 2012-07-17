package com.imdeity.mail.cmds.mail;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
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
        String sender = player.getName();
        String receiver = args[0];
        args = DeityAPI.getAPI().getUtilAPI().getStringUtils().remFirstArg(args);
        if (args.length < 3) {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_LENGTH).replaceAll("%messageLength", "3"));
            return true;
        }
        String message = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args, " ");
        MailPlayer mPlayer = MailManager.getMailPlayer(sender);
        mPlayer.sendMail(receiver, message);
        MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_SENT).replaceAll("%messageReceiver", Matcher.quoteReplacement(receiver)).replaceAll("%messageMessage", Matcher.quoteReplacement(message)));
        return true;
    }
}
