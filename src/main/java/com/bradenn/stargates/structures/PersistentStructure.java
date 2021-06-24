package com.bradenn.stargates.structures;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BoundingBox;

import java.util.UUID;

public interface PersistentStructure extends ConfigurationSerializable {


    default String getIdentifier() {
        return "null";
    }

    Location getLocation();

    BoundingBox getBoundingBox();

    StructureOrientation getStructureOrientation();

    UUID getUUID();

    void rebuild();
}
