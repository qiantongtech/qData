package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.*;

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

        MPJLambdaWrapper<DpDataElemRuleRelDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DpDataElemRuleRelDO.class);
        lambdaWrapper.eq(StringUtils.isNotBlank(reqVO.getType()), DpDataElemRuleRelDO::getType, reqVO.getType())
                .eq(reqVO.getDataElemId() != null, DpDataElemRuleRelDO::getDataElemId, reqVO.getDataElemId())
                .eq(reqVO.getRuleId() != null, DpDataElemRuleRelDO::getRuleId, reqVO.getRuleId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DpDataElemRuleRelDO.class, lambdaWrapper);
    }

    default List<DpDataElemRuleRelDO> listByDataElemIdList(Collection<Long> dataElemIdList, String type) {
        LambdaQueryWrapper<DpDataElemRuleRelDO> queryWrapper = Wrappers.<DpDataElemRuleRelDO>lambdaQuery().in(DpDataElemRuleRelDO::getDataElemId, dataElemIdList)
                .eq(DpDataElemRuleRelDO::getType, type);
        return selectList(queryWrapper);
    }

}
