package com.imdeity.mail.object;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	public static YamlConfiguration lang = new YamlConfiguration();

	public void loadDefaults() {
		lang = YamlConfiguration.loadConfiguration(new File(
				"plugins/Mail/language.yml"));

		if (!lang.contains("header")) {
			lang.set("header", "&7[&cMail&7] &f");
		}
		if (!lang.contains("mail.inbox.new.general")) {
			lang.set("mail.inbox.new.general", "&aYou have got a message!");
		}
		if (!lang.contains("mail.inbox.new.singular")) {
			lang.set("mail.inbox.new.singular",
					"&fYou have &a%numMessages new message &fin your mailbox!");
		}
		if (!lang.contains("mail.inbox.new.plural")) {
			lang.set("mail.inbox.new.plural",
					"&fYou have &a%numMessages new messages &fin your mailbox!");
		}
		if (!lang.contains("mail.inbox.check")) {
			lang.set("mail.inbox.check", "&fUse &b/mail &fto check your inbox.");
		}
		if (!lang.contains("mail.inbox.empty.general")) {
			lang.set("mail.inbox.empty.general", "&fYour inbox is empty!");
		}
		if (!lang.contains("mail.inbox.empty.other")) {
			lang.set("mail.inbox.empty.other", "&f%player's inbox is empty!");
		}
		if (!lang.contains("mail.delete.singular")) {
			lang.set("mail.delete.singular",
					"&fSuccessfully deleted message number &a%messageNumber!");
		}
		if (!lang.contains("mail.delete.plural")) {
			lang.set("mail.delete.plural",
					"&fSuccessfully deleted all your mail");
		}
		if (!lang.contains("mail.sent")) {
			lang.set("mail.sent",
					"&fYour message has been sent to &a%messageReceiver - %messageMessage");
		}
		if (!lang.contains("mail.message.long")) {
			lang.set(
					"mail.message.long",
					"&7[%messageLocalIndex] &f%messageSender: &7%messageLongMessage &8[%messageSendDate&8]");
		}
		if (!lang.contains("mail.message.short")) {
			lang.set("mail.message.short",
					"&7[%messageLocalIndex] &f%messageSender: &7%messageShortMessage");
		}
		if (!lang.contains("mail.error.permission")) {
			lang.set("mail.error.permissions",
					"&cYou do not have permissions to do that");
		}
		if (!lang.contains("mail.error.help")) {
			lang.set("mail.error.help",
					"&eInvalid Syntax, use \"/mail ?\" for help.");
		}
		if (!lang.contains("mail.error.invalid_selected")) {
			lang.set("mail.error.invalid_selected",
					"&cThat message doesn't exist.");
		}
		if (!lang.contains("mail.error.not_long_enough")) {
			lang.set("mail.error.not_long_enough",
					"&cYour message has to be longer then &4%messageLength words");
		}
		if (!lang.contains("mail.command.header")) {
			lang.set("mail.command.header", "&c--[Mail Command Help]--");
		}
		if (!lang.contains("mail.command.command")) {
			lang.set("mail.command.command",
					"&3%command: &b%explaination");
		}
		this.save();
	}

	public void save() {
		try {
			lang.save(new File("plugins/Mail/language.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getHeader() {
		return lang.getString("header");
	}

	public static String getInboxNew() {
		return lang.getString("mail.inbox.new.general");
	}

	public static String getInboxNewSingular() {
		return lang.getString("mail.inbox.new.singular");
	}

	public static String getInboxNewPlural() {
		return lang.getString("mail.inbox.new.plural");
	}

	public static String getInboxCheck() {
		return lang.getString("mail.inbox.check");
	}

	public static String getInboxEmpty() {
		return lang.getString("mail.inbox.empty.general");
	}

	public static String getInboxEmptyOther() {
		return lang.getString("mail.inbox.empty.other");
	}

	public static String getMailDeleteSingular() {
		return lang.getString("mail.delete.singular");
	}

	public static String getMailDeletePlural() {
		return lang.getString("mail.delete.plural");
	}

	public static String getMailSent() {
		return lang.getString("mail.sent");
	}

	public static String getMailLongMessage() {
		return lang.getString("mail.message.long");
	}

	public static String getMailShortMessage() {
		return lang.getString("mail.message.short");
	}

	public static String getMailNotLongEnough() {
		return lang.getString("mail.error.not_long_enough");
	}

	public static String getMailErrorPermission() {
		return lang.getString("mail.error.permissions");
	}

	public static String getMailErrorHelp() {
		return lang.getString("mail.error.help");
	}

	public static String getMailErrorInvalidSelected() {
		return lang.getString("mail.error.invalid_selected");
	}

	public static String getMailCommandHeader() {
		return lang.getString("mail.command.header");
	}

	public static String getMailCommandMessage() {
		return lang.getString("mail.command.command");
	}
}