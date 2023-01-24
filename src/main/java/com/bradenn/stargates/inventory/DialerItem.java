package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DialerItem extends ItemStack {

    public DialerItem() {
        super(Material.COMPASS);

        ItemMeta im = getItemMeta();
        String name = StringUtils.format("&bRemote Dialer");
        assert im != null;
        im.setDisplayName(name);

        List<String> lore = new ArrayList<>();

        String description = StringUtils.format("&7Right Click a stargate while sneaking to add it to your saved addresses.");
        lore.add(description);

        im.setLore(lore);
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        setItemMeta(im);
    }

    public void openMenu() {

    }

}
