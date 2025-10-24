package tpamod.commands;

import necesse.engine.commands.*;
import necesse.engine.commands.parameterHandlers.ServerClientParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.PlayerMob;
import necesse.level.maps.Level;
import tpamod.util.PermissionUtil;

public class GetPosCommand extends ModularChatCommand {
    public GetPosCommand(String name) {
        super(name, "获取玩家位置", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        ServerClient target = (ServerClient) args[0];
        
        // 如果没有指定玩家，默认查询自己
        if (target == null) {
            target = serverClient;
        }
        
        // 检查权限：普通用户只能查询自己，管理员可以查询任何人
        if (target != serverClient && !PermissionUtil.isAdminOrOwner(serverClient)) {
            logs.add("你没有权限查询其他玩家的坐标");
            return;
        }
        
        if (target != null && target.playerMob != null) {
            PlayerMob playerMob = target.playerMob;
            
            // 获取玩家位置信息
            float playerX = playerMob.getX();
            float playerY = playerMob.getY();
            String levelIdentifier = getLevelIdentifierString(playerMob);
            String biomeIdentifier = getCurrentBiomeIdentifier(playerMob.getLevel(), playerX, playerY);
            
            // 输出完整的位置信息
            if (target == serverClient) {
                logs.add("你的当前位置信息:");
            } else {
                logs.add("玩家 " + target.getName() + " 的当前位置信息:");
            }
            
            logs.add("关卡: " + levelIdentifier);
            logs.add("群系: " + biomeIdentifier);
            logs.add("坐标: (" + String.format("%.2f", playerX) + ", " + String.format("%.2f", playerY) + ")");
            // 添加默认空行防止指令tab补全功能挡住显示信息
            logs.add(" ");

        } else {
            logs.add("无法获取玩家信息");
        }
    }

    // 获取LevelIdentifier字符串
    private String getLevelIdentifierString(PlayerMob playerMob) {
        try {
            LevelIdentifier levelIdentifier = playerMob.getLevel().getIdentifier();
            return levelIdentifier.toString();
        } catch (Exception e) {
            return "未知";
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
            System.out.println("GetPos Command: Failed to get biome identifier: " + e.getMessage());
        }
        return "未知"; // 如果获取失败，返回未知群系
    }

}