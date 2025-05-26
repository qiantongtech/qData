package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

/**
 * 数据集成任务-日志 DTO 对象 DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务类型 */
    private String type;

    /** 任务名称 */
    private String name;

    /** 任务编码 */
    private String code;

    /** 任务版本 */
    private Long version;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 责任人 */
    private String personCharge;

    /** 节点坐标信息 */
    private String locations;

    /** 描述 */
    private String description;

    /** 超时时间 */
    private Long timeout;

    /** 抽取量 */
    private Long extractionCount;

    /** 写入量 */
    private Long writeCount;

    /** 任务状态 */
    private String status;

    /** DolphinScheduler的id */
    private Long dsId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
