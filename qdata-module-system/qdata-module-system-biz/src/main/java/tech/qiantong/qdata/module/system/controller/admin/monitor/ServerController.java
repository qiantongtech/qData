package tech.qiantong.qdata.module.system.controller.admin.monitor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.config.web.domain.Server;

/**
 * 服务器监控
 *
 * @author qdata
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
