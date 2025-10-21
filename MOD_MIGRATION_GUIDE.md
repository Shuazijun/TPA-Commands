# TPA Commands 模组迁移指南

## 问题描述

当运行 `.\gradlew runDevClient` 时，您可能会遇到以下错误：

```
java.lang.NoSuchMethodError: 'void necesse.engine.commands.parameterHandlers.IntParameterHandler.<init>(int)'
    at TeleportsExtended.Commands.TPXCommand.<init>(TPXCommand.java:18)
```

或者

```
java.lang.NoClassDefFoundError: com/google/common/cache/CacheBuilder
    at teleportextended.listener.TPAListener.<init>(TPAListener.java:19)
```

## 问题原因

这些错误是由于**同时加载了两个版本的传送模组**或**缺少必要的依赖库**：

1. **新版本**：`TPA Commands` (shuazi.tpa.commands, 1.0) - 我们刚刚开发的模组
2. **老版本**：`Teleports Extended` (deadly.tp.extended, 0.1) - 通过Steam Workshop安装的旧版本

老版本的模组使用了过时的API，与当前游戏版本不兼容。

## 解决方案

### 方法1：禁用老版本模组（推荐）

1. 启动Necesse游戏
2. 进入主菜单 → 模组管理
3. 找到并禁用 `Teleports Extended` (deadly.tp.extended, 0.1)
4. 确保 `TPA Commands` (shuazi.tpa.commands, 1.0) 已启用

### 方法2：卸载老版本模组

1. 在Steam Workshop中取消订阅 `Teleports Extended` 模组
2. 重启游戏

### 方法3：重新构建新版本模组

如果遇到依赖缺失错误，请重新构建模组：

```bash
.\gradlew clean buildModJar
```

## 功能对比

| 功能 | 老版本 | 新版本 |
|------|--------|--------|
| 传送请求 (/tpa) | ✅ | ✅ |
| 接受传送 (/tpac) | ✅ | ✅ |
| 拒绝传送 (/tpad) | ✅ | ✅ |
| 坐标传送 (/tpx) | ✅ | ❌ (已移除) |
| 中文命令支持 | ❌ | ✅ |
| 最新API兼容 | ❌ | ✅ |
| 体积优化 | 3MB | 4.7MB (包含完整依赖) |

## 验证成功

成功迁移后，您应该看到：

- 游戏正常启动，无错误信息
- 控制台显示：`TPA Commands Mod initialized!`
- 可以使用以下命令：
  - `/tpa [玩家名]` 或 `/传送请求 [玩家名]`
  - `/tpac` 或 `/同意传送`
  - `/tpad` 或 `/拒绝传送`

## 注意事项

- 新版本移除了坐标传送功能 (/tpx)，专注于玩家间传送
- 新版本体积稍大，但包含了所有必要的依赖库
- 新版本支持中英文双语命令
- 新版本使用最新的Necesse API，确保长期兼容性

如果仍有问题，请检查模组管理界面，确保只有 `TPA Commands` 模组被启用。