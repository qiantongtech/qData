package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据资产操作记录 DO 对象 DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
@TableName(value = "DA_ASSET_OPERATE_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_OPERATE_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetOperateLogDO extends BaseEntity {
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

    /** 操作时间 */
    private Date operateTime;

    /** 执行时间 */
    private Date executeTime;

    /** 修改前数据(JSON数据) */
    private String updateBefore;

    /** 修改后数据(JSON数据) */
    private String updateAfter;

    /** 字段 */
    private String fieldNames;

    /** 导入文件URL */
    private String fileUrl;

    /** 导入文件名称 */
    private String fileName;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;



    /** 用户名称 */
    @TableField(exist = false)
    private String userName;

    /** 手机号码 */
    @TableField(exist = false)
    private String phoneNumber;

    /** 用户昵称 */
    @TableField(exist = false)
    private String nickName;
}
