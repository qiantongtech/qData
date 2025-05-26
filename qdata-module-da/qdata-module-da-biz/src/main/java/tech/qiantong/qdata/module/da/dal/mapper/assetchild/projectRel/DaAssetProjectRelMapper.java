package tech.qiantong.qdata.module.da.dal.mapper.assetchild.projectRel;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产与项目关联关系Mapper接口
 *
 * @author qdata
 * @date 2025-04-18
 */
public interface DaAssetProjectRelMapper extends BaseMapperX<DaAssetProjectRelDO> {

    default PageResult<DaAssetProjectRelDO> selectPage(DaAssetProjectRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetProjectRelDO>()
                .eqIfPresent(DaAssetProjectRelDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetProjectRelDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DaAssetProjectRelDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DaAssetProjectRelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetProjectRelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    void removeProjectRelByAssetId(Long id);
}
