package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据资产操作申请 DO 对象 DA_ASSET_OPERATE_APPLY
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
@TableName(value = "DA_ASSET_OPERATE_APPLY")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_OPERATE_APPLY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetOperateApplyDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Boolean delFlag;


}
