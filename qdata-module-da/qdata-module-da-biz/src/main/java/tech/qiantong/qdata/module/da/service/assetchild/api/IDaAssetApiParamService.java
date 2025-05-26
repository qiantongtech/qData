package tech.qiantong.qdata.module.da.service.assetchild.api;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-外部API-参数Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetApiParamService extends IService<DaAssetApiParamDO> {

    /**
     * 获得数据资产-外部API-参数分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-外部API-参数分页列表
     */
    PageResult<DaAssetApiParamDO> getDaAssetApiParamPage(DaAssetApiParamPageReqVO pageReqVO);

    /**
     * 创建数据资产-外部API-参数
     *
     * @param createReqVO 数据资产-外部API-参数信息
     * @return 数据资产-外部API-参数编号
     */
    Long createDaAssetApiParam(DaAssetApiParamSaveReqVO createReqVO);

    void createDaAssetApiParamDeep(List<DaAssetApiParamSaveReqVO> daAssetApiParamList, Long daAssetApiId);

    /**
     * 更新数据资产-外部API-参数
     *
     * @param updateReqVO 数据资产-外部API-参数信息
     */
    int updateDaAssetApiParam(DaAssetApiParamSaveReqVO updateReqVO);

    /**
     * 删除数据资产-外部API-参数
     *
     * @param idList 数据资产-外部API-参数编号
     */
    int removeDaAssetApiParam(Collection<Long> idList);
    int removeThemeRelByAssetApiId( Long assetApiId);

    /**
     * 获得数据资产-外部API-参数详情
     *
     * @param id 数据资产-外部API-参数编号
     * @return 数据资产-外部API-参数
     */
    DaAssetApiParamDO getDaAssetApiParamById(Long id);

    /**
     * 获得全部数据资产-外部API-参数列表
     *
     * @return 数据资产-外部API-参数列表
     */
    List<DaAssetApiParamDO> getDaAssetApiParamList();
    List<DaAssetApiParamRespVO> getDaAssetApiParamList(Long daAssetApiId);

    /**
     * 获得全部数据资产-外部API-参数 Map
     *
     * @return 数据资产-外部API-参数 Map
     */
    Map<Long, DaAssetApiParamDO> getDaAssetApiParamMap();


    /**
     * 导入数据资产-外部API-参数数据
     *
     * @param importExcelList 数据资产-外部API-参数数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetApiParam(List<DaAssetApiParamRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
