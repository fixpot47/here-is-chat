# Here Is Chat 💬

Tiny **client-side Fabric mod for Minecraft 26.2** that hides the on-screen chat while you play and shows it when you open the chat screen.

## How it works

- **Normal gameplay:** chat overlay is hidden, even when new messages arrive.
- **T:** opens the original Minecraft chat with available message history and the input field.
- **/**: command input works unchanged.
- **Enter / Esc:** closes the chat screen; the overlay is hidden again.
- **History:** the mod doesn't delete, filter, or mute incoming messages.
- Client-side only; servers don't need to install it.

## Requirements

- Minecraft Java Edition **26.2**
- Fabric Loader **0.19.3+**
- Java **25+**
- **Fabric API is not required.**

## Download

Open **Actions → Build Here Is Chat (Minecraft 26.2) → latest successful run → Artifacts → here-is-chat-1.0.1-26.2**.

Extract `here-is-chat-1.0.1+26.2.jar` from the downloaded ZIP and place it in your Fabric instance's `mods` folder. Remove any existing 26.3 build.

## Build

With Java 25 and Gradle 9.6.1:

```sh
gradle clean build
```

Output: `build/libs/here-is-chat-1.0.1+26.2.jar`.

## Implementation

A client Mixin cancels `ChatComponent.extractRenderState` only for `DisplayMode.BACKGROUND`. It doesn't change the foreground chat screen or the incoming message history.

## License

MIT
