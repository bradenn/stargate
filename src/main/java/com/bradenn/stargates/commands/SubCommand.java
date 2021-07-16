package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

    String getLabel();

    String getDescription();

    default String getPermission() {
        return "stargate";
    }

    void run(Player player, String[] args) throws Exception;

}
