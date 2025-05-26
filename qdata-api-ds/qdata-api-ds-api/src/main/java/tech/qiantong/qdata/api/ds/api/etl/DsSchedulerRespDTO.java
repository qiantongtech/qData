package tech.qiantong.qdata.api.ds.api.etl;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;

/**
 * <P>
 * 用途:调度器响应参数DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:18
 **/
@Data
public class DsSchedulerRespDTO extends DsResultDTO {
    private Schedule data;
}
