package net.autoignition.cache;


import com.hypixel.hytale.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores temporary data for a specific processing bench to optimize performance.
 * This cache tracks nearby container positions and manages timing thresholds
 * to prevent expensive operations from running every tick.
 */
public class BenchCache {
    /** List of world positions containing valid storage containers discovered during the last scan. */
    private final List<Vector3i> containerPositions = new ArrayList<>();
    /** Timestamp of the last neighborhood scan in milliseconds. */
    private long lastScanTime;
    /** Timestamp of the last logic execution (logistics and ignition) in milliseconds. */
    private long lastRunTime;

    /**
     * Initializes a new cache.
     * lastRunTime is set to 0 to ensure the logic triggers immediately upon the first check.
     */
    public BenchCache() {
        this.lastScanTime = System.currentTimeMillis();
        this.lastRunTime = System.currentTimeMillis();
    }

    /**
     * Gets the list of cached container positions.
     * @return A list of {@link Vector3i} coordinates.
     */
    public List<Vector3i> getContainerPositions() {
        return containerPositions;
    }

    /** @return The timestamp of the last scan. */
    public long getLastScanTime() { return lastScanTime; }

    /** Updates the scan timestamp to the current system time. */
    public void updateScanTime() { this.lastScanTime = System.currentTimeMillis(); }
    /** Updates the run timestamp to the current system time. */
    public void updateRunTime() { this.lastRunTime = System.currentTimeMillis(); }

    /**
     * Determines if the neighborhood scan has expired based on the provided interval.
     * @param intervalMs The allowed duration between scans in milliseconds.
     * @return true if the time elapsed exceeds the interval.
     */
    public boolean needsRescan(long intervalMs) {
        return (System.currentTimeMillis() - lastScanTime) > intervalMs;
    }

    /**
     * Checks if the bench's automated logic (refueling/transfer) is due for execution.
     * @param intervalMs The allowed duration between runs in milliseconds.
     * @return true if the logic should be executed.
     */
    public boolean needsRerun(long intervalMs) {
        return (System.currentTimeMillis() - lastRunTime) > intervalMs;
    }
}
