package tpamod.commands;

import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import tpamod.listener.TPAListener;

public class TPAcdCommand extends ModularChatCommand {
    private final TPAListener listener;

    public TPAcdCommand(String name, TPAListener listener) {
        super(name, "设置TPA请求的冷却时间（秒）", PermissionLevel.ADMIN, false,
              new necesse.engine.commands.CmdParameter("seconds", new IntParameterHandler()));
        this.listener = listener;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        int seconds = (Integer)args[0];
        if (seconds < 1) {
            logs.add("冷却时间不能小于1秒");
            return;
        }
        if (seconds > 3600) {
            logs.add("冷却时间不能超过3600秒（1小时）");
            return;
        }
        listener.setCooldownSeconds(seconds);
        logs.add("TPA请求冷却时间已设置为 " + seconds + " 秒");
    }

    public boolean shouldBeListed() {
        return true;
    }
}