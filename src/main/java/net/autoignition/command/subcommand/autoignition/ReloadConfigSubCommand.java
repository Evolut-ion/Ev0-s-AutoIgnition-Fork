package net.autoignition.command.subcommand.autoignition;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import net.autoignition.AutoIgnitionMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ReloadConfigSubCommand extends AbstractAsyncCommand {
    public ReloadConfigSubCommand() {
        super("reload", "Reload the mod config");
        this.addAliases("rl");
    }

    @Override
    @Nullable
    protected String generatePermissionNode() {
        return "autoignition.commands.autoignition.reload";
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();
        AutoIgnitionMod mod = AutoIgnitionMod.getInstance();

        boolean success = mod.reloadConfig();

        String feedback = success ? "Config reloaded successfully!" : "Config reload failed, check console.";

        if (sender instanceof Player player) {
            player.sendMessage(Message.raw(feedback));
        }

        return CompletableFuture.completedFuture(null);
    }
}
