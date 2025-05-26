package tech.qiantong.qdata.module.da.api.datasource.dto;

import lombok.Data;

/**
 * 数据源 DTO 对象 DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaDatasourceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据源名称 */
    private String datasourceName;

    /** 数据源类型 */
    private String datasourceType;

    /** 数据源配置(json字符串) */
    private String datasourceConfig;

    /** IP */
    private String ip;

    /** 端口号 */
    private Long port;

    /** 数据库表数（预留） */
    private Long listCount;

    /** 同步记录数（预留） */
    private Long syncCount;

    /** 同步数据量大小（预留） */
    private Long dataSize;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;


}
