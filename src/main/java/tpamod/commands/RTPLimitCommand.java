package tpamod.commands;

import tpamod.listener.RTPListener;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class RTPLimitCommand extends ModularChatCommand {
    private final RTPListener rtpListener;

    public RTPLimitCommand(String name, RTPListener rtpListener) {
        super(name, "设置随机传送的范围限制", PermissionLevel.ADMIN, false,
              new CmdParameter("range", new IntParameterHandler(), true));
        this.rtpListener = rtpListener;
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog logs) {
        if (args.length > 0) {
            // 设置新的范围限制
            int newRange = (int) args[0];
            rtpListener.setLimitRange(newRange);
            logs.add("随机传送范围限制已设置为: " + newRange);
            logs.add("新的随机传送将在距离边界 " + newRange + " 像素范围内生成坐标");
        } else {
            // 显示当前范围限制
            int currentRange = rtpListener.getLimitRange();
            logs.add("当前随机传送范围限制: " + currentRange);
            logs.add("随机传送在距离边界 " + currentRange + " 像素范围内生成坐标");
            logs.add("使用 /rtplimit <范围> 来修改限制 (最小范围: 100，无上限)");
        }
    }

    public boolean shouldBeListed() {
        return true;
    }
}