package tech.qiantong.qdata.module.dpp.utils.log;

import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class LogUtils {
    /**
     * Log utilities.
     * @param taskLog
     * @param msg
     */
    public static void appendLocalLogLine(StringBuilder taskLog, String msg) {
        // Handle execution logging.
        if (taskLog.length() > 0) {
            taskLog.append("\n");
        }
        // Handle execution logging.
        taskLog.append("[INFO] ")
                .append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"))
                .append(" - ")
                .append(msg);
    }
}
