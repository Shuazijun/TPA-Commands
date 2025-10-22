package tpamod.commands;

import tpamod.data.WarpData;
import tpamod.data.WarpConfig;
import tpamod.listener.WarpListener;
import tpamod.data.BackData;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;

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
        // 简化权限检查，假设所有玩家都受冷却限制
        long remainingCooldown = warpListener.getRemainingCooldown(serverClient.authentication);
        if (remainingCooldown > 0) {
            logs.add("传送冷却中，剩余时间: " + (remainingCooldown / 1000) + "秒");
            return;
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
            // 使用默认岛屿和维度坐标
            int currentIslandX = 0;
            int currentIslandY = 0;
            int currentDimension = 0;
            
            backData.recordTeleportPosition(String.valueOf(serverClient.authentication),
                                          currentIslandX, currentIslandY, currentDimension,
                                          (int)currentX, (int)currentY);
            
            // 设置冷却时间（所有玩家都受冷却限制）
            warpListener.setCooldown(serverClient.authentication, warpConfig.getCooldownSeconds());
            
            // 执行传送 - 简化版本，只设置位置
            playerMob.setPos(warpPoint.x, warpPoint.y, true);
            
            logs.add("已传送到传送点: " + warpName);
            logs.add("坐标: (" + String.format("%.2f", warpPoint.x) + ", " + String.format("%.2f", warpPoint.y) + ")");
        } else {
            logs.add("玩家数据错误，传送失败");
        }
    }


    public boolean shouldBeListed() {
        return true;
    }
}