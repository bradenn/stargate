package com.bradenn.stargates.structures;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DialerInventory implements InventoryHolder {

    private final UUID stargateUUID;
    private final Stargate stargate;
    private final Inventory inventory;

    public DialerInventory(UUID stargateUUID) throws Exception {
        Stargate stargate = Stargate.fromUUID(stargateUUID);
        if (Objects.isNull(stargate)) throw new Exception("Invalid Stargate");

        this.stargate = stargate;

        String title = String.format("%s : %s", stargate.getName(), stargate.getAddress());
        this.inventory = generateInventory(title);
        this.stargateUUID = stargateUUID;

        Stargate.getAll().forEach(this::addDestination);
    }

    private static String format(String message, Object... args) {
        String formatted = String.format(message, args);
        return ChatColor.translateAlternateColorCodes('&', formatted);
    }

    public Stargate getStargate() {
        return stargate;
    }

    private Inventory generateInventory(String title) {
        return Bukkit.createInventory(this, InventoryType.PLAYER, title);
    }

    private void addDestination(Stargate stargate) {
        if (stargate.getUUID().equals(this.stargate.getUUID())) return;

        World gateWorld = stargate.getLocation().getWorld();
        Material material;
        String address = "&7Address: &7" + stargate.getAddress();
        String world = "&7World: ";
        String gateModel = "&7Model: &7" + stargate.getModel();

        switch (gateWorld.getEnvironment()) {
            case NORMAL:
                material = Material.GRASS_BLOCK;
                world += "&aOverworld";
                break;
            case NETHER:
                material = Material.NETHERRACK;
                world += "&cNether";
                break;
            case THE_END:
                material = Material.END_STONE;
                world += "&dEnd";
                break;
            default:
                material = Material.BEDROCK;
                world += "&8Unknown";
                break;
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        String title = String.format("&f%s", stargate.getName());
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));

        List<String> lore = new ArrayList<>();

        lore.add(format(address));
        lore.add(format(world));
        lore.add(format(gateModel));
        if (!this.stargate.getModel().isMk2()) {
            if (!gateWorld.equals(this.stargate.getLocation().getWorld())){
                lore.add(format("&cMK2 Stargate required."));
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

        }

        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        inventory.addItem(itemStack);
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public UUID getStargateUUID() {
        return stargateUUID;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
