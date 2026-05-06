package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 采集任务 Request VO 对象 MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务 Request VO")
@Data
public class McTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "数据库类型", example = "")
    private String dbType;

    @Schema(description = "责任人", example = "")
    private Long leader;

    @Schema(description = "责任部门", example = "")
    private Long responsibleDept;

    @Schema(description = "责任人电话", example = "")
    private String leaderPhone;

    @Schema(description = "采集模式", example = "")
    private String collectionMode;

    @Schema(description = "采集范围", example = "")
    private String collectionScope;

    @Schema(description = "任务状态", example = "")
    private String status;

    /**
     * 采集任务类型：1-采集，2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * 采集黑名单
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;



    @Schema(description = "描述", example = "")
    private String description;

    @Schema(hidden = true)
    private String bizScopeMode;

    @Schema(hidden = true)
    private Boolean bizScopeIncludeUnassigned;


    @Schema(description = "创建时间-开始")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(exist = false)
    private Date createTimeStart;

    @Schema(description = "创建时间-结束")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(exist = false)
    private Date createTimeEnd;

}
