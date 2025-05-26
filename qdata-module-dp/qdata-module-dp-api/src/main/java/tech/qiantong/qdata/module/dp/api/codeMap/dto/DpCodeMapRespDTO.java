package tech.qiantong.qdata.module.dp.api.codeMap.dto;

import lombok.Data;

/**
 * 数据元代码映射 DTO 对象 DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpCodeMapRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据元id */
    private String dataElemId;

    /** 原始值 */
    private String originalValue;

    /** 代码名 */
    private String codeName;

    /** 代码值 */
    private String codeValue;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
