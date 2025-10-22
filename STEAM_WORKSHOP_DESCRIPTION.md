# TPA Commands Mod

A comprehensive teleportation system for Necesse that adds player-to-player teleportation, warp points, and position management features.

## Features

### 🔄 Player Teleportation
- **/tpa [player]** - Request teleport to another player
- **/tpac** - Accept teleport request  
- **/tpad** - Deny teleport request
- **Cooldown protection** - Prevents spam (15 seconds default)

### 📍 Warp System
- **/newwarp [name]** - Create warp points (admin)
- **/delwarp [name]** - Delete warp points (admin)
- **/warp [name]** - Teleport to warp points
- **/warplist** - List all available warp points
- **Preset names**: home, spawn, town, base

### 🔙 Back System
- **/back** - Return to previous position
- **Priority system**: Death location > Teleport location
- **Cooldown protection** - 15 seconds default

### 📊 Position Management
- **/getpos [player]** - Get player coordinates
- **/tppos [x] [y] [player]** - Teleport to coordinates (admin)
- **Admin features**: Query other players, coordinate teleportation

### ⚙️ Configuration
- **/tpacd [seconds]** - Set TPA cooldown (admin)
- **/warpcd [seconds]** - Set warp cooldown (admin)  
- **/backcd [seconds]** - Set back cooldown (admin)
- **Range**: 1-3600 seconds

## Chinese Commands 中文命令

### 传送系统
- **/传送请求 [玩家名]** - 请求传送到其他玩家
- **/同意传送** - 接受传送请求
- **/拒绝传送** - 拒绝传送请求

### 传送点系统
- **/新建传送点 [名称]** - 创建传送点 (管理员)
- **/删除传送点 [名称]** - 删除传送点 (管理员)
- **/传送点 [名称]** - 传送到传送点
- **/传送点列表** - 列出所有传送点

### 返回系统
- **/返回** - 返回上一个位置
- **优先级**: 死亡位置 > 传送位置

### 坐标管理
- **/获取坐标 [玩家名]** - 获取玩家坐标
- **/传送坐标 [x] [y] [玩家名]** - 传送到坐标 (管理员)

### 配置命令
- **/设置传送请求冷却时间 [秒数]** - 设置传送请求冷却时间 (管理员)
- **/设置传送点冷却时间 [秒数]** - 设置传送点冷却时间 (管理员)
- **/设置返回冷却时间 [秒数]** - 设置返回冷却时间 (管理员)

## Key Benefits

✅ **Easy to Use** - Simple commands for all players  
✅ **Admin Controls** - Full configuration options  
✅ **Cooldown Protection** - Prevents abuse  
✅ **Persistent Data** - Warp points and settings saved  
✅ **Bilingual Support** - English and Chinese commands  
✅ **Server Compatible** - Works on multiplayer servers

## Installation

1. Subscribe to this mod
2. Enable in your server/world settings
3. Enjoy enhanced teleportation features!

---

**Mod ID**: `shuazi.tpa.commands`  
**Version**: 1.1  
**Game Version**: 1.0.1+  
**Author**: Shuazi

For support or suggestions, visit the GitHub repository: [TPA Commands](https://github.com/Shuazijun/TPA-Commands)