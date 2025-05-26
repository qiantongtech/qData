package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;

import java.util.List;

/**
 * <P>
 * 用途:ds生成节点编码响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 16:57
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsNodeGenCodeRespDTO extends DsResultDTO {
    /**
     * 节点编码列表
     */
    private List<Long> data;
}
