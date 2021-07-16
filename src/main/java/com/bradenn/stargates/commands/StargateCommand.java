package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StargateCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> commands = new HashMap<>();

    private final SubCommand helpCommand = new HelpCommand();

    public StargateCommand(PluginCommand pluginCommand) {
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }

        registerSubCommand(helpCommand);
        registerSubCommand(new CreateCommand());
        registerSubCommand(new SeedCommand());
        registerSubCommand(new RemoveCommand());
        registerSubCommand(new PurgeCommand());
        registerSubCommand(new RebuildCommand());
        registerSubCommand(new RingsCommand());
        registerSubCommand(new TestCommand());
    }

    private void registerSubCommand(SubCommand subCommand) {
        commands.put(subCommand.getLabel(), subCommand);
    }

    private SubCommand getSubCommand(String commandLabel) {
        return commands.getOrDefault(commandLabel, helpCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is intended for player use.");
            return true;
        }

        Player player = (Player) sender;

        try {
            designateCommand(player, args);
        } catch (Exception exception) {
            handleError(player, exception);
        }

        return true;
    }

    private void designateCommand(Player player, String[] args) throws Exception {
        int argsCount = args.length;
        if (argsCount >= 1) {
            String commandLabel = args[0];
            SubCommand subCommand = getSubCommand(commandLabel);

            if (player.hasPermission(subCommand.getPermission())) {
                subCommand.run(player, args);
            } else {
                throw new Exception("You lack the permissions to use this command.");
            }
        } else {
            helpCommand.run(player, args);
        }
    }

    private void handleError(Player player, Exception exception) {
        Messages.sendError(player, exception.getMessage());
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

}
