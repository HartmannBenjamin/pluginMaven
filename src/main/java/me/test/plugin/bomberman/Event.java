package me.test.plugin.bomberman;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Event implements Listener {
    private final ArrayList<TntGame> allTNTFire = new ArrayList<>();

    @EventHandler
    public void fireDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                Player p = (Player) e.getEntity();
                Location loc = p.getLocation();
                Player killer = getKiller(loc);
                p.sendMessage(ChatColor.RED + "Vous avez été tué par " + ChatColor.BOLD + killer.getName());
            }
        }
    }

    private Player getKiller(Location location) {
        AtomicReference<Player> playerKiller = new AtomicReference<>();

        allTNTFire.forEach((tntGame) -> tntGame.getBlocks().forEach((fire) -> {
            if (fire.getBlockX() == location.getBlockX() && fire.getBlockZ() ==  location.getBlockZ()) {
                playerKiller.set(tntGame.getCreator());
            }
        }));

        return playerKiller.get();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block tnt = event.getBlockPlaced();

        if (tnt.getType() == Material.TNT) {
            if(tnt.getLocation().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                TntGame fire = new TntGame(tnt.getLocation(), event.getPlayer());
                explosion(tnt.getLocation(), fire, 30);
            } else {
                event.setCancelled(true);
            }
        }
    }

    private void explosion(Location location, TntGame fire, int preTime) {
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

    private void clearFire(TntGame fire) {
        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->
            fire.getBlocks().forEach((currentFireBLock) -> {

                if (!isOtherBomb(currentFireBLock)) {
                    currentFireBLock.getBlock().setType(Material.VOID_AIR);
                }
        }), 5L);
    }

    private boolean isOtherBomb(Location location) {
        AtomicInteger result = new AtomicInteger();

        allTNTFire.forEach((tntBLocks) -> tntBLocks.getBlocks().forEach((fire) -> {
            if (fire.getBlockX() == location.getBlockX() && fire.getBlockZ() ==  location.getBlockZ()) {
                result.getAndIncrement();
            }
        }));

        return result.get() >= 1;
    }

    private void breakDirt(Block block) {
        Location location = block.getLocation();
        block.setType(Material.FIRE);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).setType(Material.VOID_AIR);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 2, location.getBlockZ()).setType(Material.VOID_AIR);
    }

    public boolean testBlock(Block block) {
        if (block.getType() == Material.BEDROCK || block.getType() == Material.DIRT) {
            return false;
        }

        if (block.getType() == Material.TNT) {
            AtomicReference<TntGame> tntGameElem = null;

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

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if(player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
            event.setCancelled(true);

            if (event.getClickedBlock() == null) {
                return;
            }

            Location blockLoc = event.getClickedBlock().getLocation();
            PersistentDataContainer container = player.getWorld().getPersistentDataContainer();

            int b1X = container.has(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) : 0;
            int b1Y = container.has(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER): 0 ;
            int b1Z = container.has(new NamespacedKey(Main.getPlugin(), "b1Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b1Z"), PersistentDataType.INTEGER) : 0;
            int b2X = container.has(new NamespacedKey(Main.getPlugin(), "b2X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b2X"), PersistentDataType.INTEGER) : 0;
            int b2Y = container.has(new NamespacedKey(Main.getPlugin(), "b2Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b2Y"), PersistentDataType.INTEGER) : 0;
            int b2Z = container.has(new NamespacedKey(Main.getPlugin(), "b2Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b2Z"), PersistentDataType.INTEGER) : 0;

            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (blockLoc.getBlockY() != b2Y) {
                    player.sendMessage(ChatColor.RED + "Pas le même axe X");
                }

                if ((Math.abs(blockLoc.getBlockX() - b2X)%2 != 0) || (Math.abs(blockLoc.getBlockZ() - b2Z)%2 != 0)) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions");
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b2X)%2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b2Z)%2);
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
                }

                if (Math.abs(blockLoc.getBlockX() - b1X)%2 !=0 || Math.abs(blockLoc.getBlockZ() - b1Z)%2 != 0) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b1X)%2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b1Z)%2);
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
}