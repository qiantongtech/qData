package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:调度器新增请求参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:11
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsSchedulerSaveReqDTO {

    /**
     * {
     * "startTime":"2025-02-21 00:00:00",//开始时间直接默认当前时间 格式yyyy-MM-dd HH:mm:ss
     * "endTime":"2125-02-21 00:00:00",//结束时间直接默认当前时间的100年后 格式yyyy-MM-dd HH:mm:ss
     * "crontab":"0 0 * * * ? *",//cron表达式（必填）
     * "timezoneId":"Asia/Shanghai"//时区直接默认为 Asia/Shanghai
     * }
     */
    private String schedule;

    /**
     * 任务编码（必填）
     */
    private String processDefinitionCode;

    /**
     * 失败策略默认为 CONTINUE
     */
    private String failureStrategy;

    /**
     * 默认 default
     */
    private String workerGroup;

    /**
     * 默认 default
     */
    private String tenantCode;

}
