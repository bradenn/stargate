package com.bradenn.stargates;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.Date;
import java.util.Random;


public class ParticleEffects {

    public ParticleEffects() {

    }

    public static void drawBoundingBox(BoundingBox boundingBox) {
        World world = Bukkit.getWorld("world");
        assert world != null;

        Color color = Color.fromRGB(0, 128, 255);
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        for (double width = boundingBox.getMinX(); width < boundingBox.getMaxX(); width += 1) {
            for (double height = boundingBox.getMinZ(); height < boundingBox.getMaxZ(); height += 1) {
                Location point = new Location(world, width, boundingBox.getMaxY(), height);

                world.spawnParticle(Particle.REDSTONE, point.getBlock().getLocation().add(0.5, 0, 0.5), 1, dustOptions);
            }
        }

    }

    public static void spawnPortal(Location center) {
        World world = Bukkit.getWorld("world");

        Color color = Color.fromRGB(0, 128, 255);
        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleColor.BLUE.getColor(), 2);
        Random random = new Random();
        random.setSeed(new Date().getTime());
        new BukkitRunnable() {
            double j = 0;

            @Override
            public void run() {
                double particles = j * 3 * Math.E;

                for (double i = 0; i < particles; i += 0.5) {
                    double delta = (Math.PI * 2) / particles;
                    double posX = Math.cos(delta * i) * j;
                    double posY = Math.sin(delta * i) * j;

                    world.spawnParticle(Particle.REDSTONE, center.clone().add(posX, posY, 0), 1, blueDust);
                }


                j += 0.1;
                if (j >= 2.75) {
                    cancel();
                }
            }

        }.runTaskTimer(Stargate.plugin, 0, 1);
    }

    public static void idlePortal(Location location) {
        World world = Bukkit.getWorld("world");
        location.setWorld(world);
        Color color = Color.fromRGB(0, 128, 255);
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);

        new BukkitRunnable() {
            double radius = 3;

            @Override
            public void run() {
                for (int i = 0; i < 32; i++) {
                    for (int j = 0; j <= 3; j += 0.5) {
                        double count = radius * j;
                        double slice = (Math.PI * 2) / count;
                        double posX = Math.cos(slice * i) * j;
                        double posY = Math.sin(slice * i) * j;
                        assert world != null;
                        world.spawnParticle(Particle.REDSTONE, location.clone().add(posX, posY + 2.5, 0), 1, dustOptions);
                    }

                }
                radius -= 0.2;
                if (radius <= 0) {
                    cancel();
                }
            }

        }.runTaskTimer(Stargate.plugin, 0, 1);

    }

    public enum ParticleColor {
        ORANGE(255, 159, 10),
        RED(255, 69, 58),
        BLUE(10, 132, 255),
        LIGHT_BLUE(94, 92, 230),
        GREY(142, 142, 147),
        INDIGO(94, 92, 230),
        PURPLE(191, 90, 242),
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
