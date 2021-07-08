package com.bradenn.stargates.structures.dialer;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.inventory.DestinationItem;
import com.bradenn.stargates.inventory.Menu;
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

public class DialerMenu extends Menu {

    private final Map<ItemMeta, Stargate> destinations;
    private final Stargate stargate;

    public DialerMenu(Stargate stargate) {
        super(String.format("Stargate: %s", stargate.getName()), InventoryType.BARREL);
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
                throw new Exception("There is an unknown item in the bagging area.");
            }
            Wormhole wormhole = new Wormhole(this.stargate, destinations.get(e.getCurrentItem().getItemMeta()), 200);
            Orchestrator.addWormhole(wormhole);
        } catch (Exception exception) {
            Messages.sendError((Player) e.getWhoClicked(), "%s", exception.getMessage());
        }

    }
}
