package com.bradenn.stargates;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ItemFrameDialer {

    private final Location dialerCenter;
    private int rotation;

    public ItemFrameDialer(Location location) {
        this.dialerCenter = findBlockFace(location.getBlock()).add(0.5, 1, 0.5);
    }

    private Location findBlockFace(Block block) {
        for (int i = -1; i <= 1; i++) {
            Block deltaX = block.getRelative(i, 0, 0);
            Block deltaZ = block.getRelative(0, 0, i);
            if (deltaX.getType().equals(Material.DEEPSLATE_TILES)) {
                this.rotation = i >= 0 ? 2 : -2;
                return deltaX.getLocation();
            } else if (deltaZ.getType().equals(Material.DEEPSLATE_TILES)) {
                this.rotation = i >= 0 ? 0 : -4;
                return deltaZ.getLocation();
            }
        }
        return block.getLocation();
    }

    private boolean isItemFrame(Entity entity) {
        return entity.getType().equals(EntityType.GLOW_ITEM_FRAME);
    }

    private BoundingBox getZBox(Location location) {
        return BoundingBox.of(location, 0.5, 1, 1);
    }

    private BoundingBox getXBox(Location location) {
        return BoundingBox.of(location, 1, 1, 0.5);
    }

    private ItemFrame castFrame(Entity entity) {
        return (ItemFrame) entity;
    }

    private int sortByX(Entity a, Entity b) {
        return b.getLocation().getBlockX() - a.getLocation().getBlockX();
    }

    private int sortByZ(Entity a, Entity b) {
        return b.getLocation().getBlockZ() - a.getLocation().getBlockZ();
    }

    private List<ItemFrame> findItemFrameInput(Location location) {
        World world = location.getWorld();
        if (Objects.isNull(world)) return null;

        List<ItemFrame> frameMap = new ArrayList<>();

        Collection<Entity> xFrames = world.getNearbyEntities(getXBox(location), this::isItemFrame);
        Collection<Entity> zFrames = world.getNearbyEntities(getZBox(location), this::isItemFrame);

        if (xFrames.size() == 3) {
            xFrames.stream().sorted(this::sortByX).forEach(entity -> frameMap.add((ItemFrame) entity));
        } else if (zFrames.size() == 3) {
            zFrames.stream().sorted(this::sortByZ).forEach(entity -> frameMap.add((ItemFrame) entity));
        }

        return frameMap;
    }

    public String getAddress() {
        List<ItemFrame> frames = findItemFrameInput(dialerCenter);
        StringBuilder stringBuilder = new StringBuilder();

        assert frames != null;

        for (ItemFrame frame : frames) {
            frame.setVisible(false);
            ItemStack is = new ItemStack(Material.AMETHYST_SHARD);
            int fullRotation = (frame.getRotation().ordinal() - 3) + rotation;
            if(fullRotation < 0) fullRotation = fullRotation + 8;
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(fullRotation + "");
            is.setItemMeta(im);
            frame.setItem(is);
            stringBuilder.append(fullRotation);
        }
        if(rotation == -4 || rotation == 2) stringBuilder.reverse();
        return stringBuilder.toString();
    }

    public void setAddress(int[] values) {
        List<ItemFrame> frames = findItemFrameInput(dialerCenter);

        for (int i = 0; i < frames.size(); i++) {
            ItemFrame frame = frames.get(i);
            frame.setVisible(false);

            ItemStack is = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = is.getItemMeta();

            int fullRotation = (frame.getRotation().ordinal() - 3) + rotation;
            if(fullRotation < 0) fullRotation = fullRotation + 8;
            im.setDisplayName(fullRotation + "");
            is.setItemMeta(im);
            frame.setItem(is);

            frame.setRotation(Rotation.values()[values[i]]);
        }

    }


}
