package com.bradenn.stargates.structures;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum StructureOrientation implements ConfigurationSerializable {

    NORTH,
    SOUTH,
    WEST,
    EAST;

    @NotNull
    public static StructureOrientation deserialize(Map<String, Object> args) {
        int ordinal = (int) args.get("facing");
        return StructureOrientation.values()[ordinal];
    }

    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> object = new HashMap<>();
        object.put("facing", this.ordinal());
        return object;
    }
}
