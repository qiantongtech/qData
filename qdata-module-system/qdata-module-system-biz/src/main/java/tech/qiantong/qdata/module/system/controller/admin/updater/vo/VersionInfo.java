package tech.qiantong.qdata.module.system.controller.admin.updater.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author asd
 */
@Data
@Schema(description = "版本信息")
public class VersionInfo {

    // 本地版本号
    private String currentVersion;

    // 最新版本号
    private String latestVersion;

    // 是否需要更新
    private boolean needUpdate;

}
