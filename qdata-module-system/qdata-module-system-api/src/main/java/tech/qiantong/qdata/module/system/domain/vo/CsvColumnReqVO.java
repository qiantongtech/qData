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
public class CsvColumnReqVO implements Serializable {
    private static final long serialVersionUID = 5343220360439610872L;

    @Schema(description = "csv文件路径")
    private String file;
}
