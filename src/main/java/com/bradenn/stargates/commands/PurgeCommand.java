package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.StructureManager;
import org.bukkit.entity.Player;

public class PurgeCommand implements SubCommand {

    public String getLabel() {
        return "purge";
    }

    public String getDescription() {
        return "Destroy all stargates.";
    }

    public Permission getPermission() {
        return Permission.PURGE;
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length >= 1) {
            StructureManager.removeAll();
            Messages.sendInfo(player, "All structures have been permanently destroyed.");
        } else {
            throw new Exception("Insufficient arguments.");
        }
    }
}
