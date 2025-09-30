package tech.qiantong.qdata.module.att.service.rule;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 清洗规则Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttCleanRuleService extends IService<AttCleanRuleDO> {

    /**
     * 获得清洗规则分页列表
     *
     * @param pageReqVO 分页请求
     * @return 清洗规则分页列表
     */
    PageResult<AttCleanRuleDO> getAttCleanRulePage(AttCleanRulePageReqVO pageReqVO);

    /**
     * 创建清洗规则
     *
     * @param createReqVO 清洗规则信息
     * @return 清洗规则编号
     */
    Long createAttCleanRule(AttCleanRuleSaveReqVO createReqVO);

    /**
     * 更新清洗规则
     *
     * @param updateReqVO 清洗规则信息
     */
    int updateAttCleanRule(AttCleanRuleSaveReqVO updateReqVO);

    /**
     * 删除清洗规则
     *
     * @param idList 清洗规则编号
     */
    int removeAttCleanRule(Collection<Long> idList);

    /**
     * 获得清洗规则详情
     *
     * @param id 清洗规则编号
     * @return 清洗规则
     */
    AttCleanRuleDO getAttCleanRuleById(Long id);

    /**
     * 获得全部清洗规则列表
     *
     * @return 清洗规则列表
     */
    List<AttCleanRuleDO> getAttCleanRuleList();
    List<AttCleanRuleRespVO> getAttCleanRuleList(AttCleanRulePageReqVO attCleanRule);

    /**
     * 获得全部清洗规则 Map
     *
     * @return 清洗规则 Map
     */
    Map<Long, AttCleanRuleDO> getAttCleanRuleMap();

    /**
     * 导入清洗规则数据
     *
     * @param importExcelList 清洗规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importAttCleanRule(List<AttCleanRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取清洗规则树形结构
     *
     * @return 树形结构列表
     */
    List<AttCleanRuleRespVO> getAttCleanRuleTree(Long dataElemId);

    List<AttCleanRuleRespVO> getCleaningRuleTree(Long[] dataElemId);

    /**
     * @param catCode {@link AttCleanCatDO#code}
     */
    Long getCount(String catCode);

}
