package com.bradenn.stargates.commands;

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

    private void registerSubCommand(SubCommand subCommand) {
        commands.put(subCommand.getLabel(), subCommand);
    }

    private SubCommand getSubCommand(String commandLabel) {
        return commands.getOrDefault(commandLabel, helpCommand);
    }

    public void init(PluginCommand pluginCommand) {
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }

        registerSubCommand(helpCommand);

        registerSubCommand(new CreateCommand());
        registerSubCommand(new RegisterCommand());
        registerSubCommand(new LocateCommand());
        registerSubCommand(new DialerCommand());
        registerSubCommand(new PurgeCommand());
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
            getSubCommand(commandLabel).onRun(player, args);
        } else {
            helpCommand.onRun(player, args);
        }
    }

    private void handleError(Player player, Exception exception) {
        player.sendMessage(exception.getMessage());
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

}
