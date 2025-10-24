package tpamod.util;

import necesse.engine.network.server.ServerClient;

public class PermissionUtil {
    
    /**
     * 检查玩家是否具有管理员权限（包括所有者）
     * 在Necesse中，服务器所有者通常具有最高权限，包括管理员权限
     * @param serverClient 服务器客户端
     * @return 如果玩家是管理员或所有者返回true
     */
    public static boolean isAdminOrOwner(ServerClient serverClient) {
        if (serverClient == null) {
            return false;
        }
        
        // 检查是否是管理员
        // 在Necesse中，服务器所有者通常也具有管理员权限
        return serverClient.getPermissionLevel() == necesse.engine.commands.PermissionLevel.ADMIN;
    }
}