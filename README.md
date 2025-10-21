# TPA Commands Mod

一个为Necesse游戏添加TPA命令的模组。

**项目仓库**: [https://github.com/Shuazijun/TPA-Commands](https://github.com/Shuazijun/TPA-Commands)

## 项目概述

- **模组ID**: `shuazi.tpa.commands`
- **模组名称**: `TPA Commands`
- **模组版本**: `1.0`
- **目标游戏版本**: `1.0.1`
- **模组描述**: `Add TPA command to the game`
- **作者**: `Shuazi`
- **客户端模式**: `false` (需要服务器支持)

## 功能特性

### 传送系统
- **/tpa [玩家名]** - 请求传送到另一个玩家
- **/tpac** - 接受传送请求
- **/tpad** - 拒绝传送请求
- **/传送请求 [玩家名]** - 请求传送到另一个玩家（中文命令）
- **/同意传送** - 接受传送请求（中文命令）
- **/拒绝传送** - 拒绝传送请求（中文命令）

## 开发环境

### 系统要求
- **操作系统**: Windows 11 (或其他支持系统)
- **Java版本**: Java 17 (要求)
- **构建工具**: Gradle
- **项目类型**: Necesse 游戏模组

### 项目结构
```
TPA-Commands/
├── build.gradle          # 项目构建配置
├── settings.gradle       # 项目设置
├── README.md             # 项目说明
├── MOD_MIGRATION_GUIDE.md # 模组迁移指南
├── src/
│   └── main/
│       ├── java/
│       │   └── tpamod/
│       │       ├── TPAMod.java                  # 主模组类
│       │       ├── commands/                    # 命令类
│       │       │   ├── TPACommand.java
│       │       │   ├── TPAcCommand.java
│       │       │   └── TPAdCommand.java
│       │       ├── events/                      # 事件类
│       │       │   ├── TPARequestEvent.java
│       │       │   └── TPAResponseEvent.java
│       │       └── listener/                    # 监听器
│       │           └── TPAListener.java
│       └── resources/
│           ├── locale/                          # 本地化文件
│           │   ├── en.lang
│           │   └── zh_cn.lang
│           └── preview.png                      # 模组预览图
```

## 构建和运行

### Gradle 构建指令

#### 基础构建指令
```bash
# 编译项目
./gradlew build

# 清理构建文件
./gradlew clean

# 构建模组JAR文件
./gradlew buildModJar
```

#### Necesse 模组专用指令
```bash
# 运行客户端（带当前模组）
./gradlew runClient

# 运行开发客户端（带调试模式）
./gradlew runDevClient

# 运行服务器（带当前模组）
./gradlew runServer
```

## 使用说明

### 传送命令
**玩家间传送**:
```
/tpa [玩家名] - 发送传送请求
/tpac - 接受请求
/tpad - 拒绝请求
/传送请求 [玩家名] - 发送传送请求（中文命令）
/同意传送 - 接受请求（中文命令）
/拒绝传送 - 拒绝请求（中文命令）
```

## 开发说明

### 技术特点
- 使用Java 17和Gradle构建系统
- 完整的传送请求/响应系统
- 事件驱动的架构
- 缓存机制防止重复请求
- 多语言本地化支持

### 代码结构
- **commands**: 所有聊天命令实现
- **events**: 自定义游戏事件
- **listener**: 事件监听器
- **data**: 数据传输对象
- **staticmethods**: 工具方法

## 许可证

本项目基于以下开源项目构建：
- [ExampleMod](https://github.com/Shuazijun/ExampleMod) - 框架模板（仅作为开发参考）
- [Necesse-Teleports-Extended](https://github.com/deadly990/Necesse-Teleports-Extended) - 传送功能（仅作为功能参考）

## 贡献

欢迎提交问题和拉取请求来改进这个模组。

## 联系方式

如有问题或建议，请通过GitHub Issues联系：[TPA Commands Issues](https://github.com/Shuazijun/TPA-Commands/issues)