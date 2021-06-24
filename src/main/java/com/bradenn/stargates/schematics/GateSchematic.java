package com.bradenn.stargates.schematics;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.Map;

public class GateSchematic implements StructureSchematic{

    Map<BlockVector, Material> blocks = new HashMap<>();

    void init() {

    }



    BoundingBox dimensions = null;



}
