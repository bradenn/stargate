package com.bradenn.stargates;

import com.bradenn.stargates.structures.Stargate;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spacetime {

    private final Map<Stargate, Stargate> wormholes;

    public Spacetime() {
        wormholes = new HashMap<>();

    }

    public void run() {
        Spacetime spacetime = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                wormholes.forEach(spacetime::updateWormhole);
            }
        }.runTaskTimer(Main.plugin, 0, 3);
    }

    public void updateWormhole(Stargate outgoing, Stargate target) {
        outgoing.renderPortal();
        target.renderPortal();
    }

    public void intersectsPortal(Player player) {
        wormholes.forEach((from, to) -> {
            if (from.getBoundingBox().overlaps(player.getBoundingBox())) {
                to.summonPlayer(player);
            }
        });
    }

    private boolean isBusy(Stargate gate) {
        return (wormholes.containsKey(gate) || wormholes.containsValue(gate));
    }

    public void establishWormhole(Stargate outgoing, Stargate target) throws Exception {
        if (isBusy(outgoing)) {
            throw new Exception("Outgoing Stargate is busy.");
        } else if (isBusy(target)) {
            throw new Exception("Target Stargate is busy.");
        }
        wormholes.put(outgoing, target);

        BossBar bossBar = Bukkit.getServer().createBossBar(String.format("Stargate: %s → %s", outgoing.getName(), target.getName()), BarColor.WHITE, BarStyle.SEGMENTED_12);

        for (Entity entity : outgoing.getLocation().getWorld().getNearbyEntities(outgoing.getLocation(), 10, 10, 10)) {
            if (entity instanceof Player) {
                bossBar.addPlayer((Player) entity);
            }
        }

        new BukkitRunnable() {
            double countdown = 12;

            @Override
            public void run() {
                countdown -= 0.20;
                bossBar.setProgress(countdown / 12);
                outgoing.renderPortal();
                target.renderPortal();
                if (countdown <= 0) {
                    wormholes.remove(outgoing, target);
                    bossBar.removeAll();
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 20, 4);

    }


}
