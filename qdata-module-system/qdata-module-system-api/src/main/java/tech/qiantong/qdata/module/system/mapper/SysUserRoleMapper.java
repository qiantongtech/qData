package tech.qiantong.qdata.module.system.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.module.system.domain.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author qdata
 */
public interface SysUserRoleMapper
{
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param sysUserRoleList 用户ID和角色id
     * @return 结果
     */
    public int deleteUserRoleList(@Param("sysUserRoleList") List<SysUserRole> sysUserRoleList);

    /**
     * 根据用户id集合查询信息
     *
     * @param userIdList 用户ID
     * @return 结果
     */
    public List<SysUserRole> getByUserIdList(@Param("userIdList") List<Long> userIdList);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public List<SysUserRole> getUserRoleByRoleId(Long roleId);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
