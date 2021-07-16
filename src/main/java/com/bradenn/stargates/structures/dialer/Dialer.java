package com.bradenn.stargates.structures.dialer;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.cosmetics.BlockStand;
import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Structure;
import com.bradenn.stargates.structures.stargate.Stargate;
import com.bradenn.stargates.structures.stargate.StargateModel;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.function.Consumer;

public class Dialer extends Structure {

    private final StargateModel model;
    private final String name;
    private final UUID uuid;
    private UUID stargateUUID;

    /**
     * Create a new dialer.
     *
     * @param base        The y + 1 location of the dialer.
     * @param name        Name of the dialer.
     * @param orientation The N W S E orientation of the dialer.
     */
    public Dialer(String name, Location base, BoundingBox bounds, Orientation orientation) {
        super(name, base, bounds, orientation);
        this.name = name;
        this.model = StargateModel.MK1;
        this.uuid = UUID.randomUUID();
        this.stargateUUID = UUID.randomUUID();
        build();
        save();
    }

    /**
     * Load dialer from database document.
     *
     * @param document The mongodb BSON document.
     */
    @SuppressWarnings("unchecked")
    public Dialer(Document document) {
        super((Document) document.get("structure"));
        this.name = document.getString("name");
        this.model = StargateModel.deserialize((Map<String, Object>) document.get("model"));
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.stargateUUID = UUID.fromString(document.getString("stargateUUID"));
    }

    /**
     * Get all dialer objects.
     */
    public static List<Dialer> getAll() {
        List<Dialer> dialers = new ArrayList<>();
        Database.getCollection("dialers").find().map(Dialer::new).forEach((Consumer<? super Dialer>) dialers::add);
        return dialers;
    }

    /**
     * Get a dialer object from its UUID.
     */
    public static Dialer fromUUID(UUID uuid) {
        MongoCollection<Document> dialers = Database.getCollection("dialers");
        Document match = dialers.find(new Document("uuid", uuid.toString())).first();
        if (Objects.isNull(match)) return null;
        return new Dialer(match);
    }

    /**
     * Get a dialer object from its UUID.
     */
    public static Dialer fromStargate(UUID uuid) {
        MongoCollection<Document> dialers = Database.getCollection("dialers");
        Document match = dialers.find(new Document("stargateUUID", uuid.toString())).first();
        if (Objects.isNull(match)) return null;
        return new Dialer(match);
    }

    public String getIdentifier() {
        return "dialers";
    }

    public UUID getUUID() {
        return uuid;
    }

    /**
     * Get a bson document of this serialized class.
     *
     * @return MongoDB bson document.
     */
    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("model", model.serialize());
        document.put("uuid", uuid.toString());
        document.put("stargateUUID", stargateUUID.toString());
        document.put("structure", super.getDocument());
        return document;
    }

    /**
     * Get the stargate object assigned to this dialer.
     *
     * @return Stargate object reference
     */
    public Stargate getStargate() {
        return Stargate.fromUUID(this.stargateUUID);
    }

    /**
     * Set the stargate.
     */
    public void assignStargate(UUID uuid) {
        stargateUUID = uuid;
    }

    /**
     * Build the correct dialer.
     */
    public void build() {
        if (model.isMk2()) {
            buildMk2();
        } else {
            buildMk1();
        }
    }

    /**
     * Build the MK1 dialer.
     */
    public void buildMk1() {
        double yOffset = 1.625;
        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);

        double outer = 8;
        BlockStand baseRing = new BlockStand(this, world);

        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double baseDelta = unitCircle / outer;
            double baseAngle = baseDelta * i + baseDelta * (outer / 4);

            double baseX = Math.cos(baseDelta * i) * 0.45;
            double baseY = Math.sin(baseDelta * i) * 0.45;


            double y = i % 4 == 0 ? 0.002 : (i % 2 == 0 ? 0.003 : -0.005);
            Location outerLocation = centerLocation.clone().add(baseX, y, baseY);
            baseRing.setRotation((float) Math.toDegrees(baseAngle), 0);
            baseRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            baseRing.smallBlockAt(outerLocation, new EulerAngle(0, 0, 0));

        }

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.smallBlockAt(centerLocation.clone().add(0, -0.25, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.smallBlockAt(centerLocation.clone().add(0, -1 + 0.125, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.LIGHTNING_ROD);
        baseRing.smallBlockAt(centerLocation.clone().add(0.25, 0.5, 0.25), new EulerAngle(Math.PI, 0, 0));
    }

    /**
     * Build the MK2 dialer.
     */
    public void buildMk2() {
        double yOffset = 1.625;
        World world = getWorld();
        Location centerLocation = getLocation().clone().add(0, yOffset, 0);

        double outer = 8;
        BlockStand baseRing = new BlockStand(this, world);


        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double baseDelta = unitCircle / outer;
            double baseAngle = baseDelta * i + baseDelta * (outer / 4);

            double baseX = Math.cos(baseDelta * i) * 0.45;
            double baseY = Math.sin(baseDelta * i) * 0.45;


            double y = i % 4 == 0 ? 0.002 : (i % 2 == 0 ? 0.003 : -0.005);
            Location outerLocation = centerLocation.clone().add(getOrientation().translate(baseX, y, baseY));
            baseRing.setRotation((float) Math.toDegrees(baseAngle), 0);
            baseRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            baseRing.smallBlockAt(outerLocation, new EulerAngle(0, 0, 0));

        }

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.smallBlockAt(centerLocation.clone().add(getOrientation().translate(0, -0.25, 0)), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.smallBlockAt(centerLocation.clone().add(0, -1 + 0.125, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.DARK_PRISMARINE_SLAB);
        baseRing.smallBlockAt(centerLocation.clone().add(0.125 / 2, 0.25 - 0.13, -0.125 / 2), new EulerAngle(Math.PI / 2, 0, Math.PI / 4));
    }

    /**
     * Destroy the correct dialer.
     */
    public void destroy() {
        Collection<Entity> nearbyEntities = getWorld().getNearbyEntities(getBoundingBox().clone().expand(3, 3, 3), e -> BlockStand.isArmorStand(e, uuid));
        nearbyEntities.forEach(Entity::remove);
    }

    @Override
    public void onInteract(Player player) {
        DialerMenu dialerMenu = new DialerMenu(getStargate());
        dialerMenu.showMenu(player);
    }
}
