package net.autoignition.util;

import com.hypixel.hytale.math.vector.Vector3i;

/**
 * Utility class for spatial mathematics and coordinate transformations.
 */
public class AutoIgnitionMathUtil {

    /**
     * Calculates the offset for the secondary part of a multi-block bench.
     * @param rotationIndex The Hytale rotation index (0: South, 1: East, 2: North, 3: West).
     * @return A {@link Vector3i} representing the relative offset, or null if the index is invalid.
     */
    public static Vector3i getSecondBenchBlockOffset(int rotationIndex) {
        return switch (rotationIndex) {
            case 0 -> new Vector3i(-1, 0, 0);
            case 1 -> new Vector3i(0, 0, 1);
            case 2 -> new Vector3i(1, 0, 0);
            case 3 -> new Vector3i(0, 0, -1);
            default -> null;
        };
    }

    /**
     * Computes a new world position by adding an offset to a base position.
     * @param base The starting world position (the master block).
     * @param offset The relative displacement to apply.
     * @return A new {@link Vector3i} representing the calculated target position.
     */
    public static Vector3i getRelativePosition(Vector3i base, Vector3i offset) {
        return new Vector3i(
                base.x + offset.x,
                base.y + offset.y,
                base.z + offset.z
        );
    }

}
