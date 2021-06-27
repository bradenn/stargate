package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public class LocateCommand implements SubCommand {

    public String getLabel() {
        return "locate";
    }

    public String getDescription() {
        return "Find your nearest stargate.";
    }

    public void onRun(Player player, String[] args) {


    }

    private void error() {

    }

}
