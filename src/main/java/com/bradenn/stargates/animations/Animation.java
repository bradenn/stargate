package com.bradenn.stargates.animations;

import com.bradenn.stargates.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Animation {


    private final long duration;
    private final long frequency;

    private int frames;

    private Map<Integer, Consumer<Integer>> keyframes;

    /**
     * @param duration  the total length of the animation, in ticks. (20 ticks per second)
     * @param frequency the number of ticks between keyframes
     */
    public Animation(long duration, long frequency) {
        this.keyframes = new HashMap<>();
        this.duration = duration;
        this.frequency = frequency;
        this.frames = 0;
    }

    public void setKeyframe(int frame, Consumer<Integer> consumer) {
        keyframes.put(frame, consumer);
    }

    public void run(Consumer<Integer> consumer) {
        new BukkitRunnable(){
            @Override
            public void run() {
                frames++;
                if(frames*frequency >= duration){
                    cancel();
                }
                keyframes.getOrDefault(frames, consumer).accept(frames);

            }
        }.runTaskTimer(Main.plugin, 0, frequency);
    }

    private void dump(int id) {

    }


}
