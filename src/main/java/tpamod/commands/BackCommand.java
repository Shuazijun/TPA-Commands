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
    private void teleportPlayer(ServerClient serverClient, int islandX, int islandY, int dimension, int x, int y, CommandLog logs) {
        PlayerMob playerMob = serverClient.playerMob;
        if (playerMob != null) {
            // 执行传送 - 简化版本，只设置位置
            playerMob.setPos((float)x, (float)y, true);
            
            logs.add("坐标: (" + x + ", " + y + ") 岛屿: (" + islandX + ", " + islandY + ")");
        } else {
            logs.add("玩家数据错误，传送失败");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
}