package com.bradenn.stargates.structures;

import com.bradenn.stargates.runtime.Orchestrator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.UUID;

public interface Port {

    BoundingBox getTriggerArea();

    UUID getUUID();

    Location getLocation();

    String getName();

    void summonPlayer(Player player);

    void departPlayer(Player player);

    void openPort();

    void closePort();

    void idle();

    default boolean isLocked() {
        return Orchestrator.checkLock(getUUID());
    }

    default void lock() {
        Orchestrator.lock(getUUID());
    }

    default void unlock() {
        Orchestrator.unlock(getUUID());
    }
}
