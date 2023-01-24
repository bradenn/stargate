package com.bradenn.stargates.inventory;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class MenuLauncher extends ItemStack {

    private final UUID uuid;

    public MenuLauncher(@NotNull Material type) {
        super(type);
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Document getDocument() {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("uuid", uuid.toString());
        return document;
    }


}
