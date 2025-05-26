package tech.qiantong.qdata.api.ds.api.base;

import lombok.Data;

/**
 * <P>
 * 用途:DS结果VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 15:58
 **/
@Data
public class DsResultDTO {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 是否失败
     */
    private Boolean failed;

    /**
     * 是否成功
     */
    private Boolean success;
}
