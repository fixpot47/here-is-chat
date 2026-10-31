# Here Is Chat 💬

Lightweight client-side **Fabric** mod for Minecraft **26.2**.

## Features (v1.1.0)

- In gameplay, the background chat HUD only displays messages whose sender matches a name in your personal allowlist.
- Messages from everybody else are still available in **full chat history** when you press **T**.
- **/** command input and normal message sending work unchanged.
- **F6** turns the entire Here Is Chat filtering feature on/off. When OFF, vanilla chat is fully restored.
- **Mod Menu → Here Is Chat → Config** opens a simple username manager.
- Add or remove usernames (case-insensitive). The list has **no hardcoded size limit**, with pagination.
- Your names and toggle state are saved to `config/here-is-chat.json`.

> Name recognition uses the visible message header and common separators (`<Player>`, `Player: text`, `Player » text`). Some servers use unusual chat formats: those may require pattern adjustments. Whitelisted users' messages are not individually filtered from the full chat when T is open.

## Requirements

- Minecraft Java Edition **26.2**
- Fabric Loader **0.19.3+**
- Fabric API for **26.2**
- Java **25+**
- Mod Menu **20.x** (optional, but required to open the configuration GUI)

## Download

Go to **Actions → latest successful build → Artifacts → here-is-chat-1.1.0-26.2**, extract the `.jar`, put it in your Fabric instance's `mods` folder.
Remove any previous Here Is Chat JAR.

## Build

With Java 25 and Gradle 9.6.1:

```sh
gradle clean build
```

## License

MIT
