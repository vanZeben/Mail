package com.imdeity.mail.cmds.mail;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
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
        int shownIndex = 0;
        int actualIndex = 0;
        try {
            shownIndex = Integer.parseInt(args[0]);
            actualIndex = shownIndex - 1;
        } catch (NumberFormatException e) {
            return false;
        }
        MailType lastType = MailInboxCommand.lastInboxMailType.get(player.getName().toLowerCase());
        if (lastType == null) {
            lastType = MailType.UNREAD;
        }
        if (actualIndex > mPlayer.getAllMail(lastType).size()) {
            MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_ERROR_INVALID_MAIL));
            return true;
        }
        Mail mail = mPlayer.getAllMail(lastType).get(actualIndex);
        if (lastType == MailType.UNREAD) {
            mPlayer.setMailType(mail.getId(), MailType.READ);
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
