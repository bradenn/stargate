package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.inventory.DestinationItem;
import com.bradenn.stargates.inventory.Menu;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.runtime.Wormhole;
import com.bradenn.stargates.structures.StructureManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class RemoteMenu extends Menu {

    private final Map<ItemMeta, Stargate> destinations;
    private final Player player;
    private Stargate stargate;

    public RemoteMenu(Player player) throws Exception {
        super(String.format("Remote Dialer: %s", player.getName()), 6 * 9);
        this.player = player;
        this.destinations = new HashMap<>();
        getNearbyStargate();
        generateInventory(this);
        Stargate.getAll().forEach(this::addDestination);
    }


    private void getNearbyStargate() throws Exception {
        Stream<Entity> entityStream = player.getNearbyEntities(8, 0, 8).stream().filter(BlockStand::isGenericArmorStand);
        var val = entityStream.findFirst();
        if (val.isEmpty()) throw new Exception("No nearby stargates.");

        var sample = val.get();
        UUID uuid = UUID.fromString(Objects.requireNonNull(sample.getCustomName()).split(";")[1]);
        this.stargate = StructureManager.getStructureFromUUID(uuid, Stargate.class);
        setTitle(String.format("Remote Dialer: %s", stargate.getName()));
    }

    private void addDestination(Stargate stargate) {
        if (stargate.getUUID().equals(this.stargate.getUUID())) return;
        if (stargate.checkPreference(StargatePreferences.PRIVATE)) return;

        ItemStack item = new DestinationItem(this.stargate, stargate);
        destinations.put(item.getItemMeta(), stargate);
        getInventory().addItem(item);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            if (destinations.containsKey(Objects.requireNonNull(e.getCurrentItem()).getItemMeta())) {
                Stargate destination = destinations.get(e.getCurrentItem().getItemMeta());
                if (!stargate.getWorld().equals(destination.getWorld())) {
                    if (!stargate.getModel().equals(StargateModel.MK2)) {
                        throw new Exception("An MK2 Stargate is required for extra-dimensional travel.");
                    }
                }
                Wormhole wormhole = new Wormhole(this.stargate, destination, 200);
                Orchestrator.addWormhole(wormhole);
            }
        } catch (Exception exception) {
            Messages.sendError((Player) e.getWhoClicked(), "%s", exception.getMessage());
        }

    }
}
