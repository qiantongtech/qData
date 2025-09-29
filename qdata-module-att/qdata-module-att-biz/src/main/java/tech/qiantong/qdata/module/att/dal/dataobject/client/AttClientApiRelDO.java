package tech.qiantong.qdata.module.att.dal.dataobject.client;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 应用API服务关联 DO 对象 ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Data
@TableName(value = "ATT_CLIENT_API_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_CLIENT_API_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttClientApiRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用ID
     */
    private Long clientId;

    /**
     * API服务ID
     */
    private Long apiId;

    /**
     * API服务名称
     */
    @TableField(exist = false)
    private String apiName;

    /**
     * API服务路径
     */
    @TableField(exist = false)
    private String apiUrl;

    /**
     * 请求方式 1：get，2：post（ds_api_bas_info_api_method_type）
     */
    @TableField(exist = false)
    private String reqMethod;

    /**
     * 是否永久有效
     */
    private String pvFlag;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 授权状态
     */
    private String status;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;
}
