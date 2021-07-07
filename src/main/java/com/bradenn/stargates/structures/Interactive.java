package com.bradenn.stargates.structures;

import org.bukkit.entity.Player;

public interface Interactive {


    default void onInteract(Player player) {
    }


}
