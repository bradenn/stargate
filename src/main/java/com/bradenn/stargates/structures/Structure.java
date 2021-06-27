package com.bradenn.stargates.structures;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.Map;

public class Structure {

    private final World world;
    private final Location location;
    private final BoundingBox boundingBox;
    private final Orientation orientation;

    public Structure(Location location, BoundingBox boundingBox, Orientation orientation) {
        this.world = location.getWorld();
        this.location = location;
        this.boundingBox = boundingBox;
        this.orientation = orientation;
    }

    public Structure(World world, Location location, BoundingBox boundingBox, Orientation orientation) {
        this.world = world;
        this.location = location;
        this.boundingBox = boundingBox;
        this.orientation = orientation;
    }

    @SuppressWarnings("unchecked")
    public Structure(Document document) {
        this.world = Bukkit.getWorld(document.getString("world"));
        this.location = Location.deserialize((Map<String, Object>) document.get("location"));
        this.boundingBox = BoundingBox.deserialize((Map<String, Object>) document.get("bounds"));
        this.orientation = Orientation.deserialize((Map<String, Object>) document.get("orientation"));
    }

    public Document getDocument() {
        Document document = new Document();
        document.append("world", getWorld().getName());
        document.append("location", getLocation().serialize());
        document.append("bounds", getBoundingBox().serialize());
        document.append("orientation", getOrientation().serialize());
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
