package tpamod.commands;

import tpamod.data.BackData;
import tpamod.data.BackConfig;
import tpamod.listener.BackListener;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.PlayerMob;

public class BackCommand extends ModularChatCommand {
    private final BackData backData;
    private final BackConfig backConfig;
    private final BackListener backListener;

    public BackCommand(String name, BackData backData, BackConfig backConfig, BackListener backListener) {
        super(name, "返回上一个坐标点", PermissionLevel.USER, false);
        this.backData = backData;
        this.backConfig = backConfig;
        this.backListener = backListener;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        String playerAuth = String.valueOf(serverClient.authentication);
        
        // 检查冷却时间（管理员不受冷却限制）
        if (serverClient.getPermissionLevel() != PermissionLevel.ADMIN) {
            long remainingCooldown = backListener.getRemainingCooldown(playerAuth);
            if (remainingCooldown > 0) {
                logs.add("返回冷却中，剩余时间: " + (remainingCooldown / 1000) + "秒");
                return;
            }
        }
        
        // 获取玩家的坐标记录
        BackData.PlayerBackData playerData = backData.getPlayerBackData(playerAuth);
        
        
        // 优先返回死亡坐标，如果没有死亡记录则返回传送坐标
        if (playerData.hasDeathRecord) {
            // 返回死亡坐标
            teleportPlayer(serverClient,
                playerData.lastDeathIslandX, playerData.lastDeathIslandY,
                playerData.lastDeathDimension, playerData.lastDeathX, playerData.lastDeathY, logs);
            
            // 清除死亡记录
            backData.clearDeathRecord(playerAuth);
            logs.add("已返回上一次死亡位置");
            
        } else if (playerData.hasTeleportRecord) {
            // 返回传送坐标
            teleportPlayer(serverClient,
                playerData.lastTeleportIslandX, playerData.lastTeleportIslandY,
                playerData.lastTeleportDimension, playerData.lastTeleportX, playerData.lastTeleportY, logs);
            
            // 清除传送记录
            backData.clearTeleportRecord(playerAuth);
            logs.add("已返回上一次传送前位置");
            
        } else {
            logs.add("没有可返回的坐标记录");
            return;
        }
        
        // 设置冷却时间（管理员不受冷却限制）
        if (serverClient.getPermissionLevel() != PermissionLevel.ADMIN) {
            backListener.setCooldown(playerAuth, backConfig.getCooldownSeconds());
        }
    }

    // 传送玩家到指定坐标
    private void teleportPlayer(ServerClient serverClient, int islandX, int islandY, int levelType, int x, int y, CommandLog logs) {
        PlayerMob playerMob = serverClient.playerMob;
        if (playerMob != null) {
            // 检查是否需要切换关卡
            int currentLevelType = getCurrentLevelType(playerMob);
            if (currentLevelType != levelType) {
                // 需要切换关卡
                logs.add("正在切换关卡...");
                logs.add("目标关卡: " + getLevelName(levelType) + " (当前关卡: " + getLevelName(currentLevelType) + ")");
                
                // 获取目标关卡的LevelIdentifier
                LevelIdentifier targetLevelIdentifier = getLevelIdentifierFromType(levelType);
                if (targetLevelIdentifier != null) {
                    try {
                        // 使用changeLevel API切换关卡并直接传送到目标位置
                        serverClient.changeLevel(targetLevelIdentifier, level -> {
                            return new java.awt.Point(x, y);
                        }, true);
                        logs.add("关卡切换成功");
                        logs.add("已切换到关卡: " + getLevelName(levelType));
                        logs.add("已传送到坐标: (" + x + ", " + y + ")");
                    } catch (Exception e) {
                        logs.add("关卡切换失败: " + e.getMessage());
                        logs.add("请稍后重试");
                    }
                } else {
                    logs.add("无法识别的目标关卡: " + levelType);
                    logs.add("请使用 /setlevel 命令手动切换关卡");
                }
            } else {
                // 在同一关卡内传送
                playerMob.setPos((float)x, (float)y, true);
                logs.add("坐标: (" + x + ", " + y + ") 关卡: " + getLevelName(levelType));
            }
        } else {
            logs.add("玩家数据错误，传送失败");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
    
    // 获取玩家当前关卡类型
    private int getCurrentLevelType(PlayerMob playerMob) {
        try {
            // 使用LevelIdentifier获取关卡信息
            LevelIdentifier levelIdentifier = playerMob.getLevel().getIdentifier();
            String levelIdentifierString = levelIdentifier.toString();
            
            // 根据LevelIdentifier字符串判断关卡类型
            if (levelIdentifierString.contains("flat")) {
                return 2; // 平坦世界关卡
            } else if (levelIdentifierString.contains("surface")) {
                return 0; // 地表关卡
            } else if (levelIdentifierString.contains("cave")) {
                return 1; // 地下洞穴关卡
            } else if (levelIdentifierString.contains("deepcave")) {
                return 2; // 深层洞穴关卡
            }
            
            // 如果无法识别，使用默认地表关卡
            System.out.println("Back System: Unknown level identifier: " + levelIdentifierString + ", using default (0)");
            return 0; // 默认关卡
        } catch (Exception e) {
            System.out.println("Back System: Failed to get level type, using default (0)");
            return 0; // 默认关卡
        }
    }
    
    // 获取关卡名称
    private String getLevelName(int levelType) {
        switch (levelType) {
            case 0:
                return "地表 (0)";
            case 1:
                return "地下洞穴 (1)";
            case 2:
                return "深层洞穴 (2)";
            default:
                return "未知 (" + levelType + ")";
        }
    }
    
    // 根据关卡类型获取对应的LevelIdentifier
    private LevelIdentifier getLevelIdentifierFromType(int levelType) {
        try {
            // 使用LevelIdentifier的字符串构造函数
            switch (levelType) {
                case 0: // 地表关卡
                    return new LevelIdentifier("surface");
                case 1: // 地下洞穴关卡
                    return new LevelIdentifier("cave");
                case 2: // 深层洞穴关卡/平坦世界关卡
                    return new LevelIdentifier("deepcave");
                default:
                    return new LevelIdentifier("surface"); // 默认地表关卡
            }
        } catch (Exception e) {
            System.out.println("Back System: Failed to create LevelIdentifier for level type " + levelType);
            return new LevelIdentifier("surface"); // 默认地表关卡
        }
    }
}