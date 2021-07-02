package com.bradenn.stargates.commands;

import com.bradenn.stargates.cosmetics.BlockStand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.util.UUID;

public class TestCommand implements SubCommand {

    public String getLabel() {
        return "test";
    }

    public String getDescription() {
        return "Test a stargate feature.";
    }

    public void run(Player player, String[] args) throws Exception {
        BlockStand blockStand = new BlockStand(UUID.randomUUID(), player.getWorld());
        blockStand.setMaterial(Material.SMOOTH_STONE_SLAB);
        blockStand.smallBlockAt(player.getLocation().add(0, 0, -0.25), new EulerAngle(Math.toRadians(90), 0, 0));
    }
}
