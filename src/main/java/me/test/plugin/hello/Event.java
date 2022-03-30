package me.test.plugin.hello;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Event implements Listener {

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

            if (action == Action.RIGHT_CLICK_BLOCK) {
                player.sendMessage(ChatColor.GREEN + "You've right clicked while holding the Magic Pickaxe.");

                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b1X"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockX()
                );
                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b1Y"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockY()
                );
                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b1Z"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockZ()
                );
            } else if (action == Action.LEFT_CLICK_BLOCK) {
                player.sendMessage(ChatColor.RED + "You've right clicked while holding the Magic Pickaxe.");

                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b2X"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockX()
                );
                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b2Y"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockY()
                );
                container.set(
                        new NamespacedKey(Hello.getPlugin(), "b2Z"),
                        PersistentDataType.INTEGER,
                        blockLoc.getBlockZ()
                );
            }

            int b1X = container.has(new NamespacedKey(Hello.getPlugin(), "b1X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1X"), PersistentDataType.INTEGER) : 0;
            int b1Y = container.has(new NamespacedKey(Hello.getPlugin(), "b1Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1Y"), PersistentDataType.INTEGER): 0 ;
            int b1Z = container.has(new NamespacedKey(Hello.getPlugin(), "b1Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1Z"), PersistentDataType.INTEGER) : 0;
            int b2X = container.has(new NamespacedKey(Hello.getPlugin(), "b2X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2X"), PersistentDataType.INTEGER) : 0;
            int b2Y = container.has(new NamespacedKey(Hello.getPlugin(), "b2Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2Y"), PersistentDataType.INTEGER) : 0;
            int b2Z = container.has(new NamespacedKey(Hello.getPlugin(), "b2Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2Z"), PersistentDataType.INTEGER) : 0;

            player.sendMessage("b1X : " + b1X + " | b1Y : " + b1Y + " | b1Z : " + b1Z);
            player.sendMessage("b2X : " + b2X + " | b2Y : " + b2Y + " | b2Z : " + b2Z);
        }
    }
}