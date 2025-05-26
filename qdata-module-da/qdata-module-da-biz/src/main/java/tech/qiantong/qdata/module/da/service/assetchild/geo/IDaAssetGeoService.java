package tech.qiantong.qdata.module.da.service.assetchild.geo;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-矢量Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetGeoService extends IService<DaAssetGeoDO> {

    /**
     * 获得数据资产-矢量分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-矢量分页列表
     */
    PageResult<DaAssetGeoDO> getDaAssetGeoPage(DaAssetGeoPageReqVO pageReqVO);

    DaAssetGeoRespVO getDaAssetGeoByAssetId(Long assetId);

    /**
     * 创建数据资产-矢量
     *
     * @param createReqVO 数据资产-矢量信息
     * @return 数据资产-矢量编号
     */
    Long createDaAssetGeo(DaAssetGeoSaveReqVO createReqVO);

    /**
     * 更新数据资产-矢量
     *
     * @param updateReqVO 数据资产-矢量信息
     */
    int updateDaAssetGeo(DaAssetGeoSaveReqVO updateReqVO);

    /**
     * 删除数据资产-矢量
     *
     * @param idList 数据资产-矢量编号
     */
    int removeDaAssetGeo(Collection<Long> idList);

    /**
     * 获得数据资产-矢量详情
     *
     * @param id 数据资产-矢量编号
     * @return 数据资产-矢量
     */
    DaAssetGeoDO getDaAssetGeoById(Long id);

    /**
     * 获得全部数据资产-矢量列表
     *
     * @return 数据资产-矢量列表
     */
    List<DaAssetGeoDO> getDaAssetGeoList();

    /**
     * 获得全部数据资产-矢量 Map
     *
     * @return 数据资产-矢量 Map
     */
    Map<Long, DaAssetGeoDO> getDaAssetGeoMap();


    /**
     * 导入数据资产-矢量数据
     *
     * @param importExcelList 数据资产-矢量数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetGeo(List<DaAssetGeoRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
