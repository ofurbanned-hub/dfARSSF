# Mod — Fabric Notepad prank

A lightweight Windows-only, optional Notepad typing prank. **No shutdown or restart functionality.**

## Installation

Build with GitHub Actions, then put the resulting non-sources JAR in the `mods` folder of a Fabric instance. On first launch it writes `config/mod.properties` and remains disabled. Set `enabled=true` and restart Minecraft to activate. The file also controls message and typing delays. This uses the Fabric `preLaunch` entry point and no gameplay APIs.

## Compatibility

Compiled against Minecraft 1.20.1 and Fabric Loader 0.16.10 using Java 17. This minimizes game-specific dependencies but **does not guarantee compatibility across every version**. Test before marking any Minecraft version compatible on Modrinth. Minecraft 1.17 and older often use Java versions below 17 and will not load this build. Some modern Minecraft versions require newer Java versions at runtime. Notepad typing uses the US keyboard layout and depends on Notepad retaining focus.

## GitHub

Upload the contents of this folder to the repository root, preserving `.github/workflows/build.yml`. Run Actions > Build Fabric Mod > Run workflow. Download `mod-jar` from Artifacts.
