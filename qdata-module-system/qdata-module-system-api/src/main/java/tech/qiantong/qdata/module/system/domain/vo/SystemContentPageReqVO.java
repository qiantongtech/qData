package tech.qiantong.qdata.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 系统配置 Request VO 对象 system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "系统配置 Request VO")
@Data
public class SystemContentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Integer id;
    @Schema(description = "系统名称", example = "")
    private String sysName;

    @Schema(description = "logo", example = "")
    private String logo;

    @Schema(description = "轮播图", example = "")
    private String carouselImage;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "电子邮箱", example = "")
    private String email;

    @Schema(description = "版权方", example = "")
    private String copyright;

    @Schema(description = "备案号", example = "")
    private String recordNumber;


    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "备注", example = "")
    private String remark;


}
