package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Stargate;
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

            Stargate stargate = (Stargate) StructureFactory.createStructure(StructureFactory.StructureType.STARGATE, name, player.getLocation());
            stargate.save();

            Dialer dialer = (Dialer) StructureFactory.createStructure(StructureFactory.StructureType.DIALER, name, player.getLocation());
            dialer.assignStargate(stargate.getUUID());
            dialer.save();

            Messages.sendInfo(player, "A stargate named '%s' has been created.", stargate.getName());
        }else{
            throw new Exception("Insufficient arguments.");
        }
    }
}
