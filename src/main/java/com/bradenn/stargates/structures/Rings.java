package com.bradenn.stargates.structures;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.cosmetics.ParticleEffects;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public class Rings extends Structure {

    /* Private variables */

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

    /* Initialization functions */

    /**
     * Create a new stargate.
     *
     * @param name        Name of the stargate.
     * @param base        The y + 1 location of the stargate.
     * @param orientation The N W S E orientation of the stargate.
     */
    public Rings(String name, Location base, BoundingBox bounds, Orientation orientation) {
        super(base, bounds, orientation);

        this.name = name;
        this.uuid = UUID.randomUUID();

        build();
        save();
    }

    /**
     * Construct a Stargate from its database record.
     *
     * @param uuid Stargate uuid
     * @return Stargate object
     */
    public static Rings fromUUID(UUID uuid) {
        MongoCollection<Document> stargates = Database.getCollection(getIdentifier());
        Document match = stargates.find(new Document("uuid", uuid.toString())).first();
        if (Objects.isNull(match)) return null;
        return new Rings(match);
    }

    /**
     * Get all stargate objects.
     */
    public static List<Rings> getAll() {
        List<Rings> stargates = new ArrayList<>();
        Database.getCollection(getIdentifier()).find().map(Rings::new).forEach((Consumer<? super Rings>) stargates::add);
        return stargates;
    }

    /* Getter functions */

    /**
     * Rebuild all of the stargates.
     */
    public static void rebuildAll() {
        Database.getCollection(getIdentifier()).find().forEach((Consumer<? super Document>) stargate -> new Rings(stargate).rebuild());
    }

    /**
     * Destroy all of the stargate structures and remove them from the database.
     */
    public static void terminateAll() {
        Database.getCollection(getIdentifier()).find().forEach((Consumer<? super Document>) stargate -> {
            Rings stargateRef = new Rings(stargate);
            stargateRef.terminate();
        });
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

    public static String getIdentifier() {
        return "rings";
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

    /**
     * Draw the particle accretion disk of the stargate.
     */
    public void renderOpeningPortal() {
//        Location center = getLocation().clone().add(0, 3, 0);
//        World world = center.getWorld();
//        if (Objects.isNull(world)) return;
//        double itr = 0.8;
//        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleEffects.ParticleColor.PURPLE.getColor(), 2);
//        for (double j = 0; j < 3; j += 0.2) {
//            double particleCount = 4 * j;
//            for (double i = 0; i < particleCount; i += 0.2) {
//                double delta = (Math.PI * 2) / particleCount;
//                double posX = Math.cos(delta * i) * j;
//                double posY = Math.sin(delta * i) * j;
//                double posZ = -Math.abs(itr * Math.pow(j, 2)) + itr * 9;
//                Vector adjusted = getOrientation().translate(posX, posY, posZ);
//                world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
//            }
//        }
    }

    /* Structure functions */

    /**
     * Destroy the stargate structure and remove it from the database.
     */
    public void summonPlayer(Player player) {
        Location safeTeleport = getLocation().clone().add(0, 1, 0);
        safeTeleport.getChunk().load();
        safeTeleport.setYaw(getOrientation().playerYaw());

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.CONFUSION, 100, 10, true);
        player.addPotionEffect(potionEffect);
        player.teleport(safeTeleport, PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setVelocity(getOrientation().translate(0, 0, 0.2));
    }

    /**
     * Construct the stargate structure.
     */
    public void build() {
        if (!getLocation().getChunk().isLoaded()) return;
        double yOffset = 1.8125;

        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);
        int outer = 34;
        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double outerDelta = unitCircle / 34;
            double outerAngle = outerDelta * i + outerDelta * 8.5;

            double outerX = Math.cos(outerDelta * i) * 3.25;
            double outerY = Math.sin(outerDelta * i) * 3.25;

            BlockStand outerRing = new BlockStand(uuid, world);

            Location outerLocation = centerLocation.clone().add(getOrientation().rotate(outerX, outerY, (i % 2 == 0 ? 0.005 : -0.005)));
            outerRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            Vector outerRotation = getOrientation().rotate(0, 0, -outerAngle);
            outerRing.largeBlockAt(outerLocation, new EulerAngle(outerRotation.getX(), outerRotation.getY(), outerRotation.getZ()));


            if (i > 32) continue;
            double innerDelta = unitCircle / 32;
            double innerAngle = innerDelta * i + innerDelta * 8;

            double innerX = Math.cos(innerDelta * i) * 3;
            double innerY = Math.sin(innerDelta * i) * 3;

            BlockStand innerRing = new BlockStand(uuid, world);
            Location innerLocation = centerLocation.clone().add(getOrientation().rotate(innerX, innerY, (i % 2 == 0 ? 0.002 : -0.002)));
            innerRing.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
            Vector innerRotation = getOrientation().rotate(0, 0, -innerAngle);
            innerRing.largeBlockAt(innerLocation, new EulerAngle(innerRotation.getX(), innerRotation.getY(), innerRotation.getZ()));

        }
    }

    /**
     * Find and remove all structure-related objects.
     */
    public void destroy() {
        if (!getLocation().getChunk().isLoaded()) return;
        Vector v = getOrientation().translate(4, 4, 1);
        Collection<Entity> nearbyEntities = getWorld().getNearbyEntities(BoundingBox.of(getLocation().clone().add(0, 2.5, 0), v.getX(), v.getY(), v.getZ()), e -> BlockStand.isArmorStand(e, uuid));
        Bukkit.broadcastMessage(nearbyEntities.size() + " stargate armor stands removed... !");
        nearbyEntities.forEach(Entity::remove);
    }

    /**
     * Destroy the stargate structure and remove it from the database.
     */
    public void terminate() {
        this.destroy();
        Database.getCollection("stargates").findOneAndDelete(new Document("uuid", getUUID().toString()));
        Document dialer = Database.getCollection("dialers").findOneAndDelete(new Document("stargateUUID", getUUID().toString()));
        assert dialer != null;
        new Dialer(dialer).destroy();
    }


}
