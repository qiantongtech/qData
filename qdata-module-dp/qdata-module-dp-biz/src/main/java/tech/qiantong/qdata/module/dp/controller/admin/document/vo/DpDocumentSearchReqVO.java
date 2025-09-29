package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * <P>
 * 用途:标准检索响应VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-22 10:08
 **/
@Data
public class DpDocumentSearchReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "检索内容", example = "")
    private String search;
}
