package tech.qiantong.qdata.module.att.dal.mapper.project;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 项目与用户关联关系Mapper接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface AttProjectUserRelMapper extends BaseMapperX<AttProjectUserRelDO> {

    default PageResult<AttProjectUserRelDO> selectPage(AttProjectUserRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        MPJLambdaWrapper<AttProjectUserRelDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(AttProjectUserRelDO.class)
                .select("u.nick_name AS nickName, u.user_name AS userName , u.phonenumber AS phoneNumber, u.status AS status ,d.dept_name AS deptName")
                .leftJoin("SYSTEM_USER u on t.user_id = u.user_id")
                .leftJoin("SYSTEM_DEPT d on u.dept_id = d.dept_id")
                .eq("u.del_flag","0")
                .eq("d.del_flag","0")
                .eq("t.del_flag","0")
                .eq(reqVO.getProjectId() != null, AttProjectUserRelDO::getProjectId, reqVO.getProjectId())
                .eq(reqVO.getUserId() != null, AttProjectUserRelDO::getUserId, reqVO.getUserId())
                .like(StringUtils.isNotBlank(reqVO.getUserName()), "u.user_name", reqVO.getUserName())
                .like(StringUtils.isNotBlank(reqVO.getNickName()), "u.nick_name", reqVO.getNickName())
                .like(StringUtils.isNotBlank(reqVO.getPhoneNumber()), "u.phonenumber", reqVO.getPhoneNumber())
                .between(reqVO.getStartTime() != null && reqVO.getEndTime() != null,
                        AttProjectUserRelDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null);

        return selectJoinPage(reqVO, AttProjectUserRelDO.class, lambdaWrapper);
    }
}
