package me.test.plugin.bomberman;

import me.test.plugin.bomberman.game.commands.StartGameCommand;
import me.test.plugin.bomberman.game.events.Event;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("startgame").setExecutor(new StartGameCommand());
        getServer().getPluginManager().registerEvents(new Event(), this);
        getLogger().info("onEnable has been invoked!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("onDisable has been invoked!");
    }

    public static Main getPlugin() {
        return plugin;
    }
}
