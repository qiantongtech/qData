package tech.qiantong.qdata.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-13 15:49
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ColumnRespVO implements Serializable {
    private static final long serialVersionUID = 5343220360439610872L;

    @Schema(description = "Excel文件路径")
    private String csvFile;

    @Schema(description = "字段")
    private List<String> columnList;
}
