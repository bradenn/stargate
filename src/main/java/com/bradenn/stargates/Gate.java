package com.bradenn.stargates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Gate {

    private String address;
    private Location location;

    public Gate(String address) {
        List<Map<?, ?>> list = Stargate.plugin.getConfig().getMapList("stargates");
        for (Map<?, ?> element : list) {
            Location source = Location.deserialize((Map<String, Object>) element.get("location"));
            if (element.get("address").equals(address)) {
                this.location = source;
                this.address = (String) element.get("address");
            }
        }
        assignAddress();
    }

    public Gate(Location location) {
        String near = nearestGate(location);
        if (near.isEmpty()) {
            Random r = new Random();
            StringBuilder bytes = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                bytes.append(r.nextInt(8));
            }
            this.address = bytes.toString();
        } else {
            this.address = near;
        }
        assignAddress();

    }

    public String nearestGate(Location from) {
        List<Map<?, ?>> list = Stargate.plugin.getConfig().getMapList("stargates");
        for (Map<?, ?> element : list) {
            Location source = Location.deserialize((Map<String, Object>) element.get("location"));
            if (source.distance(from) <= 8) {
                this.location = source;
                return (String) element.get("address");
            }
        }
        return "";
    }

    public void save() {
        Map<String, Object> items = new HashMap<>();
        items.put("location", location.serialize());
        items.put("address", address);
        List<Map<?, ?>> list = Stargate.plugin.getConfig().getMapList("stargates");
        list.add(items);
        Stargate.plugin.getConfig().set("stargates", list);
        Stargate.plugin.saveConfig();
    }

    public void assignAddress() {
        ItemFrameDialer itemFrameDialerN = new ItemFrameDialer(location.clone().add(0, 4, 1));
        ItemFrameDialer itemFrameDialerS = new ItemFrameDialer(location.clone().add(0, 4, -1));
        int[] add = new int[3];
        for (int i = 0; i <= 2; i++) {
            add[i] = Integer.parseInt(address.substring(i, i + 1));
        }
        itemFrameDialerN.setAddress(add);
        itemFrameDialerS.setAddress(add);
    }

    public void setItemFrame(Block block, int position) {
        Entity[] entities = block.getLocation().getChunk().getEntities();
        for (Entity entity : entities) {
            if (entity instanceof ItemFrame) {
                if (entity.getLocation().getBlock().equals(block)) {
                    ItemFrame itemFrame = (ItemFrame) entity;
                    itemFrame.setItem(new ItemStack(Material.CHAIN));
                    itemFrame.setRotation(Rotation.values()[position]);
                }
            }
        }

    }

    public void connect(String address) {
        if (!address.equals(this.address)) {
            Gate target = new Gate(address);
            if (target.getLocation() != null && getLocation() != null) {
                Bukkit.broadcastMessage(String.format("Opening wormhole from '%s' to '%s'.", getAddress(), target.getAddress()));
                target.openGate();
                openGate();
                try {
                    Stargate.spacetime.establishWormhole(getAddress(), target.getAddress());
                } catch (Exception e) {
                    Bukkit.broadcastMessage(e.getMessage());
                }
            }

        }
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public void openGate() {

        ParticleEffects.spawnPortal(location);

    }

}
