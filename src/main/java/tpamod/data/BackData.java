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
        public int lastTeleportDimension;
        public int lastTeleportX;
        public int lastTeleportY;
        public String lastTeleportBiome; // 传送前群系
        
        public int lastDeathDimension;
        public int lastDeathX;
        public int lastDeathY;
        public String lastDeathBiome; // 死亡时群系
        
        public boolean hasDeathRecord;
        public boolean hasTeleportRecord;

        public PlayerBackData() {
            // 默认设置为出生点坐标
            this.lastTeleportDimension = 0;
            this.lastTeleportX = 0;
            this.lastTeleportY = 0;
            this.lastTeleportBiome = "default";
            
            this.lastDeathDimension = 0;
            this.lastDeathX = 0;
            this.lastDeathY = 0;
            this.lastDeathBiome = "default";
            
            this.hasDeathRecord = false;
            this.hasTeleportRecord = false;
        }
    }

    // 获取玩家的坐标记录
    public PlayerBackData getPlayerBackData(String playerAuth) {
        return playerBackDataMap.computeIfAbsent(playerAuth, k -> new PlayerBackData());
    }

    // 记录传送前坐标
    public void recordTeleportPosition(String playerAuth, int dimension, int x, int y) {
        recordTeleportPosition(playerAuth, dimension, x, y, "default");
    }
    
    // 记录传送前坐标（带群系信息）
    public void recordTeleportPosition(String playerAuth, int dimension, int x, int y, String biome) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.lastTeleportDimension = dimension;
        data.lastTeleportX = x;
        data.lastTeleportY = y;
        data.lastTeleportBiome = biome;
        data.hasTeleportRecord = true;
        saveBackData();
    }

    // 记录死亡坐标
    public void recordDeathPosition(String playerAuth, int dimension, int x, int y) {
        recordDeathPosition(playerAuth, dimension, x, y, "default");
    }
    
    // 记录死亡坐标（带群系信息）
    public void recordDeathPosition(String playerAuth, int dimension, int x, int y, String biome) {
        PlayerBackData data = getPlayerBackData(playerAuth);
        data.lastDeathDimension = dimension;
        data.lastDeathX = x;
        data.lastDeathY = y;
        data.lastDeathBiome = biome;
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
                playerData.addInt("teleportDimension", data.lastTeleportDimension);
                playerData.addInt("teleportX", data.lastTeleportX);
                playerData.addInt("teleportY", data.lastTeleportY);
                playerData.addUnsafeString("teleportBiome", data.lastTeleportBiome);
                playerData.addInt("deathDimension", data.lastDeathDimension);
                playerData.addInt("deathX", data.lastDeathX);
                playerData.addInt("deathY", data.lastDeathY);
                playerData.addUnsafeString("deathBiome", data.lastDeathBiome);
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
                            data.lastTeleportDimension = playerData.getInt("teleportDimension", 0);
                            data.lastTeleportX = playerData.getInt("teleportX", 0);
                            data.lastTeleportY = playerData.getInt("teleportY", 0);
                            data.lastTeleportBiome = playerData.getUnsafeString("teleportBiome", "default");
                            data.lastDeathDimension = playerData.getInt("deathDimension", 0);
                            data.lastDeathX = playerData.getInt("deathX", 0);
                            data.lastDeathY = playerData.getInt("deathY", 0);
                            data.lastDeathBiome = playerData.getUnsafeString("deathBiome", "default");
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