package me.test.plugin.bomberman.game.entities;

import me.test.plugin.bomberman.Main;
import me.test.plugin.bomberman.game.BomberMan;
import me.test.plugin.bomberman.game.helpers.Items;
import me.test.plugin.bomberman.game.helpers.PlayerService;
import me.test.plugin.bomberman.game.helpers.TntService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void explosion(Location location, int preTime) {
        TntService.addFire(this);
        PlayerService.playTntSound(location);

        new BukkitRunnable()
        {
            int time = 10 + preTime;
            int distance = 1;
            final int power = Items.getPlayerBombPower(getCreator());
            boolean Xup = true;
            boolean Xdown = true;
            boolean Zup = true;
            boolean Zdown = true;

            @Override
            public void run()
            {
                if (time > 9 && location.getBlock().getType() == Material.FIRE) {
                    this.cancel();
                    clear(false);
                }

                if (time == 10) {
                    location.getBlock().setType(Material.FIRE);
                    location.getBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.VOID_AIR);
                    addLocation(location);
                }

                if (time < 10) {
                    if (Xup) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX() + distance, location.getBlockY(), location.getBlockZ());
                        Xup = TntService.testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            addLocation(block.getLocation());
                            TntService.breakDirt(block);
                        }

                        if (Xup) {
                            addLocation(block.getLocation());
                        }
                    }

                    if (Xdown) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX() - distance, location.getBlockY(), location.getBlockZ());
                        Xdown = TntService.testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            addLocation(block.getLocation());
                            TntService.breakDirt(block);
                        }

                        if (Xdown) {
                            addLocation(block.getLocation());
                        }
                    }

                    if (Zup) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + distance);
                        Zup = TntService.testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            addLocation(block.getLocation());
                            TntService.breakDirt(block);
                        }

                        if (Zup) {
                            addLocation(block.getLocation());
                        }
                    }

                    if (Zdown) {
                        Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - distance);
                        Zdown = TntService.testBlock(block);

                        if (block.getType() == Material.DIRT) {
                            addLocation(block.getLocation());
                            TntService.breakDirt(block);
                        }

                        if (Zdown) {
                            addLocation(block.getLocation());
                        }
                    }

                    if (distance == power) {
                        PlayerService.playTntExplosionSound(location);
                        clear(true);
                        this.cancel();
                    }

                    distance++;
                }

                this.time--;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }


    private void clear(boolean refund) {
        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
            TntService.removeFire(this);

            getBlocks().forEach((currentFireBLock) -> {
                if (!TntService.isOtherBomb(currentFireBLock)) {
                    currentFireBLock.getBlock().setType(Material.VOID_AIR);
                }
            });

            if (refund && BomberMan.isGame()) {
                getCreator().getInventory().addItem(new ItemStack(Material.TNT, 1));
            }
        }, 5L);
    }
}
