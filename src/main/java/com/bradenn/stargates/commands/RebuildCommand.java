package com.bradenn.stargates.commands;

import com.bradenn.stargates.structures.StructureManager;
import org.bukkit.entity.Player;

public class RebuildCommand implements SubCommand {

    public String getLabel() {
        return "rebuild";
    }

    public String getDescription() {
        return "Destroy and rebuild all stargates and dialers.";
    }

    public Permission getPermission() {
        return Permission.REBUILD;
    }

    public void run(Player player, String[] args) throws Exception {
        StructureManager.rebuildAll();
    }
}
