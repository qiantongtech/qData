package tech.qiantong.qdata.quality.controller.da.datasource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;


/**
 * 数据源 Response VO 对象 DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据源 Response VO")
@Data
public class DaDatasourceRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "数据源配置(json字符串)")
    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Excel(name = "项目名称")
    @Schema(description = "项目名称", example = "")
    private String projectName;

    @Excel(name = "是否是管理员分配给数据研发")
    @Schema(description = "是否是管理员分配给数据研发", example = "")
    private Boolean isAdminAddTo;

    @Excel(name = "IP")
    @Schema(description = "IP", example = "")
    private String ip;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    @Excel(name = "数据库表数", readConverterExp = "预=留")
    @Schema(description = "数据库表数", example = "")
    private Long listCount;

    @Excel(name = "同步记录数", readConverterExp = "预=留")
    @Schema(description = "同步记录数", example = "")
    private Long syncCount;

    @Excel(name = "同步数据量大小", readConverterExp = "预=留")
    @Schema(description = "同步数据量大小", example = "")
    private Long dataSize;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
