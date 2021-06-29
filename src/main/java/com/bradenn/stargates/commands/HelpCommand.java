package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    public String getLabel() {
        return "help";
    }

    public String getDescription() {
        return "Get help with the stargate plugin.";
    }

    public void run(Player player, String[] args) {
        player.sendMessage("Here is your help: Suck a duck.");
    }

}
