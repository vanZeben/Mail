package com.imdeity.mail.object;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;

public class MailManager {
    
    private static final Map<String, MailPlayer> players = new HashMap<String, MailPlayer>();
    
    public static MailPlayer addMailPlayer(String playername) {
        MailPlayer mPlayer = new MailPlayer(playername);
        players.put(playername.toLowerCase(), mPlayer);
        return mPlayer;
    }
    
    public static MailPlayer getMailPlayer(String playername) {
        MailPlayer player = players.get(playername.toLowerCase());
        while (player == null) {
            player = addMailPlayer(playername);
        }
        return player;
    }
    
    public static void removeMailPlayer(String playername) {
        players.remove(playername.toLowerCase());
    }
    
    public static void reload() {
        players.clear();
        for (Player p : DeityAPI.getAPI().getPlayerAPI().getOnlinePlayers()) {
            getMailPlayer(p.getName());
        }
    }
}
