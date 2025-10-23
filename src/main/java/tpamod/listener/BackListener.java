package tpamod.listener;

import tpamod.data.BackData;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;

import java.util.concurrent.TimeUnit;

public class BackListener {
    private final BackData backData;
    private final Cache<String, Long> cooldownCache;

    public BackListener(BackData backData) {
        this.backData = backData;
        this.cooldownCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    // 记录玩家传送前坐标
    public void recordTeleportPosition(ServerClient serverClient) {
        if (serverClient != null && serverClient.playerMob != null) {
            String playerAuth = String.valueOf(serverClient.authentication);
            PlayerMob playerMob = serverClient.playerMob;
            
            // 获取玩家当前位置
            // 使用关卡标识符字符串作为关卡类型标识
            String levelIdentifier = playerMob.getLevel().getIdentifier().toString();
            int levelType = getLevelTypeFromIdentifier(levelIdentifier);
            int x = (int) playerMob.x;
            int y = (int) playerMob.y;
            
            backData.recordTeleportPosition(playerAuth, 0, 0, levelType, x, y);
            System.out.println("Back System: Recorded teleport position for " + serverClient.getName());
        }
    }

    // 记录玩家死亡坐标
    public void recordDeathPosition(ServerClient serverClient) {
        if (serverClient != null && serverClient.playerMob != null) {
            String playerAuth = String.valueOf(serverClient.authentication);
            PlayerMob playerMob = serverClient.playerMob;
            
            // 获取玩家当前位置
            // 使用关卡标识符字符串作为关卡类型标识
            String levelIdentifier = playerMob.getLevel().getIdentifier().toString();
            int levelType = getLevelTypeFromIdentifier(levelIdentifier);
            int x = (int) playerMob.x;
            int y = (int) playerMob.y;
            
            backData.recordDeathPosition(playerAuth, 0, 0, levelType, x, y);
            System.out.println("Back System: Recorded death position for " + serverClient.getName());
        }
    }

    // 设置冷却时间
    public void setCooldown(String playerAuth, int cooldownSeconds) {
        long cooldownEndTime = System.currentTimeMillis() + (cooldownSeconds * 1000L);
        cooldownCache.put(playerAuth, cooldownEndTime);
    }

    // 获取剩余冷却时间（毫秒）
    public long getRemainingCooldown(String playerAuth) {
        Long cooldownEndTime = cooldownCache.getIfPresent(playerAuth);
        if (cooldownEndTime != null) {
            long remaining = cooldownEndTime - System.currentTimeMillis();
            return Math.max(0, remaining);
        }
        return 0;
    }

    // 设置冷却时间（秒）- 用于BackCDCommand
    public void setCooldownSeconds(int seconds) {
        // 这个方法用于BackCDCommand设置全局冷却时间
        // 注意：这里我们只更新配置，实际冷却时间在BackConfig中管理
        System.out.println("Back System: Global cooldown time updated to " + seconds + " seconds");
    }
    
    // 根据关卡标识符获取关卡类型
    private int getLevelTypeFromIdentifier(String levelIdentifier) {
        try {
            // 简化处理：使用关卡标识符的哈希值作为类型标识
            // 在实际使用中，关卡类型主要用于区分不同的关卡位置
            return Math.abs(levelIdentifier.hashCode() % 1000);
        } catch (Exception e) {
            System.out.println("Back System: Failed to get level type from identifier: " + levelIdentifier);
            return 0; // 默认关卡
        }
    }
}