package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.stargate.Stargate;
import com.bradenn.stargates.structures.stargate.StargateModel;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SeedCommand implements SubCommand {

    public String getLabel() {
        return "seed";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public Permission getPermission() {
        return Permission.SEED;
    }

    public void run(Player player, String[] args) throws Exception {
        if (args.length == 1) {
            Block block = player.getWorld().getHighestBlockAt(player.getLocation());
            Stargate stargate = StructureManager.createStructure(RandomStringUtils.randomAlphanumeric(4).toUpperCase(), block.getLocation().clone().add(0, 1, 0), Stargate.class);
            stargate.save();
            stargate.setModel(StargateModel.SEED);

            Messages.sendInfo(player, "A stargate named '%s' has been seeded.", stargate.getName());
        } else {
            throw new Exception("Excessive arguments.");
        }
    }
}
