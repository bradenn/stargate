package com.bradenn.stargates.effects;

import org.bukkit.Location;

public interface ParticleEffect {

    long getFrequency();

    void display(Location location);

}
