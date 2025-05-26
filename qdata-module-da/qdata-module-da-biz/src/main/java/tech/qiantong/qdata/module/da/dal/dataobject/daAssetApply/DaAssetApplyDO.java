package tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;

import java.util.List;

/**
 * 数据资产申请 DO 对象 DA_ASSET_APPLY
 *
 * @author shu
 * @date 2025-03-19
 */
@Data
@TableName(value = "DA_ASSET_APPLY")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_APPLY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetApplyDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 资产类目 */
    @TableField(exist = false)
    private String assetName;

    /** 资产英文名称 */
    @TableField(exist = false)
    private String assetTableName;

    /** 资产类目 */
    @TableField(exist = false)
    private String catAssetName;

    /** 资产类目 */
    @TableField(exist = false)
    private String catAssetCode;

    /** 是申请过来的资产还是项目自己生成的资产0：申请，1：自创 */
    private String sourceType;

    /** 项目id */
    private Long projectId;

    /** 项目名称 */
    @TableField(exist = false)
    private String projectName;

    /** 项目编码 */
    private String projectCode;

    /** 主题名称 */
    @TableField(exist = false)
    private String themeName;

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    /** 申请理由 */
    private String applyReason;

    /** 审批理由 */
    private String approvalReason;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /** 数据源名称 */
    @TableField(exist = false)
    private String datasourceName;

    /** 数据源ip */
    @TableField(exist = false)
    private String datasourceIp;

    /** 数据源类型 */
    @TableField(exist = false)
    private String datasourceType;

    /** 资产描述 */
    @TableField(exist = false)
    private String description;

    /** 手机号码 */
    @TableField(exist = false)
    private String phonenumber;

    @TableField(exist = false)
    private String type;

}
