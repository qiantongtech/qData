package tech.qiantong.qdata.module.dpp.utils.log;

import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class LogUtils {
    /**
     * 日志
     * @param taskLog
     * @param msg
     */
    public static void appendLocalLogLine(StringBuilder taskLog, String msg) {
        // 多条日志之间使用换行分隔，保持日志文件按行展示。
        if (taskLog.length() > 0) {
            taskLog.append("\n");
        }
        // 日志样例格式：[INFO] 2025-09-30 16:08:54.444 - message
        taskLog.append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"))
                .append(" - ")
                .append(msg);
    }
}
