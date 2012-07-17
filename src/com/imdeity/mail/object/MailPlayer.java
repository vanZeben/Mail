package com.imdeity.mail.object;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;

public class MailPlayer {
    
    private Map<MailType, List<Mail>> inbox = new HashMap<MailType, List<Mail>>();
    private String playerName;
    
    public MailPlayer(String playerName) {
        this.playerName = playerName;
        this.inbox.put(MailType.READ, new ArrayList<Mail>());
        this.inbox.put(MailType.UNREAD, new ArrayList<Mail>());
        new LoadMail(this);
    }
    
    public void getInboxFromDatabase() {
        String sql = "SELECT * FROM " + MailMain.getMySQLTableName() + " WHERE receiver = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, playerName);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                int id = -1;
                String sender = "";
                String receiver = "";
                String message = "";
                Date sendDate = null;
                MailType type = null;
                try {
                    id = query.getInteger(i, "id");
                    sender = query.getString(i, "sender");
                    receiver = query.getString(i, "receiver");
                    message = query.getString(i, "message");
                    sendDate = query.getDate(i, "send_date");
                    type = MailType.getFromString(query.getString(i, "type"));
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
                Mail mail = new Mail(id, sender, receiver, message, sendDate, type);
                this.addMail(mail, type);
            }
        }
    }
    
    public int getUnreadCount() {
        if (inbox.get(MailType.UNREAD) == null) { return 0; }
        return inbox.get(MailType.UNREAD).size();
    }
    
    public int getReadCount() {
        if (inbox.get(MailType.READ) == null) { return 0; }
        return inbox.get(MailType.READ).size();
    }
    
    public Mail sendMail(String receiver, String message) {
        String sql = "INSERT INTO " + MailMain.getMySQLTableName() + " (" + "sender, receiver, message" + ") VALUES (?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.playerName, receiver, message);
        int id = this.getUnreadCount() + this.getReadCount() + 1;
        Date sendDate = Calendar.getInstance().getTime();
        MailType type = MailType.UNREAD;
        Mail mail = new Mail(id, this.playerName, receiver, message, sendDate, type);
        
        MailPlayer mReceiver = MailManager.getMailPlayer(receiver);
        mReceiver.addMail(mail, type);
        if (DeityAPI.getAPI().getPlayerAPI().getOnlinePlayer(receiver) != null) {
            MailMain.plugin.chat.sendPlayerMessage(DeityAPI.getAPI().getPlayerAPI().getOnlinePlayer(receiver), MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_NEW_GENERAL));
        }
        
        return mail;
    }
    
    public List<Mail> getAllMail() {
        List<Mail> tmp = new ArrayList<Mail>();
        for (MailType mt : this.inbox.keySet()) {
            tmp.addAll(this.inbox.get(mt));
        }
        return tmp;
    }
    
    public List<Mail> getAllMail(MailType type) {
        return inbox.get(type);
    }
    
    public Mail getMail(int id) {
        for (MailType mt : this.inbox.keySet()) {
            for (Mail m : this.inbox.get(mt)) {
                if (m.getId() == id) { return m; }
            }
        }
        return null;
    }
    
    public Mail getUnreadMailAtIndex(int index) {
        if (index <= this.inbox.get(MailType.UNREAD).size()) { return this.inbox.get(MailType.UNREAD).get(index); }
        return null;
    }
    
    public List<Mail> getUnreadMail() {
        return this.inbox.get(MailType.UNREAD);
    }
    
    public void setMailType(int id, MailType type) {
        Mail mail = getMail(id);
        if (mail != null) {
            this.inbox.get(mail.getMailType()).remove(mail);
            mail.setMailType(type);
            mail.save();
            this.inbox.get(type).add(mail);
        }
    }
    
    public class LoadMail implements Runnable {
        
        private MailPlayer mPlayer;
        
        public LoadMail(MailPlayer mPlayer) {
            this.mPlayer = mPlayer;
            MailMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(MailMain.plugin, this);
        }
        
        public void run() {
            mPlayer.getInboxFromDatabase();
        }
    }
    
    public void addMail(Mail mail, MailType type) {
        if (this.inbox.containsKey(type)) {
            this.inbox.get(type).add(mail);
        } else {
            List<Mail> tmp = new ArrayList<Mail>();
            tmp.add(mail);
            this.inbox.put(type, tmp);
        }
    }
}
