package tech.qiantong.qdata.module.da.service.assetchild.audit;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产质量结果记录Service接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditRuleService extends IService<DaAssetAuditRuleDO> {

    /**
     * 获得数据资产质量结果记录分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产质量结果记录分页列表
     */
    PageResult<DaAssetAuditRuleDO> getDaAssetAuditRulePage(DaAssetAuditRulePageReqVO pageReqVO);

    /**
     * 创建数据资产质量结果记录
     *
     * @param createReqVO 数据资产质量结果记录信息
     * @return 数据资产质量结果记录编号
     */
    Long createDaAssetAuditRule(DaAssetAuditRuleSaveReqVO createReqVO);

    /**
     * 更新数据资产质量结果记录
     *
     * @param updateReqVO 数据资产质量结果记录信息
     */
    int updateDaAssetAuditRule(DaAssetAuditRuleSaveReqVO updateReqVO);

    /**
     * 删除数据资产质量结果记录
     *
     * @param idList 数据资产质量结果记录编号
     */
    int removeDaAssetAuditRule(Collection<Long> idList);

    /**
     * 获得数据资产质量结果记录详情
     *
     * @param id 数据资产质量结果记录编号
     * @return 数据资产质量结果记录
     */
    DaAssetAuditRuleDO getDaAssetAuditRuleById(Long id);

    /**
     * 获得全部数据资产质量结果记录列表
     *
     * @return 数据资产质量结果记录列表
     */
    List<DaAssetAuditRuleDO> getDaAssetAuditRuleList();

    /**
     * 获得全部数据资产质量结果记录 Map
     *
     * @return 数据资产质量结果记录 Map
     */
    Map<Long, DaAssetAuditRuleDO> getDaAssetAuditRuleMap();


    /**
     * 导入数据资产质量结果记录数据
     *
     * @param importExcelList 数据资产质量结果记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetAuditRule(List<DaAssetAuditRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
