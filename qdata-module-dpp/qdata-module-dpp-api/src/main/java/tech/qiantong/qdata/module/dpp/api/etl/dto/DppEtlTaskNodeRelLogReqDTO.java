package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

/**
 * 数据集成任务节点关系-日志 DTO 对象 DPP_ETL_TASK_NODE_REL_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskNodeRelLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 任务版本 */
    private Long taskVersion;

    /** 前节点id */
    private Long preNodeId;

    /** 前节点编码 */
    private String preNodeCode;

    /** 前节点版本 */
    private Long preNodeVersion;

    /** 后节点id */
    private Long postNodeId;

    /** 后节点编码 */
    private String postNodeCode;

    /** 后节点版本 */
    private Long postNodeVersion;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
