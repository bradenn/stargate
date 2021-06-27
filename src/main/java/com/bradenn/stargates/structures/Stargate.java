package com.bradenn.stargates.structures;

import com.bradenn.stargates.BlockStand;
import com.bradenn.stargates.Database;
import com.bradenn.stargates.ParticleEffects;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
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

public class Stargate implements Buildable {

    private final UUID uuid;
    private final String name;
    private final String address;
    private final Structure structure;

    /**
     * Create a new stargate.
     *
     * @param name         Name of the stargate.
     * @param baseLocation The y + 1 location of the stargate.
     * @param orientation  The N W S E orientation of the stargate.
     */
    public Stargate(String name, Location baseLocation, Orientation orientation) {
        this.name = name;
        this.structure = generateStructure(baseLocation, orientation);
        this.uuid = UUID.randomUUID();
        this.address = RandomStringUtils.randomAlphanumeric(4);
        build();
        Database.getCollection("stargates").insertOne(getDocument());
        new Dialer(name, this.uuid, baseLocation, orientation);
    }

    /**
     * Load stargate from database document.
     *
     * @param document The mongodb BSON document.
     */
    public Stargate(Document document) {
        this.name = document.getString("name");
        this.address = document.getString("address");
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.structure = new Structure((Document) document.get("structure"));
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Structure getStructure() {
        return structure;
    }

    public Location getLocation() {
        return structure.getLocation();
    }

    public BoundingBox getBoundingBox() {
        return structure.getBoundingBox();
    }

    public void teleportPlayer(Player player) {
        Location safeTeleport = getLocation().clone().add(0,0.5,0);
        safeTeleport.getChunk().load();
        safeTeleport.setYaw(getStructure().getOrientation().playerYaw());

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.CONFUSION, 100, 10, true);
        player.addPotionEffect(potionEffect);
        player.teleport(safeTeleport, PlayerTeleportEvent.TeleportCause.PLUGIN);

    }

    public static List<Stargate> getAll() {
        List<Stargate> stargates = new ArrayList<>();
        Database.getCollection("stargates").find().map(Stargate::new).forEach((Consumer<? super Stargate>) stargates::add);
        return stargates;
    }

    public static void rebuildAll() {
        Database.getCollection("stargates").find().forEach((Consumer<? super Document>) stargate -> new Stargate(stargate).rebuild());
    }

    public static void destroyAll() {
        Database.getCollection("stargates").find().forEach((Consumer<? super Document>) stargate -> {
            Stargate stargateRef = new Stargate(stargate);
            stargateRef.terminate();
        });
    }

    public void terminate(){
        this.destroy();
        Database.getCollection("stargates").findOneAndDelete(new Document("uuid", getUUID().toString()));

        Document dialer = Database.getCollection("dialers").findOneAndDelete(new Document("stargateUUID", getUUID().toString()));
        assert dialer != null;
        new Dialer(dialer).destroy();
    }

    /**
     * @param baseLocation The base location of the structure.
     * @param orientation  The orientation of the stargate.
     * @return A stargate structure reference.
     */
    private Structure generateStructure(Location baseLocation, Orientation orientation) {
        Block center = baseLocation.getBlock();
        Vector adjusted = orientation.translate(1.75, 1.75, -0.33);
        BoundingBox bounds = BoundingBox.of(center.getLocation().clone().add(0.5,2.5,0.5), adjusted.getX(), adjusted.getY(), adjusted.getZ());

        return new Structure(baseLocation.getBlock().getLocation().clone().add(0.5,-0.5,0.5), bounds, orientation);
    }

    public void drawIdle() {
        Location center = getStructure().getLocation().clone().add(0, 3, 0);
        World world = center.getWorld();
        if(Objects.isNull(world)) return;

        Particle.DustOptions blueDust = new Particle.DustOptions(ParticleEffects.ParticleColor.BLUE.getColor(), 2);
        for (double j = 0; j < 3; j += 0.2) {
            double particleCount = 4 * j;
            for (double i = 0; i < particleCount; i += 0.2) {
                double delta = (Math.PI * 2) / particleCount;
                double posX = Math.cos(delta * i) * j;
                double posY = Math.sin(delta * i) * j;
                Vector adjusted = structure.getOrientation().translate(posX, posY, 0);
                world.spawnParticle(Particle.REDSTONE, center.clone().add(adjusted.getX(), adjusted.getY(), adjusted.getZ()), 1, blueDust);
            }
        }
    }

