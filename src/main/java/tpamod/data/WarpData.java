package tpamod.data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpData {
    private Map<String, WarpPoint> warpPoints;
    private File warpFile;

    public WarpData() {
        this.warpPoints = new HashMap<>();
        this.warpFile = new File("config/tpamod/warp.list");
        loadWarpPoints();
    }

    // 传送点数据类
    public static class WarpPoint {
        public final String name;
        public final String levelIdentifier; // 使用关卡标识符字符串
        public final float x;
        public final float y;

        public WarpPoint(String name, int islandX, int islandY, String levelIdentifier, float x, float y) {
            this.name = name;
            this.levelIdentifier = levelIdentifier;
            this.x = x;
            this.y = y;
        }
    }

    // 添加传送点
    public boolean addWarpPoint(String name, int islandX, int islandY, String levelIdentifier, float x, float y) {
        if (warpPoints.containsKey(name.toLowerCase())) {
            return false; // 传送点已存在
        }
        warpPoints.put(name.toLowerCase(), new WarpPoint(name, islandX, islandY, levelIdentifier, x, y));
        saveWarpPoints();
        return true;
    }

    // 删除传送点
    public boolean removeWarpPoint(String name) {
        if (!warpPoints.containsKey(name.toLowerCase())) {
            return false; // 传送点不存在
        }
        warpPoints.remove(name.toLowerCase());
        saveWarpPoints();
        return true;
    }

    // 获取传送点
    public WarpPoint getWarpPoint(String name) {
        return warpPoints.get(name.toLowerCase());
    }

    // 获取所有传送点名称
    public Set<String> getWarpPointNames() {
        return warpPoints.keySet();
    }

    // 检查传送点是否存在
    public boolean hasWarpPoint(String name) {
        return warpPoints.containsKey(name.toLowerCase());
    }

    // 保存传送点到文件
    private void saveWarpPoints() {
        try {
            warpFile.getParentFile().mkdirs(); // 确保目录存在
            try (PrintWriter writer = new PrintWriter(new FileWriter(warpFile))) {
                for (Map.Entry<String, WarpPoint> entry : warpPoints.entrySet()) {
                    WarpPoint point = entry.getValue();
                    writer.println(point.name + "," + 0 + "," + 0 + "," +
                                 point.levelIdentifier + "," + point.x + "," + point.y);
                }
            }
        } catch (Exception e) {
            System.out.println("Warp System: Failed to save warp points: " + e.getMessage());
        }
    }

    // 从文件加载传送点
    private void loadWarpPoints() {
        try {
            if (warpFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(warpFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            String name = parts[0];
                            // 跳过islandX和islandY参数（第1和第2部分），因为它们不再使用
                            String levelIdentifier = parts[3];
                            float x = Float.parseFloat(parts[4]);
                            float y = Float.parseFloat(parts[5]);
                            warpPoints.put(name.toLowerCase(), new WarpPoint(name, 0, 0, levelIdentifier, x, y));
                        }
                    }
                }
                System.out.println("Warp System: Loaded " + warpPoints.size() + " warp points");
            }
        } catch (Exception e) {
            System.out.println("Warp System: Failed to load warp points: " + e.getMessage());
        }
    }
}