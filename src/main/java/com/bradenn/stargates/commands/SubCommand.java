package com.bradenn.stargates.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

    void onRun(Player player, String[] args) throws Exception;

    String getLabel();

    String getDescription();


}
