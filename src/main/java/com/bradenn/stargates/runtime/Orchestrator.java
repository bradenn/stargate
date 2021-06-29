package com.bradenn.stargates.runtime;

import com.bradenn.stargates.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Orchestrator {

    private static List<Ephemeral> ephemeralList;
    private static List<UUID> occupiedUUIDS;


    public Orchestrator() {
        ephemeralList = new ArrayList<>();
        occupiedUUIDS = new ArrayList<>();
        run();
    }

    public static void add(Ephemeral ephemeralObject) throws Exception {
        List<UUID> uuids = new ArrayList<>(List.copyOf(ephemeralObject.getUUIDS()));
        if (uuids.removeAll(occupiedUUIDS)) {
                throw new Exception("Wormhole is busy.");
        }
        occupiedUUIDS.addAll(ephemeralObject.getUUIDS());
        ephemeralList.add(ephemeralObject);
        ephemeralObject.initiate();
    }

    public void run() {
        new BukkitRunnable() {
            public void run() {
                ephemeralList.forEach(ephemeral -> {
                    ephemeral.tick();
                    if (ephemeral.getTicks() % ephemeral.getLifeTicks() == 0) {
                        ephemeral.terminate();
                        occupiedUUIDS.removeAll(ephemeral.getUUIDS());
                        ephemeralList.remove(ephemeral);
                    } else if (ephemeral.getTicks() % ephemeral.getUpdateTicks() == 0) {
                        ephemeral.update();
                    }
                });
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }

}
