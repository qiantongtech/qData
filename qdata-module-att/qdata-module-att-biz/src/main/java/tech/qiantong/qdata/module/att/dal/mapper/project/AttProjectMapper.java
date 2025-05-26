package tech.qiantong.qdata.module.att.dal.mapper.project;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 项目Mapper接口
 *
 * @author shu
 * @date 2025-01-20
 */
public interface AttProjectMapper extends BaseMapperX<AttProjectDO> {

    default PageResult<AttProjectDO> selectPage(AttProjectPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttProjectDO>()
                .likeIfPresent(AttProjectDO::getName, reqVO.getName())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttProjectDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
                .orderByDesc(AttProjectDO::getCreateTime));
    }

    AttProjectDO selectById(Long id);


    Page<AttProjectDO> selectAttProjectListByPage(Page page,@Param("params") AttProjectPageReqVO reqVO);
}
