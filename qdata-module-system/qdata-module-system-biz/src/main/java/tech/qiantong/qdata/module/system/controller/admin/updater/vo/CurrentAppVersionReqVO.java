package tech.qiantong.qdata.module.system.controller.admin.updater.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Current app version request.
 *
 * @author qdata
 */
@Data
@Schema(description = "Current App Version Request")
public class CurrentAppVersionReqVO {

    @Schema(description = "Application name")
    private String name;

    @Schema(description = "Application version")
    private String version;

    @Schema(description = "Application description")
    private String description;

    @Schema(description = "Application author")
    private String author;

}
