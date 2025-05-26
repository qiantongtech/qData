package tech.qiantong.qdata.module.da.service.assetchild.api;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据资产-外部APIService接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetApiService extends IService<DaAssetApiDO> {

    /**
     * 获得数据资产-外部API分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-外部API分页列表
     */
    PageResult<DaAssetApiDO> getDaAssetApiPage(DaAssetApiPageReqVO pageReqVO);

    DaAssetApiRespVO getDaAssetApiByAssetId(Long assetId);

    /**
     * 创建数据资产-外部API
     *
     * @param createReqVO 数据资产-外部API信息
     * @return 数据资产-外部API编号
     */
    Long createDaAssetApi(DaAssetApiSaveReqVO createReqVO);

    /**
     * 更新数据资产-外部API
     *
     * @param updateReqVO 数据资产-外部API信息
     */
    int updateDaAssetApi(DaAssetApiSaveReqVO updateReqVO);

    /**
     * 删除数据资产-外部API
     *
     * @param idList 数据资产-外部API编号
     */
    int removeDaAssetApi(Collection<Long> idList);

    /**
     * 获得数据资产-外部API详情
     *
     * @param id 数据资产-外部API编号
     * @return 数据资产-外部API
     */
    DaAssetApiDO getDaAssetApiById(Long id);

    /**
     * 获得全部数据资产-外部API列表
     *
     * @return 数据资产-外部API列表
     */
    List<DaAssetApiDO> getDaAssetApiList();

    /**
     * 获得全部数据资产-外部API Map
     *
     * @return 数据资产-外部API Map
     */
    Map<Long, DaAssetApiDO> getDaAssetApiMap();


    /**
     * 导入数据资产-外部API数据
     *
     * @param importExcelList 数据资产-外部API数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetApi(List<DaAssetApiRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void queryServiceForwarding(HttpServletResponse response, DaAssetApiReqVO daAssetApi);
}
