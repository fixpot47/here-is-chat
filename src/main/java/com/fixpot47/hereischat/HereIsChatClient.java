package com.fixpot47.hereischat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class HereIsChatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChatSettings.load();

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("here-is-chat", "controls"));
        KeyMapping toggleKey = KeyMappingHelper.registerKeyMapping(
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
            }
        });
    }
}
