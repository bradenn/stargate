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

    public static void drawBoundingBox(BoundingBox boundingBox) {
        World world = Bukkit.getWorld("world");
        assert world != null;


        Color color = Color.fromRGB(0, 128, 255);
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        new BukkitRunnable(){
            @Override
            public void run() {

                for(double x = boundingBox.getMinX(); x <= boundingBox.getMaxX(); x+=0.5){
                    for(double y = boundingBox.getMinY(); y <= boundingBox.getMaxY(); y+=0.5){
                        for(double z = boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z+=0.5){
                            Location l = new Location(world, x, y, z);
                            world.spawnParticle(Particle.REDSTONE, l, 1, dustOptions);
                        }
                    }
                }

            }
        }.runTaskTimer(Main.plugin, 2, 2);


//        world.spawnParticle(Particle.REDSTONE, boundingBox.getCenter(), 1, dustOptions);

    }

    public static void drawAccretionDisk(Location center) {
        World world = center.getWorld();
        if(Objects.isNull(world)) return;
        Random random = new Random();

        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleColor.BLUE.getColor(), 2);
        for (double j = 0; j < 3; j += 0.2) {
            double particleCount = 4 * j;
            for (double i = 0; i < particleCount; i += 0.2) {
                double delta = (Math.PI * 2) / particleCount;
                double posX = Math.cos(delta * i) * j;
                double posY = Math.sin(delta * i) * j;
                world.spawnParticle(Particle.REDSTONE, center.clone().add(posX, posY, 0), 1, blueDust);
            }
        }
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

        }.runTaskTimer(Main.plugin, 0, 1);

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
