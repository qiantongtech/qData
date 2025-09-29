package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 标准信息分类管理Mapper接口
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface AttDocumentCatMapper extends BaseMapperX<AttDocumentCatDO> {

    default PageResult<AttDocumentCatDO> selectPage(AttDocumentCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttDocumentCatDO>()
                .likeIfPresent(AttDocumentCatDO::getName, reqVO.getName())
                .eqIfPresent(AttDocumentCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttDocumentCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttDocumentCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttDocumentCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDocumentCatDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttDocumentCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Update(value = "update ATT_DOCUMENT_CAT set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
