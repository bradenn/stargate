package com.bradenn.stargates.runtime;

import com.bradenn.stargates.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Orchestrator {

    private static List<Wormhole> activeWormholes;
    private static List<UUID> lockedPorts;


    public Orchestrator() {
        activeWormholes = new ArrayList<>();
        lockedPorts = new ArrayList<>();

    }

    public static void lock(UUID uuid) {
        lockedPorts.add(uuid);
    }

    public static void unlock(UUID uuid) {
        lockedPorts.remove(uuid);
    }

    public static boolean checkLock(UUID uuid) {
        return lockedPorts.contains(uuid);
    }

    public static void addWormhole(Wormhole wormhole) {
        activeWormholes.add(wormhole);
    }

    public static void checkTrigger(Player player) {
        activeWormholes.forEach(w -> w.acceptPlayer(player));
    }

    public void run() {
        new BukkitRunnable() {
            public void run() {
                activeWormholes.removeIf(wormhole -> !wormhole.tick());
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }
}
