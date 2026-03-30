package net.autoignition.system;

import javax.annotation.Nonnull;

import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.ChunkFlag;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import net.autoignition.inventory.BenchProcessor;

/**
 * Main ticking system for AutoIgnition.
 * Orchestrates the logic for every processing bench in loaded chunks.
 */
public class AutoIgnitionSystem extends EntityTickingSystem<ChunkStore> {

    private final ComponentType<ChunkStore, ProcessingBenchBlock> benchComponentType;
    private final ComponentType<ChunkStore, BenchBlock> benchBlockComponentType;
    private final ComponentType<ChunkStore, BlockModule.BlockStateInfo> blockStateInfoComponentType;

    public AutoIgnitionSystem() {
        this.benchComponentType = ProcessingBenchBlock.getComponentType();
        this.benchBlockComponentType = BenchBlock.getComponentType();
        this.blockStateInfoComponentType = BlockModule.BlockStateInfo.getComponentType();
    }

    @Override
    public void tick(
            float dt,
            int entityIndex,
            @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk,
            @Nonnull Store<ChunkStore> store,
            @Nonnull CommandBuffer<ChunkStore> commandBuffer
    ) {
        ProcessingBenchBlock bench = archetypeChunk.getComponent(entityIndex, this.benchComponentType);
        if (bench == null) return;

        BlockModule.BlockStateInfo info = archetypeChunk.getComponent(entityIndex, this.blockStateInfoComponentType);
        if (info == null) return;

        BenchBlock benchBlock = archetypeChunk.getComponent(entityIndex, this.benchBlockComponentType);
        if (benchBlock == null) return;

        Ref<ChunkStore> chunkRef = info.getChunkRef();
        WorldChunk chunk = chunkRef.getStore().getComponent(chunkRef, WorldChunk.getComponentType());
        if (chunk == null) return;

        if (!chunk.is(ChunkFlag.TICKING)) return;

        BenchProcessor.handle(bench, benchBlock, info, chunk);
    }

    @Override
    public Query<ChunkStore> getQuery() {
        return this.benchComponentType;
    }
}
