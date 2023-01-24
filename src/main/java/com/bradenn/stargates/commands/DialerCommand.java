package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.inventory.DialerItem;
import org.bukkit.entity.Player;

public class DialerCommand implements SubCommand {

    public String getLabel() {
        return "dialer";
    }

    public String getDescription() {
        return "Dial a nearby stargate.";
    }

    public Permission getPermission() {
        return Permission.CREATE;
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length == 1) {
            player.getInventory().addItem(new DialerItem());

            Messages.sendInfo(player, "Right click the dialer on a stargate to add it to your saved gates.");
        } else {
            throw new Exception("Excessive arguments.");
        }
    }
}
