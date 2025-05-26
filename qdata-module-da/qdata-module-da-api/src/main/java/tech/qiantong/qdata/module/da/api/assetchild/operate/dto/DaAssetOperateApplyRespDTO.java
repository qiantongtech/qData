package tech.qiantong.qdata.module.da.api.assetchild.operate.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据资产操作申请 DTO 对象 DA_ASSET_OPERATE_APPLY
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetOperateApplyRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 数据连接id */
    private Long datasourceId;

    /** 表名称 */
    private String tableName;

    /** 表注释/表描述 */
    private String tableComment;

    /** 操作类型 */
    private String operateType;

    /** 操作JSON数据 */
    private String operateJson;

    /** 操作时间 */
    private Date operateTime;

    /** 是否已执行 */
    private String executeFlag;

    /** 执行时间 */
    private Date executeTime;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
