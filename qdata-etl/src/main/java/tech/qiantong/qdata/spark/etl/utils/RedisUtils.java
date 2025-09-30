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

    // ================= 默认值（兼容历史任务数据） =================
    private static final String DEFAULT_HOST = "redis";
    /** 默认 Redis 端口 */
    private static final int DEFAULT_PORT = 6379;
    /** 默认 Redis 数据库索引 */
    private static final int DEFAULT_DATABASE = 0;
    /** 默认连接超时时间（毫秒） */
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    /** 默认 Redis 密码（null 表示无密码） */
    private static final String DEFAULT_PASSWORD = "J98%FHF#9h@e88h9fre9";

    /** 心跳周期（秒）。想关掉心跳就设为 0 或负数 */
    private static final int DEFAULT_HEARTBEAT_SECONDS = 10;

    private static volatile RedisClient client;
    private static volatile StatefulRedisConnection<String, String> conn;
    /** 缓存上次 init 的配置，断线可自动重连 */
    private static volatile JSONObject lastConfig;

    /** 心跳线程（单线程守护） */
    private static volatile ScheduledExecutorService heartbeatExec;
    private static volatile ScheduledFuture<?> heartbeatTask;

    private RedisUtils() {}

    // ======= 初始化 / 关闭 =======

    /** 幂等初始化；支持 host/port/database/password/timeoutMs/heartbeatSeconds */
    public static synchronized void init(JSONObject config) {
        // 缓存配置
        lastConfig = (config == null) ? new JSONObject() : new JSONObject(config);

        // 先关闭旧连接（如果存在）
        internalClose(false);

        // 构造 URI
        RedisURI uri = buildUri(lastConfig);

        // 建 client + 基本高可用选项
        client = RedisClient.create(uri);
        client.setOptions(ClientOptions.builder()
                .autoReconnect(true)                 // 关键：自动重连
                .pingBeforeActivateConnection(true)  // 建连前 PING，提高首次可用性
                .timeoutOptions(TimeoutOptions.enabled()) // 让超时能触发重连
                .build());

        // 建连接
        conn = client.connect();
        log.info("[Redis] connected to {}:{}, db={}, timeout={}ms",
                uri.getHost(), uri.getPort(), uri.getDatabase(), uri.getTimeout().toMillis());

        // 心跳（可选）
        int hb = getInt(lastConfig, "heartbeatSeconds", DEFAULT_HEARTBEAT_SECONDS);
        startHeartbeat(hb);
    }

    /** 是否已连接可用 */
    public static boolean isReady() {
        return client != null && conn != null && conn.isOpen();
    }

    /** 优雅关闭（进程退出前调用一次即可） */
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

    // ======= 核心保障：懒加载 + 自动重连 =======

    private static RedisCommands<String, String> cmd() {
        ensureReady();
        try {
            return conn.sync();
        } catch (Exception e) {
            // 极端情况下再重连一次
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

    // ======= 心跳（可选，一行配置即可开启） =======

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
                conn.async().ping(); // 异步 PING，轻量保活
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

    // ======= 工具 =======

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

    // ======= 自测 =======

    public static void main(String[] args) throws Exception {
        JSONObject cfg = new JSONObject();
        cfg.put("host", "127.0.0.1");
        cfg.put("port", 12138);
        cfg.put("database", 0);
        cfg.put("timeoutMs", 3000);
        cfg.put("heartbeatSeconds", 60); // 打开保活（可不配）

        RedisUtils.init(cfg);
        try {
            RedisUtils.set("demo:key", "hello-redis", 30);
            System.out.println("GET demo:key = " + RedisUtils.get("demo:key"));

            Map<String, String> map = new HashMap<>();
            map.put("f1", "v1");
            map.put("f2", "v2");
            RedisUtils.hset("demo:hash", map);
            System.out.println("HGETALL demo:hash = " + RedisUtils.hgetAll("demo:hash"));

            // 模拟长时间空闲：几小时后再调用也会自动保活/重连
            // Thread.sleep(TimeUnit.HOURS.toMillis(2));
            // System.out.println("GET again = " + RedisUtils.get("demo:key"));
        } finally {
            RedisUtils.close();
        }
    }
}
