package com.bradenn.stargates.structures;

import java.util.Map;

public class StructureManager {

    public static Map<String, Class<? extends Structure>> registeredAliases;

    public static void registerAlias(String alias, Class<? extends Structure> structure) {
        registeredAliases.put(alias, structure);
    }

}
