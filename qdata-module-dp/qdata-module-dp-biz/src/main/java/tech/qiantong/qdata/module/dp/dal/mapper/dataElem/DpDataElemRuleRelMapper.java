package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据元数据规则关联信息Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemRuleRelMapper extends BaseMapperX<DpDataElemRuleRelDO> {

    default PageResult<DpDataElemRuleRelDO> selectPage(DpDataElemRuleRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpDataElemRuleRelDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDataElemRuleRelDO.class);
        if (StringUtils.isNotBlank(reqVO.getRuleType())) {
            switch (reqVO.getRuleType()) {
                case "1"://1:稽核规则
                    lambdaWrapper.select("t2.DESCRIPTION", "t2.NAME", "t2.QUALITY_DIM", "t2.TYPE", "t2.LEVEL")
                            .leftJoin("ATT_AUDIT_RULE t2 on t.RULE_ID = t2.id AND t2.DEL_FLAG = '0'");
                    break;
                case "2"://清洗规则
                    lambdaWrapper.select("t2.DESCRIPTION", "t2.NAME", "t2.TYPE", "t2.LEVEL")
                            .leftJoin("ATT_CLEAN_RULE t2 on t.RULE_ID = t2.id AND t2.DEL_FLAG = '0'");
                    break;
            }
        }
        lambdaWrapper.eq(StringUtils.isNotBlank(reqVO.getRuleType()), DpDataElemRuleRelDO::getRuleType, reqVO.getRuleType())
                .eq(StringUtils.isNotBlank(reqVO.getDataElemId()), DpDataElemRuleRelDO::getDataElemId, reqVO.getDataElemId())
                .eq(StringUtils.isNotBlank(reqVO.getRuleId()), DpDataElemRuleRelDO::getRuleId, reqVO.getRuleId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DpDataElemRuleRelDO.class, lambdaWrapper);
    }

    List<DpDataElemRuleRelRespDTO> listByDataElemIdList(@Param("dataElemIdList") List<Long> dataElemIdList, @Param("ruleType") String ruleType);
}
