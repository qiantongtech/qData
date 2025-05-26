package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * 数据元代码 DTO 对象 DP_DATA_ELEM_CODE
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemCodeReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据元id */
    private String dataElemId;

    /** 代码值 */
    private String codeValue;

    /** 代码名称 */
    private String codeName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
