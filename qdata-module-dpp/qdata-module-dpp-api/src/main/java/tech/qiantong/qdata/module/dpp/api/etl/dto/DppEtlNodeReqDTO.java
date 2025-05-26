package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

/**
 * 数据集成节点 DTO 对象 DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlNodeReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 节点类型 */
    private String type;

    /** 节点名称 */
    private String name;

    /** 节点编码 */
    private String code;

    /** 节点版本 */
    private Long version;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 节点参数 */
    private String parameters;

    /** 任务优先级 */
    private String priority;

    /** 失败重试次数 */
    private Long failRetryTimes;

    /** 失败重试间隔（分钟） */
    private Long failRetryInterval;

    /** 超时时间 */
    private Long timeout;

    /** 延迟执行时间（分钟） */
    private Long delayTime;

    /** CPU配额 */
    private Long cpuQuota;

    /** 最大内存 */
    private Long memoryMax;

    /** 描述 */
    private String description;

    /** DolphinScheduler的id */
    private Long dsId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
