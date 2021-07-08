package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.inventory.MenuItem;
import org.bukkit.Material;

public enum StargatePreferences {

    RETURN_TO_SENDER("Return to Sender", Material.BOW, "Incoming travelers without permission will\nbe sent back to the stargate they came from.", false),
    INCINERATE("Incinerator", Material.FIRE_CHARGE, "Incinerate all incoming travelers.", false);

    String name, description;
    Material material;
    boolean enabled;

    StargatePreferences(String displayName, Material iconMaterial, String description, boolean enabledByDefault) {
        this.name = displayName;
        this.material = iconMaterial;
        this.description = description;
        this.enabled = enabledByDefault;
    }

    public MenuItem getItem() {
        return new MenuItem(name, material, enabled, description.split("\n"));
    }


}
