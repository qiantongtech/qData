package tech.qiantong.qdata.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-13 15:49
 **/
@Data
public class ExcelColumnReqVO implements Serializable {
    private static final long serialVersionUID = 5343220360439610872L;

    @Schema(description = "Excel文件路径")
    private String excelFile;

    @Schema(description = "列开始行")
    private Integer startColumn;

    @Schema(description = "数据开始行")
    private Integer startData;
}
