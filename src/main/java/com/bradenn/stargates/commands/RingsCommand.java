package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.rings.Rings;
import org.bukkit.entity.Player;

public class RingsCommand implements SubCommand {

    public String getLabel() {
        return "rings";
    }

    public String getDescription() {
        return "Build a ring teleporter.";
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length >= 2) {
            String name = args[1];

            Rings rings = StructureManager.createStructure(name, player.getLocation(), Rings.class);
            rings.save();

            Messages.sendInfo(player, "A new ring teleporter named '%s' has been created.", rings.getName());
        } else {
            throw new Exception("Insufficient arguments.");
        }
    }
}
