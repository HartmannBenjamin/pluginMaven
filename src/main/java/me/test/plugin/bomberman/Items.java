package me.test.plugin.bomberman;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Items {
    public static int getPlayerBombPower(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        return container.has(new NamespacedKey(Main.getPlugin(), "power"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "power"), PersistentDataType.INTEGER) : 1;
    }

    public static void setPlayerBombPower(Player p, int power) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        container.set(
                new NamespacedKey(Main.getPlugin(), "power"),
                PersistentDataType.INTEGER,
                power
        );

        Scoreboard.set(p, power, getPlayerSpeed(p), getPlayerBombNumber(p));
    }

    public static int getPlayerSpeed(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        return container.has(new NamespacedKey(Main.getPlugin(), "speed"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "speed"), PersistentDataType.INTEGER) : 1;
    }

    public static void setPlayerSpeed(Player p, int speed) {
        if (speed < 15) {
            PersistentDataContainer container = p.getPersistentDataContainer();

            container.set(
                    new NamespacedKey(Main.getPlugin(), "speed"),
                    PersistentDataType.INTEGER,
                    speed
            );

            p.setWalkSpeed(0.15F + (float) speed/20);

            Scoreboard.set(p, getPlayerBombPower(p), speed, getPlayerBombNumber(p));
        }
    }

    public static int getPlayerBombNumber(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        return container.has(new NamespacedKey(Main.getPlugin(), "number"), PersistentDataType.INTEGER) ?
                container.get(new NamespacedKey(Main.getPlugin(), "number"), PersistentDataType.INTEGER) : 1;
    }

    public static void setPlayerBombNumber(Player p, int number) {
        PersistentDataContainer container = p.getPersistentDataContainer();

        container.set(
                new NamespacedKey(Main.getPlugin(), "number"),
                PersistentDataType.INTEGER,
                number
        );

        Scoreboard.set(p, getPlayerBombPower(p), getPlayerSpeed(p), number);
    }

    public static void pickUpItem(Material material, int amount, Player p) {
        switch (material) {
            case FIREWORK_ROCKET:
                Items.setPlayerBombPower(p, Items.getPlayerBombPower(p) + amount);
                break;
            case FEATHER:
                Items.setPlayerSpeed(p, Items.getPlayerSpeed(p) + amount);
                break;
            case REDSTONE:
                p.getInventory().addItem(new ItemStack(Material.TNT, amount));
                Items.setPlayerBombNumber(p, Items.getPlayerBombNumber(p) + amount);
                break;
        }
    }
}
