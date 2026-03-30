package net.autoignition.command.subcommand.autoignition;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.autoignition.AutoIgnitionMod;
import net.autoignition.pages.SettingsPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SettingsSubCommand extends AbstractPlayerCommand {
    public SettingsSubCommand() {
        super("settings", "Change mod configuration");
        this.addAliases("config", "c", "s");
    }

    @Override
    @Nullable
    protected String generatePermissionNode() {
        return "autoignition.commands.autoignition.settings";
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CommandSender sender = commandContext.sender();
        AutoIgnitionMod mod = AutoIgnitionMod.getInstance();
        HytaleLogger logger = mod.getLogger();

        if (sender instanceof Player player) {
            SettingsPage settingsPage = new SettingsPage(playerRef, AutoIgnitionMod.getConfig());
            player.getPageManager().openCustomPage(ref, store, settingsPage);
        } else {
            logger.at(Level.INFO).log("Only players can use this command.");
        }
    }
}