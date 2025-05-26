package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据发现任务类目管理 Response VO 对象 ATT_DISCOVER_TASK_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "数据发现任务类目管理 Response VO")
@Data
public class AttDiscoverTaskCatRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "项目id")
    @Schema(description = "项目id")
    private Long projectId;

    @Excel(name = "项目code")
    @Schema(description = "项目code")
    private String projectCode;

    @Excel(name = "类别名称")
    @Schema(description = "类别名称", example = "")
    private String name;

    @Excel(name = "关联上级ID")
    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Excel(name = "类别排序")
    @Schema(description = "类别排序", example = "")
    private Long sortOrder;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "层级编码")
    @Schema(description = "层级编码", example = "")
    private String code;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
