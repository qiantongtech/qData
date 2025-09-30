package tech.qiantong.qdata.module.dpp.service.qa;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
/**
 * 数据质量任务-稽查对象Service接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskObjService extends IService<DppQualityTaskObjDO> {

    /**
     * 获得数据质量任务-稽查对象分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量任务-稽查对象分页列表
     */
    PageResult<DppQualityTaskObjDO> getDppQualityTaskObjPage(DppQualityTaskObjPageReqVO pageReqVO);

    /**
     * 创建数据质量任务-稽查对象
     *
     * @param createReqVO 数据质量任务-稽查对象信息
     * @return 数据质量任务-稽查对象编号
     */
    Long createDppQualityTaskObj(DppQualityTaskObjSaveReqVO createReqVO);

    /**
     * 更新数据质量任务-稽查对象
     *
     * @param updateReqVO 数据质量任务-稽查对象信息
     */
    int updateDppQualityTaskObj(DppQualityTaskObjSaveReqVO updateReqVO);

    /**
     * 删除数据质量任务-稽查对象
     *
     * @param idList 数据质量任务-稽查对象编号
     */
    int removeDppQualityTaskObj(Collection<Long> idList);

    /**
     * 获得数据质量任务-稽查对象详情
     *
     * @param id 数据质量任务-稽查对象编号
     * @return 数据质量任务-稽查对象
     */
    DppQualityTaskObjDO getDppQualityTaskObjById(Long id);

    /**
     * 获得全部数据质量任务-稽查对象列表
     *
     * @return 数据质量任务-稽查对象列表
     */
    List<DppQualityTaskObjDO> getDppQualityTaskObjList();
    List<DppQualityTaskObjDO> getDppQualityTaskObjList(DppQualityTaskObjPageReqVO pageReqVO);

    /**
     * 获得全部数据质量任务-稽查对象 Map
     *
     * @return 数据质量任务-稽查对象 Map
     */
    Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap();


    /**
     * 导入数据质量任务-稽查对象数据
     *
     * @param importExcelList 数据质量任务-稽查对象数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