    public Document getDocument() {
        Document document = new Document();
        document.put("name", name);
        document.put("address", address);
        document.put("uuid", uuid.toString());
        document.put("structure", structure.getDocument());
        return document;
    }

    public static Stargate fromUUID(UUID uuid) {
        MongoCollection<Document> stargates = Database.getCollection("stargates");
        Document match = stargates.find(new Document("uuid", uuid.toString())).first();
        if(Objects.isNull(match)) return null;
        return new Stargate(match);
    }

    public static Stargate fromAddress(String address) {
        MongoCollection<Document> stargates = Database.getCollection("stargates");
        Document match = stargates.find(new Document("address", address)).first();
        if(Objects.isNull(match)) return null;
        return new Stargate(match);
    }

    @Override
    public void rebuild() {
        destroy();
        build();
    }

    @Override
    public void build() {
        double yOffset = 1.75 + 0.0625;

        World world = structure.getWorld();
        Location centerLocation = structure.getLocation().clone().add(0, yOffset, 0);
        int outer = 34;
        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double outerDelta = unitCircle / 34;
            double outerAngle = outerDelta * i + outerDelta * 8.5;

            double outerX = Math.cos(outerDelta * i) * 3.25;
            double outerY = Math.sin(outerDelta * i) * 3.25;

            BlockStand outerRing = new BlockStand(uuid, world);

            Location outerLocation = centerLocation.clone().add(structure.getOrientation().rotate(outerX, outerY, (i % 2 == 0 ? 0.005 : -0.005)));
            outerRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            Vector outerRotation = structure.getOrientation().rotate(0, 0, -outerAngle);
            outerRing.largeBlockAt(outerLocation, new EulerAngle(outerRotation.getX(), outerRotation.getY(), outerRotation.getZ()));


            if (i > 32) continue;
            double innerDelta = unitCircle / 32;
            double innerAngle = innerDelta * i + innerDelta * 8;

            double innerX = Math.cos(innerDelta * i) * 3;
            double innerY = Math.sin(innerDelta * i) * 3;

            BlockStand innerRing = new BlockStand(uuid, world);
            Location innerLocation = centerLocation.clone().add(structure.getOrientation().rotate(innerX, innerY, (i % 2 == 0 ? 0.002 : -0.002)));
            innerRing.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
            Vector innerRotation = structure.getOrientation().rotate(0, 0, -innerAngle);
            innerRing.largeBlockAt(innerLocation, new EulerAngle(innerRotation.getX(), innerRotation.getY(), innerRotation.getZ()));


            if (i > 16) continue;
            double chevronDelta = unitCircle / 8;

            double chevronAngle = chevronDelta * i + chevronDelta * 2;

            double chevronX = Math.cos(chevronDelta * i) * 3.125;
            double chevronY = Math.sin(chevronDelta * i) * 3.125;

            BlockStand chevronRing = new BlockStand(uuid, world);
            Location chevronLocationA = centerLocation.clone().add(structure.getOrientation().rotate(chevronX, chevronY, (i % 2 == 0 ? 0.025 : -0.025)));
            Location chevronLocationB = centerLocation.clone().add(structure.getOrientation().rotate(chevronX, chevronY, (i % 2 == 0 ? -0.025 : 0.025)));
            chevronRing.setMaterial(Material.WAXED_CUT_COPPER_SLAB);
            Vector chevronRotation = structure.getOrientation().rotateSpin(0, 0, -chevronAngle);
            chevronRing.largeBlockAt(chevronLocationA, new EulerAngle(chevronRotation.getX(), chevronRotation.getY(), chevronRotation.getZ()));
            chevronRing.largeBlockAt(chevronLocationB, new EulerAngle(chevronRotation.getX(), chevronRotation.getY(), chevronRotation.getZ()));
        }
    }

    @Override
    public void destroy() {
        Collection<Entity> nearbyEntities = structure.getWorld().getNearbyEntities(structure.getLocation().clone().add(0, 4.25, 0), 4, 4, 4, e -> BlockStand.isArmorStand(e, uuid));
        nearbyEntities.forEach(Entity::remove);
    }
}
