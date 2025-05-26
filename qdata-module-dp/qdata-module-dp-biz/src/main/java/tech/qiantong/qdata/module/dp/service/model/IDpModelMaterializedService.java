package tech.qiantong.qdata.module.dp.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpMaterializedMethodReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 物化模型记录Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelMaterializedService extends IService<DpModelMaterializedDO> {

    /**
     * 获得物化模型记录分页列表
     *
     * @param pageReqVO 分页请求
     * @return 物化模型记录分页列表
     */
    PageResult<DpModelMaterializedDO> getDpModelMaterializedPage(DpModelMaterializedPageReqVO pageReqVO);

    /**
     * 创建物化模型记录
     *
     * @param createReqVO 物化模型记录信息
     * @return 物化模型记录编号
     */
    Long createDpModelMaterialized(DpModelMaterializedSaveReqVO createReqVO);

    /**
     * 更新物化模型记录
     *
     * @param updateReqVO 物化模型记录信息
     */
    int updateDpModelMaterialized(DpModelMaterializedSaveReqVO updateReqVO);

    /**
     * 删除物化模型记录
     *
     * @param idList 物化模型记录编号
     */
    int removeDpModelMaterialized(Collection<Long> idList);

    /**
     * 获得物化模型记录详情
     *
     * @param id 物化模型记录编号
     * @return 物化模型记录
     */
    DpModelMaterializedDO getDpModelMaterializedById(Long id);

    /**
     * 获得全部物化模型记录列表
     *
     * @return 物化模型记录列表
     */
    List<DpModelMaterializedDO> getDpModelMaterializedList();

    /**
     * 获得全部物化模型记录 Map
     *
     * @return 物化模型记录 Map
     */
    Map<Long, DpModelMaterializedDO> getDpModelMaterializedMap();


    /**
     * 导入物化模型记录数据
     *
     * @param importExcelList 物化模型记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpModelMaterialized(List<DpModelMaterializedRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Long createMaterializedTable(DpMaterializedMethodReqVO dpModelMaterialized);
}
