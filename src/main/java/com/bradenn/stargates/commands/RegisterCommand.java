package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public class RegisterCommand implements SubCommand {

    public String getLabel() {
        return "register";
    }

    public String getDescription() {
        return "Register an existing stargate.";
    }

    public void onRun(Player player, String[] args) {

    }

    private void error() {

    }

}
