package com.bradenn.stargates;

import com.bradenn.stargates.commands.StargateCommand;
import com.bradenn.stargates.structures.DialerStructure;
import com.bradenn.stargates.structures.GateStructure;
import com.bradenn.stargates.structures.PersistentStructure;
import com.bradenn.stargates.structures.StructureOrientation;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Stargate extends JavaPlugin {

    public static Plugin plugin;
    public static Spacetime spacetime;

    static {
        ConfigurationSerialization.registerClass(GateStructure.class, "GateStructure");
        ConfigurationSerialization.registerClass(DialerStructure.class, "DialerStructure");
        ConfigurationSerialization.registerClass(StructureOrientation.class, "StructureOrientation");
    }

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
    }

    private void init() {
        plugin = this;

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new StargateListener(), this);
        PluginCommand stargateCommand = getCommand("stargate");
        new StargateCommand().init(stargateCommand);

        spacetime = new Spacetime(plugin);
        spacetime.run();
        try {

            PersistenceManager.forEachStructure("stargate")
                    .forEach(gate -> {
                        GateStructure persistentStructure = GateStructure.deserialize(gate);
                        persistentStructure.rebuild();
                    });

            PersistenceManager.forEachStructure("dialer")
                    .forEach(gate -> {
                        DialerStructure persistentStructure = DialerStructure.deserialize(gate);
                        persistentStructure.rebuild();
                    });

        }catch (Exception ignored){

        }


//        PersistenceManager.updateGates();
    }
}
