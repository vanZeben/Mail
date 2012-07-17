package com.imdeity.mail.object;

import java.util.Date;
import java.util.regex.Matcher;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;

public class Mail {
    
    private int id;
    private String sender;
    private String receiver;
    private String message;
    private Date sendDate = null;
    private MailType hasRead;
    private boolean hasUpdated = false;
    
    public Mail(int id, String sender, String receiver, String message, Date sendDate, MailType hasRead) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sendDate = sendDate;
        this.hasRead = hasRead;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getSender() {
        return this.sender;
    }
    
    public String getReceiver() {
        return this.receiver;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getSendDate() {
        return (this.sendDate != null ? DeityAPI.getAPI().getUtilAPI().getTimeUtils().timeApproxToDate(sendDate) : "Not Available");
    }
    
    public MailType getMailType() {
        return hasRead;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
        hasUpdated = true;
    }
    
    public void setReceiver(String receiver) {
        this.receiver = receiver;
        hasUpdated = true;
    }
    
    public void setMessage(String message) {
        this.message = message;
        hasUpdated = true;
    }
    
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
        hasUpdated = true;
    }
    
    public void setMailType(MailType hasRead) {
        this.hasRead = hasRead;
        this.hasUpdated = true;
    }
    
    public void save() {
        if (hasUpdated) {
            String sql = "UPDATE " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("mail_", "data") + " SET " + "sender = ?, receiver = ?, message = ?, type = ?, send_date = ? WHERE id = ?;";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, sender, receiver, message, hasRead.name(), sendDate, id);
            hasUpdated = false;
        }
    }
    
    public void remove() {
        String sql = "DELETE FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("mail_", "data") + " WHERE id = ?;";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, id);
    }
    
    public String[] toShortString(int index) {
        return this.performReplacements(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_MESSAGE_SHORT), index);
    }
    
    public String[] toLongString(int index) {
        return this.performReplacements(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_MESSAGE_LONG), index);
    }
    
    public String[] performReplacements(String msg, int index) {
        msg = msg.replaceAll("%messageId", this.getId() + "").replaceAll("%messageLocalIndex", index + "").replaceAll("%messageSender", Matcher.quoteReplacement(this.getSender())).replaceAll("%messageLongMessage", Matcher.quoteReplacement(this.message))
                .replaceAll("%messageShortMessage", Matcher.quoteReplacement(DeityAPI.getAPI().getUtilAPI().getStringUtils().maxLength(this.message, 30))).replaceAll("%messageReceiver", Matcher.quoteReplacement(this.getReceiver())).replaceAll("%messageSendDate", this.getSendDate());
        
        String[] tmpMsg = msg.split("%newline");
        return tmpMsg;
    }
}
