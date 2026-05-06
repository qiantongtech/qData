package tech.qiantong.qdata.module.mc.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;
import tech.qiantong.qdata.module.mc.service.task.impl.McTableTxService;
import tech.qiantong.qdata.module.mc.service.task.impl.McTaskServiceTemporary;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-12-17 10:41
 **/
@Component
public class DemoXxlJob {

    @Resource
    @Lazy
    private IMcTaskService mcTaskService;

    @Resource
    @Lazy
    private McTaskServiceTemporary mcTaskServiceTemporary;

    @Resource
    @Lazy
    private McTableTxService mcTableTxService;

    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");
        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        // default success
    }



    @XxlJob("runDaDiscoveryTask")
    public void runDaDiscoveryTask() throws Exception {
        String param = XxlJobHelper.getJobParam();
        if (param == null || param.isEmpty()) {
            throw new RuntimeException("任务参数不能为空");
        }
        mcTaskService.runDaDiscoveryTask(JSONUtils.convertToLong(param));

//
//        try {
//            mcTableTxService.runInNewTx(() -> mcTaskServiceTemporary.runDaDiscoveryTask(JSONUtils.convertToLong(param)));
//        }catch (Exception e){
//
//        }finally {
//
//        }
    }
}
