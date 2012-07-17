package com.imdeity.mail.cmds.mail;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;
import com.imdeity.mail.object.MailManager;

public class MailConvertCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        String oldTableName = (args.length == 0 ? "mail_mail" : args[0]);
        new ConvertTask(null, oldTableName);
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        String oldTableName = (args.length == 0 ? "mail_mail" : args[0]);
        new ConvertTask(player, oldTableName);
        return true;
    }
    
    public class ConvertTask implements Runnable {
        private Player player;
        private String oldTableName;
        
        public ConvertTask(Player player, String oldTableName) {
            this.player = player;
            this.oldTableName = oldTableName;
            MailMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(MailMain.plugin, this);
        }
        
        public void run() {
            String sql = "INSERT IGNORE INTO " + MailMain.getMySQLTableName() + " (id, sender, receiver, message, type, send_date) SELECT id, sender, receiver, message, IF(mail_mail.read = '1', 'READ', 'UNREAD') AS type, send_date FROM " + oldTableName + ";";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql);
            MailMain.plugin.reloadPlugin();
            MailManager.reload();
            if (player != null) {
                MailMain.plugin.chat.sendPlayerMessage(player, MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_CONVERT_SUCCESS));
            } else {
                MailMain.plugin.chat.out(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_CONVERT_SUCCESS));
            }
        }
    }
}
