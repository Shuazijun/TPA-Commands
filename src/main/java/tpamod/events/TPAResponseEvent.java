package tpamod.events;

import necesse.engine.events.PreventableGameEvent;
import necesse.engine.network.server.ServerClient;

public class TPAResponseEvent extends PreventableGameEvent {
    public final ServerClient teleportTarget;
    public final ServerClient sourcePlayer; // 新增：指定源玩家（可为null表示所有请求）
    public final boolean accepted;

    public TPAResponseEvent(ServerClient teleportTarget, boolean accepted) {
        this.teleportTarget = teleportTarget;
        this.sourcePlayer = null;
        this.accepted = accepted;
    }

    public TPAResponseEvent(ServerClient teleportTarget, ServerClient sourcePlayer, boolean accepted) {
        this.teleportTarget = teleportTarget;
        this.sourcePlayer = sourcePlayer;
        this.accepted = accepted;
    }
}