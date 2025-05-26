package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据资产-视频数据 DO 对象 DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_VIDEO")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_VIDEO_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetVideoDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产id */
    private Long assetId;

    /** IP */
    private String ip;

    /** 端口号 */
    private Long port;

    /** 协议 */
    private String protocol;

    /** 平台 */
    private String platform;

    /** 配置JSON */
    private String config;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
