package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import net.autoignition.cache.BenchCache;
import net.autoignition.util.AutoIgnitionBlockUtil;
import net.autoignition.util.AutoIgnitionChunkUtil;
import net.autoignition.util.AutoIgnitionMathUtil;

/**
 * Provides spatial scanning logic to identify neighboring storage containers.
 */
public class NeighborScanner {

    /** Cardinal horizontal offsets for neighbor checking */
    private static final Vector3i[] HORIZONTAL_OFFSETS = {
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1)
    };

    /**
     * Entry point for scanning containers around a specific bench.
     * It clears the existing cache and performs a fresh scan of all relevant blocks.
     */
    public static void scan(World world, Vector3i position, BenchCache cache, ProcessingBenchBlock bench) {
        cache.updateScanTime();
        cache.getContainerPositions().clear();

        scanAt(world, position, cache);

        WorldChunk benchChunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, position);
        if (benchChunk == null) return;

        BlockType blockType = benchChunk.getBlockType(position.x, position.y, position.z);
        if (blockType != null && AutoIgnitionBlockUtil.isMultiBlock(blockType)) {
            int rotationIndex = benchChunk.getRotationIndex(position.x, position.y, position.z);
            Vector3i offset = AutoIgnitionMathUtil.getSecondBenchBlockOffset(rotationIndex);
            if (offset != null) {
                Vector3i secondPartPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);

                WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, secondPartPosition);
                if (chunk != null && isSameBenchType(bench, world, secondPartPosition)) {
                    scanAt(world, secondPartPosition, cache);
                }
            }
        }
    }

    /**
     * Verifies if a block at a given position is part of the same bench type.
     */
    private static boolean isSameBenchType(ProcessingBenchBlock bench, World world, Vector3i position) {
        ProcessingBenchBlock neighborBench = BlockModule.getComponent(ProcessingBenchBlock.getComponentType(), world, position.x, position.y, position.z);
        if (neighborBench == null) return false;
        return neighborBench.getBench().getId().equals(bench.getBench().getId());
    }

    /**
     * Scans all 4 cardinal directions around a specific position for valid containers.
     */
    private static void scanAt(World world, Vector3i position, BenchCache cache) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);

            ItemContainerBlock icBlock = BlockModule.getComponent(BlockModule.get().getItemContainerBlockComponentType(), world, neighborPosition.x, neighborPosition.y, neighborPosition.z);
            if (icBlock != null) {
                if (!cache.getContainerPositions().contains(neighborPosition)) {
                    cache.getContainerPositions().add(neighborPosition);
                }

                WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, neighborPosition);
                if (chunk != null) {
                    BlockType neighborType = chunk.getBlockType(neighborPosition.x, neighborPosition.y, neighborPosition.z);
                    if (neighborType != null && neighborType.getId().contains("Large")) {
                        scanSecondChestPart(world, neighborPosition, cache, icBlock);
                    }
                }
            }
        }
    }

    /**
     * Specifically scans for the second part of a multi-block container
     * by checking which neighboring block shares the same ItemContainerBlock instance.
     */
    private static void scanSecondChestPart(World world, Vector3i position, BenchCache cache, ItemContainerBlock originalContainer) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);

            ItemContainerBlock neighborContainer = BlockModule.getComponent(BlockModule.get().getItemContainerBlockComponentType(), world, neighborPosition.x, neighborPosition.y, neighborPosition.z);
            if (neighborContainer != null && neighborContainer == originalContainer) {
                if (!cache.getContainerPositions().contains(neighborPosition)) {
                    cache.getContainerPositions().add(neighborPosition);
                }
            }
        }
    }
}
