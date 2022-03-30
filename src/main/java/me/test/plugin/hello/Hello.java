package me.test.plugin.hello;

import org.bukkit.plugin.java.JavaPlugin;

public final class Hello extends JavaPlugin {

    private static Hello plugin;

    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("fly").setExecutor(new CommandTest(this));
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getServer().getPluginManager().registerEvents(new Event(), this);
        getLogger().info("onEnable has been invoked!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("onDisable has been invoked!");
    }

    public static Hello getPlugin() {
        return plugin;
    }
}
