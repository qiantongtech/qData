package tech.qiantong.qdata.api.ds.api.etl;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;

/**
 * <P>
 * 用途:任务保存请求响应参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-20 09:06
 **/
@Data
public class DsTaskSaveRespDTO extends DsResultDTO {
    private ProcessDefinition data;
}
