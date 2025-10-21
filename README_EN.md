# TPA Commands Mod

A mod that adds TPA commands to the Necesse game.

**Repository**: [https://github.com/Shuazijun/TPA-Commands](https://github.com/Shuazijun/TPA-Commands)

## Project Overview

- **Mod ID**: `shuazi.tpa.commands`
- **Mod Name**: `TPA Commands`
- **Mod Version**: `1.0`
- **Target Game Version**: `1.0.1`
- **Mod Description**: `Add TPA command to the game`
- **Author**: `Shuazi`
- **Client Mode**: `false` (requires server support)

## Features

### Teleportation System
- **/tpa [player name]** - Request teleport to another player
- **/tpac** - Accept teleport request
- **/tpad** - Deny teleport request
- **/传送请求 [player name]** - Request teleport to another player (Chinese command)
- **/同意传送** - Accept teleport request (Chinese command)
- **/拒绝传送** - Deny teleport request (Chinese command)

## Development Environment

### System Requirements
- **Operating System**: Windows 11 (or other supported systems)
- **Java Version**: Java 17 (required)
- **Build Tool**: Gradle
- **Project Type**: Necesse game mod

### Project Structure
```
TPA-Commands/
├── build.gradle          # Project build configuration
├── settings.gradle       # Project settings
├── README.md             # Project documentation (Chinese)
├── README_EN.md          # Project documentation (English)
├── MOD_MIGRATION_GUIDE.md # Mod migration guide
├── src/
│   └── main/
│       ├── java/
│       │   └── tpamod/
│       │       ├── TPAMod.java                  # Main mod class
│       │       ├── commands/                    # Command classes
│       │       │   ├── TPACommand.java
│       │       │   ├── TPAcCommand.java
│       │       │   └── TPAdCommand.java
│       │       ├── events/                      # Event classes
│       │       │   ├── TPARequestEvent.java
│       │       │   └── TPAResponseEvent.java
│       │       └── listener/                    # Listeners
│       │           └── TPAListener.java
│       └── resources/
│           ├── locale/                          # Localization files
│           │   ├── en.lang
│           │   └── zh_cn.lang
│           └── preview.png                      # Mod preview image
```

## Building and Running

### Gradle Build Commands

#### Basic Build Commands
```bash
# Compile project
./gradlew build

# Clean build files
./gradlew clean

# Build mod JAR file
./gradlew buildModJar
```

#### Necesse Mod Specific Commands
```bash
# Run client (with current mod)
./gradlew runClient

# Run development client (with debug mode)
./gradlew runDevClient

# Run server (with current mod)
./gradlew runServer
```

## Usage Instructions

### Teleport Commands
**Player-to-player teleportation**:
```
/tpa [player name] - Send teleport request
/tpac - Accept request
/tpad - Deny request
/传送请求 [player name] - Send teleport request (Chinese command)
/同意传送 - Accept request (Chinese command)
/拒绝传送 - Deny request (Chinese command)
```

## Development Notes

### Technical Features
- Built with Java 17 and Gradle build system
- Complete teleport request/response system
- Event-driven architecture
- Cache mechanism to prevent duplicate requests
- Multi-language localization support

### Code Structure
- **commands**: All chat command implementations
- **events**: Custom game events
- **listener**: Event listeners
- **data**: Data transfer objects
- **staticmethods**: Utility methods

## License

This project is built based on the following open source projects:
- [ExampleMod](https://github.com/Shuazijun/ExampleMod) - Framework template (for development reference only)
- [Necesse-Teleports-Extended](https://github.com/deadly990/Necesse-Teleports-Extended) - Teleport functionality (for feature reference only)

## Contributing

Issues and pull requests are welcome to improve this mod.

## Contact

For questions or suggestions, please contact via GitHub Issues: [TPA Commands Issues](https://github.com/Shuazijun/TPA-Commands/issues)