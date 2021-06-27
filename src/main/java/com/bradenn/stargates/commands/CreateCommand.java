package com.bradenn.stargates.commands;

import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.entity.Player;

public class CreateCommand implements SubCommand {

    public String getLabel() {
        return "create";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public void onRun(Player player, String[] args) throws Exception {
        if (args.length >= 2) {
            String name = args[1];
            Stargate stargate = new Stargate(name, player.getLocation(), Orientation.fromYaw(player.getLocation().getYaw()));
        }else{
            throw new Exception("Insufficient arguments.");
        }
    }


    private void error() {

    }

}
