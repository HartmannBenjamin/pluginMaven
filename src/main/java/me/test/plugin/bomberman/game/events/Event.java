package me.test.plugin.bomberman.game.events;

import me.test.plugin.bomberman.game.BomberMan;
import me.test.plugin.bomberman.game.helpers.PlayerService;
import me.test.plugin.bomberman.game.helpers.MapBuilder;
import me.test.plugin.bomberman.game.helpers.Items;
import me.test.plugin.bomberman.game.helpers.TntService;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Event implements Listener {

    @EventHandler
    public void onEntityPickUpItemEvent(EntityPickupItemEvent event) {
        if (BomberMan.isGame()) {
            event.setCancelled(true);
            event.getItem().remove();

            ItemStack itemStack = event.getItem().getItemStack();
            Items.pickUpItem(itemStack.getType(), itemStack.getAmount(), (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (BomberMan.isGame()) {
            if (e.getEntity() instanceof Player) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                    Player p = (Player) e.getEntity();
                    Location loc = p.getLocation();

                    PlayerService.kill(p, loc);
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
        if (BomberMan.isGame() && PlayerService.isAlive(event.getPlayer())) {
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
            event.setCancelled(!TntService.placeBomb(event.getBlockPlaced(), event.getPlayer()));
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

            MapBuilder mapBuilder = new MapBuilder(player.getWorld());
            mapBuilder.setMapDimension(event.getClickedBlock(), action, player);
        }
    }
}