package tech.qiantong.qdata.module.dp.dal.mapper.model;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import java.util.Arrays;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

/**
 * 逻辑模型Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpModelMapper extends BaseMapperX<DpModelDO> {

    default PageResult<DpModelDO> selectPage(DpModelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpModelDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpModelDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_MODEL_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getModelName()), DpModelDO::getModelName, reqVO.getModelName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DpModelDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getModelComment()), DpModelDO::getModelComment, reqVO.getModelComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DpModelDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDocumentId()!= null, DpModelDO::getDocumentId, reqVO.getDocumentId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, DpModelDO.class, lambdaWrapper);
    }
}
