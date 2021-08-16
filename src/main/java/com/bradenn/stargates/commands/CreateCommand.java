package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Player;

public class CreateCommand implements SubCommand {

    public String getLabel() {
        return "create";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public Permission getPermission() {
        return Permission.CREATE;
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length == 1) {
            throw new Exception("Insufficient arguments.");
        } else if (args.length == 2) {
            String name = args[1];

            Stargate stargate = StructureManager.createStructure(name, player.getLocation(), Stargate.class);
            stargate.save();

            Messages.sendInfo(player, "A stargate named '%s' has been created.", stargate.getName());
        } else {
            throw new Exception("Excessive arguments.");
        }
    }
}
