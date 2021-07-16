package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Player;

public class RemoveCommand implements SubCommand {

    public String getLabel() {
        return "remove";
    }

    public String getDescription() {
        return "Remove a stargate referenced by address.";
    }

    public Permission getPermission() {
        return Permission.REMOVE;
    }

    public void run(Player player, String[] args) throws Exception {
        Stargate stargate = StructureManager.getStructureFromName(args[1], Stargate.class);

        stargate.terminate();
        Messages.sendInfo(player, "The stargate '%s' has been permanently removed.", stargate.getName());
    }
}
