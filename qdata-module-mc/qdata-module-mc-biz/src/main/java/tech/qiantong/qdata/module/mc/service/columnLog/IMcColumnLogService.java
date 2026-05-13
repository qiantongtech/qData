package tech.qiantong.qdata.module.mc.service.columnLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 元数据字段信息 - 日志Service接口
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcColumnLogService extends IService<McColumnLogDO> {

    /**
     * 获得元数据字段信息 - 日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 元数据字段信息 - 日志分页列表
     */
    PageResult<McColumnLogDO> getMcColumnLogPage(McColumnLogPageReqVO pageReqVO);

    /**
     * 创建元数据字段信息 - 日志
     *
     * @param createReqVO 元数据字段信息 - 日志信息
     * @return 元数据字段信息 - 日志编号
     */
    Long createMcColumnLog(McColumnLogSaveReqVO createReqVO);

    /**
     * 批量创建元数据字段信息 - 日志
     *
     * @param columnDOList 元数据字段信息列表
     * @return 元数据字段信息
     */
    Long createMcColumnLog(List<McColumnDO> columnDOList);

    /**
     * 更新元数据字段信息 - 日志
     *
     * @param updateReqVO 元数据字段信息 - 日志信息
     */
    int updateMcColumnLog(McColumnLogSaveReqVO updateReqVO);

    /**
     * 删除元数据字段信息 - 日志
     *
     * @param idList 元数据字段信息 - 日志编号
     */
    int removeMcColumnLog(Collection<Long> idList);

    /**
     * 获得元数据字段信息 - 日志详情
     *
     * @param id 元数据字段信息 - 日志编号
     * @return 元数据字段信息 - 日志
     */
    McColumnLogDO getMcColumnLogById(Long id);

    /**
     * 获得全部元数据字段信息 - 日志列表
     *
     * @return 元数据字段信息 - 日志列表
     */
    List<McColumnLogDO> getMcColumnLogList();

    /**
     * 获得全部元数据字段信息 - 日志 Map
     *
     * @return 元数据字段信息 - 日志 Map
     */
    Map<Long, McColumnLogDO> getMcColumnLogMap();


}
