package com.bradenn.stargates;

import com.bradenn.stargates.commands.StargateCommand;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.runtime.Wormhole;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Rings;
import com.bradenn.stargates.structures.Stargate;
import com.bradenn.stargates.structures.StructureManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static Orchestrator orchestrator;


    public static String getPluginName() {
        return plugin.getDescription().getName();
    }

    public void onEnable() {
        init();
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return super.getDefaultWorldGenerator(worldName, id);
    }

    private void init() {
        plugin = this;

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new StargateListener(), this);

        PluginCommand stargateCommand = getCommand("stargate");
        new StargateCommand(stargateCommand);

        orchestrator = new Orchestrator();
        orchestrator.run();
        Database.connect();

//        Orchestrator.activateWormhole(new Wormhole(Rings.getAll().get(0), Rings.getAll().get(1)));

        Stargate.rebuildAll();
        Dialer.rebuildAll();
        Rings.rebuildAll();

    }
}
