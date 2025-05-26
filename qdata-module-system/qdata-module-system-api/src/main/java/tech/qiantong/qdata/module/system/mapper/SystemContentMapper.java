package tech.qiantong.qdata.module.system.mapper;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统配置Mapper接口
 *
 * @author qdata
 * @date 2024-12-31
 */
public interface SystemContentMapper extends BaseMapperX<SystemContentDO> {

    default PageResult<SystemContentDO> selectPage(SystemContentPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemContentDO>()
                .likeIfPresent(SystemContentDO::getSysName, reqVO.getSysName())
                .eqIfPresent(SystemContentDO::getLogo, reqVO.getLogo())
                .eqIfPresent(SystemContentDO::getCarouselImage, reqVO.getCarouselImage())
                .eqIfPresent(SystemContentDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(SystemContentDO::getEmail, reqVO.getEmail())
                .eqIfPresent(SystemContentDO::getCopyright, reqVO.getCopyright())
                .eqIfPresent(SystemContentDO::getRecordNumber, reqVO.getRecordNumber())
                .eqIfPresent(SystemContentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(SystemContentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SystemContentDO::getRemark, reqVO.getRemark())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(SystemContentDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
