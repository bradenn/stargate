package com.bradenn.stargates.structures.stargate;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.cosmetics.ParticleEffects;
import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Port;
import com.bradenn.stargates.structures.Structure;
import com.bradenn.stargates.structures.dialer.DialerMenu;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.apache.commons.lang.RandomStringUtils;
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

public class Stargate extends Structure implements Port {

    private final StargateModel model;
    private final Map<String, Boolean> preferences;
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
        this.preferences = (Map<String, Boolean>) document.get("preferences");
        this.model = StargateModel.deserialize((Map<String, Object>) document.get("model"));
        this.address = document.getString("address");
        this.uuid = UUID.fromString(document.getString("uuid"));
    }

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
        this.preferences = new HashMap<>();
        for (StargatePreferences stargatePreferences : StargatePreferences.values()) {
            preferences.put(stargatePreferences.toString(), stargatePreferences.enabled);
        }
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

    /**
     * Serialize the stargate object into a database document.
     */
    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("preferences", preferences);
        document.put("model", model.serialize());
        document.put("address", address);
        document.put("uuid", uuid.toString());
        document.put("structure", super.getDocument());
        return document;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIdentifier() {
        return "stargates";
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, Boolean> getPreferences() {
        return preferences;
    }

    public boolean checkPreference(StargatePreferences stargatePreferences) {
        return preferences.get(stargatePreferences.toString());
    }

    public void setPreference(String key, boolean value) {
        preferences.put(key, value);
        save();
    }

    public StargateModel getModel() {
        return model;
    }

    public void setModel(StargateModel stargateModel) {

        Database.getCollection("stargates").findOneAndUpdate(new Document("uuid", this.getUUID().toString()), Updates.set("model", stargateModel.serialize()));

        Stargate stargate = Stargate.fromUUID(getUUID());
        assert stargate != null;
        stargate.rebuild();
    }

    // Port

    public BoundingBox getTriggerArea() {
        return getBoundingBox();
    }

    public void openPort() {

    }

    public void closePort() {

    }

    public void idle() {
        renderPortal();
    }

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
     * Draw the particle accretion disk of the stargate.
     */
    public void renderPortal() {
        Location center = getLocation().clone().add(0, 3, 0);
        World world = center.getWorld();
        if (Objects.isNull(world)) return;

        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleEffects.ParticleColor.BLUE.getColor(), 3);
        for (double t = 0; t < 14 * Math.PI - (Math.PI / 2); t += (Math.PI / 16)) {
            double r = t / 16;
            double posX = Math.cos(t) * r;
            double posY = Math.sin(t) * r;
            Vector adjusted = getOrientation().translate(posX, posY, 0);
            world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
        }
//        for (double j = 0; j < 3; j += 0.2) {
//            double particleCount = 4 * j;
//            for (double i = 0; i < particleCount; i += 0.2) {
//                double delta = (Math.PI * 2) / particleCount;
//                double posX = Math.cos(delta * i) * j;
//                double posY = Math.sin(delta * i) * j;
//                Vector adjusted = getOrientation().translate(posX, posY, 0);
//                world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
//            }
//        }
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

    // Structure modifiers

    /**
     * Construct the stargate structure.
     */
    public void build() {
        if (!getLocation().getChunk().isLoaded()) return;
        double yOffset = 3.25;

        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);
        BlockStand ring = new BlockStand(this, world);
        ring.setMaterial(Material.DEEPSLATE_TILE_SLAB);
        ring.createRing(centerLocation, 34, new Vector(3, 3, 0), getOrientation());
        ring.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
        ring.createRing(centerLocation.clone().add(getOrientation().translate(0, 0, -0.005)), 32, new Vector(2.75, 2.75, 0), getOrientation());
        if (getModel().equals(StargateModel.MK1)) {
            ring.setMaterial(Material.WAXED_CUT_COPPER_SLAB);
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0, 0, 0.025)), 8, new Vector(2.85, 2.85, 0), getOrientation());
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0, 0, -0.025)), 8, new Vector(2.85, 2.85, 0), getOrientation());
        } else {
            ring.setMaterial(Material.DARK_PRISMARINE_SLAB);
            ring.createRing(centerLocation.clone().add(getOrientation().translate(0, 0, -0.0025)), 32, new Vector(2.85, 2.85, 0), getOrientation());
        }

    }

    @Override
    public void rebuild() {
        if (getLocation().getChunk().isLoaded()) {
            destroy();
            build();
        }
    }

    /**
     * Find and remove all structure-related objects.
     */
    public void destroy() {

        Vector v = getOrientation().translate(4, 4, 1);
        Collection<Entity> nearbyEntities = getWorld().getNearbyEntities(BoundingBox.of(getLocation().clone().add(0, 2.5, 0), v.getX(), v.getY(), v.getZ()), e -> BlockStand.isArmorStand(e, uuid));

        nearbyEntities.forEach(Entity::remove);
    }

    @Override
    public void onInteract(Player player) {
        if (player.isSneaking()) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR)) {
                setModel(StargateModel.MK2);
            } else {
                StargateMenu stargateMenu = new StargateMenu(this);
                stargateMenu.showMenu(player);
            }
        } else {
            DialerMenu stargateMenu = new DialerMenu(this);
            stargateMenu.showMenu(player);
        }

    }
}
