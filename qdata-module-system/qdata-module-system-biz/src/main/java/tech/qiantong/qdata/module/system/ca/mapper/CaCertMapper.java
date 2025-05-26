package tech.qiantong.qdata.module.system.ca.mapper;

import tech.qiantong.qdata.module.system.ca.domain.CaCert;

import java.util.List;

/**
 * 证书管理Mapper接口
 *
 * @author qdata
 * @date 2024-08-18
 */
public interface CaCertMapper
{
    /**
     * 查询证书管理
     *
     * @param id 证书管理主键
     * @return 证书管理
     */
    public CaCert selectCaCertById(Long id);

    /**
     * 查询证书管理列表
     *
     * @param caCert 证书管理
     * @return 证书管理集合
     */
    public List<CaCert> selectCaCertList(CaCert caCert);

    /**
     * 新增证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    public int insertCaCert(CaCert caCert);

    /**
     * 修改证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    public int updateCaCert(CaCert caCert);

    /**
     * 删除证书管理
     *
     * @param id 证书管理主键
     * @return 结果
     */
    public int deleteCaCertById(Long id);

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCaCertByIds(Long[] ids);
}
