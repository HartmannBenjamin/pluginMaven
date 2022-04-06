package me.test.plugin.bomberman;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BomberMan {
    private final Player sender;
    private final MapBuilder mapBuilder;
    private static boolean is_game = false;

    public BomberMan(Player player) {
        this.sender = player;
        this.mapBuilder = new MapBuilder(player.getWorld());
    }

    public static boolean isGame() {
        return is_game;
    }

    public static void setGame(boolean is_game) {
        BomberMan.is_game = is_game;
    }

    public void startGame() {
        if (!mapBuilder.buildProcess()) {
            sender.sendMessage("Mauvaises configurations de map");
        }

        ArrayList<Location> spawnLocations = mapBuilder.getSpawnLocations();
        PlayerService.startProcess(spawnLocations);

        new BukkitRunnable()
        {
            int time = 3;

            @Override
            public void run()
            {
                if (time > 0) {
                    for (final Player player : Main.getPlugin().getServer().getOnlinePlayers())
                    {
                        setGame(true);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.time + " second(s) remains!"));
                    }
                } else {
                    for (final Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "C'est parti!"));
                        player.getInventory().addItem(new ItemStack(Material.TNT, 1));
                    }

                    this.cancel();
                }

                this.time--;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L);
    }

    public static void endGame(String WinnerName) {
        setGame(false);
        Main.getPlugin().getServer().broadcastMessage(WinnerName + " a gagné!");
        PlayerService.endProcess();
    }
}
