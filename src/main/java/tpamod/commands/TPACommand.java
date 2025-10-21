package tpamod.commands;

import tpamod.events.TPARequestEvent;
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

public class TPACommand extends ModularChatCommand {
    public TPACommand(String name) {
        super(name, "请求传送到另一个玩家", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler()));
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        ServerClient target = (ServerClient)args[0];
        if (target != null && target != serverClient) {
            String clientName = serverClient == null ? "服务器" : serverClient.getName();
            logs.add("传送到 " + target.getName());
            logs.addClient("来自 " + clientName + " 的传送请求", target);
            logs.addClient("使用 /tpac 或 /tpad " + clientName, target);
            AtomicBoolean accepted = new AtomicBoolean(false);
            GameEvents.triggerEvent(new TPARequestEvent(serverClient, target, server, logs), (TPARequestEvent e) -> {
                accepted.set(true);
            });
            if(!accepted.get()) {
                logs.addClient(clientName + " 已有待处理的传送请求", serverClient);
            }
        } else {
            logs.add("不能对自己使用");
        }
    }
    public boolean shouldBeListed() {
        return true;
    }
}