package net.autoignition.command.subcommand.autoignition;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.autoignition.AutoIgnitionMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class HelpSubCommand extends AbstractAsyncCommand {
    public HelpSubCommand() {
        super("help", "Shows all available commands for AutoIgnition");
        this.addAliases("h");
    }

    @Override
    @Nullable
    protected String generatePermissionNode() {
        return "autoignition.commands.autoignition.help";
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();
        AutoIgnitionMod mod = AutoIgnitionMod.getInstance();
        HytaleLogger logger = mod.getLogger();

        String helpMessage = """
                ====================
                HELP - AutoIgnition
                ====================
                /autoignition -> Show mod info.
                /autoignition help -> Show this message.
                /autoignition reload -> Reload the mod configuration.
                /autoignition settings -> Change mod configuration.
                /autoignition benchid -> Give the id of the targeted bench.
                ====================
                """;

        if (sender instanceof Player player) {
            player.sendMessage(Message.raw(helpMessage));
        } else {
            logger.at(Level.INFO).log(helpMessage);
        }

        return CompletableFuture.completedFuture(null);
    }
}