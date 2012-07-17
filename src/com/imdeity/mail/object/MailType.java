package com.imdeity.mail.object;

public enum MailType {
    READ, UNREAD;
    
    public static MailType getFromString(String name) {
        if (MailType.READ.name().equalsIgnoreCase(name)) {
            return MailType.READ;
        } else if (MailType.UNREAD.name().equalsIgnoreCase(name)) {
            return MailType.UNREAD;
        } else {
            return null;
        }
    }
}
