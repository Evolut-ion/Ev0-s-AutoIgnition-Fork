package net.autoignition.command.subcommand.autoignition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;

import net.autoignition.util.AutoIgnitionChunkUtil;

public class TargetIdSubCommand extends AbstractPlayerCommand {
    private static final int TARGET_RANGE = 3;

    public TargetIdSubCommand() {
        super("benchid", "Sends the target bench id");
        this.addAliases("targetbench", "target", "targetbenchid", "id", "targetid");
    }

    @Override
    @Nullable
    protected String generatePermissionNode() {
        return "autoignition.commands.autoignition.targetid";
    }

    @Override
    protected void execute(@Nonnull CommandContext context,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {

        if (!(context.sender() instanceof Player player)) {
            return;
        }

        Ref<EntityStore> entStore = player.getReference();
        if (entStore == null) {
            sendError(player, "Unable to retrieve player data.");
            return;
        }

        Vector3i targetedBlock = TargetUtil.getTargetBlock(entStore, TARGET_RANGE, entStore.getStore());
        if (targetedBlock == null) {
            sendError(player, "No blocks are targeted within a radius of " + TARGET_RANGE);
            return;
        }

        WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, targetedBlock);
        if (chunk == null) {
            sendError(player, "The targeted chunk is not loaded.");
            return;
        }

        ProcessingBenchBlock benchBlock = BlockModule.getComponent(ProcessingBenchBlock.getComponentType(), world, targetedBlock.x, targetedBlock.y, targetedBlock.z);

        if (benchBlock != null) {
            String blockId = chunk.getBlockType(targetedBlock.x, targetedBlock.y, targetedBlock.z).getId();
            player.sendMessage(Message.raw("ID : " + blockId));
        } else {
            sendError(player, "This block is not a crafting bench (ProcessingBench).");
        }
    }

    private void sendError(Player player, String error) {
        player.sendMessage(Message.raw("[Error] > " + error));
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(Message.raw(message));
    }
}