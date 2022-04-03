package me.test.plugin.hello;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class SetSpawnCommand implements CommandExecutor {

    public boolean onCommand(
            @Nonnull CommandSender sender,
            @Nonnull Command command,
            @Nonnull String label,
            @Nullable String[] args
    ) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            PersistentDataContainer container = player.getWorld().getPersistentDataContainer();

            int b1X = container.has(new NamespacedKey(Hello.getPlugin(), "b1X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1X"), PersistentDataType.INTEGER) : 0;
            int b1Y = container.has(new NamespacedKey(Hello.getPlugin(), "b1Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1Y"), PersistentDataType.INTEGER) : 0;
            int b1Z = container.has(new NamespacedKey(Hello.getPlugin(), "b1Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b1Z"), PersistentDataType.INTEGER) : 0;
            int b2X = container.has(new NamespacedKey(Hello.getPlugin(), "b2X"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2X"), PersistentDataType.INTEGER) : 0;
            int b2Y = container.has(new NamespacedKey(Hello.getPlugin(), "b2Y"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2Y"), PersistentDataType.INTEGER) : 0;
            int b2Z = container.has(new NamespacedKey(Hello.getPlugin(), "b2Z"), PersistentDataType.INTEGER) ?
                    container.get(new NamespacedKey(Hello.getPlugin(), "b2Z"), PersistentDataType.INTEGER) : 0;

            if (b1Y != b2Y) {
                return false;
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
                                    ((Math.abs(j - b1Z)%2) == 0) && ((Math.abs(i - b2X)%2) == 0)) {
                                player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                                player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                                player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                            } else {
                                boolean condition1 = (Math.abs(i - b2X) == 1 || Math.abs(i - b2X) == 2);
                                boolean condition2 = (Math.abs(j - b1Z) == 1 || Math.abs(j - b1Z) == 2);
                                boolean condition3 = (i == b1X - 1 || i == b1X - 2);
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
                                    ((Math.abs(j - b2Z)%2) == 0) && ((Math.abs(i - b2X)%2) == 0)) {
                                player.getWorld().getBlockAt(i, b1Y+1, j).setType(Material.BEDROCK);
                                player.getWorld().getBlockAt(i, b1Y+2, j).setType(Material.BEDROCK);
                                player.getWorld().getBlockAt(i, b1Y+3, j).setType(Material.BEDROCK);
                            } else {
                                boolean condition1 = (Math.abs(i - b2X) == 1 || Math.abs(i - b2X) == 2);
                                boolean condition2 = (Math.abs(j - b2Z) == 1 || Math.abs(j - b2Z) == 2);
                                boolean condition3 = (i == b1X - 1 || i == b1X - 2);
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
            }

            for (Player p : Hello.getPlugin().getServer().getOnlinePlayers()) {
//                p.teleport(new Location(player.getWorld(), mid(b1X, b2X), b1Y + 3, mid(b1Z, b2Z)));
            }

            Hello.getPlugin().getServer().getScheduler().runTaskTimer(Hello.getPlugin(), new Runnable()
            {
                int time = 3; //or any other number you want to start countdown from

                @Override
                public void run()
                {
                    if (this.time == 0)
                    {
                        //test
                    }

                    for (final Player player : Hello.getPlugin().getServer().getOnlinePlayers())
                    {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.time + " second(s) remains!"));
                    }

                    this.time--;
                }
            }, 0L, 20L);
        }

        return false;
    }

    private static int mid(int x, int y) {
        return x/2 + y/2 + (x%2 + y%2)/2;
    }
}
