package com.imdeity.mail;

public class MailObject {

    private int id, index = 0;
    private String sender, receiver, message = "";

    public MailObject(int id, int index, String sender, String receiver, String message) {
        this.id = id;
        this.index = index;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
    
    public int getId() {
        return this.id;
    }public int getIndex() {
        return this.index;
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
    
    public String toShortString() {
        String message = "";
        message = "<gray>[" + this.id + "]<white> " + this.sender + ":<gray> " + StringMgmt.maxLength(this.message, 30);
        return message;
    }
    public String toLongString() {
        String message = "";
        message = "<white>" + this.sender + ":<gray> " + this.message;
        return message;
    }
}
