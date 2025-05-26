package tech.qiantong.qdata.spark.etl.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-30 09:34
 **/
public class LogUtils {

    /**
     * 生成日志路径
     *
     * @param nodeJson
     * @return
     */
    public static String createLogPath(String resourceUrl, JSONObject nodeJson) {
        String logPath = resourceUrl
                + DateUtil.format(new Date(), "yyyyMMdd") + File.separator
                + nodeJson.getString("nodeCode") + File.separator
                + nodeJson.getString("nodeVersion") + File.separator
                + IDGeneratorUtils.getStringId() + ".log";
        FileUtil.touch(logPath);
        return logPath;
    }

    public static void writeLog(String logPath, String meesage) {
        synchronized (logPath) {
            String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
            meesage = time + " - " + meesage;
            FileUtil.appendUtf8Lines(Arrays.asList(meesage), logPath);
        }
    }

}
