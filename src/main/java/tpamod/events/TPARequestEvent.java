package tpamod.events;

import necesse.engine.commands.CommandLog;
import necesse.engine.events.PreventableGameEvent;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import tpamod.data.BackData;
// import necesse.engine.util.LevelIdentifier;
// import necesse.entity.mobs.PlayerMob;

public class TPARequestEvent extends PreventableGameEvent {
    public final ServerClient target;
    public final ServerClient source;
    // private final Server server;
    private final CommandLog logs;
    private final BackData backData;

    public TPARequestEvent(ServerClient source, ServerClient target, Server server, CommandLog logs, BackData backData) {
        this.source = source;
        this.target = target;
        // this.server = server;
        this.logs = logs;
        this.backData = backData;
    }

    public void execute() {
        // 简化传送逻辑 - 直接传送到目标玩家位置
        if (source != null && target != null && target.playerMob != null) {
            // 记录传送前坐标到back系统
            if (source.playerMob != null) {
                float currentX = source.playerMob.getX();
                float currentY = source.playerMob.getY();
                // 使用关卡标识符字符串作为关卡类型标识
                String levelIdentifier = source.playerMob.getLevel().getIdentifier().toString();
                int currentLevelType = getLevelTypeFromIdentifier(levelIdentifier);
                
                backData.recordTeleportPosition(String.valueOf(source.authentication),
                                              0, 0, currentLevelType,
                                              (int)currentX, (int)currentY);
            }
            
            source.playerMob.setPos(target.playerMob.getX(), target.playerMob.getY(), true);
            logs.addClient("已传送到 " + target.getName(), source);
        }
    }

    public void expire() {
        logs.addClient("对 " + target.getName() + " 的传送请求已过期", source);
        logs.addClient("来自 " + source.getName() + " 的传送请求已过期", target);
    }

    public void reject() {
        logs.addClient("对 " + target.getName() + " 的传送请求已被拒绝", source);
        logs.addClient("来自 " + source.getName() + " 的传送请求已被拒绝", target);
    }

    // 根据关卡标识符获取关卡类型
    private int getLevelTypeFromIdentifier(String levelIdentifier) {
        try {
            // 简化处理：使用关卡标识符的哈希值作为类型标识
            // 在实际使用中，关卡类型主要用于区分不同的关卡位置
            return Math.abs(levelIdentifier.hashCode() % 1000);
        } catch (Exception e) {
            System.out.println("TPA System: Failed to get level type from identifier: " + levelIdentifier);
            return 0; // 默认关卡
        }
    }
    
}