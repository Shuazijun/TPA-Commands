package tpamod.data;

import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import java.io.File;

public class RTPConfig {
    private int cooldownSeconds = 15;
    private File configFile;

    public RTPConfig() {
        this.configFile = new File("config/tpamod/rtpconfig.dat");
        loadConfig();
    }

    // 获取冷却时间
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    // 设置冷却时间
    public void setCooldownSeconds(int seconds) {
        if (seconds >= 1 && seconds <= 3600) {
            this.cooldownSeconds = seconds;
            saveConfig();
        }
    }

    // 保存配置到文件
    private void saveConfig() {
        try {
            SaveData saveData = new SaveData("RTPConfig");
            saveData.addInt("cooldownSeconds", cooldownSeconds);
            configFile.getParentFile().mkdirs(); // 确保目录存在
            saveData.saveScript(configFile);
        } catch (Exception e) {
            System.out.println("RTP System: Failed to save config: " + e.getMessage());
        }
    }

    // 从文件加载配置
    private void loadConfig() {
        try {
            if (configFile.exists()) {
                LoadData loadData = new LoadData(configFile);
                int savedCooldown = loadData.getInt("cooldownSeconds", 15);
                if (savedCooldown >= 1 && savedCooldown <= 3600) {
                    this.cooldownSeconds = savedCooldown;
                    System.out.println("RTP System: Loaded cooldown time: " + savedCooldown + " seconds");
                }
            }
        } catch (Exception e) {
            System.out.println("RTP System: Using default cooldown time (15 seconds)");
        }
    }
}