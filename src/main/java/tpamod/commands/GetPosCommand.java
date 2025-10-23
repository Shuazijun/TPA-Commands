package tpamod.commands;

import necesse.engine.commands.*;
import necesse.engine.commands.parameterHandlers.ServerClientParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.PlayerMob;

public class GetPosCommand extends ModularChatCommand {
    public GetPosCommand(String name) {
        super(name, "获取玩家位置和关卡信息", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        ServerClient target = (ServerClient) args[0];
        
        // 如果没有指定玩家，默认查询自己
        if (target == null) {
            target = serverClient;
        }
        
        // 检查权限：普通用户只能查询自己，管理员可以查询任何人
        if (target != serverClient && serverClient.getPermissionLevel() != PermissionLevel.ADMIN) {
            logs.add("你没有权限查询其他玩家的坐标");
            return;
        }
        
        if (target != null && target.playerMob != null) {
            PlayerMob playerMob = target.playerMob;
            
            // 获取玩家位置信息
            float playerX = playerMob.getX();
            float playerY = playerMob.getY();
            String levelIdentifier = getLevelIdentifierString(playerMob);
            
            // 输出完整的位置信息
            if (target == serverClient) {
                logs.add("你的当前位置信息:");
            } else {
                logs.add("玩家 " + target.getName() + " 的当前位置信息:");
            }
            
            logs.add("关卡标识符: " + levelIdentifier);
            logs.add("坐标: (" + String.format("%.2f", playerX) + ", " + String.format("%.2f", playerY) + ")");
            
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

}