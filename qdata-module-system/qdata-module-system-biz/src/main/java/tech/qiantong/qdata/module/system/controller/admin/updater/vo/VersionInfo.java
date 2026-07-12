package tech.qiantong.qdata.module.system.controller.admin.updater.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author asd
 */
@Data
@Schema(description = "Version Info")
public class VersionInfo {

    // Local version number
    private String currentVersion;

    // Latest version number
    private String latestVersion;

    // Whether update is needed
    private boolean needUpdate;

}
