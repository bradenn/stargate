package com.bradenn.stargates.structures;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum Orientation implements ConfigurationSerializable {

    /* WEST -> EAST */
    NORTH,
    /* EAST -> WEST */
    SOUTH,
    /* SOUTH -> NORTH */
    WEST,
    /* NORTH -> SOUTH */
    EAST;

    public static Orientation fromYaw(float yaw) {
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return SOUTH;
        } else if (yaw < 135) {
            return WEST;
        } else if (yaw < 225) {
            return NORTH;
        } else if (yaw < 315) {
            return EAST;
        }
        return SOUTH;
    }

    @NotNull
    public static Orientation deserialize(Map<String, Object> args) {
        int ordinal = (int) args.get("facing");
        return Orientation.values()[ordinal];
    }

    public Vector rotate(double x, double y, double z) {
        switch (this) {
            case NORTH:
                return new Vector(x, y, z);
            case SOUTH:
                return new Vector(x, y, z);
            case WEST:
                return new Vector(z, y, x);
            case EAST:
                return new Vector(z, y, x);
        }
        return new Vector(0, 0, 0);
    }

    public float playerYaw() {
        switch (this) {
            case NORTH:
                return 0;
            case SOUTH:
                return -180;
            case WEST:
                return -90;
            case EAST:
                return 90;
        }
        return 0;
    }

    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> object = new HashMap<>();
        object.put("facing", this.ordinal());
        return object;
    }

    public Vector translate(double x, double y, double z) {
        switch (this) {
            case NORTH:
                return new Vector(x, y, z);
            case SOUTH:
                return new Vector(-x, y, -z);
            case WEST:
                return new Vector(z, y, -x);
            case EAST:
                return new Vector(-z, y, x);
        }
        return new Vector(0, 0, 0);
    }

    public Vector rotateSpin(double x, double y, double z) {
        switch (this) {
            case NORTH:
                return new Vector(x, y, z);
            case SOUTH:
                return new Vector(x, y, z);
            case WEST:
                return new Vector(z, y, x);
            case EAST:
                return new Vector(z, y, x);
        }
        return new Vector(0, 0, 0);
    }
}
