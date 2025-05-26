package tech.qiantong.qdata.module.da.api.datasource.dto;

import lombok.Data;

/**
 * 数据源与项目关联关系 DTO 对象 DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Data
public class DaDatasourceProjectRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 数据源id */
    private Long datasourceId;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;


}
