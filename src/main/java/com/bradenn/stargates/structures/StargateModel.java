package com.bradenn.stargates.structures;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum StargateModel {
    MK1,
    MK2;

    public boolean isMk2() {
        return this.equals(MK2);
    }

    @NotNull
    public static StargateModel deserialize(Map<String, Object> args) {
        int ordinal = (int) args.get("model");
        return StargateModel.values()[ordinal];
    }

    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> object = new HashMap<>();
        object.put("model", this.ordinal());
        return object;
    }
}
