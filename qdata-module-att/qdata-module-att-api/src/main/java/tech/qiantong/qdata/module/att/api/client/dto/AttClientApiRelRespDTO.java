package tech.qiantong.qdata.module.att.api.client.dto;

import lombok.Data;

import java.util.Date;

/**
 * 应用API服务关联 DTO 对象 ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Data
public class AttClientApiRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 应用ID */
    private Long clientId;

    /** API服务ID */
    private Long apiId;

    /** 是否永久有效 */
    private String pvFlag;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 授权状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
