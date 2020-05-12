package dev.tinchx.mute.manager;

import com.google.common.collect.Maps;
import dev.tinchx.mute.profile.Profile;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private static Map<UUID, Profile> profileMap = Maps.newHashMap();

    public static void register(Profile profile) {
        profileMap.put(profile.getUuid(), profile);
    }

    public static void unload(OfflinePlayer player) {
        getProfile(player).save();
        profileMap.remove(player.getUniqueId());
    }

    public static Profile getProfile(OfflinePlayer player) {
        Profile profile = profileMap.get(player.getUniqueId());
        if (profile == null) {
            profile = new Profile(player.getUniqueId());
        }
        return profile;
    }
}
