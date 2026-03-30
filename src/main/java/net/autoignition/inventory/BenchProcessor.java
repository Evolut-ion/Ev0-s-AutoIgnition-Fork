package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;

import net.autoignition.AutoIgnitionMod;
import net.autoignition.cache.BenchCache;
import net.autoignition.cache.BenchCacheManager;
import net.autoignition.config.AutoIgnitionConfig;

/**
 * Core logic controller for processing benches.
 * Orchestrates timing, inventory logistics, and automatic ignition states
 * for individual blocks in the world.
 */
public class BenchProcessor {

    /**
     * Primary entry point for bench logic.
     * Validates timing thresholds before executing resource management
     * and ignition state updates.
     */
    public static void handle(ProcessingBenchBlock bench, BenchBlock benchBlock, BlockModule.BlockStateInfo info, WorldChunk chunk) {
        int localX = ChunkUtil.xFromIndex(info.getIndex());
        int localY = ChunkUtil.yFromIndex(info.getIndex());
        int localZ = ChunkUtil.zFromIndex(info.getIndex());
        int worldX = ChunkUtil.worldCoordFromLocalCoord(chunk.getX(), localX);
        int worldY = ChunkUtil.MIN_Y + localY;
        int worldZ = ChunkUtil.worldCoordFromLocalCoord(chunk.getZ(), localZ);
        Vector3i position = new Vector3i(worldX, worldY, worldZ);
        World world = chunk.getWorld();

        BlockType blockType = chunk.getBlockType(worldX, worldY, worldZ);
        if (blockType != null && AutoIgnitionMod.getConfig().getBlacklistedProcessorBenches().contains(blockType.getId())) return;

        BenchCache cache = BenchCacheManager.getOrCreate(position, world, bench);

        AutoIgnitionConfig config = AutoIgnitionMod.getConfig();

        if (!cache.needsRerun(config.getUpdateIntervalMs())) return;
        cache.updateRunTime();

        handleLogistics(bench, world, cache);

        boolean hasNoRecipe = (bench.getRecipe() == null);
        if (hasNoRecipe) {
            if (config.isEnableAutoFuelStop()) bench.setActive(false, benchBlock, info);
        } else if (config.isEnableAutoFuelStart()) {
            attemptIgnition(bench, benchBlock, info);
        }
    }

    /**
     * Handles the movement of items between the bench and nearby containers.
     */
    private static void handleLogistics(ProcessingBenchBlock bench, World world, BenchCache cache) {
        AutoIgnitionConfig config = AutoIgnitionMod.getConfig();

        if (config.isEnableAutoRefuel()) ItemMover.refillFuel(bench, world, cache);
        if (config.isEnableInputTransfer()) ItemMover.refillInput(bench, world, cache);
        if (config.isEnableOutputTransfer()) ItemMover.emptyOutput(bench, world, cache);
    }

    /**
     * Analyzes the bench's internal containers to decide if it should be activated.
     */
    private static void attemptIgnition(ProcessingBenchBlock bench, BenchBlock benchBlock, BlockModule.BlockStateInfo info) {
        if (bench.isActive() || bench.getRecipe() == null) return;

        if (!bench.getFuelContainer().isEmpty() && !bench.getInputContainer().isEmpty()) {
            bench.setActive(true, benchBlock, info);
        }
    }
}
