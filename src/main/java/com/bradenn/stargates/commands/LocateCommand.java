package com.bradenn.stargates.commands;

import com.bradenn.stargates.Stargate;
import com.bradenn.stargates.structures.GateStructure;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocateCommand implements SubCommand {

    public String getLabel() {
        return "locate";
    }

    public String getDescription() {
        return "Find your nearest stargate.";
    }

    public void onRun(Player player, String[] args) {


    }

    private void error() {

    }

}
