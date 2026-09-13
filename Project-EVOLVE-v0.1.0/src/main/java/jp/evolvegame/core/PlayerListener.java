package jp.evolvegame.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {
    private final MatchManager match;

    public PlayerListener(MatchManager match) {
        this.match = match;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (match.getJoined().contains(event.getPlayer().getUniqueId())) {
            match.leave(event.getPlayer());
        }
    }
}
