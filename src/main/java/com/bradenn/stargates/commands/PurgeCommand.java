package com.bradenn.stargates.commands;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.entity.Player;

public class PurgeCommand implements SubCommand {

    public String getLabel() {
        return "purge";
    }

    public String getDescription() {
        return "Destroy all stargates.";
    }

    public void onRun(Player player, String[] args) throws Exception {
        if (args.length >= 1) {
            Stargate.destroyAll();
        }else{
            throw new Exception("Insufficient arguments.");
        }
    }


    private void error() {

    }

}
