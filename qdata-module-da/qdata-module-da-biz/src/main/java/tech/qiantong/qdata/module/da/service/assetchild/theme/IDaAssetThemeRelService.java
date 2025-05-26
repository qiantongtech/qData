package tech.qiantong.qdata.module.da.service.assetchild.theme;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme.DaAssetThemeRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-主题关联关系Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetThemeRelService extends IService<DaAssetThemeRelDO> {

    /**
     * 获得数据资产-主题关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-主题关联关系分页列表
     */
    PageResult<DaAssetThemeRelDO> getDaAssetThemeRelPage(DaAssetThemeRelPageReqVO pageReqVO);

    /**
     * 获得全部数据资产-主题关联关系列表
     *
     * @return 数据资产-主题关联关系列表
     */
    List<DaAssetThemeRelRespVO> getDaAssetThemeRelList(DaAssetThemeRelPageReqVO pageReqVO);
    List<Long> getDaAssetIdList(List<Long> themeIdList);

    /**
     * 创建数据资产-主题关联关系
     *
     * @param createReqVO 数据资产-主题关联关系信息
     * @return 数据资产-主题关联关系编号
     */
    Long createDaAssetThemeRel(DaAssetThemeRelSaveReqVO createReqVO);

    void createDaAssetThemeRelList(List<String> themeIdList, Long id);

    /**
     * 更新数据资产-主题关联关系
     *
     * @param updateReqVO 数据资产-主题关联关系信息
     */
    int updateDaAssetThemeRel(DaAssetThemeRelSaveReqVO updateReqVO);

    /**
     * 删除数据资产-主题关联关系
     *
     * @param idList 数据资产-主题关联关系编号
     */
    int removeDaAssetThemeRel(Collection<Long> idList);
    int removeThemeRelByAssetId( Long assetId);

    /**
     * 获得数据资产-主题关联关系详情
     *
     * @param id 数据资产-主题关联关系编号
     * @return 数据资产-主题关联关系
     */
    DaAssetThemeRelDO getDaAssetThemeRelById(Long id);

    /**
     * 获得全部数据资产-主题关联关系列表
     *
     * @return 数据资产-主题关联关系列表
     */
    List<DaAssetThemeRelDO> getDaAssetThemeRelList();

    /**
     * 获得全部数据资产-主题关联关系 Map
     *
     * @return 数据资产-主题关联关系 Map
     */
    Map<Long, DaAssetThemeRelDO> getDaAssetThemeRelMap();


    /**
     * 导入数据资产-主题关联关系数据
     *
     * @param importExcelList 数据资产-主题关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetThemeRel(List<DaAssetThemeRelRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
