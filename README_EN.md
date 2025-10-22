# TPA Commands Mod

A mod that adds TPA commands to the Necesse game.

**Repository**: [https://github.com/Shuazijun/TPA-Commands](https://github.com/Shuazijun/TPA-Commands)

## Project Overview

- **Mod ID**: `shuazi.tpa.commands`
- **Mod Name**: `TPA Commands`
- **Mod Version**: `1.1`
- **Target Game Version**: `1.0.1`
- **Mod Description**: `Add TPA and Warp commands to the game`
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

### Back System (New in v1.1)
- **/back** - Return to previous position (priority to death location, then teleport location)
- **/backcd [seconds]** - Set back cooldown time (admin only, default 15 seconds)
- **/返回** - Return to previous position (Chinese command)
- **/设置返回冷却时间 [seconds]** - Set back cooldown time (admin only, Chinese command)

### Warp System (New in v1.1)
- **/newwarp [warpName]** - Create warp point at current location (admin only, supports preset names like home, spawn, town, base)
- **/delwarp [warpName]** - Delete specified warp point (admin only)
- **/warp [warpName]** - Teleport to specified warp point (default cooldown 15 seconds)
- **/warplist** - List all available warp points
- **/warpcd [seconds]** - Set warp point cooldown time (admin only, default 15 seconds)
- **/新建传送点 [warpName]** - Create warp point at current location (admin only, Chinese command)
- **/删除传送点 [warpName]** - Delete specified warp point (admin only, Chinese command)
- **/传送点 [warpName]** - Teleport to specified warp point (Chinese command)
- **/传送点列表** - List all available warp points (Chinese command)
- **/设置传送点冷却时间 [seconds]** - Set warp point cooldown time (admin only, Chinese command)

### Position Query and Teleportation
- **/getpos [player name]** - Get player position (admins can query other players)
- **/tppos [x] [y] [player name]** - Teleport to specified coordinates (admin only)
- **/获取坐标 [player name]** - Get player position (admins can query other players, Chinese command)
- **/传送坐标 [x] [y] [player name]** - Teleport to specified coordinates (admin only, Chinese command)

### Teleportation System Management
- **/tpacd [seconds]** - Set TPA request cooldown time (admin only, default 15 seconds)
- **/设置传送请求冷却时间 [seconds]** - Set TPA request cooldown time (admin only, Chinese command)

## Usage Instructions

### Back System
**Admin Commands**:
```
/backcd [seconds] - Set back cooldown time (1-3600 seconds, default 15)
/设置返回冷却时间 [seconds] - Set back cooldown time (Chinese command)
```

**Player Commands**:
```
/back - Return to previous position (priority to death location, then teleport location)
/返回 - Return to previous position (Chinese command)
```

### Warp System
**Admin Commands**:
```
/newwarp [warpName] - Create warp point at current location (supports preset names like home, spawn, town, base)
/delwarp [warpName] - Delete specified warp point
/warpcd [seconds] - Set warp point cooldown time (1-3600 seconds, default 15)
/新建传送点 [warpName] - Create warp point at current location (Chinese command)
/删除传送点 [warpName] - Delete specified warp point (Chinese command)
/设置传送点冷却时间 [seconds] - Set warp point cooldown time (Chinese command)
```

**Player Commands**:
```
/warp [warpName] - Teleport to specified warp point (with cooldown, default 15 seconds)
/warplist - List all available warp points
/传送点 [warpName] - Teleport to specified warp point (Chinese command)
/传送点列表 - List all available warp points (Chinese command)
```

### Position Query and Teleportation
**Admin Commands**:
```
/getpos [player name] - Get player position (can query other players)
/tppos [x] [y] [player name] - Teleport to specified coordinates
/获取坐标 [player name] - Get player position (Chinese command)
/传送坐标 [x] [y] [player name] - Teleport to specified coordinates (Chinese command)
```

**Player Commands**:
```
/getpos - Get own current position
/获取坐标 - Get own current position (Chinese command)
```

### Teleportation System
**Player-to-player teleportation**:
```
/tpa [player name] - Send teleport request (with cooldown, default 15 seconds)
/tpac - Accept request
/tpad - Deny request
/tpacd [seconds] - Set TPA request cooldown time (admin only)
/传送请求 [player name] - Send teleport request (Chinese command)
/同意传送 - Accept request (Chinese command)
/拒绝传送 - Deny request (Chinese command)
/设置传送请求冷却时间 [seconds] - Set TPA request cooldown time (admin only, Chinese command)
```

## Cooldown Configuration
All teleportation-related features have cooldown protection, default set to 15 seconds:
- TPA request cooldown: 15 seconds
- Warp point usage cooldown: 15 seconds
- Back command cooldown: 15 seconds

Admins can adjust cooldown times using the respective configuration commands (1-3600 seconds).

## License

This project is built based on the following open source projects:
- [ExampleMod](https://github.com/Shuazijun/ExampleMod) - Framework template (for development reference only)
- [Necesse-Teleports-Extended](https://github.com/deadly990/Necesse-Teleports-Extended) - Teleport functionality (for feature reference only)

## Contributing

Issues and pull requests are welcome to improve this mod.

## Contact

For questions or suggestions, please contact via GitHub Issues: [TPA Commands Issues](https://github.com/Shuazijun/TPA-Commands/issues)