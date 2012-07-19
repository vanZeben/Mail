package com.imdeity.mail;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.mail.cmds.MailCommandHandler;
import com.imdeity.mail.event.MailPlayerListener;
import com.imdeity.mail.object.MailManager;
import com.imdeity.mail.object.MailPlayer;

public class MailMain extends DeityPlugin {
    
    public static MailMain plugin = null;
    private final MailCommandHandler mailCommandHandler = new MailCommandHandler("Mail");
    public final MailManager manager = new MailManager();
    
    @Override
    protected void initCmds() {
        this.registerCommand(mailCommandHandler);
    }
    
    @Override
    protected void initConfig() {
        this.config.addDefaultConfigValue(MailConfigHelper.MAIL_COST_INBOX, 0.0);
        this.config.addDefaultConfigValue(MailConfigHelper.MAIL_COST_READ, 0.0);
        this.config.addDefaultConfigValue(MailConfigHelper.MAIL_COST_WRITE, 0.0);
        this.config.addDefaultConfigValue(MailConfigHelper.MAIL_CLOSE_MATCH, 5);
    }
    
    @Override
    protected void initLanguage() {
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_INBOX_NEW_GENERAL, "&aYou have got a new message!");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_INBOX_NEW_SINGLE, "&fYou have &a%numMessages new message &fin your mailbox!");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_INBOX_NEW_PLURAL, "&fYou have &a%numMessages new messages &fin your mailbox!");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_INBOX_EMPTY_GENERAL, "&fYour inbox is empty!");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_INBOX_EMPTY_OTHER, "&f%player's inbox is empty!");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_SENT, "&fYour message has been sent to &a%messageReceiver - %messageMessage");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_MESSAGE_HEADER_READ, "&6Your read mail: ");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_MESSAGE_HEADER_UNREAD, "&6Your new mail: ");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_MESSAGE_LONG, "&7[%messageLocalIndex] &f%messageSender: &7%messageLongMessage &8[%messageSendDate&8]");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_MESSAGE_SHORT, "&7[%messageLocalIndex] &f%messageSender: &7%messageShortMessage");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_RELOAD_SUCCESS, "Reloaded the plugin");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_CONVERT_SUCCESS, "Converted the old data and reloaded the plugin");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_ERROR_INVALID_MAIL, "&cThat message doesn't exist.");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_ERROR_INVALID_LENGTH, "&cYour message has to be longer then &4%messageLength words");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_ERROR_INVALID_FUNDS, "&cYou do not have enough money for this action");
        this.language.addDefaultLanguageValue(MailLanguageHelper.MAIL_ERROR_CLOSE_MATCH, "&cYou have already sent a message close to this one to that player");
    }
    
    @Override
    protected void initDatabase() {
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getMySQLTableName() + " (" + "`id` INT(16) NOT NULL AUTO_INCREMENT ," + "`sender` VARCHAR(16) NOT NULL ," + "`receiver` VARCHAR(16) NOT NULL ," + "`message` TEXT NOT NULL ," + "`type` VARCHAR(8) NOT NULL DEFAULT 'UNREAD',"
                        + "`send_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + "PRIMARY KEY (`id`)," + "INDEX (`receiver`)," + "INDEX (`sender`)" + ") ENGINE = MyISAM COMMENT = 'In-Game mail';");
        
    }
    
    @Override
    protected void initInternalDatamembers() {
        MailManager.reload();
    }
    
    @Override
    protected void initListeners() {
        this.registerListener(new MailPlayerListener());
    }
    
    @Override
    protected void initPlugin() {
        plugin = this;
    }
    
    @Override
    protected void initTasks() {
    }
    
    public MailPlayer getMailPlayerAPI(String playername) {
        return MailManager.getMailPlayer(playername);
    }
    
    public static String getMySQLTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("mail_", "data");
    }
}
