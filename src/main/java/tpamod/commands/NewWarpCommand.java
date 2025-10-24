package tpamod.commands;

import tpamod.data.WarpData;
import necesse.engine.commands.*;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.level.maps.Level;

public class NewWarpCommand extends ModularChatCommand {
    private final WarpData warpData;

    public NewWarpCommand(String name, WarpData warpData) {
        super(name, "创建传送点", PermissionLevel.ADMIN, false,
              new CmdParameter("warpName", new PresetStringParameterHandler(false, "home", "spawn", "town", "base")));
        this.warpData = warpData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        String warpName = (String) args[0];
        
        if (serverClient != null) {
            // 获取玩家当前位置
            float playerX = serverClient.playerMob.getX();
            float playerY = serverClient.playerMob.getY();
            // 获取当前关卡标识符
            String currentLevelIdentifier = serverClient.playerMob.getLevel().getIdentifier().toString();
            // 获取当前群系标识符
            String currentBiomeIdentifier = getCurrentBiomeIdentifier(serverClient.playerMob.getLevel(), playerX, playerY);
            
            // 创建传送点
            if (warpData.addWarpPoint(warpName, currentLevelIdentifier, currentBiomeIdentifier, playerX, playerY)) {
                logs.add("传送点 '" + warpName + "' 已创建");
                logs.add("位置: 关卡(" + currentLevelIdentifier + ") 群系(" + currentBiomeIdentifier + ") 坐标(" + String.format("%.2f", playerX) + ", " + String.format("%.2f", playerY) + ")");
            } else {
                logs.add("传送点 '" + warpName + "' 已存在");
            }
        } else {
            logs.add("无法获取玩家信息");
        }
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
            System.out.println("Failed to get biome identifier: " + e.getMessage());
        }
        return "unknown"; // 如果获取失败，返回未知群系
    }

    public boolean shouldBeListed() {
        return true;
    }
}