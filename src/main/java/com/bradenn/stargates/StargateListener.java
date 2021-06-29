package com.bradenn.stargates;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.runtime.Wormhole;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StargateListener implements Listener {

    @EventHandler
    public void playerEnterPortal(PlayerMoveEvent e) {
        Player player = e.getPlayer();
//        Wormhole.checkIntersection(player);
    }

//    public void chunkLoad(ChunkLoadEvent e) {
//        if (!e.isNewChunk()) {
//            Arrays.stream(e.getChunk().getEntities()).filter(entity -> {
//                if (!entity.isPersistent()) return false;
//                if (entity.getCustomName() == null) return false;
//
//                if (entity.getCustomName().length() == 32) {
//                    return true;
//                }
//
//                return false;
//            }).map(entity -> {
//                UUID uuid = UUID.fromString(entity.getCustomName());
//                return uuid;
//            }).forEach(uuid -> {
//                Stargate.fromUUID()
//            });
//        }
//    }

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

        String line = itemLore.get(2);

        if (title.startsWith("Stargate: ")) {

            try {
                String sourceAddress = title.replace("Stargate: ", "");
                Stargate sourceStructure = Stargate.fromAddress(sourceAddress);

                String targetAddress = line.replace(ChatColor.GRAY + "Address: ", "");
                Stargate targetStructure = Stargate.fromAddress(targetAddress);

                Wormhole wormhole = new Wormhole(sourceStructure, targetStructure);
                Orchestrator.add(wormhole);
            } catch (Exception exp) {
                Messages.sendError((Player) e.getWhoClicked(), exp.getMessage());
            }
            e.setCancelled(true);

        }

    }

    private int sortByDistance(Stargate a, Stargate b, Stargate c) {
        Vector locA = a.getStructure().getLocation().toVector();
        Vector locB = b.getStructure().getLocation().toVector();
        Vector locC = c.getStructure().getLocation().toVector();
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

                if (Objects.isNull(dialer)) throw new Exception("Dialer does not exist.");
                Stargate stargate = dialer.getStargate();

                InventoryManager inventoryManager = new InventoryManager("Stargate: " + stargate.getAddress());

                List<Stargate> stargateList = Stargate.getAll();

                stargateList.removeIf(stargateRef ->
                        stargateRef.getUUID().equals(stargate.getUUID()));

                stargateList.sort((a, b) -> sortByDistance(a, b, stargate));

                stargateList.forEach(stargateRef -> {
                    World worldRef = stargateRef.getLocation().getWorld();
                    World userWorld = entity.getWorld();
                    String address = String.format("&7Address: %s", stargateRef.getAddress());
                    double absoluteDistance = stargateRef.getStructure().getLocation().toVector().distance(stargate.getStructure().getLocation().toVector());
                    String distance = String.format("&7Distance: %.0f blocks", absoluteDistance);
                    String world;
                    Material icon = Material.GRASS_BLOCK;
                    assert worldRef != null;
                    boolean alternateDimension = worldRef.getEnvironment() != userWorld.getEnvironment();
                    String requirement = alternateDimension?"&cMK.2 Ring Required":"";
                    switch (Objects.requireNonNull(worldRef).getEnvironment()) {
                        case NORMAL:
                            icon = Material.GRASS_BLOCK;
                            world = String.format("&7World: &a%s", "Overworld");
                            break;
                        case NETHER:
                            icon = Material.NETHERRACK;
                            world = String.format("&7World: &c%s", "Nether");
                            break;
                        case THE_END:
                            icon = Material.END_STONE;
                            world = String.format("&7World: &d%s", "End");
                            break;
                        default:
                            distance = "&7Distance: ∞ blocks";
                            world = "&7World: Unknown";
                    }
                    inventoryManager.addItem("&f" + stargateRef.getName(), icon, alternateDimension, distance, world, address, requirement);
                });

                inventoryManager.openInventory(e.getPlayer());

            } catch (Exception ex) {
                e.getPlayer().sendMessage(ex.getMessage());
            }

        }
    }

}
