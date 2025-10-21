package tpamod.commands;

import tpamod.events.TPAResponseEvent;
import necesse.engine.GameEvents;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class TPAdCommand extends ModularChatCommand {
    public TPAdCommand(String name) {
        super(name, "拒绝来自其他玩家的传送请求", PermissionLevel.USER, false);
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        AtomicBoolean accepted = new AtomicBoolean(false);
            GameEvents.triggerEvent(new TPAResponseEvent(serverClient, false), (TPAResponseEvent e) -> {
                accepted.set(true);
            });
        if(!accepted.get()) {
            logs.addClient("没有待处理的传送请求", serverClient);
        }
    }
    public boolean shouldBeListed() {
        return false;
    }
}