package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <P>
 * 用途:实例日志响应数据
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-01 13:49
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DppEtlTaskInstanceLogStatusRespDTO {

    /**
     * 状态;0：提交成功 1：正在执行 2:准备暂停 3：暂停 4：准备停止 5：停止 6：失败 7：成功 12：延时执行  14：串行等待  15 ：准备锁定 16：锁定
     * 5：停止 6：失败 7：成功 时停止日志轮询
     */
    private String status;

    /**
     * 状态 1:进行中 2:已结束
     */
    private String log;

    /**
     * 节点实例列表
     */
    private List<DppEtlNodeInstanceRespDTO> nodeInstanceList;
}
