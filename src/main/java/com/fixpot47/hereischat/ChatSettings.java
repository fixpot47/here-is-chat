package com.fixpot47.hereischat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class ChatSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("here-is-chat.json");
    private static final Pattern VALID_NICK = Pattern.compile("[A-Za-z0-9_]{3,16}");
    private static final LinkedHashSet<String> PLAYERS = new LinkedHashSet<>();
    private static boolean enabled = true;

    private ChatSettings() {}

    private static class SaveData {
        boolean enabled = true;
        List<String> allowedPlayers = new ArrayList<>();
    }

    public static void load() {
        if (!Files.isRegularFile(PATH)) return;
        try {
            SaveData data = GSON.fromJson(Files.readString(PATH, StandardCharsets.UTF_8), SaveData.class);
            if (data == null) return;
            enabled = data.enabled;
            PLAYERS.clear();
            if (data.allowedPlayers != null) {
                for (String nick : data.allowedPlayers) {
                    if (isValidNick(nick)) PLAYERS.add(nick.toLowerCase(Locale.ROOT));
                }
            }
        } catch (Exception e) {
            System.err.println("[Here Is Chat] Failed to read settings: " + e);
        }
    }

    public static void save() {
        SaveData data = new SaveData();
        data.enabled = enabled;
        data.allowedPlayers.addAll(PLAYERS);
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[Here Is Chat] Failed to save settings: " + e);
        }
    }

    public static boolean isEnabled() { return enabled; }

    public static void setEnabled(boolean value) {
        enabled = value;
        save();
    }

    public static boolean isValidNick(String name) {
        return name != null && VALID_NICK.matcher(name).matches();
    }

    public static boolean addPlayer(String nick) {
        if (!isValidNick(nick)) return false;
        boolean added = PLAYERS.add(nick.toLowerCase(Locale.ROOT));
        if (added) save();
        return added;
    }

    public static void removePlayer(String nick) {
        if (PLAYERS.remove(nick.toLowerCase(Locale.ROOT))) save();
    }

    public static List<String> getPlayers() {
        return List.copyOf(PLAYERS);
    }

    public static boolean hasPlayers() {
        return !PLAYERS.isEmpty();
    }

    /**
     * Only compare the visible sender prefix, not text after the message separator.
     * Supports vanilla <name> and common server formats [G] name: text / name » text.
     * No incoming messages are deleted; matching only affects the background HUD.
     */
    public static boolean showInBackground(String message) {
        if (PLAYERS.isEmpty() || message == null) return false;

        int separator = -1;
        for (int i = 0; i < Math.min(128, message.length()); i++) {
            char c = message.charAt(i);
            if (c == ':' || c == '>' || c == '»' || c == '›' || c == '→') {
                separator = i;
                break;
            }
        }
        if (separator < 0) return false;
        String prefix = message.substring(0, separator).toLowerCase(Locale.ROOT);

        for (String nick : PLAYERS) {
            int found = prefix.indexOf(nick);
            while (found >= 0) {
                int end = found + nick.length();
                boolean startOK = found == 0 || !isNickChar(prefix.charAt(found - 1));
                boolean endOK = end == prefix.length() || !isNickChar(prefix.charAt(end));
                if (startOK && endOK) return true;
                found = prefix.indexOf(nick, found + 1);
            }
        }
        return false;
    }

    private static boolean isNickChar(char ch) {
        return ch == '_' || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }
}
