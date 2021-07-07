package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    private final InventoryType inventoryType;
    private final String title;

    public Menu(String title, InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        this.title = title;
    }

    abstract void onClick(InventoryClickEvent e);

    public void generateInventory(InventoryHolder inventoryHolder) {
        this.inventory = Bukkit.createInventory(inventoryHolder, inventoryType, StringUtils.format(title));
    }

    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public void showMenu(Player player) {
        player.openInventory(inventory);
    }


}
