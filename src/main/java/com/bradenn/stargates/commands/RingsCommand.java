package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.Rings;
import com.bradenn.stargates.structures.StructureFactory;
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

            Rings rings = (Rings) StructureFactory.createStructure(StructureFactory.StructureType.RINGS, name, player.getLocation());
            rings.save();

            Messages.sendInfo(player, "A new ring teleporter named '%s' has been created.", rings.getName());
        } else {
            throw new Exception("Insufficient arguments.");
        }
    }
}
