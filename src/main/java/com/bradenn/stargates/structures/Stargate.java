package com.bradenn.stargates.structures;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.cosmetics.ParticleEffects;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public class Stargate extends Structure implements Port {

    /* Private variables */

    private final StargateModel model;
    private final UUID uuid;
    private final String name;
    private final String address;

    /**
     * Load stargate from database document.
     *
     * @param document The mongodb BSON document.
     */
    @SuppressWarnings("unchecked")
    public Stargate(Document document) {
        super((Document) document.get("structure"));
        this.name = document.getString("name");
        this.model = StargateModel.deserialize((Map<String, Object>) document.get("model"));
        this.address = document.getString("address");
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
    public Stargate(String name, Location base, BoundingBox bounds, Orientation orientation) {
        super(name, base, bounds, orientation);

        this.name = name;
        this.model = StargateModel.MK1;
        this.address = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
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
    public static Stargate fromUUID(UUID uuid) {
        MongoCollection<Document> stargates = Database.getCollection("stargates");
        Document match = stargates.find(new Document("uuid", uuid.toString())).first();
        if (Objects.isNull(match)) return null;
        return new Stargate(match);
    }

    /**
     * Construct a Stargate from its database record.
     *
     * @param address Stargate address
     * @return Stargate object
     */
    public static Stargate fromAddress(String address) {
        MongoCollection<Document> stargates = Database.getCollection("stargates");
        Document match = stargates.find(new Document("address", address)).first();
        if (Objects.isNull(match)) return null;
        return new Stargate(match);
    }

    /**
     * Get all stargate objects.
     */
    public static List<Stargate> getAll() {
        List<Stargate> stargates = new ArrayList<>();
        Database.getCollection("stargates").find().map(Stargate::new).forEach((Consumer<? super Stargate>) stargates::add);
        return stargates;
    }

    /* Getter functions */

    /**
     * Rebuild all of the stargates.
     */
    public static void rebuildAll() {
        Database.getCollection("stargates").find().forEach((Consumer<? super Document>) stargate -> new Stargate(stargate).rebuild());
    }

    /**
     * Destroy all of the stargate structures and remove them from the database.
     */
    public static void terminateAll() {
        Database.getCollection("stargates").find().forEach((Consumer<? super Document>) stargate -> {
            Stargate stargateRef = new Stargate(stargate);
            stargateRef.terminate();
        });
    }

    /**
     * Serialize the stargate object into a database document.
     */
    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("model", model.serialize());
        document.put("address", address);
        document.put("uuid", uuid.toString());
        document.put("structure", super.getDocument());
        return document;
    }

    public String getIdentifier() {
        return "stargates";
    }

    /* Macro Getter functions */

    public UUID getUUID() {
        return uuid;
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
        renderPortal();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public StargateModel getModel() {
        return model;
    }

    /* Bulk Static Class Constructors */

    public void setModel(StargateModel stargateModel) {

        Database.getCollection("stargates").findOneAndUpdate(new Document("uuid", this.getUUID().toString()), Updates.set("model", stargateModel.serialize()));
        Database.getCollection("dialers").findOneAndUpdate(new Document("stargateUUID", this.getUUID().toString()), Updates.set("model", stargateModel.serialize()));

        Dialer dialer = Dialer.fromStargate(getUUID());
        assert dialer != null;
        dialer.rebuild();
        Stargate stargate = Stargate.fromUUID(getUUID());
        assert stargate != null;
        stargate.rebuild();
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

        player.teleport(safeTeleport, PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setVelocity(getOrientation().translate(0, 0, 0.2));
    }

    public void departPlayer(Player player) {

    }

    /**
     * Construct the stargate structure.
     */
    public void build() {
        if (!getLocation().getChunk().isLoaded()) return;
        double yOffset = 3.25;

        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);
        BlockStand ring = new BlockStand(uuid, world);
        ring.setMaterial(Material.DEEPSLATE_TILE_SLAB);
        ring.createRing(centerLocation, 34, new Vector(3.25, 3.25, 0), false, getOrientation());
        ring.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
        ring.createRing(centerLocation.clone().add(getOrientation().translate(0,0,-0.005)), 32, new Vector(3, 3, 0), false, getOrientation());
        if (getModel().equals(StargateModel.MK1)) {
            ring.setMaterial(Material.WAXED_CUT_COPPER_SLAB);
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0,0,0.025)), 8, new Vector(3.125, 3.125, 0), false, getOrientation());
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0,0,-0.025)), 8, new Vector(3.125, 3.125, 0), false, getOrientation());
        } else {
            ring.setMaterial(Material.DARK_PRISMARINE_SLAB);
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0,0,-0.0025)), 32, new Vector(3.125, 3.125, 0), false, getOrientation());
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
