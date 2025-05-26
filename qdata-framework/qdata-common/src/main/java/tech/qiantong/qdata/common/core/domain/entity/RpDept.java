package tech.qiantong.qdata.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务资源门户部门 DO 对象 RP_DEPT
 *
 * @author qdata
 * @date 2025-04-18
 */
@Data
public class RpDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long deptId;

    /** 父部门id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    private String deptName;
    private String deptCode;
    private String deptType;

    /** 显示顺序 */
    private String orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 部门状态（0正常 1停用） */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    private List<RpDept> children = new ArrayList<RpDept>();

}
