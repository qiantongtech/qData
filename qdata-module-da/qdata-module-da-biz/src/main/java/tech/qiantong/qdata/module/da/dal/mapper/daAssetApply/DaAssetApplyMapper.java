package tech.qiantong.qdata.module.da.dal.mapper.daAssetApply;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections4.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 数据资产申请Mapper接口
 *
 * @author shu
 * @date 2025-03-19
 */
public interface DaAssetApplyMapper extends BaseMapperX<DaAssetApplyDO> {

    default PageResult<DaAssetApplyDO> selectPage(DaAssetApplyPageReqVO reqVO) {
        MPJLambdaWrapper<DaAssetApplyDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DaAssetApplyDO.class)
                .select("t2.NAME AS assetName, t2.TABLE_NAME AS assetTableName, t5.NAME AS catAssetName, t5.CODE AS carAssetCode, t3.NAME AS projectName")
                .leftJoin("DA_ASSET t2 on t.ASSET_ID = t2.ID")
                .leftJoin("ATT_PROJECT t3 on t.PROJECT_ID = t3.ID AND t3.DEL_FLAG = 0")
                .leftJoin("ATT_ASSET_CAT t5 on t2.CAT_CODE = t5.CODE AND t5.DEL_FLAG = 0")
                .eq("t2.DEL_FLAG", 0)
                .in(CollectionUtils.isNotEmpty(reqVO.getAssetIds()), "t2.ID", reqVO.getAssetIds())
                .like(StringUtils.isNotEmpty(reqVO.getAssetName()), "t2.NAME", reqVO.getAssetName())
                .like(StringUtils.isNotEmpty(reqVO.getCreateBy()), DaAssetApplyDO::getCreateBy, reqVO.getCreateBy())
                .eq(StringUtils.isNotEmpty(reqVO.getStatus()), DaAssetApplyDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotEmpty(reqVO.getCatAssetCode()), "t5.CODE", reqVO.getCatAssetCode())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DaAssetApplyDO.class, lambdaWrapper);
    }
}
