package tech.qiantong.qdata.module.dp.dal.mapper.model;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 逻辑模型属性信息Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpModelColumnMapper extends BaseMapperX<DpModelColumnDO> {

    default PageResult<DpModelColumnDO> selectPage(DpModelColumnPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpModelColumnDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpModelColumnDO.class)
                .select("t2.NAME AS dataElemName")
                .leftJoin("DP_DATA_ELEM t2 on t.DATA_ELEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(reqVO.getModelId() != null, DpModelColumnDO::getModelId, reqVO.getModelId())
                .like(tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(reqVO.getEngName()), DpModelColumnDO::getEngName, reqVO.getEngName())
                .like(tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(reqVO.getCnName()), DpModelColumnDO::getCnName, reqVO.getCnName())
                .eq(tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(reqVO.getColumnType()), DpModelColumnDO::getColumnType, reqVO.getColumnType())
                .eq(reqVO.getColumnLength() != null, DpModelColumnDO::getColumnLength, reqVO.getColumnLength())
                .eq(reqVO.getColumnScale() != null, DpModelColumnDO::getColumnScale, reqVO.getColumnScale())
                .eq(tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(reqVO.getDefaultValue()), DpModelColumnDO::getDefaultValue, reqVO.getDefaultValue())
                .eq(reqVO.getPkFlag() != null, DpModelColumnDO::getPkFlag, reqVO.getPkFlag())
                .eq(reqVO.getNullableFlag() != null, DpModelColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eq(reqVO.getSortOrder() != null, DpModelColumnDO::getSortOrder, reqVO.getSortOrder())
                .eq(tech.qiantong.qdata.common.utils.StringUtils.isNotBlank(reqVO.getAuthorityDept()), DpModelColumnDO::getAuthorityDept, reqVO.getAuthorityDept())
                .eq(reqVO.getDataElemId() != null, DpModelColumnDO::getDataElemId, reqVO.getDataElemId())
                .eq(reqVO.getCreateTime() != null, DpModelColumnDO::getCreateTime, reqVO.getCreateTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null);
        return selectJoinPage(reqVO, DpModelColumnDO.class, lambdaWrapper);
    }
}
