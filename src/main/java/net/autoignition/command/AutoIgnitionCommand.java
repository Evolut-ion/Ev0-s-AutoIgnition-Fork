package net.autoignition.command;

import com.hypixel.hytale.common.plugin.AuthorInfo;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.common.semver.SemverRange;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.autoignition.AutoIgnitionMod;
import net.autoignition.command.subcommand.autoignition.HelpSubCommand;
import net.autoignition.command.subcommand.autoignition.ReloadConfigSubCommand;
import net.autoignition.command.subcommand.autoignition.SettingsSubCommand;
import net.autoignition.command.subcommand.autoignition.TargetIdSubCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class AutoIgnitionCommand extends AbstractCommand {
    public AutoIgnitionCommand() {
        super("autoignition", "Main command for AutoIgnition");
        this.addSubCommand(new HelpSubCommand());
        this.addSubCommand(new ReloadConfigSubCommand());
        this.addSubCommand(new SettingsSubCommand());
        this.addSubCommand(new TargetIdSubCommand());
    }

    @Override
    @Nullable
    protected String generatePermissionNode() { return "autoignition.commands.autoignition"; }


    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();
        AutoIgnitionMod mod = AutoIgnitionMod.getInstance();
        PluginManifest manifest = mod.getManifest();
        HytaleLogger logger = mod.getLogger();

        String modName = manifest.getName();
        String modVersion = manifest.getVersion().toString(); // TODO: Add a check to see if a new version is available
        String modDescription = manifest.getDescription();
        String modAuthors = manifest.getAuthors().stream()
                .map(AuthorInfo::getName)
                .collect(Collectors.joining(", "));
        String modWebsite = manifest.getWebsite();
        String modServerVersion = manifest.getServerVersion();

        String infoMessage = """
            
            ====================
            %s v%s
            - Author(s): %s
            - Description: %s
            - Website: %s
            - Server Version(s): %s
            - Use /autoignition help for commands.
            ====================
            """.formatted(
                modName,
                modVersion,
                modAuthors,
                modDescription,
                modWebsite,
                modServerVersion
        );

        if (sender instanceof Player player) {
            player.sendMessage(Message.raw(infoMessage));
        } else {
            logger.at(Level.INFO).log(infoMessage);
        }

        return CompletableFuture.completedFuture(null);
    }
}
