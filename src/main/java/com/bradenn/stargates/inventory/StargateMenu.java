package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.runtime.Wormhole;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StargateMenu extends Menu {

    private final Map<ItemMeta, Stargate> destinations;
    private final Stargate stargate;

    public StargateMenu(Stargate stargate) {
        super("Stargate", InventoryType.BARREL);
        this.stargate = stargate;
        this.destinations = new HashMap<>();
        generateInventory(this);

        Stargate.getAll().forEach(this::addDestination);
    }

    private void addDestination(Stargate stargate) {
        if (stargate.getUUID().equals(this.stargate.getUUID())) return;
        ItemStack item = new DestinationItem(stargate);
        destinations.put(item.getItemMeta(), stargate);
        getInventory().addItem(item);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            if (!destinations.containsKey(Objects.requireNonNull(e.getCurrentItem()).getItemMeta())) {
                throw new Exception("Unknown item in the bagging area");
            }
            Wormhole wormhole = new Wormhole(this.stargate, destinations.get(e.getCurrentItem().getItemMeta()), 200);
            Orchestrator.addWormhole(wormhole);
        } catch (Exception exception) {
            Messages.sendError((Player) e.getWhoClicked(), "%s", exception.getMessage());
        }

    }
}
