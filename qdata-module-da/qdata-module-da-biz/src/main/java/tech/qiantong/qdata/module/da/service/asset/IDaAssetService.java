package tech.qiantong.qdata.module.da.service.asset;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetService extends IService<DaAssetDO> {

    /**
     * 获得数据资产分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产分页列表
     */
    PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO,String daAssetQueryType);
    List<DaAssetDO> getDaAssetList(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getTablesByDataSourceId(DaAssetPageReqVO pageReqVO);
    DaAssetDO getDaAssetByDaAssetPageReqVO(DaAssetPageReqVO pageReqVO);




    /**
     * 创建数据资产
     *
     * @param createReqVO 数据资产信息
     * @return 数据资产编号
     */
    Long createDaAsset(DaAssetSaveReqVO createReqVO);

    /**
     * 更新数据资产
     *
     * @param updateReqVO 数据资产信息
     */
    int updateDaAsset(DaAssetSaveReqVO updateReqVO);

    /**
     * 删除数据资产
     *
     * @param idList 数据资产编号
     */
    int removeDaAsset(Collection<Long> idList);
    int removeDaAsset(Long id);

    /**
     * 获得数据资产详情
     *
     * @param id 数据资产编号
     * @return 数据资产
     */
    DaAssetRespVO getDaAssetById(Long id);

    DaAssetRespVO getDaAssetByIdSimple(Long id);



    /**
     * 获得全部数据资产列表
     *
     * @return 数据资产列表
     */
    List<DaAssetDO> getDaAssetList();

    /**
     * 获得全部数据资产 Map
     *
     * @return 数据资产 Map
     */
    Map<Long, DaAssetDO> getDaAssetMap();


    /**
     * 导入数据资产数据
     *
     * @param importExcelList 数据资产数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAsset(List<DaAssetRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 数据资产预览带有脱敏规则后的数据预览
     * @param jsonObject 主键id和条件查询的内容
     * @return
     */
    Map<String,Object> getColumnData(JSONObject jsonObject);

    /**
     * 对数据资产的数据进行脱敏
     * @param id 数据资产id
     * @param data 数据资产的数据
     * @return
     */
    List<Map<String, Object>> dataMasking(Long id, List<Map<String, Object>> data);

    void insertAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO, List<DaAssetColumnSaveReqVO> columnSaveReqVOList);

    void updateAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO);

    PageResult<DaAssetDO> getDppAssetPage(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getDppAssetNoPageList(DaAssetPageReqVO daAsset);

    Long createDaAssetNew(DaAssetSaveReqVO daAsset);

    int updateDaAssetNew(DaAssetSaveReqVO daAsset);

    AjaxResult startDaAssetDatasourceTask(Long id);

    List<DaAssetColumnRelRuleVO> listRelRule(Long id, String type);

    List<DaAssetColumnRelRuleVO> listRelRule(Long datasourceId, String tableName, String type);
}
