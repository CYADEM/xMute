package dev.tinchx.mute.commands;

import dev.tinchx.mute.manager.ProfileManager;
import dev.tinchx.mute.profile.Profile;
import dev.tinchx.mute.utiliities.chat.ColorText;
import dev.tinchx.mute.utiliities.command.RootCommand;
import dev.tinchx.mute.utiliities.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class MuteCommand extends RootCommand {

    public MuteCommand() {
        super("mute", null, "tempmute", "tmute");
        setPermission("senior.command.mute");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < (label.equalsIgnoreCase("mute") ? 2 : 1)) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>" + (label.equalsIgnoreCase("mute") ? "" : " <duration>")));
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (!player.isOnline() && !player.hasPlayedBefore()) {
                sender.sendMessage(ColorText.translate("&cPlayer '" + args[0] + "' has never played before."));
            } else {
                Profile profile = ProfileManager.getProfile(player);
                if (profile.getMuteTime() > 0L) {
                    sender.sendMessage(ColorText.translate("&cThat player is already muted."));
                } else {
                    boolean permanent = label.equalsIgnoreCase("mute");
                    long duration = Long.MAX_VALUE;
                    if (!permanent) {
                        if (TimeUtils.parse(args[1]) <= 0L) {
                            sender.sendMessage(ColorText.translate("&cDuration mustn't be 0."));
                            return;
                        }
                        duration = TimeUtils.parse(args[1]);
                    }
                    profile.setMuteTime(duration);
                    profile.setPermanentMute(permanent);

                    if (!player.isOnline()) {
                        ProfileManager.unload(player);
                    } else {
                        player.getPlayer().sendMessage(ColorText.translate("&cYou have been " + (permanent ? "permanently" : "temporarily") + " muted by " + sender.getName() + '.'));
                    }

                    Bukkit.broadcast(ColorText.translate("&7[STAFF] &c" + player.getName() + " has been " + (permanent ? "permanently" : "temporarily") + " muted by " + sender.getName() + '.'), "senior.command.mute");
                }
            }
        }
    }
}
