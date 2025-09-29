package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import lombok.Data;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-09-01 17:43
 **/
@Data
public class AttSysUserReqVO extends SysUser {

    private Long projectId;
}
