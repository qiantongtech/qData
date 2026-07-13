/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.spark.etl.utils;

import com.alibaba.fastjson2.JSONObject;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class RedisUtils {

    // ================= Default value (compatible with historical mission data) =================
    private static final String DEFAULT_HOST = "redis";
    /** Default Redis port */
    private static final int DEFAULT_PORT = 6379;
    /** Default Redis database index */
    private static final int DEFAULT_DATABASE = 0;
    /**Default connection timeout (milliseconds) */
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    /** Default Redis password (null means no password) */
    private static final String DEFAULT_PASSWORD = "J98%FHF#9h@e88h9fre9";

    /** Heartbeat period (seconds). If you want to turn off the heartbeat, set it to 0 or a negative number */
    private static final int DEFAULT_HEARTBEAT_SECONDS = 10;

    private static volatile RedisClient client;
    private static volatile StatefulRedisConnection<String, String> conn;
    /** Cache the configuration of the last init, and automatically reconnect when disconnected */
    private static volatile JSONObject lastConfig;

    /** Heartbeat thread (single-threaded daemon) */
    private static volatile ScheduledExecutorService heartbeatExec;
    private static volatile ScheduledFuture<?> heartbeatTask;

    private RedisUtils() {}

    // ======= Initialization / Shutdown =======

    /** Idempotent initialization; supports host/port/database/password/timeoutMs/heartbeatSeconds */
    public static synchronized void init(JSONObject config) {
        // Cache configuration
        lastConfig = (config == null) ? new JSONObject() : new JSONObject(config);

        // Close the old connection first (if it exists)
        internalClose(false);

        // Construct URI
        RedisURI uri = buildUri(lastConfig);

        // Build client + basic high availability options
        client = RedisClient.create(uri);
        client.setOptions(ClientOptions.builder()
                .autoReconnect(true)                 // Key: Automatic reconnection
                .pingBeforeActivateConnection(true)  // PING before establishing a connection to improve first-time availability
                .timeoutOptions(TimeoutOptions.enabled()) // Allow timeout to trigger reconnection
                .build());

        // Establish a connection
        conn = client.connect();
        log.info("[Redis] connected to {}:{}, db={}, timeout={}ms",
                uri.getHost(), uri.getPort(), uri.getDatabase(), uri.getTimeout().toMillis());

        // Heartbeat (optional)
        int hb = getInt(lastConfig, "heartbeatSeconds", DEFAULT_HEARTBEAT_SECONDS);
        startHeartbeat(hb);
    }

    /** Whether the connection is available */
    public static boolean isReady() {
        return client != null && conn != null && conn.isOpen();
    }

    /** Graceful shutdown (just call it once before the process exits) */
    public static synchronized void close() {
        stopHeartbeat();
        internalClose(true);
        lastConfig = null;
        log.info("[Redis] closed.");
    }

    private static void internalClose(boolean shutdownClient) {
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        conn = null;
        if (shutdownClient) {
            try { if (client != null) client.shutdown(); } catch (Exception ignored) {}
            client = null;
        }
    }

    // ======= KV =======

    public static void set(String key, String value, long expireSeconds) {
        try {
            if (expireSeconds > 0) cmd().setex(key, expireSeconds, value);
            else cmd().set(key, value);
        } catch (Exception e) {
            throw wrap("SET key=" + key, e);
        }
    }

    public static String get(String key) {
        try {
            return cmd().get(key);
        } catch (Exception e) {
            throw wrap("GET key=" + key, e);
        }
    }

    public static long del(String key) {
        try {
            Long n = cmd().del(key);
            return n == null ? 0L : n;
        } catch (Exception e) {
            throw wrap("DEL key=" + key, e);
        }
    }

    public static Boolean hasKey(String key) {
        return cmd().exists(key) > 0;
    }

    public static boolean expire(String key, long seconds) {
        try {
            Boolean ok = cmd().expire(key, seconds);
            return ok != null && ok;
        } catch (Exception e) {
            throw wrap("EXPIRE key=" + key, e);
        }
    }

    public static long incr(String key) {
        try {
            Long v = cmd().incr(key);
            return v == null ? 0L : v;
        } catch (Exception e) {
            throw wrap("INCR key=" + key, e);
        }
    }

    public static boolean setnx(String key, String value, Long expireSeconds) {
        try {
            Boolean ok = cmd().setnx(key, value);
            if (Boolean.TRUE.equals(ok) && expireSeconds != null && expireSeconds > 0) {
                cmd().expire(key, expireSeconds);
            }
            return Boolean.TRUE.equals(ok);
        } catch (Exception e) {
            throw wrap("SETNX key=" + key, e);
        }
    }

    // ======= Hash =======

    public static void hset(String key, Map<String, String> map) {
        if (map == null || map.isEmpty()) return;
        try { cmd().hset(key, map); } catch (Exception e) { throw wrap("HSET key=" + key, e); }
    }

    public static void hset(String key, String field, String value) {
        try { cmd().hset(key, field, value); } catch (Exception e) { throw wrap("HSET key=" + key + ", field=" + field, e); }
    }

    public static Map<String, String> hgetAll(String key) {
        try {
            Map<String, String> m = cmd().hgetall(key);
            return (m == null) ? Collections.<String, String>emptyMap() : m;
        } catch (Exception e) {
            throw wrap("HGETALL key=" + key, e);
        }
    }

    public static String hget(String key, String field) {
        try { return cmd().hget(key, field); } catch (Exception e) { throw wrap("HGET key=" + key + ", field=" + field, e); }
    }

    public static long hdel(String key, String field) {
        try {
            Long n = cmd().hdel(key, field);
            return n == null ? 0L : n;
        } catch (Exception e) {
            throw wrap("HDEL key=" + key + ", field=" + field, e);
        }
    }

    // ======= Core guarantee: lazy loading + automatic reconnect =======

    private static RedisCommands<String, String> cmd() {
        ensureReady();
        try {
            return conn.sync();
        } catch (Exception e) {
            // In extreme cases, reconnect again
            log.warn("[Redis] sync() failed, retry reconnect once. cause={}", e.toString());
            reconnectOnce();
            return conn.sync();
        }
    }

    private static void ensureReady() {
        if (conn != null && conn.isOpen()) return;
        synchronized (RedisUtils.class) {
            if (conn != null && conn.isOpen()) return;
            if (client == null) {
                if (lastConfig == null) throw new IllegalStateException("Redis not initialized. Call init() first.");
                init(lastConfig);
                return;
            }
            try {
                conn = client.connect();
                conn.sync().ping();
                log.info("[Redis] reconnected.");
            } catch (Exception e) {
                throw new IllegalStateException("[Redis] reconnect failed: " + e.getMessage(), e);
            }
        }
    }

    private static void reconnectOnce() {
        synchronized (RedisUtils.class) {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            conn = client.connect();
            conn.sync().ping();
            log.info("[Redis] reconnected (retry).");
        }
    }

    // ======= Heartbeat (optional, one line of configuration can enable it) =======

    private static synchronized void startHeartbeat(int seconds) {
        stopHeartbeat();
        if (seconds <= 0) return;

        if (heartbeatExec == null || heartbeatExec.isShutdown()) {
            heartbeatExec = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "redis-heartbeat");
                t.setDaemon(true);
                return t;
            });
        }
        heartbeatTask = heartbeatExec.scheduleAtFixedRate(() -> {
            try {
                ensureReady();
                conn.async().ping(); // Asynchronous PING, lightweight keep-alive
            } catch (Exception e) {
                log.warn("[Redis] heartbeat ping failed: {}", e.toString());
            }
        }, seconds, seconds, TimeUnit.SECONDS);

        log.info("[Redis] heartbeat started: {}s", seconds);
    }

    private static synchronized void stopHeartbeat() {
        try {
            if (heartbeatTask != null) heartbeatTask.cancel(false);
        } catch (Exception ignored) {}
        heartbeatTask = null;

        if (heartbeatExec != null) {
            try { heartbeatExec.shutdownNow(); } catch (Exception ignored) {}
            heartbeatExec = null;
        }
    }

    // ======= Tools =======

    private static RuntimeException wrap(String op, Exception e) {
        return new RuntimeException("[Redis] " + op + " failed: " + e.getMessage(), e);
    }

    private static RedisURI buildUri(JSONObject cfg) {
        String host = getStr(cfg, "host", DEFAULT_HOST);
        int port = getInt(cfg, "port", DEFAULT_PORT);
        int db = getInt(cfg, "database", DEFAULT_DATABASE);
        int timeoutMs = getInt(cfg, "timeoutMs", DEFAULT_TIMEOUT_MS);
        String password = getStr(cfg, "password", DEFAULT_PASSWORD);

        RedisURI.Builder builder = RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withDatabase(db)
                .withTimeout(Duration.ofMillis(timeoutMs));
        if (password != null && !password.isEmpty()) {
            builder.withPassword(password.toCharArray());
        }
        return builder.build();
    }

    private static String getStr(JSONObject cfg, String k, String defVal) {
        return (cfg != null && cfg.containsKey(k)) ? cfg.getString(k) : defVal;
    }
    private static int getInt(JSONObject cfg, String k, int defVal) {
        if (cfg == null || !cfg.containsKey(k)) return defVal;
        try { return cfg.getIntValue(k); } catch (Exception ignore) { return defVal; }
    }

    // ======= Self-test =======

    public static void main(String[] args) throws Exception {
        JSONObject cfg = new JSONObject();
        cfg.put("host", "127.0.0.1");
        cfg.put("port", 12138);
        cfg.put("database", 0);
        cfg.put("timeoutMs", 3000);
        cfg.put("heartbeatSeconds", 60); // Turn on keep-alive (optional)

        RedisUtils.init(cfg);
        try {
            RedisUtils.set("demo:key", "hello-redis", 30);
            System.out.println("GET demo:key = " + RedisUtils.get("demo:key"));

            Map<String, String> map = new HashMap<>();
            map.put("f1", "v1");
            map.put("f2", "v2");
            RedisUtils.hset("demo:hash", map);
            System.out.println("HGETALL demo:hash = " + RedisUtils.hgetAll("demo:hash"));

            // Simulate long-term idle time: calling again after a few hours will automatically keep alive/reconnect
            // Thread.sleep(TimeUnit.HOURS.toMillis(2));
            // System.out.println("GET again = " + RedisUtils.get("demo:key"));
        } finally {
            RedisUtils.close();
        }
    }
}
