package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.Structure;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.dialer.Dialer;
import com.bradenn.stargates.structures.rings.Rings;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Player;

import java.util.List;

public class RebuildCommand implements SubCommand {

    public String getLabel() {
        return "rebuild";
    }

    public String getDescription() {
        return "Destroy and rebuild all stargates and dialers.";
    }

    public void run(Player player, String[] args) throws Exception {
        StructureManager.rebuildAll();
    }
}
