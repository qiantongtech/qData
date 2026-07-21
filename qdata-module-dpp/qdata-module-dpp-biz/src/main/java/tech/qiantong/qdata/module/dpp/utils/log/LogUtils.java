package tech.qiantong.qdata.module.dpp.utils.log;

import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class LogUtils {
    /**
     * Appends a log entry.
     * @param taskLog
     * @param msg
     */
    public static void appendLocalLogLine(StringBuilder taskLog, String msg) {
        // Separate log entries with line breaks so the log file remains line-oriented.
        if (taskLog.length() > 0) {
            taskLog.append("\n");
        }
        // Example log format: [INFO] 2025-09-30 16:08:54.444 - message
        taskLog.append("[INFO] ")
                .append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"))
                .append(" - ")
                .append(msg);
    }
}
