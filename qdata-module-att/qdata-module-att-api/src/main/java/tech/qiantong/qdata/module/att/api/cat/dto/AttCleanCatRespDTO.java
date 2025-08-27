package tech.qiantong.qdata.module.att.api.cat.dto;

import lombok.Data;

/**
 * 清洗规则类目 DTO 对象 ATT_CLEAN_CAT
 *
 * @author qdata
 * @date 2025-08-11
 */
@Data
public class AttCleanCatRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 类别名称 */
    private String name;

    /** 关联上级ID */
    private Long parentId;

    /** 类别排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 层级编码 */
    private String CODE;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
