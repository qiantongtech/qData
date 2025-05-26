package tech.qiantong.qdata.api.ds.api.project;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;

/**
 * <P>
 * 用途:项目删除响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Data
public class DsProjectDeleteRespDTO extends DsResultDTO {
    /**
     * 是否删除成功
     */
    private Boolean data;
}
