package tpamod;

import tpamod.commands.TPACommand;
import tpamod.commands.TPAcCommand;
import tpamod.commands.TPAdCommand;
import tpamod.events.TPARequestEvent;
import tpamod.events.TPAResponseEvent;
import tpamod.listener.TPAListener;
import necesse.engine.GameEvents;
import necesse.engine.commands.CommandsManager;
import necesse.engine.modLoader.annotations.ModEntry;

@ModEntry
public class TPAMod {

    public void preInit() {
        // 预初始化代码可以放在这里
    }

    public void init() {
        System.out.println("TPA Commands Mod initialized!");

        // 初始化传送系统监听器
        TPAListener listener = new TPAListener();
        GameEvents.addListener(TPARequestEvent.class, listener.getRequestListener());
        GameEvents.addListener(TPAResponseEvent.class, listener.getResponseListener());
        
        // 注册传送命令
        CommandsManager.registerServerCommand(new TPACommand("tpa"));
        CommandsManager.registerServerCommand(new TPAcCommand("tpac"));
        CommandsManager.registerServerCommand(new TPAdCommand("tpad"));
        CommandsManager.registerServerCommand(new TPACommand("传送请求"));
        CommandsManager.registerServerCommand(new TPAcCommand("同意传送"));
        CommandsManager.registerServerCommand(new TPAdCommand("拒绝传送"));
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