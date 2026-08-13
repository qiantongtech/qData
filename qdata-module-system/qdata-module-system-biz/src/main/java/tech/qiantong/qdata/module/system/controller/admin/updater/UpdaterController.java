package tech.qiantong.qdata.module.system.controller.admin.updater;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.CurrentAppVersionReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.VersionInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Version Update
 *
 * @author qknow
 */
@RestController
@RequestMapping("/updater")
public class UpdaterController extends BaseController {

    /** System base config */
    @Autowired
    private AniviaConfig qdataConfig;

    /**
     * Get current deployment instance version
     */
    @GetMapping("/getLocalVersion")
    public CommonResult<Map<String, Object>> getLocalVersion() {
        String currentVersion = qdataConfig.getVersion();
        Map<String, Object> result = new HashMap<>();
        result.put("latestVersion", currentVersion);
        return CommonResult.success(result);
    }

    /**
     * Check if current instance is the latest version
     */
    @GetMapping("/getCurrentAppVersion")
    public CommonResult<VersionInfo> getCurrentAppVersion(CurrentAppVersionReqVO reqVO) {
        // Get local version info
        String currentVersion = reqVO.getVersion();
        // Initial latest version
        String latestVersion = "";
        // Whether update is needed
        boolean needUpdate = true;
        try {
            String remoteUrl = "https://qdata-pro.qiantong.tech/prod-api/updater/getLocalVersion";
            HttpUtils.ResponseObject response = HttpUtils.sendGet(remoteUrl, null);
            if (response.getStatus() == 200) {
                // Convert body to Map directly
                Map<?, ?> responseMap = parseResponseBody(response.getBody());
                if (responseMap != null) {
                    // Extract version info
                    Object versionData = responseMap.get("data");
                    Object version;
                    if (versionData instanceof Map) {
                        version = ((Map<?, ?>) versionData).get("latestVersion");
                    } else {
                        version = responseMap.get("latestVersion");
                    }
                    if (version != null) {
                        latestVersion = version.toString();
                    }
                    needUpdate = !currentVersion.equals(latestVersion);
                }
            }
        } catch (Exception e) {
            logger.error("Version check failed", e);
        }
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setCurrentVersion(currentVersion);
        versionInfo.setLatestVersion(latestVersion);
        versionInfo.setNeedUpdate(needUpdate);
        return CommonResult.success(versionInfo);
    }

    @PostMapping("/tractlatestAppVersion")
    public void tractlatestAppVersion(@RequestBody SystemVersionTrackSaveReqVO reqVO) {
        try {
            String remoteUrl = "https://qdata-pro.qiantong.tech/prod-api/system/VersionTrack";
            HttpUtils.sendPost(remoteUrl, buildVersionTrackParams(reqVO), null);
        } catch (Exception e) {
            logger.error("Version track forwarding failed", e);
        }
    }

    /**
     * Build version track forwarding parameters.
     */
    private Map<String, Object> buildVersionTrackParams(SystemVersionTrackSaveReqVO reqVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", reqVO.getId());
        params.put("name", reqVO.getName());
        params.put("currVersion", reqVO.getCurrVersion());
        params.put("description", reqVO.getDescription());
        params.put("author", reqVO.getAuthor());
        params.put("remark", reqVO.getRemark());
        return params;
    }

    /**
     * Parse response body to Map
     */
    private Map<?, ?> parseResponseBody(Object body) {
        if (body instanceof Map) {
            return (Map<?, ?>) body;
        } else if (body instanceof String) {
            try {
                return JSONObject.parseObject((String) body, Map.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
