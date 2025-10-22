package tpamod.commands;

import tpamod.data.WarpData;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class DelWarpCommand extends ModularChatCommand {
    private final WarpData warpData;

    public DelWarpCommand(String name, WarpData warpData) {
        super(name, "删除传送点", PermissionLevel.ADMIN, false,
              new CmdParameter("warpName", new PresetStringParameterHandler(false, "home", "spawn", "town", "base")));
        this.warpData = warpData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        String warpName = (String) args[0];
        
        if (warpData.removeWarpPoint(warpName)) {
            logs.add("传送点 '" + warpName + "' 已删除");
        } else {
            logs.add("传送点 '" + warpName + "' 不存在");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
}