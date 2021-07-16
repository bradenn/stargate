package com.bradenn.stargates;

import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.inventory.Menu;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.structures.Structure;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class StargateListener implements Listener {

    @EventHandler
    public void playerEnterPortal(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Orchestrator.checkTrigger(player);
    }

    @EventHandler
    public void entityInteract(PlayerArmorStandManipulateEvent e) {
        if (Objects.isNull(e.getRightClicked().getCustomName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void inventoryEvent(InventoryClickEvent e) {
        if (Objects.requireNonNull(e.getClickedInventory()).getHolder() instanceof Menu) {
            Menu menu = (Menu) e.getInventory().getHolder();
            assert menu != null;
            menu.onClick(e);

            e.setCancelled(true);
        }

    }

    private int sortByDistance(Stargate a, Stargate b, Stargate c) {
        Vector locA = a.getLocation().toVector();
        Vector locB = b.getLocation().toVector();
        Vector locC = c.getLocation().toVector();
        return (int) (locA.distance(locC) - locB.distance(locC));
    }

    @EventHandler
    public void entityInteract(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();

        if (BlockStand.isGenericArmorStand(entity)) {
            String identifier = Objects.requireNonNull(entity.getCustomName());
            String[] args = identifier.split(";");

            try {
                Structure s = StructureManager.getStructureFromUUID(UUID.fromString(args[1]), Class.forName(args[0]).asSubclass(Structure.class));
                s.onInteract(e.getPlayer());
            } catch (Exception exception) {
                e.getPlayer().sendMessage(exception.getMessage());
            }


        }
    }

}
