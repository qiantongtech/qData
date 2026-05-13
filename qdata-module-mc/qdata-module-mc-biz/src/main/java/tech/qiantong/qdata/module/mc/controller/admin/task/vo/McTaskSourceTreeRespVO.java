package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 来源系统树形结构 Response VO
 *
 * @author qdata
 * @date 2026-04-27
 */
@Schema(description = "来源系统树形结构 Response VO")
@Data
public class McTaskSourceTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     * - SOURCE节点: 来源系统ID
     * - DATASOURCE节点: 数据源ID
     * - DATABASE节点: 任务ID(统一使用任务ID,方便后续查询)
     */
    @Schema(description = "节点ID", example = "1")
    private Long id;

    /**
     * 节点名称
     */
    @Schema(description = "节点名称", example = "名称")
    private String name;

    /**
     * 节点类型: SOURCE-来源系统, DATASOURCE-数据源, DATABASE-数据库
     */
    @Schema(description = "节点类型: SOURCE-来源系统, DATASOURCE-数据源, DATABASE-数据库", example = "SOURCE")
    private String type;

    @Schema(description = "数据源类型")
    private String datasourceType;


    @Schema(description = "采集任务ID")
    private Long taskId;

    /**
     * 子节点列表
     */
    @Schema(description = "子节点列表")
    private List<McTaskSourceTreeRespVO> children;
}
