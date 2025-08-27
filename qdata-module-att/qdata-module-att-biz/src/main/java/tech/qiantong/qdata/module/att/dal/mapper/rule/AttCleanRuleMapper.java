package tech.qiantong.qdata.module.att.dal.mapper.rule;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 清洗规则Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttCleanRuleMapper extends BaseMapperX<AttCleanRuleDO> {

    default PageResult<AttCleanRuleDO> selectPage(AttCleanRulePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<AttCleanRuleDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttCleanRuleDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_CLEAN_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), AttCleanRuleDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), AttCleanRuleDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getType()), AttCleanRuleDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getCode()), AttCleanRuleDO::getCode, reqVO.getCode())
                .eq(StringUtils.isNotBlank(reqVO.getLevel()), AttCleanRuleDO::getLevel, reqVO.getLevel())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, AttCleanRuleDO.class, lambdaWrapper);
    }

    List<AttCleanRuleDO> selectAttCleanRuleList(@Param("dataElemId") Long dataElemId);

    List<AttCleanRuleDO> getCleaningRuleTreeIds(Long[] dataElemId);
}
