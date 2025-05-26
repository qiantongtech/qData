package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SqlParseDto implements Serializable {

    private static final long serialVersionUID=1L;

    @NotBlank(message = "数据源不能为空")
    private String sourceId;

    @NotBlank(message = "SQL不能为空")
    private String sqlText;
}
