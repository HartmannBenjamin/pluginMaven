package me.test.plugin.bomberman.game.helpers;

import me.test.plugin.bomberman.Main;
import me.test.plugin.bomberman.game.entities.TntGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TntService {
    private static final ArrayList<TntGame> allTNTFire = new ArrayList<>();

    public static ArrayList<TntGame> getAllTNTFire() {
        return allTNTFire;
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


            tntGameElem.get().explosion(block.getLocation(), 0);
            return false;
        }

        block.setType(Material.FIRE);
        return true;
    }

    public static boolean isOtherBomb(Location location) {
        AtomicInteger result = new AtomicInteger();

        allTNTFire.forEach((tntBLocks) -> tntBLocks.getBlocks().forEach((fire) -> {
            if (fire.getBlockX() == location.getBlockX() && fire.getBlockZ() ==  location.getBlockZ()) {
                result.getAndIncrement();
            }
        }));

        return result.get() >= 1;
    }

    public static void breakDirt(Block block) {
        Location location = block.getLocation();
        block.setType(Material.FIRE);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).setType(Material.VOID_AIR);
        block.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 2, location.getBlockZ()).setType(Material.VOID_AIR);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> spawnItem(location), 10L);
    }

    private static void spawnItem(Location location) {
        Random r = new Random();
        int low = 1;
        int high = 12;
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

    public static boolean placeBomb(Block tnt, Player player) {
        if (tnt.getType() == Material.TNT) {
            if(tnt.getLocation().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                tnt.getLocation().add(0, 1, 0).getBlock().setType(Material.BARRIER);

                TntGame newTnt = new TntGame(tnt.getLocation(), player);
                newTnt.explosion(tnt.getLocation(), 30);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public static void addFire(TntGame fire) {
        allTNTFire.add(fire);
    }

    public static void removeFire(TntGame fire) {
        allTNTFire.remove(fire);
    }
}
