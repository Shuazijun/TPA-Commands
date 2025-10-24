package tpamod.data;

import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import java.io.File;

public class RTPLimitConfig {
    private int limitRange = 1000; // 默认范围1000
    private File configFile;

    public RTPLimitConfig() {
        this.configFile = new File("config/tpamod/rtplimit.dat");
        loadConfig();
    }

    // 获取限制范围
    public int getLimitRange() {
        return limitRange;
    }

    // 设置限制范围
    public void setLimitRange(int range) {
        if (range >= 100) {
            this.limitRange = range;
            saveConfig();
        }
    }

    // 保存配置到文件
    private void saveConfig() {
        try {
            SaveData saveData = new SaveData("RTPLimitConfig");
            saveData.addInt("limitRange", limitRange);
            configFile.getParentFile().mkdirs(); // 确保目录存在
            saveData.saveScript(configFile);
        } catch (Exception e) {
            System.out.println("RTP Limit System: Failed to save config: " + e.getMessage());
        }
    }

    // 从文件加载配置
    private void loadConfig() {
        try {
            if (configFile.exists()) {
                LoadData loadData = new LoadData(configFile);
                int savedRange = loadData.getInt("limitRange", 1000);
                if (savedRange >= 100) {
                    this.limitRange = savedRange;
                    System.out.println("RTP Limit System: Loaded limit range: " + savedRange);
                }
            }
        } catch (Exception e) {
            System.out.println("RTP Limit System: Using default limit range (1000)");
        }
    }
}