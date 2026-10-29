package com.fixpot47.hereischat.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Suppress only the always-visible chat HUD.
 * Foreground chat rendering is untouched, so T and / still open
 * the normal chat screen with normal history and message input.
 */
@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Inject(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hereIsChat$hideBackgroundChat(
        GuiGraphicsExtractor graphics,
        Font font,
        int ticks,
        int mouseX,
        int mouseY,
        ChatComponent.DisplayMode displayMode,
        boolean changeCursorOnInsertions,
        CallbackInfo ci
    ) {
        if (displayMode == ChatComponent.DisplayMode.BACKGROUND) {
            ci.cancel();
        }
    }
}
