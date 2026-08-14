package tech.qiantong.qdata.module.system.controller.admin.updater;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.CurrentAppVersionReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.service.updater.ISystemVersionTrackService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Version Update
 *
 * @author qknow
 */
@RestController
@RequestMapping("/updater")
public class UpdaterController extends BaseController {

    @Resource
    private ISystemVersionTrackService systemVersionTrackService;


    /**
     * Get current deployment instance version
     */
  /*  @GetMapping("/getLocalVersion")
    public CommonResult<Map<String, Object>> getLocalVersion(CurrentAppVersionReqVO reqVO) {
        String currentVersion = qdataConfig.getVersion();
        Map<String, Object> result = new HashMap<>();
        result.put("latestVersion", currentVersion);
        return CommonResult.success(result);
    }*/

    @PostMapping("/getLocalVersion")
    public void getLocalVersion(@RequestBody CurrentAppVersionReqVO reqVO) {
        if (reqVO != null) {
            SystemVersionTrackSaveReqVO saveReqVO = new SystemVersionTrackSaveReqVO();
            saveReqVO.setName(reqVO.getName());
            saveReqVO.setCurrVersion(reqVO.getVersion());
            saveReqVO.setAuthor(reqVO.getAuthor());
            saveReqVO.setDescription(reqVO.getDescription());
            systemVersionTrackService.createSystemVersionTrack(saveReqVO);
        }
    }

    /**
     * Check if current instance is the latest version
     */
//    @GetMapping("/getCurrentAppVersion")
//    public CommonResult<VersionInfo> getCurrentAppVersion(CurrentAppVersionReqVO reqVO) {
//        // Get local version info
//        String currentVersion = reqVO.getVersion();
//        // Initial latest version
//        String latestVersion = "";
//        // Whether update is needed
//        boolean needUpdate = true;
//        try {
//            String remoteUrl = "https://qdata-pro.qiantong.tech/prod-api/updater/getLocalVersion";
//            HttpUtils.ResponseObject response = HttpUtils.sendGet(remoteUrl, null);
//            if (response.getStatus() == 200) {
//                // Convert body to Map directly
//                Map<?, ?> responseMap = parseResponseBody(response.getBody());
//                if (responseMap != null) {
//                    // Extract version info
//                    Object versionData = responseMap.get("data");
//                    Object version;
//                    if (versionData instanceof Map) {
//                        version = ((Map<?, ?>) versionData).get("latestVersion");
//                    } else {
//                        version = responseMap.get("latestVersion");
//                    }
//                    if (version != null) {
//                        latestVersion = version.toString();
//                    }
//                    needUpdate = !currentVersion.equals(latestVersion);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Version check failed", e);
//        }
//        VersionInfo versionInfo = new VersionInfo();
//        versionInfo.setCurrentVersion(currentVersion);
//        versionInfo.setLatestVersion(latestVersion);
//        versionInfo.setNeedUpdate(needUpdate);
//        return CommonResult.success(versionInfo);
//    }

    @GetMapping("/getCurrentAppVersion")
    public void getCurrentAppVersion(CurrentAppVersionReqVO reqVO) {
        try {
            String remoteUrl = "https://demo.qdata.tech/prod-api/updater/getLocalVersion";
            //String remoteUrl = "http://localhost:8080/updater/getLocalVersion";
            Map<String,Object> mp=  new HashMap<String,Object>();
            if (reqVO != null) {
                mp.put("name",reqVO.getName());
                mp.put("version",reqVO.getVersion());
                mp.put("author",reqVO.getAuthor());
                mp.put("description",reqVO.getDescription());
            }
            List<HeaderEntity> headers = new ArrayList<>();
            HeaderEntity contentType = new HeaderEntity();
            contentType.setKey("Content-Type");
            contentType.setValue("application/json;charset=UTF-8");
            headers.add(contentType);
            HttpUtils.sendPost(remoteUrl, mp, headers);
        } catch (Exception e) {
            logger.error("Version check failed", e);
        }
    }

   /* @PostMapping("/tractlatestAppVersion")
    public void tractlatestAppVersion(@RequestBody SystemVersionTrackSaveReqVO reqVO) {
        try {
            String remoteUrl = "https://qdata-pro.qiantong.tech/prod-api/system/VersionTrack";
            HttpUtils.sendPost(remoteUrl, buildVersionTrackParams(reqVO), null);
        } catch (Exception e) {
            logger.error("Version track forwarding failed", e);
        }
    }
*/
    /**
     * Build version track forwarding parameters.
     */
//    private Map<String, Object> buildVersionTrackParams(SystemVersionTrackSaveReqVO reqVO) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", reqVO.getId());
//        params.put("name", reqVO.getName());
//        params.put("currVersion", reqVO.getCurrVersion());
//        params.put("description", reqVO.getDescription());
//        params.put("author", reqVO.getAuthor());
//        params.put("remark", reqVO.getRemark());
//        return params;
//    }

}
