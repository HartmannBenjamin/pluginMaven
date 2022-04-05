package me.test.plugin.bomberman;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BomberMan {
    private final Player player;
    private static boolean is_game = false;
    private final ArrayList<Location> spawnsLocation = new ArrayList<>();
    private static final ArrayList<TntGame> allTNTFire = new ArrayList<>();
    private final int b1X;
    private final int b1Y;
    private final int b1Z;
    private final int b2X;
    private final int b2Y;
    private final int b2Z;

    public BomberMan(Player player) {
        PersistentDataContainer container = player.getWorld().getPersistentDataContainer();

        this.player = player;
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

    public static boolean isGame() {
        return is_game;
    }

    public void setGame(boolean is_game) {
        BomberMan.is_game = is_game;
    }

    public void generateMap() {
        if (b1Y == b2Y) {
            buildProcess(b1X, b1Y, b1Z, b2X, b2Z);
        } else {
            player.sendMessage("Mauvaises configurations de map");
        }
    }

    private void buildProcess(int b1X, int b1Y, int b1Z, int b2X, int b2Z) {
        if (b1X < b2X) {
            if (b1Z < b2Z) {
                spawnsLocation.add(new Location(player.getWorld(), b1X + 1.5, b1Y + 2, b1Z + 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b1X + 1.5, b1Y + 2, b2Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X - 0.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X - 0.5, b1Y + 2, b2Z - 0.5));
            } else {
                spawnsLocation.add(new Location(player.getWorld(), b1X + 1.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b1X + 1.5, b1Y + 2, b2Z + 1.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X - 0.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X - 0.5, b1Y + 2, b2Z + 1.5));
            }
        } else {
            if (b1Z < b2Z) {
                spawnsLocation.add(new Location(player.getWorld(), b1X - 0.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(player.getWorld(), b1X - 0.5, b1Y + 2, b2Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X + 1.5, b1Y + 2, b1Z + 1.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X + 1.5, b1Y + 2, b2Z - 0.5));
            } else {
                spawnsLocation.add(new Location(player.getWorld(), b1X - 0.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b1X - 0.5, b1Y + 2, b2Z + 1.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X + 1.5, b1Y + 2, b1Z - 0.5));
                spawnsLocation.add(new Location(player.getWorld(), b2X + 1.5, b1Y + 2, b2Z + 1.5));
            }
        }

        if (b1X < b2X) {
            for (int i = b1X; i <= b2X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                }
            }
        } else {
            for (int i = b2X; i <= b1X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.VOID_AIR);
                        player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.VOID_AIR);
                    }
                }
            }
        }

        if (b1X < b2X) {
            for (int i = b1X; i <= b2X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b1Z)%2) == 0) && ((Math.abs(i - b1X)%2) == 0)) {
                            player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition1 = (Math.abs(i - b1X) == 1 || Math.abs(i - b1X) == 2);
                            boolean condition2 = (Math.abs(j - b1Z) == 1 || Math.abs(j - b1Z) == 2);
                            boolean condition3 = (i == b2X - 1 || i == b2X - 2);
                            boolean condition4 = (j == b2Z - 1 || j == b2Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b2Z)%2) == 0) && ((Math.abs(i - b1X)%2) == 0)) {
                            player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition1 = (Math.abs(i - b1X) == 1 || Math.abs(i - b1X) == 2);
                            boolean condition2 = (Math.abs(j - b2Z) == 1 || Math.abs(j - b2Z) == 2);
                            boolean condition3 = (i == b2X - 1 || i == b2X - 2);
                            boolean condition4 = (j == b1Z - 1 || j == b1Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                }
            }
        } else {
            for (int i = b2X; i <= b1X; i++) {
                if (b1Z < b2Z) {
                    for (int j = b1Z; j <= b2Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b1Z) % 2) == 0) && ((Math.abs(i - b2X) % 2) == 0)) {
                            player.getWorld().getBlockAt(i, b1Y + 1, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y + 2, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y + 3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition1 = (Math.abs(i - b2X) == 1 || Math.abs(i - b2X) == 2);
                            boolean condition2 = (Math.abs(j - b1Z) == 1 || Math.abs(j - b1Z) == 2);
                            boolean condition3 = (i == b1X - 1 || i == b1X - 2);
                            boolean condition4 = (j == b2Z - 1 || j == b2Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    player.getWorld().getBlockAt(i, b1Y + 1, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y + 2, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y + 3, j).setType(Material.DIRT);
                                }
                            }
                        }


                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                } else {
                    for (int j = b2Z; j <= b1Z; j++) {
                        if (i == b1X || i == b2X || j == b1Z || j == b2Z ||
                                ((Math.abs(j - b2Z) % 2) == 0) && ((Math.abs(i - b2X) % 2) == 0)) {
                            player.getWorld().getBlockAt(i, b1Y + 1, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y + 2, j).setType(Material.BEDROCK);
                            player.getWorld().getBlockAt(i, b1Y + 3, j).setType(Material.BEDROCK);
                        } else {
                            boolean condition1 = (Math.abs(i - b2X) == 1 || Math.abs(i - b2X) == 2);
                            boolean condition2 = (Math.abs(j - b2Z) == 1 || Math.abs(j - b2Z) == 2);
                            boolean condition3 = (i == b1X - 1 || i == b1X - 2);
                            boolean condition4 = (j == b1Z - 1 || j == b1Z - 2);

                            if (!(condition1 && condition2) && !(condition3 && condition4)
                                    && !(condition1 && condition4) && !(condition3 && condition2)) {
                                Random r = new Random();

                                if (r.nextInt(100) > 50) {
                                    player.getWorld().getBlockAt(i, b1Y + 1, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y + 2, j).setType(Material.DIRT);
                                    player.getWorld().getBlockAt(i, b1Y + 3, j).setType(Material.DIRT);
                                }
                            }
                        }

                        player.getWorld().getBlockAt(i, b1Y, j).setType(Material.BEDROCK);
                    }
                }
            }
        }
    }

    public void startGame() {
        for (final Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
            if (spawnsLocation.size() > 0) {
                p.teleport(spawnsLocation.get(0));
                Main.getPlugin().getServer().broadcastMessage("X " + spawnsLocation.get(0).getBlockX() +
                        " Z " + spawnsLocation.get(0).getBlockZ());
                spawnsLocation.remove(0);
            }

            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "C'est parti!"));
        }

        new BukkitRunnable()
        {
            int time = 403;

            @Override
            public void run()
            {
                if (time > 400) {
                    for (final Player player : Main.getPlugin().getServer().getOnlinePlayers())
                    {
                        setGame(true);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((this.time - 400) + " second(s) remains!"));
                    }
                }

                if (this.time == 400)
                {
                    for (final Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "C'est parti!"));
                    }
                }

                this.time--;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L);
    }

    static Player getKiller(Location location) {
        AtomicReference<Player> playerKiller = new AtomicReference<>();

        allTNTFire.forEach((tntGame) -> tntGame.getBlocks().forEach((fire) -> {
            if (fire.getBlockX() == location.getBlockX() && fire.getBlockZ() ==  location.getBlockZ()) {
                playerKiller.set(tntGame.getCreator());
            }
        }));

        return playerKiller.get();
    }

    static void explosion(Location location, TntGame fire, int preTime) {
        allTNTFire.add(fire);

        new BukkitRunnable()
        {
            int time = 10 + preTime;
            int distance = 1;
            final int power = 4;
            boolean Xup = true;
            boolean Xdown = true;
            boolean Zup = true;
            boolean Zdown = true;

            @Override
            public void run()
            {
                if (time > 9 && location.getBlock().getType() == Material.FIRE) {
                    this.cancel();
                    clearFire(fire);
                    allTNTFire.remove(fire);
                }

                if (time == 10) {
                    location.getBlock().setType(Material.FIRE);
                    location.getBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.VOID_AIR);
                    fire.addLocation(location);
                }

                if (time < 10) {
                    if (Xup) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX() + distance, location.getBlockY(), location.getBlockZ());
                        Xup = testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            fire.addLocation(block.getLocation());
                            breakDirt(block);
                        }

                        if (Xup) {
                            fire.addLocation(block.getLocation());
                        }
                    }

                    if (Xdown) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX() - distance, location.getBlockY(), location.getBlockZ());
                        Xdown = testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            fire.addLocation(block.getLocation());
                            breakDirt(block);
                        }

                        if (Xdown) {
                            fire.addLocation(block.getLocation());
                        }
                    }

                    if (Zup) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + distance);
                        Zup = testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            fire.addLocation(block.getLocation());
                            breakDirt(block);
                        }

                        if (Zup) {
                            fire.addLocation(block.getLocation());
                        }
                    }

                    if (Zdown) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - distance);
                        Zdown = testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            fire.addLocation(block.getLocation());
                            breakDirt(block);
                        }

                        if (Zdown) {
                            fire.addLocation(block.getLocation());
                        }
                    }

                    if (distance == power) {
                        clearFire(fire);
                        allTNTFire.remove(fire);
                        this.cancel();
                    }

                    distance++;
                }

                this.time--;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }

    private static void clearFire(TntGame fire) {
        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->
                fire.getBlocks().forEach((currentFireBLock) -> {

                    if (!isOtherBomb(currentFireBLock)) {
                        currentFireBLock.getBlock().setType(Material.VOID_AIR);
                    }
                }), 5L);
    }

    private static boolean isOtherBomb(Location location) {
        AtomicInteger result = new AtomicInteger();

        allTNTFire.forEach((tntBLocks) -> tntBLocks.getBlocks().forEach((fire) -> {
            if (fire.getBlockX() == location.getBlockX() && fire.getBlockZ() ==  location.getBlockZ()) {
                result.getAndIncrement();
            }
        }));

        return result.get() >= 1;
    }

    private static void breakDirt(Block block) {
        Location location = block.getLocation();
        block.setType(Material.FIRE);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).setType(Material.VOID_AIR);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 2, location.getBlockZ()).setType(Material.VOID_AIR);
    }

    public static boolean testBlock(Block block) {
        if (block.getType() == Material.BEDROCK || block.getType() == Material.DIRT) {
            return false;
        }

        if (block.getType() == Material.TNT) {
            AtomicReference<TntGame> tntGameElem = new AtomicReference<>();

            allTNTFire.forEach((tntGame) -> {
                if (tntGame.getLocation().getBlockX() == block.getLocation().getBlockX()
                        && tntGame.getLocation().getBlockZ() == block.getLocation().getBlockZ()) {
                    tntGameElem.set(tntGame);
                }
            });


            explosion(block.getLocation(), tntGameElem.get(), 0);
            return false;
        }

        block.setType(Material.FIRE);
        return true;
    }
}
