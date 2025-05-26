package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:手动启动一次的参数
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 18:12
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsStartTaskReqDTO {
    /**
     * 流程编码
     */
    private Long processDefinitionCode;
    /**
     * 失败策略 写死 CONTINUE
     * CONTINUE 为继续
     */
    private String failureStrategy;
    /**
     * 写死 NONE
     */
    private String warningType;
    /**
     * 写死 MEDIUM
     */
    private String processInstancePriority;
    /**
     * 写死{"complementStartDate":"当前天 00:00:00","complementEndDate":"当前天 00:00:00"}
     * 例子{"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
     */
    private String scheduleTime;//: {"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
}
