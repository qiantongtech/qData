package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据质量日志Service接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IDppQualityLogService extends IService<DppQualityLogDO> {

    /**
     * 获得数据质量日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量日志分页列表
     */
    PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO);

    /**
     * 创建数据质量日志
     *
     * @param createReqVO 数据质量日志信息
     * @return 数据质量日志编号
     */
    Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO);

    /**
     * 更新数据质量日志
     *
     * @param updateReqVO 数据质量日志信息
     */
    int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO);

    /**
     * 删除数据质量日志
     *
     * @param idList 数据质量日志编号
     */
    int removeDppQualityLog(Collection<Long> idList);

    /**
     * 获得数据质量日志详情
     *
     * @param id 数据质量日志编号
     * @return 数据质量日志
     */
    DppQualityLogDO getDppQualityLogById(Long id);

    /**
     * 获得全部数据质量日志列表
     *
     * @return 数据质量日志列表
     */
    List<DppQualityLogDO> getDppQualityLogList();

    /**
     * 获得全部数据质量日志 Map
     *
     * @return 数据质量日志 Map
     */
    Map<Long, DppQualityLogDO> getDppQualityLogMap();


    /**
     * 导入数据质量日志数据
     *
     * @param importExcelList 数据质量日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
