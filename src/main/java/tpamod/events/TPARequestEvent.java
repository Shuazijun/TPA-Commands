package tpamod.events;

import necesse.engine.commands.CommandLog;
import necesse.engine.events.PreventableGameEvent;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class TPARequestEvent extends PreventableGameEvent {
    public final ServerClient target;
    public final ServerClient source;
    // private final Server server;
    private final CommandLog logs;

    public TPARequestEvent(ServerClient source, ServerClient target, Server server, CommandLog logs) {
        this.source = source;
        this.target = target;
        // this.server = server;
        this.logs = logs;
    }

    public void execute() {
        // 简化传送逻辑 - 直接传送到目标玩家位置
        if (source != null && target != null && target.playerMob != null) {
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

}