package tpamod.commands;

import tpamod.events.TPAResponseEvent;
import necesse.engine.GameEvents;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.ServerClientParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class TPAcCommand extends ModularChatCommand {
    public TPAcCommand(String name) {
        super(name, "接受来自其他玩家的传送请求", PermissionLevel.USER, false,
              new CmdParameter("player", new ServerClientParameterHandler(), true));
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        ServerClient target = (ServerClient)args[0];
        AtomicBoolean accepted = new AtomicBoolean(false);
        
        if (target != null) {
            // 点对点同意：指定特定玩家的请求
            GameEvents.triggerEvent(new TPAResponseEvent(serverClient, target, true), (TPAResponseEvent e) -> {
                accepted.set(true);
            });
        } else {
            // 无参数：同意所有待处理请求（保持向后兼容）
            GameEvents.triggerEvent(new TPAResponseEvent(serverClient, null, true), (TPAResponseEvent e) -> {
                accepted.set(true);
            });
        }
        
        if(!accepted.get()) {
            if (target != null) {
                logs.addClient("没有来自 " + target.getName() + " 的待处理传送请求", serverClient);
            } else {
                logs.addClient("没有待处理的传送请求", serverClient);
            }
        }
    }
    public boolean shouldBeListed() {
        return false;
    }
}