package tpamod.listener;

import tpamod.data.RTPConfig;
import tpamod.util.PermissionUtil;
import necesse.engine.network.server.ServerClient;
import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;

public class RTPListener {
    private RTPConfig config;
    private Cache<String, Long> rtpCooldowns;

    public RTPListener() {
        this.config = new RTPConfig();
        rebuildCache();
    }

    private void rebuildCache() {
        this.rtpCooldowns = CacheBuilder.newBuilder()
                .expireAfterWrite(config.getCooldownSeconds(), TimeUnit.SECONDS)
                .build();
    }

    // 检查玩家是否在冷却中
    public boolean isOnCooldown(String playerName) {
        return rtpCooldowns.getIfPresent(playerName) != null;
    }

    // 获取剩余冷却时间
    public long getRemainingCooldown(long playerAuth) {
        String playerKey = String.valueOf(playerAuth);
        Long lastRTPTime = rtpCooldowns.getIfPresent(playerKey);
        if (lastRTPTime == null) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastRTPTime;
        long remaining = (config.getCooldownSeconds() * 1000) - elapsed;
        return Math.max(0, remaining);
    }

    // 设置玩家冷却时间
    public void setCooldown(long playerAuth, int cooldownSeconds) {
        String playerKey = String.valueOf(playerAuth);
        rtpCooldowns.put(playerKey, System.currentTimeMillis());
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

    // 检查玩家是否受冷却限制（管理员和所有者不受限制）
    public boolean isCooldownExempt(ServerClient serverClient) {
        return PermissionUtil.isAdminOrOwner(serverClient);
    }
}