package tpamod.commands;

import tpamod.listener.RTPListener;
import tpamod.data.BackData;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameRandom;
import necesse.level.maps.Level;

public class RTPCommand extends ModularChatCommand {
    private final RTPListener rtpListener;
    private final BackData backData;

    public RTPCommand(String name, RTPListener rtpListener, BackData backData) {
        super(name, "随机传送到当前关卡的随机位置", PermissionLevel.USER, false);
        this.rtpListener = rtpListener;
        this.backData = backData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        try {
            // 检查玩家是否在冷却中（管理员和所有者不受限制）
            if (!rtpListener.isCooldownExempt(serverClient)) {
                long remainingCooldown = rtpListener.getRemainingCooldown(serverClient.authentication);
                if (remainingCooldown > 0) {
                    long remainingSeconds = (remainingCooldown + 999) / 1000; // 向上取整到秒
                    logs.add("随机传送冷却中，剩余时间: " + remainingSeconds + " 秒");
                    return;
                }
            }

            // 获取玩家当前关卡
            Level currentLevel = serverClient.getLevel();
            if (currentLevel == null) {
                logs.add("无法获取当前关卡信息");
                return;
            }

            // 记录传送前的坐标和群系信息（用于返回命令）
            float originalX = serverClient.playerMob.x;
            float originalY = serverClient.playerMob.y;
            String originalBiome = getCurrentBiomeIdentifier(currentLevel, originalX, originalY);
            
            // 使用配置的范围限制生成随机坐标
            int limitRange = rtpListener.getLimitRange();
            float randomX = GameRandom.globalRandom.getFloatBetween(limitRange, currentLevel.tileWidth * 32 - limitRange);
            float randomY = GameRandom.globalRandom.getFloatBetween(limitRange, currentLevel.tileHeight * 32 - limitRange);

            // 执行传送
            serverClient.playerMob.setPos(randomX, randomY, true);
            
            // 记录传送信息到返回系统
            backData.recordTeleportPosition(
                String.valueOf(serverClient.authentication),
                0, // dimension参数不再使用，使用关卡标识符
                (int)originalX,
                (int)originalY,
                originalBiome
            );

            // 设置冷却时间（管理员和所有者不受限制）
            if (!rtpListener.isCooldownExempt(serverClient)) {
                rtpListener.setCooldown(serverClient.authentication, rtpListener.getCooldownSeconds());
            }

            logs.add("随机传送成功！新位置: (" + (int)randomX + ", " + (int)randomY + ")");
            
        } catch (Exception e) {
            logs.add("随机传送失败: " + e.getMessage());
            System.out.println("RTP Command Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // 获取当前位置的群系标识符
    private String getCurrentBiomeIdentifier(Level level, float x, float y) {
        try {
            int tileX = (int)(x / 32.0F);
            int tileY = (int)(y / 32.0F);
            
            var biome = level.getBiome(tileX, tileY);
            if (biome != null) {
                return biome.getStringID();
            }
        } catch (Exception e) {
            System.out.println("Failed to get biome identifier: " + e.getMessage());
        }
        return "unknown";
    }
}