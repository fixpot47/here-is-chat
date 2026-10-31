package com.fixpot47.hereischat.mixin;

import com.fixpot47.hereischat.ChatSettings;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;
    @Unique private List<GuiMessage.Line> hereIsChat$backup;

    @Inject(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hereIsChat$filterBackground(
        GuiGraphicsExtractor graphics, Font font, int ticks, int mouseX, int mouseY,
        ChatComponent.DisplayMode displayMode, boolean changeCursorOnInsertions, CallbackInfo ci
    ) {
        if (!ChatSettings.isEnabled() || displayMode != ChatComponent.DisplayMode.BACKGROUND) return;

        // Leave vanilla's full foreground chat history untouched when T or / is open.
        if (!ChatSettings.hasPlayers()) {
            ci.cancel();
            return;
        }

        hereIsChat$backup = new ArrayList<>(trimmedMessages);
        trimmedMessages.removeIf(line -> !ChatSettings.showInBackground(line.parent().content().getString()));

        if (trimmedMessages.isEmpty()) {
            hereIsChat$restore();
            ci.cancel();
        }
    }

    @Inject(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V",
        at = @At("RETURN")
    )
    private void hereIsChat$afterRender(
        GuiGraphicsExtractor graphics, Font font, int ticks, int mouseX, int mouseY,
        ChatComponent.DisplayMode displayMode, boolean changeCursorOnInsertions, CallbackInfo ci
    ) {
        hereIsChat$restore();
    }

    @Unique
    private void hereIsChat$restore() {
        if (hereIsChat$backup != null) {
            trimmedMessages.clear();
            trimmedMessages.addAll(hereIsChat$backup);
            hereIsChat$backup = null;
        }
    }
}
