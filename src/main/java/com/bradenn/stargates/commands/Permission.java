package com.bradenn.stargates.commands;

import com.bradenn.stargates.Main;

public enum Permission {

    DEFAULT,
    PURGE,
    CREATE,
    SEED,
    REBUILD,
    REMOVE;

    String getPermissionString() {
        return String.format("%s.%s", Main.getPluginName(), name()).toLowerCase();
    }

}
