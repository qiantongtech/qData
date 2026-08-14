package tech.qiantong.qdata.module.dpp.utils.log;

import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.regex.Pattern;

public class LogUtils {

    /** DataX 原生日志行首的时间格式，例如：2026-07-23 17:15:33.826。 */
    private static final Pattern LOG_TIMESTAMP_PATTERN = Pattern.compile(
            "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}(?:\\s|$)");

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
        if (msg != null && LOG_TIMESTAMP_PATTERN.matcher(msg).find()) {
            taskLog.append(msg);
            return;
        }
        taskLog.append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"))
                .append(" - ")
                .append(msg);
    }
}
