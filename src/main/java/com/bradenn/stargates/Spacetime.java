package com.bradenn.stargates;

import com.bradenn.stargates.structures.GateStructure;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Spacetime {

    private final Map<UUID, UUID> wormholes;
    private final Plugin plugin;

    public Spacetime(Plugin plugin) {
        wormholes = new HashMap<>();
        this.plugin = plugin;
    }

    public void run() {
        Spacetime spacetime = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                wormholes.forEach(spacetime::updateWormhole);
            }
        }.runTaskTimer(plugin, 1, 2);
    }

    public void updateWormhole(UUID outgoing, UUID target) {
//        Gate outgoingGate =
//        ParticleEffects.spawnPortal(outgoingGate.getLocation());
//        Gate targetGate = new Gate(target);
//        ParticleEffects.spawnPortal(targetGate.getLocation());
    }

    public void transportPlayer(Player player, String outgoing) {
//        if (wormholes.containsKey(outgoing)) {
//            Gate targetGate = new Gate(wormholes.get(outgoing));
//            player.teleport(targetGate.getLocation());
//        }
    }

    private boolean isBusy(String gate) {
        return (wormholes.containsKey(gate) || wormholes.containsValue(gate));
    }

    public void establishWormhole(String outgoing, String target) throws Exception {
        if (isBusy(outgoing)) {
            throw new Exception("Outgoing Stargate is busy.");
        } else if (isBusy(target)) {
            throw new Exception("Target Stargate is busy.");
        }

//        wormholes.put(outgoing, target);
    }


}
