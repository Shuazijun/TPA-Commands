package tpamod.listener;

import tpamod.events.TPARequestEvent;
import tpamod.events.TPAResponseEvent;
import com.google.common.cache.*;
import necesse.engine.GameEventListener;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
// import necesse.engine.GameLog;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class TPAListener {
    private TPARequestListener requestListener;
    private TPAResponseListener responseListener;
    private int cooldownSeconds = 30;

    public TPAListener() {
        this.requestListener = new TPARequestListener(this);
        this.responseListener = new TPAResponseListener(this);
        loadConfig(); // 加载保存的配置
        rebuildCache();
    }
    private Cache<String, TPARequestEvent> requests;

    private void rebuildCache() {
        this.requests = CacheBuilder.newBuilder().expireAfterWrite(cooldownSeconds, TimeUnit.SECONDS).build();
    }

    public void setCooldownSeconds(int seconds) {
        this.cooldownSeconds = seconds;
        rebuildCache();
        // 保存设置到配置文件
        saveConfig();
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    // 保存配置到文件
    private void saveConfig() {
        try {
            SaveData saveData = new SaveData("TPAConfig");
            saveData.addInt("cooldownSeconds", cooldownSeconds);
            File configFile = new File("config/tpamod/tpaconfig.dat");
            configFile.getParentFile().mkdirs(); // 确保目录存在
            saveData.saveScript(configFile);
        } catch (Exception e) {
            System.out.println("TPA Commands: Failed to save config: " + e.getMessage());
        }
    }

    // 从文件加载配置
    private void loadConfig() {
        try {
            File configFile = new File("config/tpamod/tpaconfig.dat");
            if (configFile.exists()) {
                LoadData loadData = new LoadData(configFile);
                int savedCooldown = loadData.getInt("cooldownSeconds", 30);
                if (savedCooldown >= 1 && savedCooldown <= 3600) {
                    this.cooldownSeconds = savedCooldown;
                    System.out.println("TPA Commands: Loaded cooldown time: " + savedCooldown + " seconds");
                }
            }
        } catch (Exception e) {
            // 配置文件不存在或读取失败，使用默认值
            System.out.println("TPA Commands: Using default cooldown time (30 seconds)");
        }
    }

    private void onRequest(TPARequestEvent request) {
        // 使用"发送者->接收者"作为缓存键，确保每个玩家的冷却时间独立
        String cacheKey = getCacheKey(request.source.getName(), request.target.getName());
        if (requests.getIfPresent(cacheKey) == null) {
            requests.put(cacheKey, request);
            return;
        }
        request.preventDefault();
    }

    // 生成缓存键：发送者->接收者
    private String getCacheKey(String sourceName, String targetName) {
        return sourceName + "->" + targetName;
    }

    private void onResponse(TPAResponseEvent response) {
        // 查找所有以当前玩家为目标的请求
        TPARequestEvent request = findRequestByTarget(response.teleportTarget.getName());
        if(request == null) {
            response.preventDefault();
            return;
        }
        if(response.accepted) {
            request.execute();
        } else {
            request.reject();
        }
        // 使用正确的缓存键来移除请求
        String cacheKey = getCacheKey(request.source.getName(), request.target.getName());
        requests.invalidate(cacheKey);
    }

    // 根据目标玩家查找请求
    private TPARequestEvent findRequestByTarget(String targetName) {
        // 遍历缓存查找以该玩家为目标的请求
        for (String key : requests.asMap().keySet()) {
            if (key.endsWith("->" + targetName)) {
                return requests.getIfPresent(key);
            }
        }
        return null;
    }

    public GameEventListener<TPARequestEvent> getRequestListener() {
        return requestListener;
    }

    public GameEventListener<TPAResponseEvent> getResponseListener() {
        return responseListener;
    }

    private class TPARequestListener extends GameEventListener<TPARequestEvent> {
        private final TPAListener listener;
        public TPARequestListener(TPAListener listener) {
            super();
            this.listener = listener;
        }
        @Override
        public void onEvent(TPARequestEvent var1) {
            listener.onRequest(var1);
        }
    }

    private class TPAResponseListener extends GameEventListener<TPAResponseEvent> {
        private final TPAListener listener;
        public TPAResponseListener(TPAListener listener) {
            super();
            this.listener = listener;
        }
        @Override
        public void onEvent(TPAResponseEvent var1) {
            listener.onResponse(var1);
        }
    }
}