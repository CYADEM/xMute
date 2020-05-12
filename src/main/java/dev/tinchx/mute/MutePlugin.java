package dev.tinchx.mute;

import dev.tinchx.mute.commands.MuteCommand;
import dev.tinchx.mute.commands.UnMuteCommand;
import dev.tinchx.mute.listeners.ProfileListener;
import dev.tinchx.mute.manager.MongoManager;
import dev.tinchx.mute.utiliities.command.CommandHandler;
import dev.tinchx.mute.utiliities.configuration.ConfigExt;
import dev.tinchx.mute.utiliities.configuration.RootConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MutePlugin extends JavaPlugin {

    private RootConfig settings;
    private MongoManager mongoManager;

    @Override
    public void onEnable() {
        load();
    }

    private void load() {
        settings = new RootConfig("settings", ConfigExt.YML);
        mongoManager = new MongoManager();

        registerCommands();

        Bukkit.getPluginManager().registerEvents(new ProfileListener(), this);
    }

    private void registerCommands() {
        CommandHandler handler = new CommandHandler(this);
        handler.register(new MuteCommand());
        handler.register(new UnMuteCommand());
    }

    public static MutePlugin getInstance() {
        return getPlugin(MutePlugin.class);
    }
}
