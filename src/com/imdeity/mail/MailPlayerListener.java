package com.imdeity.mail;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class MailPlayerListener extends PlayerListener {

	public MailPlayerListener(Mail instance) {

	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("mail.join")) {
			int unread = MailSQL.getUnreadCount(player.getName());
			if (unread > 0)
				if (unread == 1)
					ChatTools
							.formatAndSend(
									("<option><white>You have <yellow>"
											+ unread + "<white> unread message in your inbox."),
									"Mail", player);
				else
					ChatTools
							.formatAndSend(
									("<option><white>You have <yellow>"
											+ unread + "<white> unread messages in your inbox."),
									"Mail", player);
			else
				ChatTools.formatAndSend(
						("<option><gray>Nothing in your inbox."), "Mail",
						player);
		}
	}
}
