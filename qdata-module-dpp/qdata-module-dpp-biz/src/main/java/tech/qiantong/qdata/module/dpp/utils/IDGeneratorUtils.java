package tech.qiantong.qdata.module.dpp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:生成数字id
 * </p>
 *
 * @author: FXB
 * @create: 2019-12-26 08:59
 **/
@Component
public class IDGeneratorUtils {

    private static long workerId;

    private static long datacenterId;

    @Value("${id.workerId}")
    public  void setWorkerId(long workerId) {
        IDGeneratorUtils.workerId = workerId;
    }

    @Value("${id.datacenterId}")
    public  void setDatacenterId(long datacenterId) {
        IDGeneratorUtils.datacenterId = datacenterId;
    }

    public static long getLongId(){
        final SnowflakeIdHelper snowflakeIdHelper = SnowflakeIdHelper.getInstance(workerId,datacenterId);
        long id = snowflakeIdHelper.nextId();
        return id;
    }
    public static String getStringId(){
        final SnowflakeIdHelper snowflakeIdHelper = SnowflakeIdHelper.getInstance(workerId,datacenterId);
        long id = snowflakeIdHelper.nextId();
        return String.valueOf(id);
    }
}
