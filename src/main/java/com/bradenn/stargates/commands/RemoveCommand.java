package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Player;

import java.util.Objects;

public class RemoveCommand implements SubCommand {

    public String getLabel() {
        return "remove";
    }

    public String getDescription() {
        return "Remove a stargate referenced by address.";
    }

    public String getPermission() {
        return "stargates.remove.all";
    }

    public void run(Player player, String[] args) throws Exception {
        Stargate stargate = Stargate.fromAddress(args[1]);
        if (Objects.isNull(stargate)) throw new Exception("Invalid stargate address.");
        stargate.terminate();
        Messages.sendInfo(player, "The stargate '%s' has been permanently removed.", stargate.getName());
    }
}
