package com.bradenn.stargates.cosmetics;

import com.bradenn.stargates.Main;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.Objects;
import java.util.Random;


public class ParticleEffects {

    public ParticleEffects() {

    }

    public enum ParticleColor {
        ORANGE(255, 159, 10),
        RED(255, 69, 58),
        BLUE(10, 132, 255),
        LIGHT_BLUE(94, 92, 230),
        GREY(200, 200, 200),
        INDIGO(94, 92, 230),
        PURPLE(191, 90, 242),
        AQUA(63, 124, 100),
        DARK_GREY(44, 44, 46);

        Color color;

        ParticleColor(int i, int i1, int i2) {
            color = Color.fromRGB(i, i1, i2);
        }

        public Color getColor() {
            return color;
        }
    }

}
