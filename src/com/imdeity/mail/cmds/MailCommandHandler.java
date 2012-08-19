package com.imdeity.mail.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.mail.cmds.mail.MailConvertCommand;
import com.imdeity.mail.cmds.mail.MailInboxCommand;
import com.imdeity.mail.cmds.mail.MailReadCommand;
import com.imdeity.mail.cmds.mail.MailReloadCommand;
import com.imdeity.mail.cmds.mail.MailWriteCommand;

public class MailCommandHandler extends DeityCommandHandler {
    
    public MailCommandHandler(String pluginName) {
        super(pluginName, "Mail");
    }
    
    @Override
    protected void initRegisteredCommands() {
        String[] writeAliases = { "new", "create" };
        this.registerCommand("reload", null, "", "Reloads the config and all mail", new MailReloadCommand(), "Mail.admin.reload");
        this.registerCommand("convert", null, "<old-table-name>", "Converts the old mail data to the new format", new MailConvertCommand(), "Mail.admin.convert");
        this.registerCommand("inbox", null, "<read/unread>", "Checks your inbox", new MailInboxCommand(), "Mail.general.inbox");
        this.registerCommand("read", null, "[index]", "Opens the specified mail", new MailReadCommand(), "Mail.general.read");
        this.registerCommand("write", writeAliases, "[receiver] [message]", "Sends a new mail", new MailWriteCommand(), "Mail.general.write");
    }
    
}
