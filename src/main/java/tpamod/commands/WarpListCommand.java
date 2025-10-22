package tpamod.commands;

import tpamod.data.WarpData;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

import java.util.Set;

public class WarpListCommand extends ModularChatCommand {
    private final WarpData warpData;

    public WarpListCommand(String name, WarpData warpData) {
        super(name, "查看传送点列表", PermissionLevel.USER, false);
        this.warpData = warpData;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        Set<String> warpNames = warpData.getWarpPointNames();
        
        if (warpNames.isEmpty()) {
            logs.add("当前没有可用的传送点");
            return;
        }
        
        logs.add("可用的传送点 (" + warpNames.size() + " 个):");
        int count = 0;
        StringBuilder warpList = new StringBuilder();
        
        for (String warpName : warpNames) {
            if (count > 0) {
                warpList.append(", ");
            }
            warpList.append(warpName);
            count++;
            
            // 每行显示5个传送点
            if (count % 5 == 0) {
                logs.add(warpList.toString());
                warpList = new StringBuilder();
            }
        }
        
        // 显示剩余的传送点
        if (warpList.length() > 0) {
            logs.add(warpList.toString());
        }
        
        logs.add("使用 /warp [传送点名称] 进行传送");
    }

    public boolean shouldBeListed() {
        return true;
    }
}