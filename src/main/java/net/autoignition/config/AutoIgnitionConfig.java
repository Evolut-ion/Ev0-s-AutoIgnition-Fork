package net.autoignition.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoIgnitionConfig {

    // CONFIG CODEC
    public static final BuilderCodec<AutoIgnitionConfig> CODEC;


    // CONFIG VALUES
    private long UpdateIntervalMs = 1000L;
    private long ScannerIntervalMs = 10000L;
    private boolean EnableOutputTransfer = true;
    private boolean EnableInputTransfer = true;
    private boolean EnableAutoRefuel = true;
    private boolean EnableAutoFuelStart = true;
    private boolean EnableAutoFuelStop = true;
    private List<String> BlacklistedFuelItems = new ArrayList<>(List.of("Wood_Sallow_Trunk"));
    private List<String> BlacklistedInputItems = new ArrayList<>(List.of("Ingredient_Bar_Gold"));
    private List<String> BlacklistedProcessorBenches = new ArrayList<>(
            List.of("Quarry_Block_Iron", "Quarry_Block_Thorium", "Quarry_Block_Cobalt", "Quarry_Block_Adamantite"));


    public AutoIgnitionConfig() { }


    // GETTERS
    public long getUpdateIntervalMs() {
        return Math.max(500L, UpdateIntervalMs);
    }

    public long getScannerIntervalMs() {
        return Math.max(5000L, ScannerIntervalMs);
    }

    public boolean isEnableOutputTransfer() {
        return EnableOutputTransfer;
    }

    public boolean isEnableInputTransfer() {
        return EnableInputTransfer;
    }

    public boolean isEnableAutoRefuel() {
        return EnableAutoRefuel;
    }

    public boolean isEnableAutoFuelStart() {
        return EnableAutoFuelStart;
    }

    public boolean isEnableAutoFuelStop() {
        return EnableAutoFuelStop;
    }

    public List<String> getBlacklistedFuelItems() {
        return BlacklistedFuelItems;
    }
    public List<String> getBlacklistedInputItems() {
        return BlacklistedInputItems;
    }
    public List<String> getBlacklistedProcessorBenches() {
        return BlacklistedProcessorBenches;
    }


    // SETTERS
    public void setEnableOutputTransfer(boolean value) { EnableOutputTransfer = value; }

    public void setEnableInputTransfer(boolean value) { EnableInputTransfer = value; }

    public void setEnableAutoRefuel(boolean value) { EnableAutoRefuel = value; }

    public void setEnableAutoFuelStart(boolean value) { EnableAutoFuelStart = value; }

    public void setEnableAutoFuelStop(boolean value) { EnableAutoFuelStop = value; }

    public void setUpdateIntervalMs(long value) { UpdateIntervalMs = value; }

    public void setScannerIntervalMs(long value) { ScannerIntervalMs = value; }


    static {
        CODEC = BuilderCodec.builder(AutoIgnitionConfig.class, AutoIgnitionConfig::new)
                        .append(new KeyedCodec<>("UpdateIntervalMs", Codec.LONG),
                                (config, value, info) -> config.UpdateIntervalMs = value,
                                (config, info) -> config.UpdateIntervalMs)
                        .add()
                        .append(new KeyedCodec<>("ScannerIntervalMs", Codec.LONG),
                                (config, value, info) -> config.ScannerIntervalMs = value,
                                (config, info) -> config.ScannerIntervalMs)
                        .add()
                        .append(new KeyedCodec<>("EnableOutputTransfer", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableOutputTransfer = value,
                                (config, info) -> config.EnableOutputTransfer)
                        .add()
                        .append(new KeyedCodec<>("EnableInputTransfer", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableInputTransfer = value,
                                (config, info) -> config.EnableInputTransfer)
                        .add()
                            .append(new KeyedCodec<>("EnableAutoRefuel", Codec.BOOLEAN),
                                    (config, value, info) -> config.EnableAutoRefuel = value,
                                    (config, info) -> config.EnableAutoRefuel)
                            .add()
                        .append(new KeyedCodec<>("EnableAutoFuelStart", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableAutoFuelStart = value,
                                (config, info) -> config.EnableAutoFuelStart)
                        .add()
                        .append(new KeyedCodec<>("EnableAutoFuelStop", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableAutoFuelStop = value,
                                (config, info) -> config.EnableAutoFuelStop)
                        .add()
                        .append(new KeyedCodec<>("BlacklistedFuelItems", Codec.STRING_ARRAY),
                                (config, value, info) -> config.BlacklistedFuelItems = new ArrayList<>(Arrays.asList(value)),
                                (config, info) -> config.BlacklistedFuelItems.toArray(new String[0]))
                        .add()
                        .append(new KeyedCodec<>("BlacklistedInputItems", Codec.STRING_ARRAY),
                                (config, value, info) -> config.BlacklistedInputItems = new ArrayList<>(Arrays.asList(value)),
                                (config, info) -> config.BlacklistedInputItems.toArray(new String[0]))
                        .add()
                        .append(new KeyedCodec<>("BlacklistedProcessorBenches", Codec.STRING_ARRAY),
                                (config, value, info) -> config.BlacklistedProcessorBenches = new ArrayList<>(Arrays.asList(value)),
                                (config, info) -> config.BlacklistedProcessorBenches.toArray(new String[0]))
                        .add()
                        .build();

    }
}
