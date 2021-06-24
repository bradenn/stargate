package com.bradenn.stargates.structures;

import com.bradenn.stargates.BlockStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DialerStructure implements PersistentStructure {

    private final UUID uuid;
    private final World world;
    private final Location location;
    private final BoundingBox boundingBox;
    private final StructureOrientation structureOrientation;

    public DialerStructure(Location location) {
        this.location = Objects.requireNonNull(location.getWorld()).getHighestBlockAt(location).getLocation().add(0.5, 0.375, 0.5);
        this.world = location.getWorld();
        this.boundingBox = new BoundingBox();
        this.structureOrientation = StructureOrientation.NORTH;
        this.uuid = UUID.randomUUID();
        build();
    }

    public DialerStructure(Location location, StructureOrientation structureOrientation, BoundingBox boundingBox, UUID uuid) {
        this.location = location;
        this.structureOrientation = structureOrientation;
        this.boundingBox = boundingBox;
        this.uuid = uuid;
        this.world = location.getWorld();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static DialerStructure deserialize(@NotNull Map<?, ?> args) {
        Map<String, Object> object = (Map<String, Object>) args;
        UUID uuid = UUID.fromString(object.get("uuid").toString());
        Location location = Location.deserialize((Map<String, Object>) object.get("location"));
        BoundingBox boundingBox = BoundingBox.deserialize((Map<String, Object>) object.get("boundingBox"));
        StructureOrientation structureOrientation = StructureOrientation.deserialize((Map<String, Object>) object.get("orientation"));
        return new DialerStructure(location, structureOrientation, boundingBox, uuid);
    }

    public void rebuild() {
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 4, 4, 4, this::isArmorStand);
        nearbyEntities.forEach(Entity::remove);
        build();
    }

    private void build() {

        double outer = 8;
        BlockStand baseRing = new BlockStand(uuid, world);

        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double baseDelta = unitCircle / outer;
            double baseAngle = baseDelta * i + baseDelta * (outer / 4);

            double baseX = Math.cos(baseDelta * i) * 0.45;
            double baseY = Math.sin(baseDelta * i) * 0.45;


            double y = i % 4 == 0 ? 0.002 : (i % 2 == 0 ? 0.003 : -0.005);
            Location outerLocation = location.clone().add(baseX, y, baseY);
            baseRing.setRotation((float) Math.toDegrees(baseAngle), 0);
            baseRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            baseRing.largeBlockAt(outerLocation, new EulerAngle(0, 0, 0));

        }
        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.largeBlockAt(location.clone().add(0, -0.25, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.POLISHED_DEEPSLATE);
        baseRing.largeBlockAt(location.clone().add(0, -1 + 0.125, 0), new EulerAngle(0, 0, 0));

        baseRing.setMaterial(Material.LIGHTNING_ROD);
        baseRing.largeBlockAt(location.clone().add(0.25, 0.5, 0.25), new EulerAngle(Math.PI, 0, 0));
    }

    private boolean isArmorStand(Entity entity) {
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            if (!armorStand.isVisible() && !Objects.isNull(armorStand.getCustomName())) {
                return armorStand.getCustomName().equalsIgnoreCase(uuid.toString());
            }
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public StructureOrientation getStructureOrientation() {
        return structureOrientation;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIdentifier() {
        return "dialer";
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("uuid", this.getUUID().toString());
        payload.put("location", this.getLocation().serialize());
        payload.put("boundingBox", this.getBoundingBox().serialize());
        payload.put("orientation", this.getStructureOrientation().serialize());
        return payload;
    }

}
