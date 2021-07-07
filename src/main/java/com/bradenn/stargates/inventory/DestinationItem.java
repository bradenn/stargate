package com.bradenn.stargates.inventory;

import com.bradenn.stargates.cosmetics.StringUtils;
import com.bradenn.stargates.structures.stargate.Stargate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DestinationItem extends Item {


    /*
     * Destination Name
     * Address: ABDH
     * World: Overworld
     * Model: MK2
     */

    public DestinationItem(Stargate stargate) {
        super();

        ItemMeta itemMeta = getItemMeta();
        Map<String, String> lore = new HashMap<>();

        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.WHITE + stargate.getName());

        lore.put("Address", stargate.getAddress());
        
        switch (Objects.requireNonNull(stargate.getWorld()).getEnvironment()) {
            case NORMAL:
                setType(Material.GRASS_BLOCK);
                lore.put("World", "&aOverworld");
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

        lore.put("Model", stargate.getModel().name());

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        List<String> loreStrings = new ArrayList<>();
        lore.forEach((k, v) -> loreStrings.add(StringUtils.format("&7%s: %s", k, v)));

        itemMeta.setLore(loreStrings);
        setItemMeta(itemMeta);

    }


}
