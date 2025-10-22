package tpamod.commands;

import tpamod.data.BackConfig;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class BackCDCommand extends ModularChatCommand {
    private final BackConfig backConfig;

    public BackCDCommand(String name, BackConfig backConfig) {
        super(name, "设置返回冷却时间（秒）", PermissionLevel.ADMIN, false,
              new CmdParameter("seconds", new IntParameterHandler()));
        this.backConfig = backConfig;
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
        backConfig.setCooldownSeconds(seconds);
        logs.add("返回冷却时间已设置为 " + seconds + " 秒");
    }

    public boolean shouldBeListed() {
        return true;
    }
}