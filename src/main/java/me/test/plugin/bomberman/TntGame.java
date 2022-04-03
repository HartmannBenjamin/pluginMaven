package me.test.plugin.bomberman;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TntGame {
    private final Location location;
    private final Player creator;
    private final ArrayList<Location> blocks;

    public TntGame(Location location, Player creator) {
        this.location = location;
        this.creator = creator;
        this.blocks = new ArrayList<>();
        this.addLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public Player getCreator() {
        return creator;
    }

    public ArrayList<Location> getBlocks() {
        return blocks;
    }

    public void addLocation(Location location) {
        this.blocks.add(location);
    }
}
