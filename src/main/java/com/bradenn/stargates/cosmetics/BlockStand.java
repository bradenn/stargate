package com.bradenn.stargates.cosmetics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import java.util.Objects;
import java.util.UUID;

public class BlockStand {

    private final World world;
    private final UUID uuid;
    private Material material;
    private EulerAngle angle;
    private float yaw = 0, pitch = 0;

    public BlockStand(UUID uuid, World world) {
        this.uuid = uuid;
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
        world.spawn(location, ArmorStand.class, this::configureSmallArmorStand);
    }

    private void configureSmallArmorStand(Entity entity) {
        ArmorStand armorStand = (ArmorStand) entity;

        EntityEquipment entityEquipment = armorStand.getEquipment();
        if (Objects.isNull(entityEquipment)) return;

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        entityEquipment.setHelmet(itemStack);
        armorStand.setHeadPose(angle);
        armorStand.setRotation(yaw, pitch);

        armorStand.setCustomName(uuid.toString());
        armorStand.setVisible(true);
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
