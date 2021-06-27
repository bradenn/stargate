package com.bradenn.stargates.structures;

import com.bradenn.stargates.BlockStand;
import com.bradenn.stargates.Database;
import com.bradenn.stargates.ParticleEffects;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.print.Doc;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class Dialer implements Buildable {

    private final String name;
    private final UUID uuid;
    private final UUID stargateUUID;
    private final Structure structure;

    /**
     * Create a new dialer.
     *
     * @param name         Name of the dialer.
     * @param baseLocation The y + 1 location of the dialer.
     * @param orientation  The N W S E orientation of the dialer.
     */
    public Dialer(String name, UUID stargateUUID, Location baseLocation, Orientation orientation) {
        this.name = name;
        Vector translate = orientation.translate(3,0,2);
        Location adjusted = baseLocation.getBlock().getLocation().add(translate.getX() + 0.5, -0.5, translate.getZ() + 0.5);
        this.structure = generateStructure(adjusted, orientation);
        this.uuid = UUID.randomUUID();
        this.stargateUUID = stargateUUID;
        build();
        Database.getCollection("dialers").insertOne(getDocument());
    }

    /**
     * Load dialer from database document.
     *
     * @param document The mongodb BSON document.
     */
    public Dialer(Document document) {
        this.name = document.getString("name");
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.stargateUUID = UUID.fromString(document.getString("stargateUUID"));
        this.structure = new Structure((Document) document.get("structure"));
    }

    public static void rebuildAll() {
        Database.getCollection("dialers").find().forEach((Consumer<? super Document>) dialer -> new Dialer(dialer).rebuild());
    }

    /**
     * @param baseLocation The base location of the structure.
     * @param orientation  The orientation of the dialer.
     * @return A dialer structure reference.
     */
    private Structure generateStructure(Location baseLocation, Orientation orientation) {
        Block center = baseLocation.getBlock();

        BoundingBox bounds = center.getBoundingBox().expand(1, 1, 1);

        return new Structure(baseLocation, bounds, orientation);
    }

    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("uuid", uuid.toString());
        document.put("stargateUUID", stargateUUID.toString());
        document.put("structure", structure.getDocument());
        return document;
    }

    public static Dialer fromUUID(UUID uuid) {
        MongoCollection<Document> dialers = Database.getCollection("dialers");
        Document match = dialers.find(new Document("uuid", uuid.toString())).first();
        if(Objects.isNull(match)) return null;
        return new Dialer(match);
    }

    public Stargate getStargate() {
        return Stargate.fromUUID(this.stargateUUID);
    }

    @Override
    public void rebuild() {
        destroy();
        build();
    }

    @Override
    public void build() {

        double yOffset = 0;
        World world = structure.getWorld();
        Location centerLocation = structure.getLocation().clone().add(0, yOffset, 0);

        double outer = 8;
        BlockStand baseRing = new BlockStand(uuid, world);

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
            baseRing.largeBlockAt(outerLocation, new EulerAngle(0, 0, 0));

        }

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.largeBlockAt(centerLocation.clone().add(0, -0.25, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.largeBlockAt(centerLocation.clone().add(0, -1 + 0.125, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.LIGHTNING_ROD);
        baseRing.largeBlockAt(centerLocation.clone().add(0.25, 0.5, 0.25), new EulerAngle(Math.PI, 0, 0));
    }

    @Override
    public void destroy() {
        Collection<Entity> nearbyEntities = structure.getWorld().getNearbyEntities(structure.getLocation().clone().add(0, 1, 0), 1,1,1, e -> BlockStand.isArmorStand(e, uuid));
        nearbyEntities.forEach(Entity::remove);
    }
}
