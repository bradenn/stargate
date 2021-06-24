package com.bradenn.stargates.commands;

import com.bradenn.stargates.Gate;
import org.bukkit.entity.Player;

public class RegisterCommand implements SubCommand {

    public String getLabel() {
        return "register";
    }

    public String getDescription() {
        return "Register an existing stargate.";
    }

    public void onRun(Player player, String[] args) {
        Gate gate = new Gate(player.getLocation());
        gate.assignAddress();
        gate.save();
    }

    private void error() {

    }

}
