package tech.qiantong.qdata.module.mc.service.tableLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 元数据信息 - 日志Service接口
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcTableLogService extends IService<McTableLogDO> {

    /**
     * 获得元数据信息 - 日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 元数据信息 - 日志分页列表
     */
    PageResult<McTableLogDO> getMcTableLogPage(McTableLogPageReqVO pageReqVO);

    /**
     * 创建元数据信息 - 日志
     *
     * @param createReqVO 元数据信息 - 日志信息
     * @return 元数据信息 - 日志编号
     */
    Long createMcTableLog(McTableLogSaveReqVO createReqVO);

    /**
     * 更新元数据信息 - 日志
     *
     * @param updateReqVO 元数据信息 - 日志信息
     */
    int updateMcTableLog(McTableLogSaveReqVO updateReqVO);

    /**
     * 删除元数据信息 - 日志
     *
     * @param idList 元数据信息 - 日志编号
     */
    int removeMcTableLog(Collection<Long> idList);

    /**
     * 获得元数据信息 - 日志详情
     *
     * @param id 元数据信息 - 日志编号
     * @return 元数据信息 - 日志
     */
    McTableLogDO getMcTableLogById(Long id);

    /**
     * 获得全部元数据信息 - 日志列表
     *
     * @return 元数据信息 - 日志列表
     */
    List<McTableLogDO> getMcTableLogList();

    /**
     * 获得全部元数据信息 - 日志 Map
     *
     * @return 元数据信息 - 日志 Map
     */
    Map<Long, McTableLogDO> getMcTableLogMap();

    /**
     * 根据元数据表信息添加元数据版本变更日志
     * @param table 元数据表信息
     * @return 元数据版本变更日志id
     */
    Long createMcTableLog(McTableSaveReqVO table);
}
