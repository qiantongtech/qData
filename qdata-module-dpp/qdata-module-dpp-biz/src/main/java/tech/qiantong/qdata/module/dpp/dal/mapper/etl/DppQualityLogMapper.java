package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 数据质量日志Mapper接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface DppQualityLogMapper extends BaseMapperX<DppQualityLogDO> {

//    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
//        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
//        // 构造动态查询条件
//        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityLogDO>()
//                .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                .eqIfPresent(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
//                .eqIfPresent(DppQualityLogDO::getStartTime, reqVO.getStartTime())
//                .eqIfPresent(DppQualityLogDO::getEndTime, reqVO.getEndTime())
//                .eqIfPresent(DppQualityLogDO::getQualityId, reqVO.getQualityId())
//                .eqIfPresent(DppQualityLogDO::getScore, reqVO.getScore())
//                .eqIfPresent(DppQualityLogDO::getProblemData, reqVO.getProblemData())
//                .eqIfPresent(DppQualityLogDO::getCreateTime, reqVO.getCreateTime())
//                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
//                // .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
//                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
//    }

    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapperX<DppQualityLogDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAll(DppQualityLogDO.class)
                .innerJoin("DPP_QUALITY_TASK t2 ON t.QUALITY_ID = t2.ID AND t2.DEL_FLAG = '0' AND t2.ASSET_FLAG = '0'")
                .likeIfExists(DppQualityLogDO::getName, reqVO.getName())
                .eqIfExists(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
                .eqIfExists(DppQualityLogDO::getStartTime, reqVO.getStartTime())
                .eqIfExists(DppQualityLogDO::getEndTime, reqVO.getEndTime())
                .eqIfExists(DppQualityLogDO::getQualityId, reqVO.getQualityId())
                .eqIfExists(DppQualityLogDO::getScore, reqVO.getScore())
                .eqIfExists(DppQualityLogDO::getProblemData, reqVO.getProblemData())
                .eqIfExists(DppQualityLogDO::getCreateTime, reqVO.getCreateTime());
        // 动态排序处理
        String orderByColumn = reqVO.getOrderByColumn();
        Boolean isAsc = StringUtils.equals("asc", reqVO.getIsAsc());
        if (StringUtils.isNotBlank(orderByColumn) && allowedColumns.contains(orderByColumn)) {
            wrapper.orderBy(true, Boolean.TRUE.equals(isAsc), orderByColumn);
        }
        return selectPage(reqVO, wrapper);
    }

    default DppQualityLogDO selectPrevLogByIdWithWrapper(String id) {
        // 1) 先拿当前记录的关键字段
        DppQualityLogDO cur = this.selectById(id);
        if (cur == null || cur.getQualityId() == null || cur.getStartTime() == null) {
            return null;
        }

        // 2) 构造 wrapper：同一 QUALITY_ID，下一个更“早”的一条
        MPJLambdaWrapperX<DppQualityLogDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAll(DppQualityLogDO.class)
                .eq(DppQualityLogDO::getQualityId, cur.getQualityId())
                .eq(DppQualityLogDO::getDelFlag, "0")
                .eq(DppQualityLogDO::getValidFlag, "1")
                // (start_time < 当前) OR (start_time = 当前 AND id <> 当前)
                .and(w -> w.lt(DppQualityLogDO::getStartTime, cur.getStartTime())
                        .or(x -> x.eq(DppQualityLogDO::getStartTime, cur.getStartTime())
                                .ne(DppQualityLogDO::getId, id)))
                // 时间倒序，保证“最近的一条早于当前”
                .orderByDesc(DppQualityLogDO::getStartTime,
                        DppQualityLogDO::getEndTime,
                        DppQualityLogDO::getUpdateTime);

        // 3) 用分页只取一条（与你现有 selectPage(reqVO, wrapper) 兼容）
        DppQualityLogPageReqVO req = new DppQualityLogPageReqVO();
        req.setPageNum(1);
        req.setPageSize(1);

        PageResult<DppQualityLogDO> page = selectPage(req, wrapper);
        return (page == null || page.getRows() == null || page.getRows().isEmpty())
                ? null
                : (DppQualityLogDO)page.getRows().get(0);
    }
}
