package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public class TestCommand implements SubCommand {

    public String getLabel() {
        return "test";
    }

    public String getDescription() {
        return "Test a stargate feature.";
    }

    public void run(Player player, String[] args) throws Exception {

    }
}
