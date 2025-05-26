package tech.qiantong.qdata.module.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhj
 * @version 1.0
 * @description: 查询用户参数
 * @date 2024/2/22 17:13
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserQueryDto extends PageDto {
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 搜索词
     */
    private String keywords;
    /**
     * 用户状态
     */
    private Integer status;

    private String name;
    /**
     * 部门id集合
     */
    private List<String> deptIdList;
    /**
     * 角色key集合
     */
    private List<String> roleIdList;
}
