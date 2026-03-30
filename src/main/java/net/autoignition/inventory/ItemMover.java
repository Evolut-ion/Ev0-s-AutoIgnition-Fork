package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.world.World;

import net.autoignition.AutoIgnitionMod;
import net.autoignition.cache.BenchCache;

/**
 * Handles the physical movement of items between benches and external storage.
 * Manages both refueling logic and output extraction by iterating through
 * discovered nearby containers.
 */
public class ItemMover {

    /**
     * Attempts to refill the bench's fuel slot from cached nearby containers.
     * If no fuel is found externally, it attempts to recycle fuel from its own output.
     */
    public static void refillFuel(ProcessingBenchBlock bench, World world, BenchCache cache) {
        ItemContainer fuelContainer = bench.getFuelContainer();
        ItemContainer outputContainer = bench.getOutputContainer();

        if (!fuelContainer.isEmpty()) return;

        for (Vector3i position : cache.getContainerPositions()) {
            ItemContainerBlock icBlock = BlockModule.getComponent(BlockModule.get().getItemContainerBlockComponentType(), world, position.x, position.y, position.z);
            if (icBlock != null) {
                ItemContainer chestContainer = icBlock.getItemContainer();
                transferFuel(chestContainer, fuelContainer);
                if (!fuelContainer.isEmpty()) return;
            }
        }

        transferFuel(outputContainer, fuelContainer);
    }

    /**
     * Internal logic for moving fuel items from a source to a destination.
     */
    private static void transferFuel(ItemContainer source, ItemContainer destination) {
        if (source == null || source.isEmpty() || destination == null || destination.getCapacity() == 0) return;

        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);

            if (itemStack == null || itemStack.getItem().getFuelQuality() <= 0) continue;
            if (AutoIgnitionMod.getConfig().getBlacklistedFuelItems().contains(itemStack.getItemId())) continue;

            if (destination.canAddItemStack(itemStack)) {
                source.moveItemStackFromSlot(i, destination);
            }

            if (!destination.isEmpty()) return;
        }
    }

    /**
     * Automatically clears the bench's output slots and transfers contents to nearby storage.
     */
    public static void emptyOutput(ProcessingBenchBlock bench, World world, BenchCache cache) {
        ItemContainer outputContainer = bench.getOutputContainer();

        if (outputContainer.isEmpty()) return;

        for (Vector3i position : cache.getContainerPositions()) {
            ItemContainerBlock icBlock = BlockModule.getComponent(BlockModule.get().getItemContainerBlockComponentType(), world, position.x, position.y, position.z);
            if (icBlock != null) {
                ItemContainer chestContainer = icBlock.getItemContainer();
                transferOutput(outputContainer, chestContainer);
                if (outputContainer.isEmpty()) break;
            }
        }
    }

    /**
     * Internal logic for moving output items to external storage.
     */
    private static void transferOutput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);

            if (itemStack != null) {
                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }

    /**
     * Automatically fills the bench's input slots from nearby storage.
     */
    public static void refillInput(ProcessingBenchBlock bench, World world, BenchCache cache) {
        ItemContainer inputContainer = bench.getInputContainer();

        for (Vector3i position : cache.getContainerPositions()) {
            ItemContainerBlock icBlock = BlockModule.getComponent(BlockModule.get().getItemContainerBlockComponentType(), world, position.x, position.y, position.z);
            if (icBlock != null) {
                ItemContainer chestContainer = icBlock.getItemContainer();
                transferInput(chestContainer, inputContainer);
            }
        }
    }

    /**
     * Internal logic for moving input items from external storage.
     */
    private static void transferInput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);
            if (itemStack != null) {
                if (AutoIgnitionMod.getConfig().getBlacklistedInputItems().contains(itemStack.getItemId())) continue;

                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }
}
