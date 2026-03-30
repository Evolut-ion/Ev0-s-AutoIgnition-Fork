package net.autoignition.pages;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.autoignition.AutoIgnitionMod;
import net.autoignition.config.AutoIgnitionConfig;

import javax.annotation.Nonnull;

public class SettingsPage extends InteractiveCustomUIPage<SettingsPage.SettingsEventData> {
    private final AutoIgnitionConfig config;

    public SettingsPage(@Nonnull PlayerRef playerRef, AutoIgnitionConfig config) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, SettingsPage.SettingsEventData.CODEC);
        this.config = config;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/AutoIgnition/SettingsPage.ui");

        updateToggleState(uiCommandBuilder, "#ToggleEnableOutputTransfer", config.isEnableOutputTransfer());
        updateToggleState(uiCommandBuilder, "#ToggleEnableInputTransfer", config.isEnableInputTransfer());
        updateToggleState(uiCommandBuilder, "#ToggleEnableAutoRefuel", config.isEnableAutoRefuel());
        updateToggleState(uiCommandBuilder, "#ToggleEnableAutoFuelStart", config.isEnableAutoFuelStart());
        updateToggleState(uiCommandBuilder, "#ToggleEnableAutoFuelStop", config.isEnableAutoFuelStop());

        uiCommandBuilder.set("#UpdateValueLabel.Text", String.valueOf(config.getUpdateIntervalMs()));
        uiCommandBuilder.set("#ScannerValueLabel.Text", String.valueOf(config.getScannerIntervalMs()));

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ToggleEnableOutputTransfer",
                EventData.of("Action", "toggleOutput")
        );

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ToggleEnableInputTransfer",
                EventData.of("Action", "toggleInput")
        );

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ToggleEnableAutoRefuel",
                EventData.of("Action", "toggleRefuel")
        );

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ToggleEnableAutoFuelStart",
                EventData.of("Action", "toggleFuelStart")
        );

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ToggleEnableAutoFuelStop",
                EventData.of("Action", "toggleFuelStop")
        );

        // UPDATE INTERVAL
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#UpdateMinus",
                EventData.of("Action", "minusUpdate")
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#UpdatePlus",
                EventData.of("Action", "plusUpdate")
        );

        // SCANNER INTERVAL
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ScannerMinus",
                EventData.of("Action", "minusScanner")
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ScannerPlus",
                EventData.of("Action", "plusScanner")
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull SettingsEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if ("toggleOutput".equals(data.action)) {
            config.setEnableOutputTransfer(!config.isEnableOutputTransfer());
        } else if ("toggleInput".equals(data.action)) {
            config.setEnableInputTransfer(!config.isEnableInputTransfer());
        } else if ("toggleRefuel".equals(data.action)) {
            config.setEnableAutoRefuel(!config.isEnableAutoRefuel());
        } else if ("toggleFuelStart".equals(data.action)) {
            config.setEnableAutoFuelStart(!config.isEnableAutoFuelStart());
        } else if ("toggleFuelStop".equals(data.action)) {
            config.setEnableAutoFuelStop(!config.isEnableAutoFuelStop());
        } else if ("minusUpdate".equals(data.action)) {
            long current = config.getUpdateIntervalMs();
            config.setUpdateIntervalMs(Math.max(500, current - 100));
        } else if ("plusUpdate".equals(data.action)) {
            long current = config.getUpdateIntervalMs();
            config.setUpdateIntervalMs(current + 100);
        } else if ("minusScanner".equals(data.action)) {
            long current = config.getScannerIntervalMs();
            config.setScannerIntervalMs(Math.max(500, current - 100));
        } else if ("plusScanner".equals(data.action)) {
            long current = config.getScannerIntervalMs();
            config.setScannerIntervalMs(current + 100);
        }

        AutoIgnitionMod.getInstance().saveConfig();

        player.getPageManager().openCustomPage(ref, store, new SettingsPage(this.playerRef, this.config));
    }

    private void updateToggleState(UICommandBuilder builder, String elementId, boolean isOn) {
        builder.set(elementId + ".Text", isOn ? "ON" : "OFF");

        if (isOn) {
            builder.set(elementId + ".Style.Default.Background", "#1a3d2e");
            builder.set(elementId + ".Style.Default.LabelStyle.TextColor", "#4aff7f");
            builder.set(elementId + ".Style.Hovered.Background", "#2a4d3e");
            builder.set(elementId + ".Style.Hovered.LabelStyle.TextColor", "#6aff9f");
        } else {
            builder.set(elementId + ".Style.Default.Background", "#3d1a1a");
            builder.set(elementId + ".Style.Default.LabelStyle.TextColor", "#ff4a4a");
            builder.set(elementId + ".Style.Hovered.Background", "#4d2a2a");
            builder.set(elementId + ".Style.Hovered.LabelStyle.TextColor", "#ff6a6a");
        }
    }

    public static class SettingsEventData {
        public String action;
        public String value;
        public static final BuilderCodec<SettingsEventData> CODEC;

        public SettingsEventData() {
        }

        static {
            CODEC = (BuilderCodec.builder(SettingsEventData.class, SettingsEventData::new))
                    .append(new KeyedCodec<>("Action", Codec.STRING), (data, val, info) -> {
                        data.action = val;
                    }, (data, info) -> data.action).add()
                    .append(new KeyedCodec<>("Value", Codec.STRING), (data, val, info) -> {
                        data.value = val;
                    }, (data, info) -> data.value).add()
                    .build();
        }
    }
}
