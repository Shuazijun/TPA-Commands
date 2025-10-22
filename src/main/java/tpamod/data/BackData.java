package tpamod.data;

import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BackData {
    private Map<String, PlayerBackData> playerBackDataMap;
    private File backFile;

    public BackData() {
        this.playerBackDataMap = new HashMap<>();
        this.backFile = new File("config/tpamod/backlist.dat");
        loadBackData();
    }

    // 玩家坐标记录数据结构
    public static class PlayerBackData {
        public int lastTeleportIslandX;
        public int lastTeleportIslandY;
        public int lastTeleportDimension;
        public int lastTeleportX;
        public int lastTeleportY;
        
        public int lastDeathIslandX;
        public int lastDeathIslandY;
        public int lastDeathDimension;
        public int lastDeathX;
        public int lastDeathY;
        
        public boolean hasDeathRecord;
        public boolean hasTeleportRecord;

        public PlayerBackData() {
            // 默认设置为出生点坐标
            this.lastTeleportIslandX = 0;
            this.lastTeleportIslandY = 0;
            this.lastTeleportDimension = 0;
            this.lastTeleportX = 0;
            this.lastTeleportY = 0;
            
            this.lastDeathIslandX = 0;
            this.lastDeathIslandY = 0;
            this.lastDeathDimension = 0;
            this.lastDeathX = 0;
            this.lastDeathY = 0;
            
            this.hasDeathRecord = false;
            this.hasTeleportRecord = false;
        }
    }

    // 获取玩家的坐标记录
    public PlayerBackData getPlayerBackData(String playerAuth) {
        return playerBackDataMap.computeIfAbsent(playerAuth, k -> new PlayerBackData());
    }

    // 记录传送前坐标
    public void recordTeleportPosition(String playerAuth, int islandX, int islandY, int dimension, int x, int y) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.lastTeleportIslandX = islandX;
        data.lastTeleportIslandY = islandY;
        data.lastTeleportDimension = dimension;
        data.lastTeleportX = x;
        data.lastTeleportY = y;
        data.hasTeleportRecord = true;
        saveBackData();
    }

    // 记录死亡坐标
    public void recordDeathPosition(String playerAuth, int islandX, int islandY, int dimension, int x, int y) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.lastDeathIslandX = islandX;
        data.lastDeathIslandY = islandY;
        data.lastDeathDimension = dimension;
        data.lastDeathX = x;
        data.lastDeathY = y;
        data.hasDeathRecord = true;
        saveBackData();
    }

    // 清除死亡记录（使用back后）
    public void clearDeathRecord(String playerAuth) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.hasDeathRecord = false;
        saveBackData();
    }

    // 清除传送记录（使用back后）
    public void clearTeleportRecord(String playerAuth) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.hasTeleportRecord = false;
        saveBackData();
    }

    // 保存数据到文件
    private void saveBackData() {
        try {
            SaveData saveData = new SaveData("BackData");
            
            for (Map.Entry<String, PlayerBackData> entry : playerBackDataMap.entrySet()) {
                String playerAuth = entry.getKey();
                PlayerBackData data = entry.getValue();
                
                SaveData playerData = new SaveData("PlayerBack");
                playerData.addUnsafeString("auth", playerAuth);
                playerData.addInt("teleportIslandX", data.lastTeleportIslandX);
                playerData.addInt("teleportIslandY", data.lastTeleportIslandY);
                playerData.addInt("teleportDimension", data.lastTeleportDimension);
                playerData.addInt("teleportX", data.lastTeleportX);
                playerData.addInt("teleportY", data.lastTeleportY);
                playerData.addInt("deathIslandX", data.lastDeathIslandX);
                playerData.addInt("deathIslandY", data.lastDeathIslandY);
                playerData.addInt("deathDimension", data.lastDeathDimension);
                playerData.addInt("deathX", data.lastDeathX);
                playerData.addInt("deathY", data.lastDeathY);
                playerData.addBoolean("hasDeathRecord", data.hasDeathRecord);
                playerData.addBoolean("hasTeleportRecord", data.hasTeleportRecord);
                
                saveData.addSaveData(playerData);
            }
            
            backFile.getParentFile().mkdirs();
            saveData.saveScript(backFile);
        } catch (Exception e) {
            System.out.println("Back System: Failed to save back data: " + e.getMessage());
        }
    }

    // 从文件加载数据
    private void loadBackData() {
        try {
            if (backFile.exists()) {
                LoadData loadData = new LoadData(backFile);
                playerBackDataMap.clear();
                
                for (LoadData playerData : loadData.getLoadData()) {
                    // 只处理PlayerBack类型的数据
                    if ("PlayerBack".equals(playerData.getName())) {
                        String playerAuth = playerData.getUnsafeString("auth", "");
                        if (!playerAuth.isEmpty()) {
                            PlayerBackData data = new PlayerBackData();
                            data.lastTeleportIslandX = playerData.getInt("teleportIslandX", 0);
                            data.lastTeleportIslandY = playerData.getInt("teleportIslandY", 0);
                            data.lastTeleportDimension = playerData.getInt("teleportDimension", 0);
                            data.lastTeleportX = playerData.getInt("teleportX", 0);
                            data.lastTeleportY = playerData.getInt("teleportY", 0);
                            data.lastDeathIslandX = playerData.getInt("deathIslandX", 0);
                            data.lastDeathIslandY = playerData.getInt("deathIslandY", 0);
                            data.lastDeathDimension = playerData.getInt("deathDimension", 0);
                            data.lastDeathX = playerData.getInt("deathX", 0);
                            data.lastDeathY = playerData.getInt("deathY", 0);
                            data.hasDeathRecord = playerData.getBoolean("hasDeathRecord", false);
                            data.hasTeleportRecord = playerData.getBoolean("hasTeleportRecord", false);
                            
                            playerBackDataMap.put(playerAuth, data);
                        }
                    }
                }
                System.out.println("Back System: Loaded back data for " + playerBackDataMap.size() + " players");
            }
        } catch (Exception e) {
            System.out.println("Back System: Failed to load back data: " + e.getMessage());
        }
    }
}