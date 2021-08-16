package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    public String getLabel() {
        return "help";
    }

    public String getDescription() {
        return "Get help with the stargate plugin.";
    }

    public void run(Player player, String[] args) throws Exception {
        Messages.sendInfo(player, "Your help.");
    }

}
