package com.imdeity.mail.cmds.mail;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;
import com.imdeity.mail.object.MailManager;

public class MailReloadCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        MailMain.plugin.reloadPlugin();
        MailManager.reload();
        MailMain.plugin.chat.out(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_RELOAD_SUCCESS));
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        MailMain.plugin.reloadPlugin();
        MailManager.reload();
        MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_RELOAD_SUCCESS));
        return true;
    }
}
