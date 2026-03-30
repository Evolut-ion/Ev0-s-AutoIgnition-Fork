package net.autoignition;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import net.autoignition.cache.BenchCacheManager;
import net.autoignition.command.AutoIgnitionCommand;
import net.autoignition.config.AutoIgnitionConfig;
import net.autoignition.system.AutoIgnitionSystem;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class AutoIgnitionMod extends JavaPlugin {

    private static AutoIgnitionMod INSTANCE;
    private static Config<AutoIgnitionConfig> configContainer;
    private static AutoIgnitionConfig config;

    public AutoIgnitionMod(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
        configContainer = this.withConfig("AutoIgnition", AutoIgnitionConfig.CODEC);
    }

    @Override
    public void setup() {
        super.setup();
        log(Level.INFO, "Starting plugin setup...");

        loadConfig();
        registerCommands();
        registerSystems();

        registerTasks();

        log(Level.INFO, "Setup complete.");
    }

    private void log(Level level, String msg) {
        this.getLogger().at(level).log(msg);
    }

    /**
     * Loads and validates the configuration file.
     */
    private void loadConfig() {
        this.getLogger().at(Level.INFO).log("[Setup] -> Loading configuration...");
        try {
            config = configContainer.get();
            configContainer.save();
            log(Level.INFO, "Configuration loaded.");
        } catch (Exception e) {
            log(Level.SEVERE, "Failed to load config: " + e.getMessage());
        }
    }

    /**
     * Registers the plugin's commands.
     */
    private void registerCommands() {
        try {
            this.getCommandRegistry().registerCommand(new AutoIgnitionCommand());
        } catch (Exception e) {
            log(Level.SEVERE, "Could not register command: " + e.getMessage());
        }
    }

    /**
     * Registers the different systems.
     */
    private void registerSystems() {
        this.getChunkStoreRegistry().registerSystem(new AutoIgnitionSystem());
    }

    /**
     * Registers the different tasks.
     */
    private void registerTasks() {
        try {
            HytaleServer.SCHEDULED_EXECUTOR.scheduleWithFixedDelay(
                    BenchCacheManager::cleanOldCaches,
                    10, 10, TimeUnit.MINUTES
            );
        } catch (Exception e) {
            log(Level.SEVERE, "Task registration failed: " + e.getMessage());
        }
    }

    /**
     * Reloads the configuration file and propagates changes.
     * @return true if reload was successful, false otherwise.
     */
    public boolean reloadConfig() {
        try {
            configContainer.load().thenAccept(loadedConfig -> {
                config = loadedConfig;
                log(Level.INFO, "Config reloaded.");
            }).exceptionally(e -> {
                log(Level.SEVERE, "Reload failed: " + e.getMessage());
                return null;
            });

            return true;
        } catch (Exception e) {
            log(Level.SEVERE, "Error initiating reload: " + e.getMessage());
            return false;
        }
    }

    public void saveConfig() {
        configContainer.save();
    }

    public static AutoIgnitionMod getInstance() { return INSTANCE; }

    public static AutoIgnitionConfig getConfig() { return config; }
}
