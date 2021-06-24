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

public class GateStructure implements PersistentStructure {


    private final Location location;
    private final World world;
    private final BoundingBox boundingBox;
    private final StructureOrientation structureOrientation;
    private final UUID uuid;

    public GateStructure(Location location, StructureOrientation structureOrientation, BoundingBox boundingBox, UUID uuid) {
        this.location = location;
        this.structureOrientation = structureOrientation;
        this.boundingBox = boundingBox;
        this.uuid = uuid;
        this.world = location.getWorld();
    }

    public GateStructure(Location location, StructureOrientation structureOrientation) throws Exception {
        World world = location.getWorld();
        if (Objects.isNull(world)) throw new Exception("Location is invalid");
        Location center = world.getHighestBlockAt(location).getLocation().add(0.5, 3, 0.5);
        this.location = center;
        this.structureOrientation = structureOrientation;
        this.boundingBox = generateBoundingBox();
        this.uuid = UUID.randomUUID();
        this.world = location.getWorld();
        build();
    }

    public String getIdentifier() {
        return "stargate";
    }

    public void rebuild() {
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 4, 4, 4, this::isArmorStand);
        nearbyEntities.forEach(Entity::remove);
        build();
    }

    private void build() {

        int outer = 34;
        for (int i = 0; i < outer; i++) {

            double unitCircle = (2 * Math.PI);

            double outerDelta = unitCircle / 34;
            double outerAngle = outerDelta * i + outerDelta * 8.5;

            double outerX = Math.cos(outerDelta * i) * 3.25;
            double outerY = Math.sin(outerDelta * i) * 3.25;

            BlockStand outerRing = new BlockStand(uuid, world);
            Location outerLocation = location.clone().add(outerX, outerY, (i % 2 == 0 ? 0.005 : -0.005));
            outerRing.setMaterial(Material.DEEPSLATE_TILE_SLAB);
            outerRing.largeBlockAt(outerLocation, new EulerAngle(0, 0, -outerAngle));


            if (i > 32) continue;
            double innerDelta = unitCircle / 32;
            double innerAngle = innerDelta * i + innerDelta * 8;

            double innerX = Math.cos(innerDelta * i) * 3;
            double innerY = Math.sin(innerDelta * i) * 3;

            BlockStand innerRing = new BlockStand(uuid, world);
            Location innerLocation = location.clone().add(innerX, innerY, (i % 2 == 0 ? 0.002 : -0.002));
            innerRing.setMaterial(Material.POLISHED_DEEPSLATE_SLAB);
            innerRing.largeBlockAt(innerLocation, new EulerAngle(0, 0, -innerAngle));


            if (i > 8) continue;
            double chevronDelta = unitCircle / 8;

            double chevronAngle = chevronDelta * i + chevronDelta * 2;

            double chevronX = Math.cos(chevronDelta * i) * 3.125;
            double chevronY = Math.sin(chevronDelta * i) * 3.125;

            BlockStand chevronRing = new BlockStand(uuid, world);
            Location chevronLocation = location.clone().add(chevronX, chevronY, (i % 2 == 0 ? 0.005 : -0.005));
            chevronRing.setMaterial(Material.WAXED_CUT_COPPER_SLAB);
            chevronRing.largeBlockAt(chevronLocation, new EulerAngle(0, Math.PI / 4, -chevronAngle));
        }
    }

    private BoundingBox generateBoundingBox() {
        if (this.structureOrientation.equals(StructureOrientation.NORTH)) {
            return BoundingBox.of(this.location, 2, 2, 1);
        } else {
            return BoundingBox.of(this.location, 1, 2, 2);
        }
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

    public UUID getUUID() {
        return uuid;
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

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("uuid", this.getUUID().toString());
        payload.put("location", this.getLocation().serialize());
        payload.put("boundingBox", this.getBoundingBox().serialize());
        payload.put("orientation", this.getStructureOrientation().serialize());
        return payload;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static GateStructure deserialize(@NotNull Map<?, ?> args) {
        Map<String, Object> object = (Map<String, Object>) args;
        UUID uuid = UUID.fromString(object.get("uuid").toString());
        Location location = Location.deserialize((Map<String, Object>) object.get("location"));
        BoundingBox boundingBox = BoundingBox.deserialize((Map<String, Object>) object.get("boundingBox"));
        StructureOrientation structureOrientation = StructureOrientation.deserialize((Map<String, Object>) object.get("orientation"));
        return new GateStructure(location, structureOrientation, boundingBox, uuid);
    }
}
