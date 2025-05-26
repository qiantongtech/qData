package tech.qiantong.qdata.api.ds.api.base;

import lombok.Data;

/**
 * <P>
 * 用途:状态相关接口响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Data
public class DsStatusRespDTO extends DsResultDTO {
    /**
     * 是否成功
     */
    private Boolean data;
}
