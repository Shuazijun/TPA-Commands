package tpamod.commands;

import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.ServerClientParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import tpamod.data.BackData;

public class TPPosCommand extends ModularChatCommand {
    private final BackData backData;

    public TPPosCommand(String name) {
        super(name, "传送到指定坐标", PermissionLevel.ADMIN, false,
              new CmdParameter("x", new IntParameterHandler()),
              new CmdParameter("y", new IntParameterHandler()),
              new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
        this.backData = null;
    }

    public TPPosCommand(String name, BackData backData) {
        super(name, "传送到指定坐标", PermissionLevel.ADMIN, false,
              new CmdParameter("x", new IntParameterHandler()),
              new CmdParameter("y", new IntParameterHandler()),
              new CmdParameter("player", new ServerClientParameterHandler(true, true), true));
        this.backData = backData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        int x = (int) args[0];
        int y = (int) args[1];
        ServerClient target = (ServerClient) args[2];
        
        // 如果没有指定玩家，默认传送到自己
        if (target == null) {
            target = serverClient;
        }
        
        if (target != null) {
            // 记录传送前坐标到back系统
            if (backData != null && target.playerMob != null) {
                float currentX = target.playerMob.getX();
                float currentY = target.playerMob.getY();
                // 使用默认岛屿和维度坐标
                int currentIslandX = 0;
                int currentIslandY = 0;
                int currentDimension = 0;
                
                backData.recordTeleportPosition(String.valueOf(target.authentication),
                                              currentIslandX, currentIslandY, currentDimension,
                                              (int)currentX, (int)currentY);
            }
            
            // 传送玩家到指定坐标
            target.playerMob.setPos(x, y, true);
            
            if (target == serverClient) {
                logs.add("已传送到坐标 (" + x + ", " + y + ")");
            } else {
                logs.add("已将玩家 " + target.getName() + " 传送到坐标 (" + x + ", " + y + ")");
            }
        } else {
            logs.add("无法获取玩家信息");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
}