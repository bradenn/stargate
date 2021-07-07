package com.bradenn.stargates.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class ProgressBar {

    private final float maxValue;
    private final float delta;
    private float remaining;
    private final BossBar bossBar;

    public ProgressBar(String title, float maxValue, float delta) {
        this.bossBar = Bukkit.createBossBar(title, BarColor.BLUE, BarStyle.SEGMENTED_10);
        this.maxValue = maxValue;
        this.remaining = maxValue;
        this.delta = delta;
    }

    private boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    public void addNearbyPlayers(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.getNearbyEntities(location, 16, 16, 16, this::isPlayer)
                .forEach(this::enrollPlayer);
    }

    private void enrollPlayer(Entity entity) {
        this.bossBar.addPlayer((Player) entity);
    }

    public void decrement() {
        remaining -= delta;
        float progress = remaining / maxValue;
        if (progress <= 0 || progress > 1) {
            terminate();
            return;
        }
        bossBar.setProgress(progress);
    }

    public void terminate() {
        bossBar.removeAll();
    }

}
