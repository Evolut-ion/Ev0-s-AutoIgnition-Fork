package net.autoignition.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.ChunkFlag;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nullable;

/**
 * Utility class to handle thread-safe chunk access.
 */
public class AutoIgnitionChunkUtil {

    /**
     * Retrieves a WorldChunk only if it is fully loaded and ready for ticking.
     * @param world The world instance.
     * @param position The block position to check.
     * @return The {@link WorldChunk} instance if safe to use, null otherwise.
     */
    @Nullable
    public static WorldChunk getSafeChunkFromBlock(World world, Vector3i position) {
        ChunkStore chunkStore = world.getChunkStore();
        long index = ChunkUtil.indexChunkFromBlock(position.x, position.z);

        Ref<ChunkStore> ref = chunkStore.getChunkReference(index);

        if (ref == null || !ref.isValid()) return null;

        WorldChunk chunk = ref.getStore().getComponent(ref, WorldChunk.getComponentType());

        if (chunk == null || !chunk.is(ChunkFlag.TICKING)) return null;

        return chunk;
    }

}
