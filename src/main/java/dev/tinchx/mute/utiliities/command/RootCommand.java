package dev.tinchx.mute.utiliities.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dev.tinchx.mute.utiliities.chat.ColorText;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RootCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    public RootCommand(String name) {
        this(name, null);
    }

    public RootCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public RootCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isOnlyPlayers() && !(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return false;
        }
        if (getArguments().isEmpty()) {
            execute(sender, label, args);
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&m" + StringUtils.repeat("-", 16) + "&5&m" + StringUtils.repeat("-", 16) + "&r&m" + StringUtils.repeat("-", 16)));
            sender.sendMessage(ColorText.translate("&5Available sub-command(s) for '&f" + command.getName() + "&5'."));
            sender.sendMessage("");

            getArguments().stream().filter(argument -> argument.getPermission() == null || sender.hasPermission(argument.getPermission())).forEach(argument -> {
                sender.sendMessage(ColorText.translate(' ' + argument.getUsage(label) + (argument.getDescription() == null ? "" : " &7- &d" + argument.getDescription())));
            });

            sender.sendMessage(ColorText.translate("&m" + StringUtils.repeat("-", 16) + "&5&m" + StringUtils.repeat("-", 16) + "&r&m" + StringUtils.repeat("-", 16)));
        } else {
            RootArgument argument = getArgument(args[0]);
            if (argument == null || (argument.getPermission() != null && !sender.hasPermission(argument.getPermission()))) {
                sender.sendMessage(ColorText.translate("&cArgument '" + args[0] + "&c' could not be found."));
            } else {
                if ((argument.isOnlyPlayers() || isOnlyPlayers()) && sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
                    return false;
                }
                argument.execute(sender, label, args);
            }
        }
        return true;
    }

    public RootArgument getArgument(String name) {
        return getArguments().stream().filter(argument -> argument.getName().equalsIgnoreCase(name) || Arrays.asList(argument.getAliases()).contains(name.toLowerCase())).findFirst().orElse(null);
    }

    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return Lists.newArrayList();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (getArguments().isEmpty()) {
            return tabComplete(sender, label, args);
        }
        List<String> results = Lists.newArrayList();
        if (args.length < 2) {

            for (RootArgument argument : getArguments()) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }

            if (results.isEmpty()) {
                return null;
            }
        } else {
            RootArgument argument = getArgument(args[0]);
            if (argument == null) {
                return results;
            }

            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                results = argument.tabComplete(sender, label, args);

                if (results == null) {
                    return null;
                }
            }
        }

        return getCompletions(args, results);
    }

    private List<String> getCompletions(String[] args, List<String> input) {
        Preconditions.checkNotNull((Object) args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(80).collect(Collectors.toList());
    }

}