package jp.evolvegame.core;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProjectEvolvePlugin extends JavaPlugin {
    private MatchManager matchManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.matchManager = new MatchManager(this);

        EvolveCommand command = new EvolveCommand(this, matchManager);
        PluginCommand evolve = getCommand("evolve");
        if (evolve != null) {
            evolve.setExecutor(command);
            evolve.setTabCompleter(command);
        }

        Bukkit.getPluginManager().registerEvents(new PlayerListener(matchManager), this);
        getLogger().info("Project EVOLVE v0.1.0 enabled.");
    }

    @Override
    public void onDisable() {
        if (matchManager != null) {
            matchManager.shutdown();
        }
    }
}
