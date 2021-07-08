package com.bradenn.stargates.cosmetics;

import com.bradenn.stargates.Main;
import com.bradenn.stargates.structures.Orientation;
import com.bradenn.stargates.structures.Structure;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class BlockStand {

    private final World world;
    private final UUID uuid;
    private Material material;
    private EulerAngle angle;
    private final Class<? extends Structure> structure;
    private float yaw = 0, pitch = 0;

    public BlockStand(Structure structure, World world) {
        this.uuid = structure.getUUID();
        this.structure = structure.getClass();
        this.world = world;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void largeBlockAt(Location location, EulerAngle angle) {
        this.angle = angle;
        world.spawn(location, ArmorStand.class, this::configureArmorStand);
    }

    public void smallBlockAt(Location location, EulerAngle angle) {
        this.angle = angle;
        double xRad = (Math.PI * angle.getX()) / 180;
        double zRad = (Math.PI * angle.getZ()) / 180;
        double x = (Math.sin(zRad) * Math.cos(xRad)) * -0.25;
        double y = (Math.cos(xRad) * Math.cos(zRad)) * -0.25;
        double z = Math.sin(xRad) * -0.25;
        world.spawn(location.clone().add(x, y - 1.4375, z), ArmorStand.class, this::configureSmallArmorStand);
    }

    public void createRing(Location center, double count, Vector size, boolean offset) {
        double unit = (Math.PI * 2) / count;
        double off = offset ? unit / 2 : 0;
        for (double i = 0; i < count; i++) {
            double dx = Math.cos(unit * i + off) * size.getX();
            double dy = Math.sin(unit * i + off) * size.getY();

            smallBlockAt(center.clone().add(dx, i * 4E-4 - (offset ? 0.02 : 0), dy), new EulerAngle(0, unit * i + off, 0));
        }
    }

    public void createRing(Location center, double count, Vector size, boolean offset, Orientation orientation) {
        double unit = (Math.PI * 2) / count;
        for (double i = 0; i < count; i++) {
            double dx = Math.cos(unit * i - Math.PI / 2) * size.getX();
            double dy = Math.sin(unit * i - Math.PI / 2) * size.getY();
            double angle = -unit * i + Math.PI;

            smallBlockAt(center.clone().add(orientation.translate(dx, dy, i * 5E-4)), orientation.translateAngle(0, 0, angle));
        }
    }

    private void configureSmallArmorStand(Entity entity) {
        ArmorStand armorStand = (ArmorStand) entity;

        EntityEquipment entityEquipment = armorStand.getEquipment();
        if (Objects.isNull(entityEquipment)) return;

        ItemStack itemStack = new ItemStack(material);


        entityEquipment.setHelmet(itemStack);
        armorStand.setHeadPose(angle);
        armorStand.setRotation(yaw, pitch);

        armorStand.setCustomName(uuid.toString());
        armorStand.setMetadata("structure", new FixedMetadataValue(Main.plugin, uuid.toString()));
        armorStand.setMetadata("class", new FixedMetadataValue(Main.plugin, structure));
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCollidable(false);
        armorStand.setPersistent(true);
        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setCanPickupItems(false);
        armorStand.setSilent(true);
    }

    private void configureArmorStand(Entity entity) {
        ArmorStand armorStand = (ArmorStand) entity;

        EntityEquipment entityEquipment = armorStand.getEquipment();
        if (Objects.isNull(entityEquipment)) return;

        entityEquipment.setHelmet(new ItemStack(material));
        armorStand.setHeadPose(angle);
        armorStand.setRotation(yaw, pitch);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.setCustomName(uuid.toString());
        armorStand.setMarker(false);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCollidable(false);
        armorStand.setPersistent(true);
        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setCanPickupItems(false);
        armorStand.setSilent(true);
    }

    public static boolean isArmorStand(Entity entity, UUID uuid) {
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            if (!Objects.isNull(armorStand.getCustomName())) {
                return armorStand.getCustomName().equalsIgnoreCase(uuid.toString());
            }
        }
        return false;
    }


}
