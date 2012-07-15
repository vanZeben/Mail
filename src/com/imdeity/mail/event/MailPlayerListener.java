package com.imdeity.mail.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.imdeity.mail.Mail;
import com.imdeity.mail.sql.MailSQL;

public class MailPlayerListener implements Listener {

	public MailPlayerListener(Mail instance) {

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player != null && player.isOnline() && player.hasPermission("mail.player.join")) {
			MailSQL.sendUnreadCount(player.getName());
		}
	}
}
