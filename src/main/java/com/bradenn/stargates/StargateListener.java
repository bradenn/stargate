package com.bradenn.stargates;

import com.bradenn.stargates.inventory.StargateMenu;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.structures.Structure;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.stargate.Stargate;
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
        Orchestrator.checkTrigger(player);
    }

    @EventHandler
    public void entityInteract(PlayerArmorStandManipulateEvent e) {
        if (Objects.isNull(e.getRightClicked().getCustomName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void inventoryEvent(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof StargateMenu)) return;

        StargateMenu menu = (StargateMenu) e.getInventory().getHolder();
        menu.onClick(e);

        ItemStack itemStack = e.getCurrentItem();
        if (Objects.isNull(itemStack)) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (Objects.isNull(itemMeta)) return;

        List<String> itemLore = itemMeta.getLore();
        if (Objects.isNull(itemLore)) return;


//        try {
//            if (itemStack.containsEnchantment(Enchantment.ARROW_INFINITE))
//                throw new Exception("A Stargate MK2 is required to establish a multi-dimensional wormhole.");
//
//
//            String targetAddress = itemLore.get(0).replace(ChatColor.GRAY + "Address: ", "");
//
//            Stargate sourceStargate = dialerInventory.getStargate();
//            Stargate targetStargate = Stargate.fromAddress(ChatColor.stripColor(targetAddress));
//
//
//            Wormhole staticWormhole = new Wormhole(sourceStargate, targetStargate, 200);
//            Orchestrator.addWormhole(staticWormhole);
//        } catch (Exception exp) {
//            Messages.sendError((Player) e.getWhoClicked(), exp.getMessage());
//        }

        e.setCancelled(true);

    }

    private int sortByDistance(Stargate a, Stargate b, Stargate c) {
        Vector locA = a.getLocation().toVector();
        Vector locB = b.getLocation().toVector();
        Vector locC = c.getLocation().toVector();
        return (int) (locA.distance(locC) - locB.distance(locC));
    }

    @EventHandler
    @SuppressWarnings("unchecked")
    public void entityInteract(PlayerInteractAtEntityEvent e) throws Exception {
        Entity entity = e.getRightClicked();

        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;

            if (armorStand.getMetadata("structure").isEmpty()) return;
            if (armorStand.getMetadata("class").isEmpty()) return;

            Class<? extends Structure> structure = (Class<? extends Structure>) armorStand.getMetadata("class").get(0).value();

            Structure s = StructureManager.getStructureFromUUID(UUID.fromString(armorStand.getMetadata("structure").get(0).asString()), structure);
            s.onInteract(e.getPlayer());

//            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR)) {
//
//                dialer.getStargate().setModel(StargateModel.MK2);
//            } else {
//
//                dialerInventory.openInventory(e.getPlayer());
//            }
        }
    }

}
