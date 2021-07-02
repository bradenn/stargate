package com.bradenn.stargates.structures;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;


public class StructureFactory {

    public static Structure createStructure(StructureType type, String name, Location base) throws Exception {
        Orientation orientation = Orientation.fromYaw(base.getYaw());
        Location baseOffset = type.getOffset(base, orientation);
        BoundingBox bounds = type.getBounds(base, orientation);
        switch (type) {
            case STARGATE:
                return new Stargate(name, baseOffset, bounds, orientation);
            case DIALER:
                return new Dialer(name, baseOffset, bounds, orientation);
            case RINGS:
                return new Rings(name, baseOffset, bounds, orientation);
            default:
                throw new Exception(String.format("Invalid structure type '%s'.", type));
        }
    }

    public enum StructureType {
        STARGATE(new Vector(0, -0.5, 0), new Vector(0.5, 2.5, 0.5), new Vector(1.75, 1.75, -0.33)),
        DIALER(new Vector(3, -0.5, 2), new Vector(3, 1, 2), new Vector(1, 1, 1)),
        RINGS(new Vector(0, -0.75, 0), new Vector(0, -0.75, 0), new Vector(3, 3, 3));

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
            Location offsetBase = base.clone().add(orientation.translate(boundsOffset.getX(), boundsOffset.getY(), boundsOffset.getZ()));
            return BoundingBox.of(offsetBase, bounds.getX(), bounds.getY(), bounds.getZ());
        }


    }

}
