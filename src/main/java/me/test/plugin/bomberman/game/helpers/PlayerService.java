package me.test.plugin.bomberman.game.helpers;

import me.test.plugin.bomberman.game.BomberMan;
import me.test.plugin.bomberman.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerService {
    private static final ArrayList<Player> remainingPlayers = new ArrayList<>();

    public static Player getKiller(Location location) {
        AtomicReference<Player> playerKiller = new AtomicReference<>();
        ArrayList<Location> locations = new ArrayList<>();

        locations.add(location);
        locations.add(location.getBlock().getLocation().add(1, 0, 0));
        locations.add(location.getBlock().getLocation().add(1, 0, 1));
        locations.add(location.getBlock().getLocation().add(0, 0, 1));
        locations.add(location.getBlock().getLocation().add(-1, 0, 0));
        locations.add(location.getBlock().getLocation().add(-1, 0, 0));
        locations.add(location.getBlock().getLocation().add(-1, 0, -1));
        locations.add(location.getBlock().getLocation().add(0, 0, -1));

        while (locations.size() > 0) {
            Location currentLoc = locations.get(0);

            TntService.getAllTNTFire().forEach((tntGame) -> tntGame.getBlocks().forEach((fire) -> {
                if (fire.getBlockX() == currentLoc.getBlockX() && fire.getBlockZ() ==  currentLoc.getBlockZ()) {
                    playerKiller.set(tntGame.getCreator());
                }
            }));

            if (playerKiller.get() != null) {
                return playerKiller.get();
            }

            locations.remove(0);
        }

        return null;
    }

    public static void removePlayer(Player player) {
        remainingPlayers.remove(player);
        player.setGameMode(GameMode.SPECTATOR);

        for (final Player p : remainingPlayers) {
            Main.getPlugin().getServer().broadcastMessage("remaining: " +  p);
        }

        if (remainingPlayers.size() < 2) {
            BomberMan.endGame(remainingPlayers.get(0).getName());
        }
    }

    public static void startProcess(ArrayList<Location> spawnLocations) {
        for (final Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
            if (spawnLocations.size() > 0) {
                p.teleport(spawnLocations.get(0));
                spawnLocations.remove(0);
            }

            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
            p.setWalkSpeed(0.2F);
            remainingPlayers.add(p);
            Items.setPlayerBombPower(p, 1);
            Items.setPlayerSpeed(p, 1);
            Items.setPlayerBombNumber(p, 1);
            Scoreboard.set(p, 1, 1, 1);
        }
    }

    public static void endProcess() {
        for (final Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
            p.setGameMode(GameMode.CREATIVE);
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.getInventory().clear();
            p.setWalkSpeed(0.2F);
        }
    }

    public static void playTntSound(Location location) {
        for (final Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
            p.playSound(location, Sound.ENTITY_TNT_PRIMED, 1, 1);
        }
    }

    public static void playSoundPickup(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

    public static void playTntExplosionSound(Location location) {
        for (final Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
            p.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
        }
    }

    public static boolean isAlive(Player player) {
        return remainingPlayers.contains(player);
    }

    public static void kill(Player p, Location loc) {
        Player killer = getKiller(loc);

        if (p.getName().equals(killer.getName())) {
            Main.getPlugin().getServer().broadcastMessage(ChatColor.RED + p.getName() + " s'est tué(e) tout seul :/");
        } else {
            Main.getPlugin().getServer().broadcastMessage(ChatColor.RED + p.getName() + " a été tué(e) par "
                    + ChatColor.BOLD + killer.getName());
        }

        p.setHealth(0);
        removePlayer(p);
    }
}
