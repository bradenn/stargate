package com.bradenn.stargates;

import com.bradenn.stargates.commands.StargateCommand;
import com.bradenn.stargates.structures.Dialer;
import com.bradenn.stargates.structures.Stargate;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static Spacetime spacetime;

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

        Database.connect();

        Stargate.rebuildAll();
        Dialer.rebuildAll();

    }
}
