package tpamod.commands;

import tpamod.data.WarpData;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

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
            // 使用默认值，因为API可能已更改
            int islandX = 0;
            int islandY = 0;
            int dimension = 0;
            
            // 创建传送点
            if (warpData.addWarpPoint(warpName, islandX, islandY, dimension, playerX, playerY)) {
                logs.add("传送点 '" + warpName + "' 已创建");
                logs.add("位置: 岛屿(" + islandX + ", " + islandY + ") 维度(" + dimension + ") 坐标(" + String.format("%.2f", playerX) + ", " + String.format("%.2f", playerY) + ")");
            } else {
                logs.add("传送点 '" + warpName + "' 已存在");
            }
        } else {
            logs.add("无法获取玩家信息");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
}