package com.bradenn.stargates;

import com.bradenn.stargates.commands.StargateCommand;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.structures.StructureManager;
import com.bradenn.stargates.structures.dialer.Dialer;
import com.bradenn.stargates.structures.rings.Rings;
import com.bradenn.stargates.structures.stargate.Stargate;
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

    private void init() {
        plugin = this;

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new StargateListener(), this);

        PluginCommand stargateCommand = getCommand("stargate");
        new StargateCommand(stargateCommand);

        Database.connect();

        StructureManager.init();
        StructureManager.rebuildAll();

        orchestrator = new Orchestrator();
        orchestrator.run();

    }
}
