package me.test.plugin.bomberman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TntService {
    private static final ArrayList<TntGame> allTNTFire = new ArrayList<>();

    public static ArrayList<TntGame> getAllTNTFire() {
        return allTNTFire;
    }

    static void explosion(Location location, TntGame fire, int preTime) {
        allTNTFire.add(fire);
        PlayerService.playTntSound(location);

        new BukkitRunnable()
        {
            int time = 10 + preTime;
            int distance = 1;
            final int power = Items.getPlayerBombPower(fire.getCreator());
            boolean Xup = true;
            boolean Xdown = true;
            boolean Zup = true;
            boolean Zdown = true;

            @Override
            public void run()
            {
                if (time > 9 && location.getBlock().getType() == Material.FIRE) {
                    this.cancel();
                    clearFire(fire, false);
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
                        PlayerService.playTntExplosionSound(location);
                        clearFire(fire, true);
                        this.cancel();
                    }

                    distance++;
                }

                this.time--;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
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


    private static void clearFire(TntGame fire, boolean refund) {
        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
            allTNTFire.remove(fire);

            fire.getBlocks().forEach((currentFireBLock) -> {
                if (!isOtherBomb(currentFireBLock)) {
                    currentFireBLock.getBlock().setType(Material.VOID_AIR);
                }
            });

            if (refund && BomberMan.isGame()) {
                fire.getCreator().getInventory().addItem(new ItemStack(Material.TNT, 1));
            }
        }, 5L);
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

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> spawnItem(location), 10L);
    }

    private static void spawnItem(Location location) {
        Random r = new Random();
        int low = 1;
        int high = 4;
        int result = r.nextInt(high-low) + low;
        Material item = null;

        switch (result) {
            case 1:
                item = Material.FEATHER;
                break;
            case 2:
                item = Material.FIREWORK_ROCKET;
                break;
            case 3:
                item = Material.REDSTONE;
                break;
        }

        if (item != null) {
            Entity e = location.getWorld().dropItemNaturally(location.add(0, 0, 0), new ItemStack(item));
            e.setVelocity(e.getVelocity().zero());
        }
    }
}
