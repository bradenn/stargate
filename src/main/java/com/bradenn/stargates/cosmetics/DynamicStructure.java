package com.bradenn.stargates.cosmetics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicStructure {

    private final World world;
    private final Material material;
    private EulerAngle angle;
    private Location location;

    private final List<ArmorStand> stands;

    public DynamicStructure(World world, Material material) {
        this.world = world;
        this.material = material;
        this.stands = new ArrayList<>();
    }

    public void generateRing(Location center, double count, Vector size, boolean offset) {
        double unit = (Math.PI * 2) / count;
        double off = offset ? unit / 2 : 0;
        for (double i = 0; i < count; i++) {
            double dx = Math.cos(unit * i + off) * size.getX();
            double dy = Math.sin(unit * i + off) * size.getY();

            setLocation(center.clone().add(dx, i * 4E-4 - (offset ? 0.02 : 0), dy));
            setRotation(new EulerAngle(0, unit * i + off, 0));

            generatedBlock();
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRotation(EulerAngle eulerAngle) {
        this.angle = eulerAngle;
    }

    public void translateAll(Vector delta) {
        stands.forEach(stand -> stand.teleport(stand.getLocation().add(delta)));
    }

    public void removeAll() {
        stands.forEach(Entity::remove);
    }

    public void generatedBlock() {
        double xRad = (Math.PI * angle.getX()) / 180;
        double zRad = (Math.PI * angle.getZ()) / 180;
        double x = (Math.sin(zRad) * Math.cos(xRad)) * -0.25;
        double y = (Math.cos(xRad) * Math.cos(zRad)) * -0.25;
        double z = Math.sin(xRad) * -0.25;
        world.spawn(location.clone().add(x, y - 1.4375, z), ArmorStand.class, this::configureStand);
    }

    private void configureStand(Entity entity) {
        ArmorStand armorStand = (ArmorStand) entity;

        EntityEquipment entityEquipment = armorStand.getEquipment();
        if (Objects.isNull(entityEquipment)) return;

        ItemStack itemStack = new ItemStack(material);

        entityEquipment.setHelmet(itemStack);
        armorStand.setHeadPose(angle);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCollidable(true);
        armorStand.setPersistent(true);
        armorStand.setInvulnerable(true);
        armorStand.setCanPickupItems(false);
        armorStand.setSilent(true);
        stands.add(armorStand);
    }


}
