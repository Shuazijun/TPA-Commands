package tpamod.commands;

import tpamod.events.TPARequestEvent;
import tpamod.listener.TPAListener;
import tpamod.data.BackData;
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
    private final TPAListener listener;
    private final BackData backData;

    public TPACommand(String name) {
        super(name, "请求传送到另一个玩家", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler()));
        this.listener = null;
        this.backData = null;
    }

    public TPACommand(String name, TPAListener listener) {
        super(name, "请求传送到另一个玩家", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler()));
        this.listener = listener;
        this.backData = null;
    }

    public TPACommand(String name, TPAListener listener, BackData backData) {
        super(name, "请求传送到另一个玩家", PermissionLevel.USER, false, new CmdParameter("player", new ServerClientParameterHandler()));
        this.listener = listener;
        this.backData = backData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        ServerClient target = (ServerClient)args[0];
        if (target != null && target != serverClient) {
            String clientName = serverClient == null ? "服务器" : serverClient.getName();
            logs.add("传送到 " + target.getName());
            logs.addClient("来自 " + clientName + " 的传送请求", target);
            logs.addClient("使用 /tpac 或 /tpad " + clientName, target);
            AtomicBoolean accepted = new AtomicBoolean(false);
            GameEvents.triggerEvent(new TPARequestEvent(serverClient, target, server, logs, backData), (TPARequestEvent e) -> {
                accepted.set(true);
            });
            if(!accepted.get()) {
                int cooldown = getCooldownSeconds();
                logs.addClient(clientName + " 已有待处理的传送请求，请等待 " + cooldown + " 秒后重试", serverClient);
            }
        } else {
            logs.add("不能对自己使用");
        }
    }

    // 获取当前冷却时间（秒）
    private int getCooldownSeconds() {
        if (listener != null) {
            return listener.getCooldownSeconds();
        }
        return 30; // 默认值
    }
    public boolean shouldBeListed() {
        return true;
    }
}