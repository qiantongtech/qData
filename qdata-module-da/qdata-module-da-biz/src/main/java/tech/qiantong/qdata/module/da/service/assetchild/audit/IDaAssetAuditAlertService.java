package tech.qiantong.qdata.module.da.service.assetchild.audit;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-质量预警Service接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditAlertService extends IService<DaAssetAuditAlertDO> {

    /**
     * 获得数据资产-质量预警分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-质量预警分页列表
     */
    PageResult<DaAssetAuditAlertDO> getDaAssetAuditAlertPage(DaAssetAuditAlertPageReqVO pageReqVO);

    /**
     * 创建数据资产-质量预警
     *
     * @param createReqVO 数据资产-质量预警信息
     * @return 数据资产-质量预警编号
     */
    Long createDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO createReqVO);

    /**
     * 更新数据资产-质量预警
     *
     * @param updateReqVO 数据资产-质量预警信息
     */
    int updateDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO updateReqVO);

    /**
     * 删除数据资产-质量预警
     *
     * @param idList 数据资产-质量预警编号
     */
    int removeDaAssetAuditAlert(Collection<Long> idList);

    /**
     * 获得数据资产-质量预警详情
     *
     * @param id 数据资产-质量预警编号
     * @return 数据资产-质量预警
     */
    DaAssetAuditAlertDO getDaAssetAuditAlertById(Long id);

    /**
     * 获得全部数据资产-质量预警列表
     *
     * @return 数据资产-质量预警列表
     */
    List<DaAssetAuditAlertDO> getDaAssetAuditAlertList();

    /**
     * 获得全部数据资产-质量预警 Map
     *
     * @return 数据资产-质量预警 Map
     */
    Map<Long, DaAssetAuditAlertDO> getDaAssetAuditAlertMap();


    /**
     * 导入数据资产-质量预警数据
     *
     * @param importExcelList 数据资产-质量预警数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetAuditAlert(List<DaAssetAuditAlertRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
