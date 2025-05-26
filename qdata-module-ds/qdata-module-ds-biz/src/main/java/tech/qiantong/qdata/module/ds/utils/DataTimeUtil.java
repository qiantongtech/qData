package tech.qiantong.qdata.module.ds.utils;


import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间工具类
 * 2024-01-05
 */
@Slf4j
public class DataTimeUtil {


    /**
     * LocalDateTime格式对象转换成时间戳
     * @param now
     * @return
     */
    public static long timeByTimeStamp(LocalDateTime now) {
        try {
            // 转换为Instant对象
            Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
            // 从Instant对象获取时间戳（毫秒）
            return instant.toEpochMilli();
        }catch (Exception e){
            log.debug("LocalDateTime转换时间戳失败 now{}",now);
            log.debug("LocalDateTime转换时间戳失败 e{}",e.toString());
            return System.currentTimeMillis();
        }
    }
}
