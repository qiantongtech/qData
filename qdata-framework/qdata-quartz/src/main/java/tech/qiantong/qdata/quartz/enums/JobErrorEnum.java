package tech.qiantong.qdata.quartz.enums;

/**
 * 任务操作错误信息枚举
 *
 * @author qdata
 */
public enum JobErrorEnum {

    CRON_INVALID(-1L, "Cron表达式不正确"),
    RMI_NOT_ALLOWED(-2L, "目标字符串不允许'rmi'调用"),
    LDAP_NOT_ALLOWED(-3L, "目标字符串不允许'ldap(s)'调用"),
    HTTP_NOT_ALLOWED(-4L, "目标字符串不允许'http(s)'调用"),
    INVALID_TARGET(-5L, "目标字符串存在违规"),
    NOT_IN_WHITELIST(-6L, "目标字符串不在白名单内");

    private final Long code;
    private final String message;

    JobErrorEnum(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String jobName, String operationType) {
        return String.format("%s任务'%s'失败，%s", operationType, jobName, this.message);
    }

    /**
     * 根据错误码获取枚举
     */
    public static JobErrorEnum getByCode(Long code) {
        for (JobErrorEnum error : values()) {
            if (error.getCode().equals(code)) {
                return error;
            }
        }
        return null;
    }
}
