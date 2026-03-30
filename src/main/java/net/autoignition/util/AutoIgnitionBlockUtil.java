package net.autoignition.util;

import com.hypixel.hytale.server.core.asset.type.blockhitbox.BlockBoundingBoxes;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;

/**
 * Utility class for block-related checks and property identification.
 */
public class AutoIgnitionBlockUtil {

    /**
     * Determines if a bench is a multi-block structure by inspecting its hitboxes.
     * This allows for automatic compatibility with any modded bench without hardcoding IDs.
     * @param blockType The block type to check.
     * @return true if the block's hitbox width or depth exceeds a standard 1x1 tile (1.1 threshold).
     */
    public static boolean isMultiBlock(BlockType blockType) {
        if (blockType == null) return false;

        var hitboxAssetMap = BlockBoundingBoxes.getAssetMap();
        var hitboxAsset = hitboxAssetMap.getAsset(blockType.getHitboxTypeIndex());

        if (hitboxAsset != null) {
            var box = hitboxAsset.get(0).getBoundingBox();
            return box.width() > 1.1 || box.depth() > 1.1;
        }
        return false;
    }

}
