package me.test.plugin.bomberman.game.helpers;

import me.test.plugin.bomberman.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapBuilder {

    private final World world;
    private final int b1X;
    private final int b1Y;
    private final int b1Z;
    private final int b2X;
    private final int b2Y;
    private final int b2Z;

    public MapBuilder(World world) {
        PersistentDataContainer container = world.getPersistentDataContainer();

        this.world = world;

        this.b1X = container.has(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) : 0;
        this.b1Y = container.has(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER): 0 ;
        this.b1Z = container.has(new NamespacedKey(Main.getPlugin(), "b1Z"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b1Z"), PersistentDataType.INTEGER) : 0;
        this.b2X = container.has(new NamespacedKey(Main.getPlugin(), "b2X"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b2X"), PersistentDataType.INTEGER) : 0;
        this.b2Y = container.has(new NamespacedKey(Main.getPlugin(), "b2Y"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b2Y"), PersistentDataType.INTEGER) : 0;
        this.b2Z = container.has(new NamespacedKey(Main.getPlugin(), "b2Z"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "b2Z"), PersistentDataType.INTEGER) : 0;
    }

    public boolean buildProcess() {
        if (b1Y != b2Y) {
            return false;
        }

        deleteMap(this.world);

        if (b1X < b2X) {
            for (int i = b1X; i <= b2X; i++) {
                boolean condition1 = Math.abs(i - b1X) == 1 || Math.abs(i - b1X) == 2;
                boolean conditionModulo1 = (Math.abs(i - b1X) % 2) == 0;

                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b1Z)%2) == 0) && conditionModulo1) {
                            world.getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition2 = (Math.abs(j - b1Z) == 1 || Math.abs(j - b1Z) == 2);
                            boolean condition3 = (i == b2X - 1 || i == b2X - 2);
                            boolean condition4 = (j == b2Z - 1 || j == b2Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    world.getBlockAt(i, b1Y+1, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y+2, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y+3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        world.getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b2Z)%2) == 0) && conditionModulo1) {
                            world.getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition2 = (Math.abs(j - b2Z) == 1 || Math.abs(j - b2Z) == 2);
                            boolean condition3 = (i == b2X - 1 || i == b2X - 2);
                            boolean condition4 = (j == b1Z - 1 || j == b1Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    world.getBlockAt(i, b1Y+1, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y+2, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y+3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        world.getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                }
            }
        } else {
            for (int i = b2X; i <= b1X; i++) {
                boolean conditionModulo1 = (Math.abs(i - b2X) % 2) == 0;

                boolean condition1 = Math.abs(i - b2X) == 1 || Math.abs(i - b2X) == 2;
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b1Z) % 2) == 0) && conditionModulo1) {
                            world.getBlockAt(i, b1Y + 1, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y + 2, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y + 3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition2 = (Math.abs(j - b1Z) == 1 || Math.abs(j - b1Z) == 2);
                            boolean condition3 = (i == b1X - 1 || i == b1X - 2);
                            boolean condition4 = (j == b2Z - 1 || j == b2Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    world.getBlockAt(i, b1Y + 1, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y + 2, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y + 3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        world.getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b2Z) % 2) == 0) && conditionModulo1) {
                            world.getBlockAt(i, b1Y + 1, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y + 2, j).setType(Material.BEDROCK);
                            world.getBlockAt(i, b1Y + 3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition2 = (Math.abs(j - b2Z) == 1 || Math.abs(j - b2Z) == 2);
                            boolean condition3 = (i == b1X - 1 || i == b1X - 2);
                            boolean condition4 = (j == b1Z - 1 || j == b1Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    world.getBlockAt(i, b1Y + 1, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y + 2, j).setType(Material.DIRT);
                                    world.getBlockAt(i, b1Y + 3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        world.getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                }
            }
        }

        return true;
    }

    private void deleteMap(World world) {
        if (b1X < b2X) {
            for (int i = b1X; i <= b2X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        world.getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        world.getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                }
            }
        } else {
            for (int i = b2X; i <= b1X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        world.getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        world.getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        world.getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                }
            }
        }

        cleanWorld();
    }

    private void cleanWorld() {
        World world = Main.getPlugin().getServer().getWorld("world");
        List<Entity> entList = world.getEntities();

        for(Entity current : entList) {
            if (current instanceof Item) {
                current.remove();
            }
        }
    }

    public ArrayList<Location> getSpawnLocations() {
        ArrayList<Location> spawnsLocation = new ArrayList<>();

        if (b1X < b2X) {
            if (b1Z < b2Z) {
                spawnsLocation.add(new Location(world, b1X + 1.5, b1Y + 2, b1Z + 0.5));
                spawnsLocation.add(new Location(world, b1X + 1.5, b1Y + 2, b2Z - 0.5));
                spawnsLocation.add(new Location(world, b2X - 0.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(world, b2X - 0.5, b1Y + 2, b2Z - 0.5));
            } else {
                spawnsLocation.add(new Location(world, b1X + 1.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(world, b1X + 1.5, b1Y + 2, b2Z + 1.5));
                spawnsLocation.add(new Location(world, b2X - 0.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(world, b2X - 0.5, b1Y + 2, b2Z + 1.5));
            }
        } else {
            if (b1Z < b2Z) {
                spawnsLocation.add(new Location(world, b1X - 0.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(world, b1X - 0.5, b1Y + 2, b2Z - 0.5));
                spawnsLocation.add(new Location(world, b2X + 1.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(world, b2X + 1.5, b1Y + 2, b2Z - 0.5));
            } else {
                spawnsLocation.add(new Location(world, b1X - 0.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(world, b1X - 0.5, b1Y + 2, b2Z + 1.5));
                spawnsLocation.add(new Location(world, b2X + 1.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(world, b2X + 1.5, b1Y + 2, b2Z + 1.5));
            }
        }

        return spawnsLocation;
    }

    public void setMapDimension(Block clickedBlock, Action action, Player player) {
            Location blockLoc = clickedBlock.getLocation();
            PersistentDataContainer container = player.getWorld().getPersistentDataContainer();
            boolean valid = true;

            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (blockLoc.getBlockY() != b2Y) {
                    player.sendMessage(ChatColor.RED + "Pas le même axe X");
                    valid = false;
                }

                if ((Math.abs(blockLoc.getBlockX() - b2X) % 2 != 0) || (Math.abs(blockLoc.getBlockZ() - b2Z) % 2 != 0)) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions");
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b2X) % 2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b2Z) % 2);
                    valid = false;
                }

                if (valid) {
                    player.sendMessage(ChatColor.GREEN + "Dimensions bonnes et bien enregistrées");
                }

                container.set(
                        new NamespacedKey(Main.getPlugin(), "b1X"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockX()
                );
                container.set(
                        new NamespacedKey(Main.getPlugin(), "b1Y"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockY()
                );
                container.set(
                        new NamespacedKey(Main.getPlugin(), "b1Z"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockZ()
                );
            } else if (action == Action.LEFT_CLICK_BLOCK) {
                if (b1Y != blockLoc.getBlockY()) {
                    player.sendMessage(ChatColor.RED + "Pas le même axe X");
                    valid = false;
                }

                if (Math.abs(blockLoc.getBlockX() - b1X) % 2 != 0 || Math.abs(blockLoc.getBlockZ() - b1Z) % 2 != 0) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b1X) % 2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b1Z) % 2);
                    valid = false;
                }

                if (valid) {
                    player.sendMessage(ChatColor.GREEN + "Dimensions bonnes et bien enregistrées");
                }

                container.set(
                        new NamespacedKey(Main.getPlugin(), "b2X"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockX()
                );
                container.set(
                        new NamespacedKey(Main.getPlugin(), "b2Y"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockY()
                );
                container.set(
                        new NamespacedKey(Main.getPlugin(), "b2Z"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockZ()
                );
            }

    }
}
