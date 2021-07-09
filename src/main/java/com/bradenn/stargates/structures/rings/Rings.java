package com.bradenn.stargates.structures.rings;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.animations.Animation;
import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.cosmetics.DynamicStructure;
import com.bradenn.stargates.cosmetics.ParticleEffects;
import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Port;
import com.bradenn.stargates.structures.Structure;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public class Rings extends Structure implements Port {

    private final UUID uuid;
    private final String name;

    /**
     * Load rings from database document.
     *
     * @param document The mongodb BSON document.
     */
    public Rings(Document document) {
        super((Document) document.get("structure"));
        this.name = document.getString("name");
        this.uuid = UUID.fromString(document.getString("uuid"));
    }

    /**
     * Create a new ring.
     *
     * @param name        Name of the stargate.
     * @param base        The y + 1 location of the stargate.
     * @param orientation The N W S E orientation of the stargate.
     */
    public Rings(String name, Location base, BoundingBox bounds, Orientation orientation) {
        super(name, base, bounds, orientation);

        this.name = name;
        this.uuid = UUID.randomUUID();

        build();
        save();
    }


    /**
     * Get all stargate objects.
     */
    public static List<Rings> getAll() {
        List<Rings> stargates = new ArrayList<>();
        Database.getCollection("rings").find().map(Rings::new).forEach((Consumer<? super Rings>) stargates::add);
        return stargates;
    }

    /* Getter functions */
    public String getIdentifier() {
        return "rings";
    }

    /**
     * Serialize the stargate object into a database document.
     */
    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("uuid", uuid.toString());
        document.put("structure", super.getDocument());
        return document;
    }

    /* Macro Getter functions */

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }


    /* Local functions */

    /**
     * Draw the particle accretion disk of the stargate.
     */
    public void renderPortal() {
        Location center = getLocation().clone().add(0, 3, 0);
        World world = center.getWorld();
        if (Objects.isNull(world)) return;

        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleEffects.ParticleColor.BLUE.getColor(), 2);
        for (double j = 0; j < 3; j += 0.2) {
            double particleCount = 4 * j;
            for (double i = 0; i < particleCount; i += 0.2) {
                double delta = (Math.PI * 2) / particleCount;
                double posX = Math.cos(delta * i) * j;
                double posY = Math.sin(delta * i) * j;
                Vector adjusted = getOrientation().translate(posX, posY, 0);
                world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
            }
        }
    }

    /* Structure functions */

    /**
     * Destroy the stargate structure and remove it from the database.
     */
    public void summonPlayer(Player player) {
        Location safeTeleport = getLocation().clone().add(0, 1, 0);
        safeTeleport.getChunk().load();
        safeTeleport.setYaw(getOrientation().playerYaw());
        showFlash();
        player.teleport(safeTeleport, PlayerTeleportEvent.TeleportCause.PLUGIN);
        unlock();
    }

    @Override
    public void departPlayer(Player player) {
        Animation animation = new Animation(120, 1);
        animation.setKeyframe(10, e -> showRing());
        animation.setKeyframe(48, e -> showFlash());
        animation.setKeyframe(120, e -> unlock());
        animation.run(e -> {
        });
    }

    public BoundingBox getTriggerArea() {
        return getBoundingBox();
    }

    @Override
    public void openPort() {

    }

    @Override
    public void closePort() {

    }

    @Override
    public void idle() {

    }

    public void showFlash() {
        Location center = getLocation().clone().add(0, 0.1, 0);
        World world = center.getWorld();
        if (Objects.isNull(world)) return;

        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleEffects.ParticleColor.BLUE.getColor(), 1);
        for (double i = 0; i < 3; i += 0.2) {
            for (double j = 0; j < 128; j += 1) {
                double delta = (Math.PI * 2) / 100;
                double posX = Math.cos(delta * j) * 2.1875;
                double posY = Math.sin(delta * j) * 2.1875;
                Vector adjusted = getOrientation().translate(posX, i, posY);
                world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
            }
        }
    }

    /**
     * Construct the stargate structure.
     */
    public void build() {
        if (!getLocation().getChunk().isLoaded()) return;
        double yOffset = -0.75 - 0.125;

        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);

        BlockStand innerRing = new BlockStand(this, world);

        Location innerLocation = centerLocation.getBlock().getLocation().add(0.5, 2 + 0.3125 / 2, 0.5);
        innerRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
        innerRing.createRing(innerLocation, 16, new Vector(2.1875, 2.1875, 0), false);

        innerRing.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
        innerRing.createRing(innerLocation, 16, new Vector(2.1875 - 0.015, 2.1875 - 0.015, 0), true);


    }

    public void showRing() {
        double yOffset = -0.75 - 0.125;

        Location centerLocation = getLocation().clone().add(0, yOffset, 0);
        Location innerLocation = centerLocation.getBlock().getLocation().add(0.5, 2 + 0.3125 / 2, 0.5);

        Animation animation = new Animation(80, 1);
        animation.setKeyframe(1, e -> animateRing(innerLocation, 80, (double) 1 / 8));
        animation.setKeyframe(11, e -> animateRing(innerLocation, 60, (double) 1 / 8));
        animation.setKeyframe(21, e -> animateRing(innerLocation, 40, (double) 1 / 8));
        animation.setKeyframe(31, e -> animateRing(innerLocation, 20, (double) 1 / 8));
        animation.run(e -> {
        });
    }

    private void animateRing(Location center, int duration, double modifier) {
        DynamicStructure dynamicStructure = new DynamicStructure(center.getWorld(), Material.DARK_PRISMARINE_SLAB);

        Animation animation = new Animation(duration, 1);
        dynamicStructure.generateRing(center, 16, new Vector(2.1875, 2.1875, 0), false);
        dynamicStructure.generateRing(center, 16, new Vector(2.1875 - 0.015, 2.1875 - 0.015, 0), true);
        animation.setKeyframe(duration, frame -> dynamicStructure.removeAll());
        animation.run(i -> {
            double unit = ((Math.PI * 2) / duration);
            dynamicStructure.translateAll(new Vector(0, Math.pow(Math.sin(unit * i), 3) * modifier, 0));
        });
    }

    /**
     * Find and remove all structure-related objects.
     */
    public void destroy() {
        if (!getLocation().getChunk().isLoaded()) return;
        Vector v = getOrientation().translate(4, 1, 4);
        Collection<Entity> nearbyEntities = getWorld().getNearbyEntities(BoundingBox.of(getLocation().clone().add(0, 0.5, 0), v.getX(), v.getY(), v.getZ()).expand(0, 4, 0), e -> BlockStand.isArmorStand(e, uuid));
        nearbyEntities.forEach(Entity::remove);
    }



}