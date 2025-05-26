package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 数据元数据资产关联信息Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemAssetRelMapper extends BaseMapperX<DpDataElemAssetRelDO> {

    default PageResult<DpDataElemAssetRelDO> selectPage(DpDataElemAssetRelPageReqVO reqVO) {
        MPJLambdaWrapper<DpDataElemAssetRelDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DpDataElemAssetRelDO.class)
                .select("t2.name AS assetName","t2.table_comment AS tableComment","t2.DESCRIPTION AS description")
                .leftJoin("DA_ASSET t2 on t.asset_id = t2.id AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DpDataElemAssetRelDO::getTableName,
                        reqVO.getTableName())
                .like(StringUtils.isNotBlank(reqVO.getColumnName()), DpDataElemAssetRelDO::getColumnName,
                        reqVO.getColumnName())
                .eq(reqVO.getDataElemType() != null, DpDataElemAssetRelDO::getDataElemType, reqVO.getDataElemType())
                .eq(reqVO.getDataElemId() != null, DpDataElemAssetRelDO::getDataElemId, reqVO.getDataElemId())
                .eq(reqVO.getAssetId() != null, DpDataElemAssetRelDO::getAssetId, reqVO.getAssetId())
                .eq(reqVO.getColumnId() != null, DpDataElemAssetRelDO::getColumnId, reqVO.getColumnId())
                .eq(reqVO.getCreateTime() != null, DpDataElemAssetRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null);

        return selectJoinPage(reqVO, DpDataElemAssetRelDO.class, lambdaWrapper);
    }
}
