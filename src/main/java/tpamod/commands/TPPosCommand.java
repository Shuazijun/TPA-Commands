package tpamod.commands;

import necesse.engine.commands.*;
import necesse.engine.commands.parameterHandlers.*;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.LevelIdentifier;
import tpamod.data.BackData;

public class TPPosCommand extends ModularChatCommand {
    private final BackData backData;

    public TPPosCommand(String name) {
        super(name, "传送到指定坐标", PermissionLevel.ADMIN, false,
              new CmdParameter("x", new IntParameterHandler()),
              new CmdParameter("y", new IntParameterHandler()),
              new CmdParameter("level", new LevelIdentifierParameterHandler(null), true),
              new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
        this.backData = null;
    }

    public TPPosCommand(String name, BackData backData) {
        super(name, "传送到指定坐标", PermissionLevel.ADMIN, false,
              new CmdParameter("x", new IntParameterHandler()),
              new CmdParameter("y", new IntParameterHandler()),
              new CmdParameter("level", new LevelIdentifierParameterHandler(null), true),
              new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
        this.backData = backData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        int x = (int) args[0];
        int y = (int) args[1];
        LevelIdentifier levelIdentifier = (LevelIdentifier) args[2]; // 关卡参数，可选
        ServerClient target = (ServerClient) args[3];
        
        // 如果没有指定玩家，默认传送到自己
        if (target == null) {
            target = serverClient;
        }
        
        if (target != null) {
            // 记录传送前坐标到back系统
            if (backData != null && target.playerMob != null) {
                float currentX = target.playerMob.getX();
                float currentY = target.playerMob.getY();
                // 获取当前关卡标识符
                String currentLevelIdentifier = target.playerMob.getLevel().getIdentifier().toString();
                
                // 使用关卡标识符字符串作为关卡类型标识
                int levelType = Math.abs(currentLevelIdentifier.hashCode() % 1000);
                backData.recordTeleportPosition(String.valueOf(target.authentication),
                                              0, 0, levelType,
                                              (int)currentX, (int)currentY);
            }
            
            // 处理传送
            if (levelIdentifier != null) {
                // 普通关卡传送
                handleLevelTeleport(target, levelIdentifier, x, y, logs);
            } else {
                // 没有指定关卡，只传送坐标
                target.playerMob.setPos(x, y, true);
                if (target == serverClient) {
                    logs.add("已传送到坐标 (" + x + ", " + y + ")");
                } else {
                    logs.add("已将玩家 " + target.getName() + " 传送到坐标 (" + x + ", " + y + ")");
                }
            }
            
        } else {
            logs.add("无法获取玩家信息");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
    
    // 处理关卡传送
    private void handleLevelTeleport(ServerClient target, LevelIdentifier targetLevelIdentifier, int x, int y, CommandLog logs) {
        try {
            // 检查是否需要切换关卡
            LevelIdentifier currentLevelIdentifier = target.playerMob.getLevel().getIdentifier();
            String levelName = targetLevelIdentifier.toString();
            
            if (!currentLevelIdentifier.equals(targetLevelIdentifier)) {
                // 需要切换关卡
                logs.add("正在切换关卡...");
                logs.add("目标关卡: " + levelName + " (当前关卡: " + currentLevelIdentifier.toString() + ")");
                
                try {
                    // 使用changeLevel API切换关卡
                    target.changeLevel(targetLevelIdentifier, level -> {
                        return new java.awt.Point(x, y);
                    }, true);
                    logs.add("关卡切换成功");
                    logs.add("已切换到关卡: " + levelName);
                    logs.add("已传送到坐标 (" + x + ", " + y + ")");
                } catch (Exception e) {
                    logs.add("关卡切换失败: " + e.getMessage());
                    logs.add("请稍后重试");
                }
            } else {
                // 在同一关卡内传送
                target.playerMob.setPos(x, y, true);
                logs.add("已传送到坐标 (" + x + ", " + y + ") 关卡: " + levelName);
            }
        } catch (Exception e) {
            logs.add("传送失败: " + e.getMessage());
        }
    }
}