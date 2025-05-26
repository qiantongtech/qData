package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.enums.ExecuteType;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:31
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DSExecuteDTO {

    /**
     * 流程id
     */
    private Long processInstanceId;

    /**
     * 执行类型
     */
    private ExecuteType executeType;
}
