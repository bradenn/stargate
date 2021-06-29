package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.entity.Player;

public class PurgeCommand implements SubCommand {

    public String getLabel() {
        return "purge";
    }

    public String getDescription() {
        return "Destroy all stargates.";
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length >= 1) {
            int stargateCount = Stargate.getAll().size();
            Stargate.terminateAll();
            Messages.sendInfo(player, "%d stargates have been destroyed.", stargateCount);
        } else {
            throw new Exception("Insufficient arguments.");
        }
    }
}
