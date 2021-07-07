package com.bradenn.stargates.runtime;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.cosmetics.ProgressBar;
import com.bradenn.stargates.structures.stargate.Stargate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaticWormhole {

    private final Stargate source, target;
    private ProgressBar progressBar;
    private final List<Player> playerList;
    private float ticks;

    public StaticWormhole(Stargate source, Stargate target) {
        this.source = source;
        this.target = target;
        this.ticks = 0;
        this.playerList = new ArrayList<>();
    }

    private void addNearbyPlayers(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.getNearbyEntities(location, 16, 16, 16, this::isPlayer)
                .forEach(this::enrollPlayer);
    }

    private boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    private void enrollPlayer(Entity entity) {
        playerList.add((Player) entity);
    }

    public void eventHorizonPredicate(Player player) {
        if (player.getBoundingBox().overlaps(source.getBoundingBox())) {
            target.summonPlayer(player);
        }
    }

    public float getTicks() {
        return ticks;
    }

    public void tick() {
        ticks++;
    }

    public float getLifeTicks() {
        return 12 * 20; /* 12 seconds at 20 ticks per second. */
    }

    public float getUpdateTicks() {
        return 4; /* 1/5 of a second at 20 ticks per second. */
    }

    public List<UUID> getUUIDS() {
        return List.of(source.getUUID(), target.getUUID());
    }

    public void initiate() {
        addNearbyPlayers(source.getLocation());
        String progressBarTitle = String.format("%s%s %s→ %s%s", Messages.mono4, source.getName(), Messages.mono2, Messages.mono4, target.getName());
        progressBar = new ProgressBar(progressBarTitle, 10, 0.2f);
        source.renderOpeningPortal();
    }

    public void update() {
        progressBar.decrement();
        source.renderPortal();
        target.renderPortal();
        playerList.forEach(this::eventHorizonPredicate);
    }

    public void terminate() {
        progressBar.terminate();
    }

}
