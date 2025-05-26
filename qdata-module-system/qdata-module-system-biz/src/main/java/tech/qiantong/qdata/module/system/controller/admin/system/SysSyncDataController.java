package tech.qiantong.qdata.module.system.controller.admin.system;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.module.system.service.auth.SysSyncDataService;

import javax.annotation.Resource;

/**
 * 接收认证平台推送的数据
 */
@RestController
@RequestMapping("/syncData")
public class SysSyncDataController {
    private static final Logger log = LoggerFactory.getLogger(SysSyncDataController.class);
    @Resource
    private SysSyncDataService sysSyncDataService;


    /**
     * 接收认证平台推送过来的数据
     *
     * @return
     */
    @PostMapping
    @Transactional
    public AjaxResult syncData(@RequestBody JSONObject jsonObject) {
        log.info("接收认证平台推送的数据:{}", jsonObject);
        return sysSyncDataService.syncData(jsonObject);
    }
}
