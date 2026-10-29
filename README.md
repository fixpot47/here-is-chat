# Here Is Chat 💬

A tiny **client-side Fabric mod for Minecraft 26.3** that hides the on-screen chat while you play and shows it again whenever you open the chat screen.

## How it works

- **Normal gameplay:** chat overlay is completely hidden, even when new messages arrive.
- **T:** the normal Minecraft chat screen opens, with recent messages and the usual input field.
- **/**: the normal command input works unchanged.
- **Enter / Esc:** after the chat screen closes, the overlay stays hidden.
- **History:** messages are not deleted, blocked, or muted. All original chat and command behavior stays intact.
- Only affects your own client. No installation is required on a server.

## Requirements

- Minecraft Java Edition **26.3**
- Fabric Loader **0.19.5+**
- Java **25+**
- **Fabric API is not required.**

## Download

Go to **Actions → Build Here Is Chat → latest successful run → Artifacts → here-is-chat-1.0.0** to download the ZIP containing the JAR. Put the JAR in the instance's `mods` folder.

## Build

With Java 25 and Gradle 9.7.1:

```sh
gradle clean build
```

The output is `build/libs/here-is-chat-1.0.0.jar`.

## Implementation

A single client Mixin cancels `ChatComponent.extractRenderState` only in `DisplayMode.BACKGROUND`. The foreground chat screen, incoming messages, chat history and command execution are not modified.

## License

MIT
