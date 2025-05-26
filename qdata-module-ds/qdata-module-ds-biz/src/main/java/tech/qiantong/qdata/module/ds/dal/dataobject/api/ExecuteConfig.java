package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ExecuteConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @NotBlank(message = "数据源不能为空")
    private String sourceId;

    @NotNull(message = "配置方式不能为空")
    private String apiServiceType;

    private String tableId;

    private String tableName;

    private String dbName;

    @Valid
    private List<FieldParam> fieldParams;

    /**
     * 解析SQL
     */
    private String sqlText;
}
