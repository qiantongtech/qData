package tech.qiantong.qdata.module.dp.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 逻辑模型属性信息Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelColumnService extends IService<DpModelColumnDO> {

    /**
     * 获得逻辑模型属性信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型属性信息分页列表
     */
    PageResult<DpModelColumnDO> getDpModelColumnPage(DpModelColumnPageReqVO pageReqVO);

    /**
     * 创建逻辑模型属性信息
     *
     * @param createReqVO 逻辑模型属性信息信息
     * @return 逻辑模型属性信息编号
     */
    Long createDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * 更新逻辑模型属性信息
     *
     * @param updateReqVO 逻辑模型属性信息信息
     */
    int updateDpModelColumn(DpModelColumnSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型属性信息
     *
     * @param idList 逻辑模型属性信息编号
     */
    int removeDpModelColumn(Collection<Long> idList);

    /**
     * 批量删除逻辑模型属性信息
     *
     * @param modelIdList 逻辑模型编号
     */
    int removeDpModelColumnByModelId(Collection<Long> modelIdList);

    /**
     * 获得逻辑模型属性信息详情
     *
     * @param id 逻辑模型属性信息编号
     * @return 逻辑模型属性信息
     */
    DpModelColumnDO getDpModelColumnById(Long id);

    /**
     * 获得全部逻辑模型属性信息列表
     *
     * @return 逻辑模型属性信息列表
     */
    List<DpModelColumnDO> getDpModelColumnList();
    List<DpModelColumnDO> getDpModelColumnList(DpModelColumnSaveReqVO createReqVO);

    long countByDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * 获得全部逻辑模型属性信息 Map
     *
     * @return 逻辑模型属性信息 Map
     */
    Map<Long, DpModelColumnDO> getDpModelColumnMap();


    /**
     * 导入逻辑模型属性信息数据
     *
     * @param importExcelList 逻辑模型属性信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpModelColumn(List<DpModelColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 批量插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    Boolean createDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);

    /**
     * 批量修改和插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    Boolean updateDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);
}
