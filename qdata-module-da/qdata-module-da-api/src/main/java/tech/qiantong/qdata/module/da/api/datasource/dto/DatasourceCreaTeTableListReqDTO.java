package tech.qiantong.qdata.module.da.api.datasource.dto;

import lombok.Data;

import java.util.List;

@Data
public class DatasourceCreaTeTableListReqDTO {

    /** 数据源类型 */
    private String datasourceType;

    /** 数据源配置(json字符串) */
    private String datasourceConfig;

    /** IP */
    private String ip;

    /** 端口号 */
    private Long port;


    private List<DatasourceCreaTeTableReqDTO> dtoList;
}
