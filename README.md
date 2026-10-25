# SaveMyKeybinds Continued

An unofficial continuation of [SaveMyKeybinds](https://github.com/MisterCheezeCake/SaveMyKeybinds) by MisterCheezeCake, ported to Minecraft 26.2 and 26.3 (Fabric).

Save your key bindings as named presets and switch between them with two clicks. This is handy for different modpacks, game modes or players sharing one PC.

## Requirements

- Minecraft 26.2 or 26.3 with Fabric Loader 0.19 or newer
- [Controlling](https://modrinth.com/mod/controlling) is supported but not required

## Usage

Open *Options → Controls → Key Binds*. Two buttons sit at the bottom:

- **Save Presets** saves your current key bindings under a name.
- **Load Presets** lists your presets so you can load or delete them.

Loading a preset applies the keys right away and keeps them after a restart. Presets are plain JSON files in `.minecraft/config/savemykeybinds/`, so you can copy them to other instances.

## Changes in this fork

- Ported to Minecraft 26.2 and 26.3. Presets from older versions still load: they store key names like `key.keyboard.g`, which did not change with the new SDL input in 26.3.
- Loading a preset applies the keys right away and saves them to `options.txt`, so they survive a restart.

## Building

Java 25 is needed.

```
./gradlew build
```

The jars end up in `versions/<minecraft version>/build/libs`. `./gradlew :26.3:runClientGameTest` runs the in-game tests; add `-PwithoutControlling` to test without Controlling.

## Credits and license

The original mod was made by [MisterCheezeCake](https://github.com/MisterCheezeCake). This fork is maintained by [Henner4746](https://github.com/Henner4746). Please report problems with this version [here](https://github.com/Henner4746/SaveMyKeybinds/issues), not to the original author. See [OPENSOURCE.md](OPENSOURCE.md) for the projects this mod builds on.

GNU LGPL 3.0, see [LICENSE](LICENSE).
