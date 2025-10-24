package tpamod;

import tpamod.commands.TPACommand;
import tpamod.commands.TPAcCommand;
import tpamod.commands.TPAdCommand;
import tpamod.commands.TPAcdCommand;
import tpamod.commands.GetPosCommand;
import tpamod.commands.TPPosCommand;
import tpamod.commands.NewWarpCommand;
import tpamod.commands.DelWarpCommand;
import tpamod.commands.WarpCommand;
import tpamod.commands.WarpListCommand;
import tpamod.commands.WarpCDCommand;
import tpamod.commands.BackCommand;
import tpamod.commands.BackCDCommand;
import tpamod.commands.RTPCommand;
import tpamod.commands.RTPCDCommand;
import tpamod.events.TPARequestEvent;
import tpamod.events.TPAResponseEvent;
import tpamod.listener.TPAListener;
import tpamod.listener.WarpListener;
import tpamod.listener.BackListener;
import tpamod.listener.RTPListener;
import tpamod.data.WarpData;
import tpamod.data.WarpConfig;
import tpamod.data.BackData;
import tpamod.data.BackConfig;
import necesse.engine.GameEvents;
import necesse.engine.commands.CommandsManager;
import necesse.engine.modLoader.annotations.ModEntry;

@ModEntry
public class TPAMod {
    private TPAListener listener;

    public void preInit() {
        // 预初始化代码可以放在这里
    }

    public void init() {
        System.out.println("TPA Commands Mod initialized!");

        // 初始化传送系统监听器
        listener = new TPAListener();
        GameEvents.addListener(TPARequestEvent.class, listener.getRequestListener());
        GameEvents.addListener(TPAResponseEvent.class, listener.getResponseListener());
        
        // 初始化传送点系统
        WarpData warpData = new WarpData();
        WarpConfig warpConfig = new WarpConfig();
        WarpListener warpListener = new WarpListener();
        
        // 初始化返回系统
        BackData backData = new BackData();
        BackConfig backConfig = new BackConfig();
        BackListener backListener = new BackListener(backData);
        
        // 初始化随机传送系统
        RTPListener rtpListener = new RTPListener();
        
        // 注册传送命令
        CommandsManager.registerServerCommand(new TPACommand("tpa", listener, backData));
        CommandsManager.registerServerCommand(new TPAcCommand("tpac"));
        CommandsManager.registerServerCommand(new TPAdCommand("tpad"));
        CommandsManager.registerServerCommand(new TPACommand("传送请求", listener, backData));
        CommandsManager.registerServerCommand(new TPAcCommand("同意传送"));
        CommandsManager.registerServerCommand(new TPAdCommand("拒绝传送"));
        
        // 注册管理命令
        CommandsManager.registerServerCommand(new TPAcdCommand("tpacd", listener));
        CommandsManager.registerServerCommand(new TPAcdCommand("设置传送请求冷却时间", listener));
        
        // 注册坐标查询命令（已包含关卡信息）
        CommandsManager.registerServerCommand(new GetPosCommand("getpos"));
        CommandsManager.registerServerCommand(new GetPosCommand("获取坐标"));
        
        
        // 注册管理员传送命令
        CommandsManager.registerServerCommand(new TPPosCommand("tppos", backData));
        CommandsManager.registerServerCommand(new TPPosCommand("传送坐标", backData));
        
        // 注册传送点系统命令
        CommandsManager.registerServerCommand(new NewWarpCommand("newwarp", warpData));
        CommandsManager.registerServerCommand(new NewWarpCommand("新建传送点", warpData));
        CommandsManager.registerServerCommand(new DelWarpCommand("delwarp", warpData));
        CommandsManager.registerServerCommand(new DelWarpCommand("删除传送点", warpData));
        CommandsManager.registerServerCommand(new WarpCommand("warp", warpData, warpConfig, warpListener, backData));
        CommandsManager.registerServerCommand(new WarpCommand("传送点", warpData, warpConfig, warpListener, backData));
        CommandsManager.registerServerCommand(new WarpListCommand("warplist", warpData));
        CommandsManager.registerServerCommand(new WarpListCommand("传送点列表", warpData));
        CommandsManager.registerServerCommand(new WarpCDCommand("warpcd", warpListener));
        CommandsManager.registerServerCommand(new WarpCDCommand("设置传送点冷却时间", warpListener));
        
        // 注册返回系统命令
        CommandsManager.registerServerCommand(new BackCommand("back", backData, backConfig, backListener));
        CommandsManager.registerServerCommand(new BackCommand("返回", backData, backConfig, backListener));
        CommandsManager.registerServerCommand(new BackCDCommand("backcd", backConfig));
        CommandsManager.registerServerCommand(new BackCDCommand("设置返回冷却时间", backConfig));
        
        // 注册随机传送系统命令
        CommandsManager.registerServerCommand(new RTPCommand("rtp", rtpListener, backData));
        CommandsManager.registerServerCommand(new RTPCommand("随机传送", rtpListener, backData));
        CommandsManager.registerServerCommand(new RTPCDCommand("rtpcd", rtpListener));
        CommandsManager.registerServerCommand(new RTPCDCommand("设置随机传送冷却时间", rtpListener));
    }

    public void initResources() {
        // 资源初始化
    }

    public void postInit() {
        // 后初始化代码
    }

    public void dispose() {
        // 清理资源
    }
}