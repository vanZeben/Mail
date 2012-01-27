package com.imdeity.mail.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.imdeity.mail.Mail;
import com.imdeity.mail.sql.MailSQL;

public class MailPlayerListener extends PlayerListener {

	public MailPlayerListener(Mail instance) {

	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("mail.player.join")) {
			MailSQL.sendUnreadCount(player.getName());
		}
	}
}
