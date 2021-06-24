package com.bradenn.stargates.commands;

import com.bradenn.stargates.PersistenceManager;
import com.bradenn.stargates.Stargate;
import com.bradenn.stargates.structures.DialerStructure;
import com.bradenn.stargates.structures.GateStructure;
import com.bradenn.stargates.structures.PersistentStructure;
import com.bradenn.stargates.structures.StructureOrientation;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DialerCommand implements SubCommand {

    public String getLabel() {
        return "dialer";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public void onRun(Player player, String[] args) {

        try {
            DialerStructure dialerStructure = new DialerStructure(player.getLocation());
            PersistenceManager persistenceManager = new PersistenceManager(dialerStructure);
            persistenceManager.save();

        } catch (Exception e) {
            player.sendMessage(e.getMessage());
        }
    }


    private void error() {

    }

}
