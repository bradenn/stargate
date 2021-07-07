package com.bradenn.stargates.inventory;

import com.bradenn.stargates.structures.Port;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class StargateMenu extends Menu {

    private Map<ItemStack, Port> destinations;

    public StargateMenu(Stargate stargate) {
        super("Stargate", InventoryType.BARREL);

        generateInventory(this);
    }

    public void getStargates() {

    }


}
