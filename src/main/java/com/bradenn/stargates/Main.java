package com.bradenn.stargates;

import com.bradenn.stargates.commands.StargateCommand;
import com.bradenn.stargates.runtime.Orchestrator;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static Spacetime spacetime;
    public static Orchestrator orchestrator;

    public static String getPluginName() {
        return plugin.getDescription().getName();
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

        spacetime = new Spacetime();
        spacetime.run();

        orchestrator = new Orchestrator();

        Database.connect();

        Stargate.rebuildAll();
        Dialer.rebuildAll();

    }
}
