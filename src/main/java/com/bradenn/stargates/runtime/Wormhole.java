package com.bradenn.stargates.runtime;

import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.cosmetics.ProgressBar;
import com.bradenn.stargates.structures.Port;
import org.bukkit.entity.Player;

public class Wormhole {

    private final Port to, from;
    private final long expiration;
    private long ticks;
    private ProgressBar progressBar;

    public Wormhole(Port from, Port to, long expiration) throws Exception {
        if (from.isLocked()) throw new Exception("This port is already in use.");
        if (to.isLocked()) throw new Exception("Destination port is in use.");
        this.from = from;
        this.to = to;
        this.expiration = expiration;
        this.progressBar = null;
        this.ticks = 0;

        establishConnection();
    }

    private void severConnection() {

        from.closePort();
        to.closePort();

        from.unlock();
        to.unlock();

        progressBar.terminate();
    }

    private void establishConnection() {


        from.lock();
        from.openPort();

        to.lock();
        to.openPort();

        String progressBarTitle = String.format("%s%s %s→ %s%s", Messages.mono4, from.getName(), Messages.mono2, Messages.mono4, to.getName());
        this.progressBar = new ProgressBar(progressBarTitle, (float)expiration/20, 0.05f);
        this.progressBar.addNearbyPlayers(to.getLocation());
        this.progressBar.addNearbyPlayers(from.getLocation());
    }

    public boolean tick() {
        ticks+=1;
        if (ticks <= expiration) {
            progressBar.decrement();
            to.idle();
            from.idle();
        } else {
            severConnection();
            return false;
        }
        return true;
    }

    public void acceptPlayer(Player player) {
        if (player.getBoundingBox().overlaps(from.getTriggerArea())) {
            from.departPlayer(player);
            to.summonPlayer(player);
        }
    }


}
