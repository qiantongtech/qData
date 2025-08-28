package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DaDatasourceTableVO {

    /**
     * datasourceId
     */
    @NotNull(message = "id null")
    private Long id;

    private String tableName;

    private Integer withRule;

}
