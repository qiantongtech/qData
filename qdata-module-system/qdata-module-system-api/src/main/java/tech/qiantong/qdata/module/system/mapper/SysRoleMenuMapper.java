package tech.qiantong.qdata.module.system.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.module.system.domain.SysRoleMenu;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author qdata
 */
public interface SysRoleMenuMapper
{
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * 根据角色id集合批量查询菜单
     *
     * @param roleIdList 角色ID
     * @return 结果
     */
    public List<SysRoleMenu> getByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    /**
     * 批量新增角色菜单信息带有项目id
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenuProjectId(List<SysRoleMenu> roleMenuList);
}
