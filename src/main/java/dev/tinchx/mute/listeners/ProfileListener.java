package dev.tinchx.mute.listeners;

import dev.tinchx.mute.manager.ProfileManager;
import dev.tinchx.mute.profile.Profile;
import dev.tinchx.mute.utiliities.chat.ColorText;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    @EventHandler
    final void onPlayerJoin(PlayerJoinEvent event) {
        ProfileManager.register(new Profile(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    final void onPlayerQuit(PlayerQuitEvent event) {
        ProfileManager.unload(event.getPlayer());
    }

    @EventHandler
    final void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) return;

        Profile profile = ProfileManager.getProfile(player);

        if (profile.getMuteTime() > 0L) {
            event.setCancelled(true);

            if (profile.getMuteTime() >= Long.MAX_VALUE) {
                player.sendMessage(ColorText.translate("&cYou are permanently muted."));
            } else {
                player.sendMessage(ColorText.translate("&cYou're currently muted for another " + DurationFormatUtils.formatDurationWords(profile.getMuteTime(), true, true) + '.'));
            }
        }
    }
}
