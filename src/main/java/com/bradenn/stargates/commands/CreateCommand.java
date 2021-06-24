package com.bradenn.stargates.commands;

import com.bradenn.stargates.PersistenceManager;
import com.bradenn.stargates.structures.GateStructure;
import com.bradenn.stargates.structures.PersistentStructure;
import com.bradenn.stargates.structures.StructureOrientation;
import org.bukkit.entity.Player;

public class CreateCommand implements SubCommand {

    public String getLabel() {
        return "create";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public void onRun(Player player, String[] args) {

        try {
            GateStructure gateStructure = new GateStructure(player.getLocation(), StructureOrientation.NORTH);
            PersistenceManager persistenceManager = new PersistenceManager(gateStructure);
            persistenceManager.save();
//            PersistenceManager.saveGate(gateStructure);
        } catch (Exception e) {
            player.sendMessage(e.getMessage());
        }


    }


    private void error() {

    }

}
