package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.inventory.Menu;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.runtime.Wormhole;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StargateMenu extends Menu {

    private final Map<ItemMeta, Stargate> destinations;
    private final Stargate stargate;

    public StargateMenu(Stargate stargate) {
        super("Stargate Preferences", 9);
        this.stargate = stargate;
        this.destinations = new HashMap<>();
        generateInventory(this);
        generateMenu();

    }

    private void generateMenu() {
        for (StargatePreferences value : StargatePreferences.values()) {
            getInventory().addItem(value.getItem());
        }
//        getInventory().addItem(new MenuItem("Return to Sender", Material.BOW,false, "Incoming travelers without permission will be sent back to the stargate they came from."));
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
