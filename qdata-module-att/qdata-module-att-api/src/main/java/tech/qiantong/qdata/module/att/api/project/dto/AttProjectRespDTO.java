package tech.qiantong.qdata.module.att.api.project.dto;

import lombok.Data;

/**
 * 项目 DTO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Data
public class AttProjectRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目名称 */
    private String name;

    /** 项目管理员id */
    private Long managerId;

    /** 项目描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 是否分配到数据研发 */
    private Boolean dppAssigned;

    /** 项目编码 */
    private String code;

    /** 项目管理员 */
    private String nickName;
    /** 项目管理员手机号 */
    private String managerPhone;


}
