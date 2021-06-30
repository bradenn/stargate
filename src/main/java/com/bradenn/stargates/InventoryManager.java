package com.bradenn.stargates;

import com.bradenn.stargates.structures.DialerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryManager {

    private final Inventory inventory;

    public InventoryManager(UUID stargateUUID, String name) {
//        DialerInventory dialerInventory = new DialerInventory(stargateUUID);
        this.inventory = Bukkit.createInventory(null, InventoryType.PLAYER, name);

    }

    private String format(String toFormat) {
        return ChatColor.translateAlternateColorCodes('&', toFormat);
    }

    public void addItem(String name, Material material, boolean enchanted, String... args) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        if (enchanted) itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        for (String arg : args) {
            lore.add(format(arg));
        }

        itemMeta.setDisplayName(format(name));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.addItem(itemStack);

    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }


}
