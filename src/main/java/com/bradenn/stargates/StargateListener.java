package com.bradenn.stargates;

import com.bradenn.stargates.structures.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StargateListener implements Listener {

    @EventHandler
    public void playerEnterPortal(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Main.spacetime.intersectsPortal(player);
//        e.getPlayer().sendMessage(Orientation.fromYaw(e.getPlayer().getLocation().getYaw()).toString());

    }

    @EventHandler
    public void entityInteract(PlayerArmorStandManipulateEvent e) {
        if (Objects.isNull(e.getRightClicked().getCustomName())) return;
        e.setCancelled(true);

    }

    @EventHandler
    public void inventoryEvent(InventoryClickEvent e) {
        String title = e.getView().getTitle();

        ItemStack itemStack = e.getCurrentItem();
        if (Objects.isNull(itemStack)) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (Objects.isNull(itemMeta)) return;

        List<String> itemLore = itemMeta.getLore();
        if (Objects.isNull(itemLore)) return;

        String line = itemLore.get(1);

        if (title.startsWith("Stargate: ")) {

            try {
                String sourceAddress = title.replace("Stargate: ", "");
                e.getWhoClicked().sendMessage(sourceAddress);
                Stargate sourceStructure = Stargate.fromAddress(sourceAddress);

                String targetAddress = line.replace(ChatColor.GRAY + "Address: ", "");
                e.getWhoClicked().sendMessage(targetAddress.strip());
                Stargate targetStructure = Stargate.fromAddress(targetAddress);


                Main.spacetime.establishWormhole(sourceStructure, targetStructure);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
            e.setCancelled(true);

        }

    }

    private int sortByDistance(Stargate a, Stargate b, Stargate c) {
        Location locA = a.getStructure().getLocation();
        Location locB = b.getStructure().getLocation();
        Location locC = c.getStructure().getLocation();
        return (int) (locA.distance(locC) - locB.distance(locC));
    }

    @EventHandler
    public void entityInteract(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            String customName = armorStand.getCustomName();
            if (Objects.isNull(customName)) return;
            UUID uuid = UUID.fromString(customName);
            try {
                Dialer dialer = Dialer.fromUUID(uuid);

                if(Objects.isNull(dialer)) throw new Exception("Dialer does not exist.");
                Stargate stargate = dialer.getStargate();

                InventoryManager inventoryManager = new InventoryManager("Stargate: " + stargate.getAddress());

                List<Stargate> stargateList = Stargate.getAll();

                stargateList.removeIf(stargateRef ->
                        stargateRef.getUUID().equals(stargate.getUUID()));

                stargateList.sort((a, b) -> sortByDistance(a, b, stargate));

                stargateList.forEach(stargateRef -> {
                    String distance = String.format("&7Distance: %.0f blocks", stargateRef.getStructure().getLocation().distance(stargate.getStructure().getLocation()));
                    String address = String.format("&7Address: %s", stargateRef.getAddress());
                    inventoryManager.addItem("&f" + stargateRef.getName(), Material.DEEPSLATE_TILES, distance, address);
                });

                inventoryManager.openInventory(e.getPlayer());

            } catch (Exception ex) {
                e.getPlayer().sendMessage(ex.getMessage());
            }

        }
    }

}
