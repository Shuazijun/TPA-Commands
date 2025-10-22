package tpamod.listener;

import tpamod.data.WarpConfig;
import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;

public class WarpListener {
    private WarpConfig config;
    private Cache<String, Long> warpCooldowns;

    public WarpListener() {
        this.config = new WarpConfig();
        rebuildCache();
    }

    private void rebuildCache() {
        this.warpCooldowns = CacheBuilder.newBuilder()
                .expireAfterWrite(config.getCooldownSeconds(), TimeUnit.SECONDS)
                .build();
    }

    // 检查玩家是否在冷却中
    public boolean isOnCooldown(String playerName) {
        return warpCooldowns.getIfPresent(playerName) != null;
    }

    // 获取剩余冷却时间
    public long getRemainingCooldown(long playerAuth) {
        String playerKey = String.valueOf(playerAuth);
        Long lastWarpTime = warpCooldowns.getIfPresent(playerKey);
        if (lastWarpTime == null) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastWarpTime;
        long remaining = (config.getCooldownSeconds() * 1000) - elapsed;
        return Math.max(0, remaining);
    }

    // 设置玩家冷却时间
    public void setCooldown(long playerAuth, int cooldownSeconds) {
        String playerKey = String.valueOf(playerAuth);
        warpCooldowns.put(playerKey, System.currentTimeMillis());
    }

    // 获取冷却时间配置
    public int getCooldownSeconds() {
        return config.getCooldownSeconds();
    }

    // 设置冷却时间配置
    public void setCooldownSeconds(int seconds) {
        config.setCooldownSeconds(seconds);
        rebuildCache();
    }
}