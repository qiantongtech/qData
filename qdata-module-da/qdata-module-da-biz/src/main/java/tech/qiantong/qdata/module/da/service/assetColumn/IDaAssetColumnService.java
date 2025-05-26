package tech.qiantong.qdata.module.da.service.assetColumn;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产字段Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetColumnService extends IService<DaAssetColumnDO> {

    /**
     * 获得数据资产字段分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产字段分页列表
     */
    PageResult<DaAssetColumnDO> getDaAssetColumnPage(DaAssetColumnPageReqVO pageReqVO);


    AjaxResult getColumnByAssetId(DaAssetColumnPageReqVO pageReqVO);

    List<DaAssetColumnDO> getDaAssetColumnList(DaAssetColumnPageReqVO pageReqVO);
    /**
     * 创建数据资产字段
     *
     * @param createReqVO 数据资产字段信息
     * @return 数据资产字段编号
     */
    Long createDaAssetColumn(DaAssetColumnSaveReqVO createReqVO);

    /**
     * 更新数据资产字段
     *
     * @param updateReqVO 数据资产字段信息
     */
    int updateDaAssetColumn(DaAssetColumnSaveReqVO updateReqVO);

    /**
     * 删除数据资产字段
     *
     * @param idList 数据资产字段编号
     */
    int removeDaAssetColumn(Collection<Long> idList);

    /**
     * 获得数据资产字段详情
     *
     * @param id 数据资产字段编号
     * @return 数据资产字段
     */
    DaAssetColumnDO getDaAssetColumnById(Long id);

    /**
     * 获得全部数据资产字段列表
     *
     * @return 数据资产字段列表
     */
    List<DaAssetColumnDO> getDaAssetColumnList();

    /**
     * 获得全部数据资产字段 Map
     *
     * @return 数据资产字段 Map
     */
    Map<Long, DaAssetColumnDO> getDaAssetColumnMap();


    /**
     * 导入数据资产字段数据
     *
     * @param importExcelList 数据资产字段数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetColumn(List<DaAssetColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
