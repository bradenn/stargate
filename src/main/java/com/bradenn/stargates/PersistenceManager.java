package com.bradenn.stargates;

import com.bradenn.stargates.structures.PersistentStructure;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PersistenceManager {

    private final Configuration config;
    private final PersistentStructure persistentStructure;

    public PersistenceManager(PersistentStructure persistentStructure) {
        Plugin plugin = Stargate.plugin;
        this.config = plugin.getConfig();
        this.persistentStructure = persistentStructure;
    }

    public static List<Map<?, ?>> forEachStructure(String identifier) {
        return Stargate.plugin.getConfig().getMapList(identifier);
    }

    public void save() {
        List<Map<?, ?>> structures = getStructures();
        structures.add(persistentStructure.serialize());
        saveStructures(structures);
    }

    public void remove() {
        List<Map<?, ?>> structures = getStructures();
        structures.remove(persistentStructure.serialize());
        saveStructures(structures);
    }

    private List<Map<?, ?>> getStructures() {
        String identifier = persistentStructure.getIdentifier();
        return config.getMapList(identifier);
    }

    private void saveStructures(List<Map<?, ?>> structures) {
        String identifier = persistentStructure.getIdentifier();
        config.set(identifier, structures);
        Stargate.plugin.saveConfig();
    }


}
