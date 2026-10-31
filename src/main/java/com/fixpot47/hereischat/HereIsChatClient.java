package com.fixpot47.hereischat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class HereIsChatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChatSettings.load();

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("here-is-chat", "controls"));
        KeyMapping toggleKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.here-is-chat.toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_F6,
                category
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                ChatSettings.setEnabled(!ChatSettings.isEnabled());
                // Display status even though the normal chat HUD may currently be hidden.
                if (client.player != null) {
                    client.player.displayClientMessage(
                        Component.literal("Here Is Chat: " + (ChatSettings.isEnabled() ? "ON" : "OFF")),
                        true
                    );
                }
            }
        });
    }
}
