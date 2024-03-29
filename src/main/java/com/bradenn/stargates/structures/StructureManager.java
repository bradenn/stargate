package com.bradenn.stargates.structures;

import com.bradenn.stargates.Database;
import com.bradenn.stargates.Main;
import com.bradenn.stargates.cosmetics.Messages;
import com.bradenn.stargates.structures.stargate.Stargate;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class StructureManager {

    private static Map<Class<? extends Structure>, MongoCollection<Document>> structureMap;

    public static void init() {
        structureMap = new HashMap<>();
        structureMap.put(Stargate.class, Database.getCollection("stargates"));
//        structureMap.put(Rings.class, Database.getCollection("rings"));
    }

    public static <T extends Structure> T createStructure(String name, Location base, Class<T> structure) throws Exception {
        StructureType type = matchClass(structure);
        Orientation orientation = Orientation.fromYaw(base.getYaw());
        Location baseOffset = type.getOffset(base, orientation);
        BoundingBox bounds = type.getBounds(base, orientation);

        MongoCollection<Document> collection = structureMap.get(structure);
        Document queryDocument = collection.find(new Document("name", name)).first();
        if (queryDocument != null) throw new Exception("A structure with this name already exists.");

        return structure.getDeclaredConstructor(String.class, Location.class, BoundingBox.class, Orientation.class).newInstance(name, baseOffset, bounds, orientation);
    }

    public static <T extends Structure> T getStructureFromUUID(UUID uuid, Class<T> structure) throws Exception {
        MongoCollection<Document> collection = structureMap.get(structure);
        Document queryDocument = collection.find(new Document("uuid", uuid.toString())).first();
        if (queryDocument == null) {
            throw new Exception("Structure could not be found.");
        }
        return structure.getConstructor(Document.class).newInstance(queryDocument);
    }

    public static <T extends Structure> T getStructureFromName(String name, Class<T> structure) throws Exception {
        MongoCollection<Document> collection = structureMap.get(structure);
        Document queryDocument = collection.find(new Document("name", name)).first();
        if (queryDocument == null) {
            throw new Exception("Structure could not be found.");
        }
        return structure.getConstructor(Document.class).newInstance(queryDocument);
    }

    private static StructureType matchClass(Class<? extends Structure> structure) {
        return StructureType.valueOf(structure.getSimpleName().toUpperCase());
    }

    public static void rebuildAllVerbose(Player player) {
        structureMap.keySet().forEach(structureType -> {
            Messages.sendInfo(player, "Rebuilding structures of type '%s':", structureType.getSimpleName());
            getAllStructuresByClass(structureType).forEach(structure -> {
                double start = System.currentTimeMillis();
                structure.rebuild();
                Messages.sendRaw(player, "&7%s (%s) - [%.1fms]", structure.getName(), structure.getWorld().getName(), System.currentTimeMillis() - start);
            });
        });
    }

    public static void rebuildAll() {
        structureMap.keySet().forEach(StructureManager::rebuildAllStructuresByClass);
    }

    public static void removeAll() {
        structureMap.keySet().forEach(StructureManager::removeAllStructuresByClass);
    }

    private static <T extends Structure> void removeAllStructuresByClass(Class<T> structureClass) {
        getAllStructuresByClass(structureClass).forEach(Structure::terminate);
    }

    private static <T extends Structure> void rebuildAllStructuresByClass(Class<T> structureClass) {
        getAllStructuresByClass(structureClass).forEach(s -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    s.rebuild();
                }
            }.runTask(Main.plugin);
        });
    }

    private static <T extends Structure> List<T> getAllStructuresByClass(Class<T> structureClass) {
        List<T> structures = new ArrayList<>();
        MongoCollection<Document> collection = structureMap.get(structureClass);
        collection.find().forEach((Consumer<? super Document>) structureDocument -> {
            try {
                structures.add(structureClass.getConstructor(Document.class).newInstance(structureDocument));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        return structures;
    }

    public enum StructureType {
        STARGATE(new Vector(0, -0.5, 0), new Vector(0.5, 2.5, 0.5), new Vector(2, 2, 0.25)),
        DIALER(new Vector(3, -0.5, 2), new Vector(3, 1, 2), new Vector(1, 1, 1)),
        RINGS(new Vector(0, -0.75, 0), new Vector(0, 1, 0), new Vector(3, 2, 3));

        Vector offset, boundsOffset, bounds;

        StructureType(Vector offset, Vector boundsOffset, Vector bounds) {
            this.offset = offset;
            this.boundsOffset = boundsOffset;
            this.bounds = bounds;
        }

        public Location getOffset(Location base, Orientation orientation) {
            return base.getBlock().getLocation().clone().add(orientation.translate(offset.getX(), offset.getY(), offset.getZ())).add(0.5, 0, 0.5);
        }

        public BoundingBox getBounds(Location base, Orientation orientation) {
            Location center = base.getBlock().getLocation().clone();
            Location offsetBase = center.add(orientation.translate(boundsOffset.getX(), boundsOffset.getY(), boundsOffset.getZ()));
            Vector offsetBounds = orientation.translate(bounds.getX(), bounds.getY(), bounds.getZ());
            return BoundingBox.of(offsetBase, offsetBounds.getX(), offsetBounds.getY(), offsetBounds.getZ());
        }


    }

}
