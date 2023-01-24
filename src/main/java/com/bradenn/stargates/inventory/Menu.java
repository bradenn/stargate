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

    private final InventoryType inventoryType;
    private final int slots;
    private String title;
    protected Inventory inventory;

    public void setTitle(String title) {
        this.title = title;
    }

    public Menu(String title, InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.slots = 0;
    }

    public Menu(String title, int slots) {
        this.inventoryType = null;
        this.slots = slots;
        this.title = title;
    }

    public abstract void onClick(InventoryClickEvent e);

    public void generateInventory(InventoryHolder inventoryHolder) {
        if (this.inventoryType == null) {
            this.inventory = Bukkit.createInventory(inventoryHolder, slots, StringUtils.format(title));
        } else {
            this.inventory = Bukkit.createInventory(inventoryHolder, inventoryType, StringUtils.format(title));

        }
    }

    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public void showMenu(Player player) {
        player.openInventory(inventory);
    }


}
