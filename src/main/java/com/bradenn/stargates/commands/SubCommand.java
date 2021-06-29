package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

    String getLabel();

    String getDescription();

    void run(Player player, String[] args) throws Exception;

}
