package tech.qiantong.qdata.module.mc.service.tableColumnRelLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 元数据数据库与信息及字段信息关系-日志Service接口
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcTableColumnRelLogService extends IService<McTableColumnRelLogDO> {

    /**
     * 获得元数据数据库与信息及字段信息关系-日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 元数据数据库与信息及字段信息关系-日志分页列表
     */
    PageResult<McTableColumnRelLogDO> getMcTableColumnRelLogPage(McTableColumnRelLogPageReqVO pageReqVO);

    /**
     * 创建元数据数据库与信息及字段信息关系-日志
     *
     * @param createReqVO 元数据数据库与信息及字段信息关系-日志信息
     * @return 元数据数据库与信息及字段信息关系-日志编号
     */
    Long createMcTableColumnRelLog(McTableColumnRelLogSaveReqVO createReqVO);

    /**
     * 更新元数据数据库与信息及字段信息关系-日志
     *
     * @param updateReqVO 元数据数据库与信息及字段信息关系-日志信息
     */
    int updateMcTableColumnRelLog(McTableColumnRelLogSaveReqVO updateReqVO);

    /**
     * 删除元数据数据库与信息及字段信息关系-日志
     *
     * @param idList 元数据数据库与信息及字段信息关系-日志编号
     */
    int removeMcTableColumnRelLog(Collection<Long> idList);

    /**
     * 获得元数据数据库与信息及字段信息关系-日志详情
     *
     * @param id 元数据数据库与信息及字段信息关系-日志编号
     * @return 元数据数据库与信息及字段信息关系-日志
     */
    McTableColumnRelLogDO getMcTableColumnRelLogById(Long id);

    /**
     * 获得全部元数据数据库与信息及字段信息关系-日志列表
     *
     * @return 元数据数据库与信息及字段信息关系-日志列表
     */
    List<McTableColumnRelLogDO> getMcTableColumnRelLogList();

    /**
     * 获得全部元数据数据库与信息及字段信息关系-日志 Map
     *
     * @return 元数据数据库与信息及字段信息关系-日志 Map
     */
    Map<Long, McTableColumnRelLogDO> getMcTableColumnRelLogMap();


}
