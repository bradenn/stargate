package com.bradenn.stargates.structures;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.Map;

public abstract class Structure implements Persistent {

    private final World world;
    private final Location location;
    private final BoundingBox boundingBox;
    private final Orientation orientation;
    private final StructureMeta structureMeta;
    private final String name;

    @SuppressWarnings("unchecked")
    public Structure(Document document) {
        this.name = document.getString("name");
        this.structureMeta = null;
        this.world = Bukkit.getWorld(document.getString("world"));
        this.location = Location.deserialize((Map<String, Object>) document.get("location"));
        this.boundingBox = BoundingBox.deserialize((Map<String, Object>) document.get("bounds"));
        this.orientation = Orientation.deserialize((Map<String, Object>) document.get("orientation"));
    }

    public Structure(String name, Location location, BoundingBox boundingBox, Orientation orientation) {
        this.world = location.getWorld();
        this.location = location;
        this.boundingBox = boundingBox;
        this.orientation = orientation;
        this.name = name;
        this.structureMeta = null;
    }

    public StructureMeta getStructureMeta() {
        return structureMeta;
    }

    public String getName() {
        return name;
    }

    public Document getDocument() {
        Document document = new Document();
        document.append("world", getWorld().getName());
        document.append("location", getLocation().serialize());
        document.append("bounds", getBoundingBox().serialize());
        document.append("orientation", getOrientation().serialize());
        document.append("structureMeta", getStructureMeta());
        document.append("name", getName());
        document.append("uuid", getUUID().toString());
        return document;
    }

    public World getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Orientation getOrientation() {
        return orientation;
    }

}
