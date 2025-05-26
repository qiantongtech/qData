package tech.qiantong.qdata.module.att.api.client.dto;

import lombok.Data;

/**
 * 应用管理 DTO 对象 ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Data
public class AttClientRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 应用名称 */
    private String name;

    /** 应用类型 */
    private String type;

    /** 应用秘钥 */
    private String secret;

    /** 主页地址 */
    private String homepageUrl;

    /** 允许授权的url */
    private String allowUrl;

    /** 同步地址 */
    private String syncUrl;

    /** 应用图标 */
    private String logo;

    /** 应用描述 */
    private String description;

    /** 是否公开 */
    private String publicFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
