package com.bradenn.stargates.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Item extends ItemStack {

    public Item(){
        super(Material.STONE);
    }
}
