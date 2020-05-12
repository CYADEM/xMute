package dev.tinchx.mute.utiliities.command;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public abstract class AbstractCommand {

    private final List<RootArgument> arguments = Lists.newArrayList();

    private final String name;
    private final String description;
    private final String[] aliases;

    @Setter
    private String permission;
    @Setter
    private boolean onlyPlayers;

    public AbstractCommand(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public void execute(CommandSender sender, String label, String[] args) {

    }

    public List<String> tabCompleter(CommandSender sender, String label, String[] args) {
        return Lists.newArrayList();
    }

    public void register(RootArgument argument) {
        getArguments().add(argument);
    }
}