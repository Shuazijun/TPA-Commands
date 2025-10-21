package tpamod.listener;

import tpamod.events.TPARequestEvent;
import tpamod.events.TPAResponseEvent;
import com.google.common.cache.*;
import necesse.engine.GameEventListener;
// import necesse.engine.GameLog;

import java.util.concurrent.TimeUnit;

public class TPAListener {
    private TPARequestListener requestListener;
    private TPAResponseListener responseListener;
    private int cooldownSeconds = 30;

    public TPAListener() {
        this.requestListener = new TPARequestListener(this);
        this.responseListener = new TPAResponseListener(this);
        rebuildCache();
    }
    private Cache<String, TPARequestEvent> requests;

    private void rebuildCache() {
        this.requests = CacheBuilder.newBuilder().expireAfterWrite(cooldownSeconds, TimeUnit.SECONDS).build();
    }

    public void setCooldownSeconds(int seconds) {
        this.cooldownSeconds = seconds;
        rebuildCache();
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    private void onRequest(TPARequestEvent request) {
        if (requests.getIfPresent(request.target.getName()) == null) {
            requests.put(request.target.getName(), request);
            return;
        }
        request.preventDefault();
    }

    private void onResponse(TPAResponseEvent response) {
        TPARequestEvent request = requests.getIfPresent(response.teleportTarget.getName());
        if(request == null) {
            response.preventDefault();
            return;
        }
        if(response.accepted) {
            request.execute();
        } else {
            request.reject();
        }
        requests.invalidate(response.teleportTarget.getName());
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