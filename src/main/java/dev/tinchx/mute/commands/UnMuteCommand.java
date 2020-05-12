package dev.tinchx.mute.commands;

import dev.tinchx.mute.manager.ProfileManager;
import dev.tinchx.mute.profile.Profile;
import dev.tinchx.mute.utiliities.chat.ColorText;
import dev.tinchx.mute.utiliities.command.RootCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class UnMuteCommand extends RootCommand {

    public UnMuteCommand() {
        super("unmute");
        setPermission("senior.command.unmute");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (!player.isOnline() && !player.hasPlayedBefore()) {
                sender.sendMessage(ColorText.translate("&cPlayer '" + args[0] + "' has never played before."));
            } else {
                Profile profile = ProfileManager.getProfile(player);
                if (profile.getMuteTime() <= 0L) {
                    sender.sendMessage(ColorText.translate("&cThat player is not muted."));
                } else {
                    profile.setMuteTime(0L);

                    if (!player.isOnline()) {
                        ProfileManager.unload(player);
                    } else {
                        player.getPlayer().sendMessage(ColorText.translate("&aYou have been un-muted by " + sender.getName() + '.'));
                    }

                    Bukkit.broadcast(ColorText.translate("&7[STAFF] &a" + player.getName() + " has been un-muted by " + sender.getName() + '.'), "senior.command.unmute");
                }
            }
        }
    }
}
