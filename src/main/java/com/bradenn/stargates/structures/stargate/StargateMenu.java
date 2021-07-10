package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.inventory.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StargateMenu extends Menu {

    private final Stargate stargate;
    private final Map<ItemMeta, String> destinations;

    public StargateMenu(Stargate stargate) {
        super("Stargate Preferences", 9);
        this.stargate = stargate;
        this.destinations = new HashMap<>();
        generateInventory(this);
        generateMenu();

    }

    private void generateMenu() {
        stargate.getPreferences().forEach((k, v) -> {
            StargatePreferences sp = StargatePreferences.valueOf(k);
            sp.setValue(v);

            getInventory().addItem(sp.getItem());
            destinations.put(sp.getItem().getItemMeta(), k);

        });
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            ItemMeta im = Objects.requireNonNull(e.getCurrentItem()).getItemMeta();
            if (destinations.containsKey(im)) {
                String key = destinations.get(im);
                stargate.setPreference(key, !stargate.getPreferences().get(key));
                getInventory().clear();
                generateMenu();

            }
        } catch (Exception exception) {
            Messages.sendError((Player) e.getWhoClicked(), "%s", exception.getMessage());
        }

    }
}
