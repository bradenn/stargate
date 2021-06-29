package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Stargate;
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
        List<Stargate> stargates = Stargate.getAll();
        stargates.forEach(Stargate::rebuild);

        List<Dialer> dialers = Dialer.getAll();
        dialers.forEach(Dialer::rebuild);

        Messages.sendInfo(player, "Rebuilt %d stargates and %d dialers.", stargates.size(), dialers.size());
    }
}
