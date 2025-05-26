package tech.qiantong.qdata.api.ds.api.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:项目保存修改DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsProjectUpdateReqDTO {

    /**
     * 项目编码（必填）
     */
    private Long projectCode;

    /**
     * 项目名称（必填）
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String description;
}
