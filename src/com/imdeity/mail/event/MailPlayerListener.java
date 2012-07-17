package com.imdeity.mail.event;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.imdeity.deityapi.api.DeityListener;
import com.imdeity.mail.MailLanguageHelper;
import com.imdeity.mail.MailMain;
import com.imdeity.mail.object.MailManager;
import com.imdeity.mail.object.MailPlayer;

public class MailPlayerListener extends DeityListener {
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.isOnline() && player.hasPermission("mail.general")) {
            MailPlayer mPlayer = MailManager.getMailPlayer(player.getName());
            int numUnread = mPlayer.getUnreadCount();
            switch (numUnread) {
                case 0:
                    break;
                case 1:
                    MailMain.plugin.chat.sendPlayerMessage(player, Matcher.quoteReplacement(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_NEW_SINGLE).replaceAll("%numMessages", numUnread + "")));
                    break;
                default:
                    MailMain.plugin.chat.sendPlayerMessage(player, Matcher.quoteReplacement(MailMain.plugin.language.getNode(MailLanguageHelper.MAIL_INBOX_NEW_PLURAL).replaceAll("%numMessages", numUnread + "")));
                    break;
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            MailManager.removeMailPlayer(player.getName());
        }
    }
}
