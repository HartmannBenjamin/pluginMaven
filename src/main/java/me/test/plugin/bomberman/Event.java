package me.test.plugin.bomberman;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Event implements Listener {

    @EventHandler
    public void onEntityPickUpItemEvent(EntityPickupItemEvent event) {
        if (BomberMan.isGame()) {
            event.setCancelled(true);
            event.getItem().remove();
            Player p = (Player) event.getEntity();

            switch (event.getItem().getItemStack().getType()) {
                case DIRT:
                    BomberMan.setPlayerBombPower(p, BomberMan.getPlayerBombPower(p) + event.getItem().getItemStack().getAmount());
                    break;
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (BomberMan.isGame()) {
            if (e.getEntity() instanceof Player) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                    Player p = (Player) e.getEntity();
                    Location loc = p.getLocation();
                    Player killer = BomberMan.getKiller(loc);

                    if (p.getName().equals(killer.getName())) {
                        Main.getPlugin().getServer().broadcastMessage(ChatColor.RED + p.getName() + " s'est tué(e) tout seul :/");
                    } else {
                        Main.getPlugin().getServer().broadcastMessage(ChatColor.RED + p.getName() + " a été tué(e) par "
                                + ChatColor.BOLD + killer.getName());
                    }

                    p.setHealth(0);
                    BomberMan.removePlayer(p);
                }
            }
        }
    }

    @EventHandler
    public void fireDamage(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

    @EventHandler
    public void onGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        if (BomberMan.isGame()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (BomberMan.isGame()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (BomberMan.isGame()) {
            Block tnt = event.getBlockPlaced();
            if (tnt.getType() == Material.TNT) {
                if(tnt.getLocation().add(0, -1, 0).getBlock().getType() == Material.BEDROCK) {
                    tnt.getLocation().add(0, 1, 0).getBlock().setType(Material.BARRIER);
                    BomberMan.explosion(tnt.getLocation(), new TntGame(tnt.getLocation(), event.getPlayer()), 30);
                } else {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
            event.setCancelled(true);

            if (event.getClickedBlock() == null) {
                return;
            }

            Location blockLoc = event.getClickedBlock().getLocation();
            PersistentDataContainer container = player.getWorld().getPersistentDataContainer();

            int b1X = container.has(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b1X"), PersistentDataType.INTEGER) : 0;
            int b1Y = container.has(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Main.getPlugin(), "b1Y"), PersistentDataType.INTEGER) : 0;
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

                if ((Math.abs(blockLoc.getBlockX() - b2X) % 2 != 0) || (Math.abs(blockLoc.getBlockZ() - b2Z) % 2 != 0)) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions");
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b2X) % 2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b2Z) % 2);
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

                if (Math.abs(blockLoc.getBlockX() - b1X) % 2 != 0 || Math.abs(blockLoc.getBlockZ() - b1Z) % 2 != 0) {
                    player.sendMessage(ChatColor.RED + "Pas les bonnes dimensions X:"
                            + Math.abs(blockLoc.getBlockX() - b1X) % 2 + " Z: " + Math.abs(blockLoc.getBlockZ() - b1Z) % 2);
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