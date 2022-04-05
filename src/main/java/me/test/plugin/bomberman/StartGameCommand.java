package me.test.plugin.bomberman;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StartGameCommand implements CommandExecutor {

    public boolean onCommand(
            @Nonnull CommandSender sender,
            @Nonnull Command command,
            @Nonnull String label,
            @Nullable String[] args
    ) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);

            BomberMan bomberMan = new BomberMan(player);
            bomberMan.generateMap();
            bomberMan.startGame();
        }

        return false;
    }
}
