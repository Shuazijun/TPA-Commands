package tpamod.commands;

import tpamod.data.*;
import tpamod.listener.WarpListener;
import necesse.engine.commands.*;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.PlayerMob;
import necesse.level.maps.Level;
import tpamod.util.PermissionUtil;

public class WarpCommand extends ModularChatCommand {
    private final WarpData warpData;
    private final WarpConfig warpConfig;
    private final WarpListener warpListener;
    private final BackData backData;

    public WarpCommand(String name, WarpData warpData, WarpConfig warpConfig, WarpListener warpListener, BackData backData) {
        super(name, "传送到指定传送点", PermissionLevel.USER, false,
              new CmdParameter("warpName", new PresetStringParameterHandler(false, "home", "spawn", "town", "base", "hole1", "hole2", "hole3", "hole4", "hole5", "boss1", "boss2", "boss3", "boss4", "boss5", "boss6", "boss7", "boss8", "boss9", "boss10")));
        this.warpData = warpData;
        this.warpConfig = warpConfig;
        this.warpListener = warpListener;
        this.backData = backData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        String warpName = (String) args[0];
        
        // 检查传送点是否存在
        if (!warpData.hasWarpPoint(warpName)) {
            logs.add("传送点 '" + warpName + "' 不存在");
            return;
        }
        
        // 检查冷却时间（管理员不受冷却限制）
        if (!PermissionUtil.isAdminOrOwner(serverClient)) {
            long remainingCooldown = warpListener.getRemainingCooldown(serverClient.authentication);
            if (remainingCooldown > 0) {
                logs.add("传送冷却中，剩余时间: " + (remainingCooldown / 1000) + "秒");
                return;
            }
        }
        
        // 获取传送点数据
        WarpData.WarpPoint warpPoint = warpData.getWarpPoint(warpName);
        if (warpPoint == null) {
            logs.add("传送点数据读取失败");
            return;
        }
        
        
        // 传送玩家
        PlayerMob playerMob = serverClient.playerMob;
        if (playerMob != null) {
            // 记录传送前坐标到back系统
            float currentX = playerMob.getX();
            float currentY = playerMob.getY();
            // 获取当前关卡标识符
            String currentLevelIdentifier = playerMob.getLevel().getIdentifier().toString();
            
            // 使用关卡标识符字符串作为关卡类型标识
            int levelType = Math.abs(currentLevelIdentifier.hashCode() % 1000);
            // 获取当前群系标识符
            String currentBiomeIdentifier = getCurrentBiomeIdentifier(playerMob.getLevel(), currentX, currentY);
            backData.recordTeleportPosition(String.valueOf(serverClient.authentication),
                                          levelType,
                                          (int)currentX, (int)currentY, currentBiomeIdentifier);
            
            // 设置冷却时间（管理员不受冷却限制）
            if (!PermissionUtil.isAdminOrOwner(serverClient)) {
                warpListener.setCooldown(serverClient.authentication, warpConfig.getCooldownSeconds());
            }
            
            // 检查是否需要切换关卡
            String currentLevelIdentifierString = playerMob.getLevel().getIdentifier().toString();
            if (!currentLevelIdentifierString.equals(warpPoint.levelIdentifier)) {
                // 需要切换关卡
                logs.add("正在切换关卡...");
                logs.add("目标关卡: " + warpPoint.levelIdentifier + " (当前关卡: " + currentLevelIdentifierString + ")");
                
                try {
                    // 创建目标关卡的LevelIdentifier
                    LevelIdentifier targetLevelIdentifier = new LevelIdentifier(warpPoint.levelIdentifier);
                    
                    // 使用changeLevel API切换关卡并直接传送到目标位置
                    serverClient.changeLevel(targetLevelIdentifier, level -> {
                        return new java.awt.Point((int)warpPoint.x, (int)warpPoint.y);
                    }, true);
                    logs.add("关卡切换成功");
                    logs.add("已切换到关卡: " + warpPoint.levelIdentifier);
                    logs.add("已传送到传送点: " + warpName);
                    logs.add("坐标: (" + String.format("%.2f", warpPoint.x) + ", " + String.format("%.2f", warpPoint.y) + ")");
                    logs.add("群系: " + warpPoint.biomeIdentifier);
                } catch (Exception e) {
                    logs.add("关卡切换失败: " + e.getMessage());
                    logs.add("请稍后重试");
                }
            } else {
                // 在同一关卡内传送
                playerMob.setPos(warpPoint.x, warpPoint.y, true);
                logs.add("已传送到传送点: " + warpName);
                logs.add("坐标: (" + String.format("%.2f", warpPoint.x) + ", " + String.format("%.2f", warpPoint.y) + ") 关卡: " + warpPoint.levelIdentifier);
                logs.add("群系: " + warpPoint.biomeIdentifier);
            }
        } else {
            logs.add("玩家数据错误，传送失败");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
    
    // 获取当前位置的群系标识符
    private String getCurrentBiomeIdentifier(Level level, float x, float y) {
        try {
            // 将坐标转换为瓦片坐标
            int tileX = (int)(x / 32.0F);
            int tileY = (int)(y / 32.0F);
            
            // 获取该位置的群系
            var biome = level.getBiome(tileX, tileY);
            if (biome != null) {
                return biome.getStringID();
            }
        } catch (Exception e) {
            System.out.println("Warp Command: Failed to get biome identifier: " + e.getMessage());
        }
        return "unknown"; // 如果获取失败，返回未知群系
    }
}