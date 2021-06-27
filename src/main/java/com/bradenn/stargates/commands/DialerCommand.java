package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public class DialerCommand implements SubCommand {

    public String getLabel() {
        return "dialer";
    }

    public String getDescription() {
        return "Build and register a stargate.";
    }

    public void onRun(Player player, String[] args) {

    }


    private void error() {

    }

}
