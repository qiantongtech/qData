-- Hydrology schema (MySQL 5.7 safe; no FKs/PKs/UNIQUE/indexes/partitions/views; no init)
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS hydrology
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE hydrology;

-- Hydrology schema (MySQL 5.7 safe; add AUTO_INCREMENT IDs)

CREATE DATABASE IF NOT EXISTS hydrology
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE hydrology;

-- 1) 站点基础表
DROP TABLE IF EXISTS hyd_station;
CREATE TABLE hyd_station (
                             station_id        BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '站点主键',
                             station_code      VARCHAR(64)            COMMENT '站点编码（第三方库唯一）',
                             station_name      VARCHAR(200)           COMMENT '站点名称',
                             station_type      ENUM('HYDRO','RAIN','FLOW','COMPOSITE') DEFAULT 'COMPOSITE' COMMENT '站点类型',
                             basin_code        VARCHAR(64)            COMMENT '流域编码',
                             river_name        VARCHAR(128)           COMMENT '河流名称',
                             longitude         DECIMAL(10,6)          COMMENT '经度',
                             latitude          DECIMAL(10,6)          COMMENT '纬度',
                             admin_region_code VARCHAR(12)            COMMENT '行政区划码',
                             elevation_m       DECIMAL(8,2)           COMMENT '站点高程(米)',
                             status            TINYINT                DEFAULT 1 COMMENT '1启用/0停用',
                             ext_json          TEXT                   COMMENT '扩展字段（原 JSON）',
                             created_at        TIMESTAMP              DEFAULT CURRENT_TIMESTAMP,
                             updated_at        TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水文站点基础信息';

-- 2) 传感器/数据源表
DROP TABLE IF EXISTS hyd_sensor;
CREATE TABLE hyd_sensor (
                            sensor_id       BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '传感器主键',
                            station_code    VARCHAR(64)              COMMENT '所属站点编码（软关联）',
                            sensor_code     VARCHAR(64)              COMMENT '传感器/接口编码',
                            metric_type     ENUM('WATER_LEVEL','DISCHARGE','RAINFALL') COMMENT '监测指标类型',
                            unit            VARCHAR(32)              COMMENT '单位：m/m3/s/mm',
                            precision_scale DECIMAL(4,2)             COMMENT '仪器精度',
                            status          TINYINT                  DEFAULT 1 COMMENT '1启用/0停用',
                            ext_json        TEXT                     COMMENT '扩展参数（原 JSON）',
                            created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP,
                            updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='传感器/数据源';

-- 3) 质量代码字典（保持不变，质量码不自增）
DROP TABLE IF EXISTS hyd_quality_code;
CREATE TABLE hyd_quality_code (
                                  quality_code   VARCHAR(16) PRIMARY KEY   COMMENT '质量代码',
                                  quality_desc   VARCHAR(200)              COMMENT '质量说明',
                                  severity       TINYINT                   DEFAULT 0 COMMENT '0正常，1提示，2可疑，3错误',
                                  created_at     TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监测数据质量代码字典';

-- 4) 水位时序表
DROP TABLE IF EXISTS hyd_water_level;
CREATE TABLE hyd_water_level (
                                 id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
                                 station_code   VARCHAR(64)               COMMENT '站点编码（软关联）',
                                 sensor_id      BIGINT UNSIGNED           COMMENT '传感器ID（软关联）',
                                 obs_time       DATETIME                  COMMENT '观测时间',
                                 obs_date       DATE                      COMMENT '观测日期',
                                 water_level_m  DECIMAL(10,3)             COMMENT '水位(米)',
                                 quality_code   VARCHAR(16)               DEFAULT 'OK' COMMENT '质量代码',
                                 source         VARCHAR(64)               COMMENT '来源',
                                 trace_id       VARCHAR(64)               COMMENT '追踪ID/批次号',
                                 ext_json       TEXT                      COMMENT '扩展字段（原 JSON）',
                                 created_at     TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水位时序数据';

DROP TABLE IF EXISTS hyd_water_level_stream;
CREATE TABLE hyd_water_level_stream (
                                 id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
                                 station_code   VARCHAR(64)               COMMENT '站点编码（软关联）',
                                 sensor_id      BIGINT UNSIGNED           COMMENT '传感器ID（软关联）',
                                 obs_time       DATETIME                  COMMENT '观测时间',
                                 obs_date       DATE                      COMMENT '观测日期',
                                 water_level_m  DECIMAL(10,3)             COMMENT '水位(米)',
                                 quality_code   VARCHAR(16)               DEFAULT 'OK' COMMENT '质量代码',
                                 source         VARCHAR(64)               COMMENT '来源',
                                 trace_id       VARCHAR(64)               COMMENT '追踪ID/批次号',
                                 ext_json       TEXT                      COMMENT '扩展字段（原 JSON）',
                                 created_at     TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水位时序数据';

DROP TABLE IF EXISTS hyd_water_level_spark;
CREATE TABLE hyd_water_level_spark (
                                        id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
                                        station_code   VARCHAR(64)               COMMENT '站点编码（软关联）',
                                        sensor_id      BIGINT UNSIGNED           COMMENT '传感器ID（软关联）',
                                        obs_time       DATETIME                  COMMENT '观测时间',
                                        obs_date       DATE                      COMMENT '观测日期',
                                        water_level_m  DECIMAL(10,3)             COMMENT '水位(米)',
                                        quality_code   VARCHAR(16)               DEFAULT 'OK' COMMENT '质量代码',
                                        source         VARCHAR(64)               COMMENT '来源',
                                        trace_id       VARCHAR(64)               COMMENT '追踪ID/批次号',
                                        ext_json       TEXT                      COMMENT '扩展字段（原 JSON）',
                                        created_at     TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水位时序数据';

-- 5) 流量时序表
DROP TABLE IF EXISTS hyd_discharge;
CREATE TABLE hyd_discharge (
                               id               BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
                               station_code     VARCHAR(64)             COMMENT '站点编码（软关联）',
                               sensor_id        BIGINT UNSIGNED         COMMENT '传感器ID（软关联）',
                               obs_time         DATETIME                COMMENT '观测时间',
                               obs_date         DATE                    COMMENT '观测日期',
                               discharge_m3s    DECIMAL(12,3)           COMMENT '流量(m^3/s)',
                               quality_code     VARCHAR(16)             DEFAULT 'OK' COMMENT '质量代码',
                               source           VARCHAR(64)             COMMENT '来源',
                               trace_id         VARCHAR(64)             COMMENT '追踪ID',
                               ext_json         TEXT                    COMMENT '扩展（原 JSON）',
                               created_at       TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流量时序数据';

-- 6) 雨量时序表
DROP TABLE IF EXISTS hyd_rainfall;
CREATE TABLE hyd_rainfall (
                              id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
                              station_code   VARCHAR(64)               COMMENT '站点编码（软关联）',
                              sensor_id      BIGINT UNSIGNED           COMMENT '传感器ID（软关联）',
                              obs_time       DATETIME                  COMMENT '统计时间(周期结束时刻)',
                              obs_date       DATE                      COMMENT '观测日期',
                              period         ENUM('MINUTE','HOUR','DAY') DEFAULT 'HOUR' COMMENT '统计周期',
                              rainfall_mm    DECIMAL(10,3)             COMMENT '降雨量(毫米)',
                              quality_code   VARCHAR(16)               DEFAULT 'OK' COMMENT '质量代码',
                              source         VARCHAR(64)               COMMENT '来源',
                              trace_id       VARCHAR(64)               COMMENT '追踪ID',
                              ext_json       TEXT                      COMMENT '扩展（原 JSON）',
                              created_at     TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='降雨量时序数据';

/* ===============================
   批量插入 1000 行初始化数据（MySQL 5.7）
   - 每张表 1000 行
   - 起始时间：2025-06-01 00:00:00
   - 一次性 INSERT ... SELECT（高效）
   =============================== */

USE hydrology;

/* 可选：重复演练前清空（谨慎操作）
TRUNCATE TABLE hyd_station;
TRUNCATE TABLE hyd_sensor;
TRUNCATE TABLE hyd_quality_code;
TRUNCATE TABLE hyd_water_level;
TRUNCATE TABLE hyd_discharge;
TRUNCATE TABLE hyd_rainfall;
*/

-- 控制参数
SET @rows := 1000;
SET @base_ts := '2025-06-01 00:00:00';

/* ========== 行生成器：产生 n = 1..1000 ========== */
-- 说明：MySQL 5.7 无递归 CTE，这里用内联数字表构造 0..999，再筛 1..1000
-- 在后续 INSERT 中直接复用这个子查询块
/* 结构参考：
SELECT (a.d*100 + b.d*10 + c.d) AS n
FROM (SELECT 0 d UNION ALL SELECT 1 ... SELECT 9) a
CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 ... SELECT 9) b
CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 ... SELECT 9) c
WHERE (a.d*100 + b.d*10 + c.d) BETWEEN 1 AND @rows;
*/

/* ========== 1) hyd_station：1000 条站点 ========== */
INSERT INTO hyd_station (
    station_id, station_code, station_name, station_type,
    basin_code, river_name, longitude, latitude,
    admin_region_code, elevation_m, status, ext_json,
    created_at, updated_at
)
SELECT
    NULL AS station_id,
    CONCAT('ST', LPAD(n, 4, '0')) AS station_code,
    CONCAT('站点-', LPAD(n, 4, '0')) AS station_name,
    CASE (n % 4)
        WHEN 0 THEN 'HYDRO'
        WHEN 1 THEN 'RAIN'
        WHEN 2 THEN 'FLOW'
        ELSE 'COMPOSITE'
        END AS station_type,
    CONCAT('B', LPAD((n % 50) + 1, 3, '0')) AS basin_code,
    CONCAT('河流-', LPAD((n % 200) + 1, 3, '0')) AS river_name,
    -- 经度：取 100~120 范围内的伪随机小数
    ROUND(100 + (n % 2000) * 0.01, 6) AS longitude,
    -- 纬度：取 20~40 范围内的伪随机小数
    ROUND(20 + (n % 2000) * 0.01, 6) AS latitude,
    CONCAT('CN', LPAD((n % 9999) + 1, 4, '0')) AS admin_region_code,
    ROUND(10 + (n % 500) * 0.1, 2) AS elevation_m,
    (n % 2) AS status,                -- 0/1 交替
    NULL AS ext_json,                 -- 你当前表定义里是 TEXT
    NOW() AS created_at,
    NOW() AS updated_at
FROM (
         SELECT (a.d*100 + b.d*10 + c.d) AS n
         FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) t
WHERE n BETWEEN 1 AND @rows;

/* ========== 2) hyd_sensor：1000 条传感器 ========== */
INSERT INTO hyd_sensor (
    sensor_id, station_code, sensor_code, metric_type, unit,
    precision_scale, status, ext_json, created_at, updated_at
)
SELECT
    NULL AS sensor_id,
    -- 传感器与站点软关联：循环映射到 ST0001~ST1000
    CONCAT('ST', LPAD(((n - 1) % 1000) + 1, 4, '0')) AS station_code,
    CONCAT('SC', LPAD(n, 4, '0')) AS sensor_code,
    CASE (n % 3)
        WHEN 0 THEN 'WATER_LEVEL'
        WHEN 1 THEN 'DISCHARGE'
        ELSE 'RAINFALL'
        END AS metric_type,
    CASE (n % 3)
        WHEN 0 THEN 'm'
        WHEN 1 THEN 'm3/s'
        ELSE 'mm'
        END AS unit,
    ROUND(0.1 + (n % 20) * 0.01, 2) AS precision_scale,
    1 AS status,
    NULL AS ext_json,
    NOW() AS created_at,
    NOW() AS updated_at
FROM (
         SELECT (a.d*100 + b.d*10 + c.d) AS n
         FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) t
WHERE n BETWEEN 1 AND @rows;

/* ========== 3) hyd_quality_code：1000 条代码 ========== */
INSERT INTO hyd_quality_code (
    quality_code, quality_desc, severity, created_at
)
SELECT
    CONCAT('Q', LPAD(n, 4, '0')) AS quality_code,
    CONCAT('模拟质量代码-', LPAD(n, 4, '0')) AS quality_desc,
    (n % 4) AS severity,            -- 0/1/2/3 循环
    NOW() AS created_at
FROM (
         SELECT (a.d*100 + b.d*10 + c.d) AS n
         FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
                  CROSS JOIN
              (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
               UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) t
WHERE n BETWEEN 1 AND @rows;

/* ========== 4) hyd_water_level：1000 条水位（从 2025-06 起，每行 +1 分钟） ========== */
INSERT INTO hyd_water_level (
    id, station_code, sensor_id, obs_time, obs_date,
    water_level_m, quality_code, source, trace_id, ext_json, created_at
)
SELECT
    NULL AS id,
    CONCAT('ST', LPAD(((n - 1) % 1000) + 1, 4, '0')) AS station_code,
    -- 这里给个软关联的传感器ID（数值字段），若你希望为空可改为 NULL
    ((n - 1) % 1000) + 1 AS sensor_id,
    DATE_ADD(@base_ts, INTERVAL n MINUTE) AS obs_time,
    DATE(DATE_ADD(@base_ts, INTERVAL n MINUTE)) AS obs_date,
    ROUND(100 + SIN(n/30.0)*2 + ((n % 7)*0.05), 3) AS water_level_m,
    'OK' AS quality_code,
    'SIM' AS source,
    CONCAT('WL-', LPAD(n, 4, '0')) AS trace_id,
    NULL AS ext_json,
    NOW() AS created_at
FROM (
    SELECT (a.d*100 + b.d*10 + c.d) AS n
    FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
    ) t
WHERE n BETWEEN 1 AND @rows;

/* ========== 5) hyd_discharge：1000 条流量（从 2025-06 起，每行 +1 分钟） ========== */
INSERT INTO hyd_discharge (
    id, station_code, sensor_id, obs_time, obs_date,
    discharge_m3s, quality_code, source, trace_id, ext_json, created_at
)
SELECT
    NULL AS id,
    CONCAT('ST', LPAD(((n - 1) % 1000) + 1, 4, '0')) AS station_code,
    ((n - 1) % 1000) + 1 AS sensor_id,
    DATE_ADD(@base_ts, INTERVAL n MINUTE) AS obs_time,
    DATE(DATE_ADD(@base_ts, INTERVAL n MINUTE)) AS obs_date,
    -- 仿真流量：基线 50 + 周期波动 + 轻抖动
    ROUND(50 + SIN(n/20.0)*5 + ((n % 9)*0.1), 3) AS discharge_m3s,
    'OK' AS quality_code,
    'SIM' AS source,
    CONCAT('Q-', LPAD(n, 4, '0')) AS trace_id,
    NULL AS ext_json,
    NOW() AS created_at
FROM (
    SELECT (a.d*100 + b.d*10 + c.d) AS n
    FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
    ) t
WHERE n BETWEEN 1 AND @rows;

/* ========== 6) hyd_rainfall：1000 条雨量（从 2025-06 起，每行 +1 分钟） ========== */
INSERT INTO hyd_rainfall (
    id, station_code, sensor_id, obs_time, obs_date, period,
    rainfall_mm, quality_code, source, trace_id, ext_json, created_at
)
SELECT
    NULL AS id,
    CONCAT('ST', LPAD(((n - 1) % 1000) + 1, 4, '0')) AS station_code,
    ((n - 1) % 1000) + 1 AS sensor_id,
    DATE_ADD(@base_ts, INTERVAL n MINUTE) AS obs_time,
    DATE(DATE_ADD(@base_ts, INTERVAL n MINUTE)) AS obs_date,
    'HOUR' AS period,  -- 统一按小时周期；若要混合可改 CASE (n%3)...
    -- 仿真降雨量：非负，偶尔为 0（干旱）；制造小波动
    ROUND(GREATEST(0, (n % 5 = 0) * 0.0 + (n % 5 <> 0) * (0.1 + (n % 13) * 0.02)), 3) AS rainfall_mm,
    'OK' AS quality_code,
    'SIM' AS source,
    CONCAT('RF-', LPAD(n, 4, '0')) AS trace_id,
    NULL AS ext_json,
    NOW() AS created_at
FROM (
    SELECT (a.d*100 + b.d*10 + c.d) AS n
    FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
    CROSS JOIN
    (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
    ) t
WHERE n BETWEEN 1 AND @rows;
