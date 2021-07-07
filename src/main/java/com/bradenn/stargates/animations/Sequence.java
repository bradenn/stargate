package com.bradenn.stargates.animations;

import com.bradenn.stargates.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sequence {

    private final long duration;
    private final long frequency;
    private long ticks;

    private final Map<Long, Consumer<Long>[]> taskMap;

    public Sequence(long duration, long frequency) {
        this.duration = duration;
        this.frequency = frequency;
        taskMap = new HashMap<>();
    }

    public void run() {
        new BukkitRunnable() {
            public void run() {
                ticks++;

                for (Consumer<Long> consumer : taskMap.getOrDefault(ticks, null)) {
                    consumer.accept(ticks);
                }

                if (ticks >= duration) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, frequency);
    }

    public void add(long delay, Consumer<Long>... consumers) {
        taskMap.put(delay, consumers);
    }

}
