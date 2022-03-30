package me.test.plugin.hello;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandTest implements CommandExecutor {

    private final Hello hello;

    public CommandTest(Hello hello) {
        this.hello = hello;
    }

    public boolean onCommand(
            @Nonnull CommandSender sender,
            @Nonnull Command command,
            @Nonnull String label,
            @Nullable String[] args
    ) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.setAllowFlight(!player.getAllowFlight());

        } else {
            sender.sendMessage("Allow Flight : " + hello.getServer().getAllowFlight());
            return false;
        }

        return false;
    }
}
