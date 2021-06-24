package com.bradenn.stargates;

import com.bradenn.stargates.structures.GateStructure;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collection;
import java.util.Objects;

public class StargateListener implements Listener {


    @EventHandler
    public void enterPortal(PlayerMoveEvent e) {
//        GateStructure gateStructure = new GateStructure(e.getPlayer().getLocation());
//        ParticleEffects.drawBoundingBox(e.getPlayer().getLocation().getBlock().getBoundingBox());

//
//        Block portalBlock = e.getTo().getBlock().getRelative(0, -1, 0);
//        if (portalBlock.getType().equals(Material.DEEPSLATE_TILES)) {
//            Gate gate = new Gate(portalBlock.getLocation());
//            Stargate.spacetime.transportPlayer(e.getPlayer(), gate.getAddress());
//        }
    }

    @EventHandler
    public void buttonClickEvent(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (Objects.nonNull(block)) {
            if (block.getType().equals(Material.STONE_BUTTON)) {
                Location buttonLocation = block.getLocation();
                ItemFrameDialer itemFrameDialer = new ItemFrameDialer(buttonLocation);
                String address = itemFrameDialer.getAddress();
                Gate gate = new Gate(block.getLocation());
                gate.connect(address);
            }
        }
    }

}
