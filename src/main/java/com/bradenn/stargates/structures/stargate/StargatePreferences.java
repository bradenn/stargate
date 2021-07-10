package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.inventory.MenuItem;
import org.bukkit.Material;

public enum StargatePreferences {

    //    INCINERATE("Incinerator", Material.FIRE_CHARGE, "Incinerate all incoming travelers.", false),
    RETURN_TO_SENDER("Return to Sender", Material.BOW, "Incoming travelers without permission will\nbe sent back to the stargate they came from.", false),
    PRIVATE("Private", Material.IRON_BARS, "When enabled, this stargate will not be\npublic displayed on other dialers.", false);

    String name, description;
    Material material;
    boolean enabled;

    StargatePreferences(String displayName, Material iconMaterial, String description, boolean enabledByDefault) {
        this.name = displayName;
        this.material = iconMaterial;
        this.description = description;
        this.enabled = enabledByDefault;
    }

    public void setValue(boolean bool) {
        this.enabled = bool;
    }

    public MenuItem getItem() {
        return new MenuItem(name, material, enabled, description.split("\n"));
    }


}
