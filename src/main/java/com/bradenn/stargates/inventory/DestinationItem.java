package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.StringUtils;
import com.bradenn.stargates.structures.stargate.Stargate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DestinationItem extends Item {


    /*
     * Destination Name
     * World: Overworld
     * Model: MK2
     */

    public DestinationItem(Stargate from, Stargate to) {
        super();

        ItemMeta itemMeta = getItemMeta();
        Map<String, String> lore = new HashMap<>();

        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.WHITE + to.getName());

        switch (Objects.requireNonNull(to.getWorld()).getEnvironment()) {
            case NORMAL:
                setType(Material.GRASS_BLOCK);
                lore.put("World", "&aOverworld");
                if (from.getLocation().getWorld().equals(to.getWorld())) {
                    lore.put("Distance", (int) to.getLocation().distance(from.getLocation()) + " blocks");
                }
                break;
            case NETHER:
                setType(Material.NETHERRACK);
                lore.put("World", "&cNether");
                break;
            case THE_END:
                setType(Material.END_STONE);
                lore.put("World", "&dEnd");
                break;
            default:
                setType(Material.BEDROCK);
                lore.put("World", "&8Unknown");
                break;
        }

        lore.put("Model", to.getModel().name());

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> loreStrings = new ArrayList<>();
        lore.forEach((k, v) -> loreStrings.add(StringUtils.format("&7%s: %s", k, v)));

        if (!Objects.equals(from.getLocation().getWorld(), to.getWorld())) {
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            loreStrings.add(StringUtils.format("&cMK2 Stargate required."));
        }

        itemMeta.setLore(loreStrings);
        setItemMeta(itemMeta);

    }


}
