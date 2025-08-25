package tech.qiantong.qdata.quality.controller.da;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.quality.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.quality.service.asset.IDaAssetService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-17 10:30
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IRedisService redisService;

    @Resource
    private IDaAssetService daAssetService;

    @PostMapping("/test2")
    public AjaxResult test2() {
        redisService.set("test", "1", 1200);
        return AjaxResult.success("测试成功>>>>" + redisService.get("test"));
    }

    @PostMapping("/test3")
    public AjaxResult test3() {
        List<DaAssetDO> list = daAssetService.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .eq(DaAssetDO::getId, "198"));
        return AjaxResult.success(list);
    }
}
