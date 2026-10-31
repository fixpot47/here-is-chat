package com.fixpot47.hereischat;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class ChatSettingsScreen extends Screen {
    private static final int PAGE_SIZE = 7;
    private final Screen parent;
    private EditBox input;
    private int page;

    public ChatSettingsScreen(Screen parent) {
        super(Component.literal("Here Is Chat"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 120;
        int y = Math.max(34, this.height / 2 - 109);

        this.addRenderableWidget(Button.builder(
            Component.literal(ChatSettings.isEnabled() ? "Here Is Chat: ON (F6)" : "Here Is Chat: OFF (F6)"),
            button -> {
                ChatSettings.setEnabled(!ChatSettings.isEnabled());
                this.rebuildWidgets();
            }
        ).bounds(x, y, 240, 20).build());

        y += 26;
        this.input = new EditBox(this.font, x, y, 170, 20, Component.literal("Minecraft username"));
        this.input.setMaxLength(16);
        this.addRenderableWidget(this.input);
        this.addRenderableWidget(Button.builder(Component.literal("+ Add"), button -> {
            if (ChatSettings.addPlayer(this.input.getValue().trim())) {
                this.page = Math.max(0, (ChatSettings.getPlayers().size() - 1) / PAGE_SIZE);
                this.rebuildWidgets();
            }
        }).bounds(x + 176, y, 64, 20).build());

        y += 25;
        List<String> names = ChatSettings.getPlayers();
        int maxPage = Math.max(0, (names.size() - 1) / PAGE_SIZE);
        page = Math.min(page, maxPage);

        for (int i = page * PAGE_SIZE; i < Math.min(names.size(), (page + 1) * PAGE_SIZE); i++) {
            String name = names.get(i);
            this.addRenderableWidget(Button.builder(
                Component.literal(name + "    [Remove]"),
                button -> {
                    ChatSettings.removePlayer(name);
                    this.rebuildWidgets();
                }
            ).bounds(x, y, 240, 18).build());
            y += 20;
        }

        int footer = this.height - 50;
        this.addRenderableWidget(Button.builder(
            Component.literal("< Previous"),
            button -> { if (page > 0) { page--; this.rebuildWidgets(); } }
        ).bounds(x, footer, 76, 20).build()).active = page > 0;

        this.addRenderableWidget(Button.builder(
            Component.literal("Players: " + names.size() + " | " + (page + 1) + "/" + (maxPage + 1)),
            button -> {}
        ).bounds(x + 78, footer, 84, 20).build()).active = false;

        this.addRenderableWidget(Button.builder(
            Component.literal("Next >"),
            button -> { if (page < maxPage) { page++; this.rebuildWidgets(); } }
        ).bounds(x + 164, footer, 76, 20).build()).active = page < maxPage;

        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            button -> this.onClose()
        ).bounds(x, this.height - 25, 240, 20).build());
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}
