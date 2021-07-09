package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.StringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItem extends Item {


    /*
     * Destination Name
     * World: Overworld
     * Model: MK2
     */

    public MenuItem(String title, Material material, boolean value, String... description) {
        super();

        setType(material);

        ItemMeta itemMeta = getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(ChatColor.WHITE + title);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Map<String, String> loreMap = new HashMap<>();
        loreMap.put("State", value ? "§aEnabled" : "§cDisabled");

        List<String> loreStrings = new ArrayList<>();
        loreMap.forEach((k, v) -> loreStrings.add(StringUtils.format("&7%s: %s", k, v)));

        for (String s : description) {
            loreStrings.add(org.bukkit.ChatColor.GRAY + s);
        }

        itemMeta.setLore(loreStrings);

        setItemMeta(itemMeta);

    }


}
