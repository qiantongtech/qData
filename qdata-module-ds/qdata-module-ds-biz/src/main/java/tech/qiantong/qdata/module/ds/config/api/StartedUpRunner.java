package tech.qiantong.qdata.module.ds.config.api;


import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.enums.DataConstant;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.handler.MappingHandlerMapping;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;

import javax.annotation.Resource;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Resource
    private IDsApiService dsApiService;

    @Resource
    private MappingHandlerMapping mappingHandlerMapping;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            // 项目启动时，初始化已发布的接口
            List<DsApiDO> list = dsApiService.lambdaQuery()
                    .eq(DsApiDO::getStatus, DataConstant.ApiState.WAIT.getKey())
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(api -> mappingHandlerMapping.registerMapping(api));
            }
        }
    }
}
