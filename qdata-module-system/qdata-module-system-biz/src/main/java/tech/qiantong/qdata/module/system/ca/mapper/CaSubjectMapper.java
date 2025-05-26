package tech.qiantong.qdata.module.system.ca.mapper;

import tech.qiantong.qdata.module.system.ca.domain.CaSubject;

import java.util.List;

/**
 * 主体管理Mapper接口
 *
 * @author qdata
 * @date 2024-08-18
 */
public interface CaSubjectMapper
{
    /**
     * 查询主体管理
     *
     * @param id 主体管理主键
     * @return 主体管理
     */
    public CaSubject selectCaSubjectById(Long id);

    /**
     * 查询主体管理列表
     *
     * @param caSubject 主体管理
     * @return 主体管理集合
     */
    public List<CaSubject> selectCaSubjectList(CaSubject caSubject);

    /**
     * 新增主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    public int insertCaSubject(CaSubject caSubject);

    /**
     * 修改主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    public int updateCaSubject(CaSubject caSubject);

    /**
     * 删除主体管理
     *
     * @param id 主体管理主键
     * @return 结果
     */
    public int deleteCaSubjectById(Long id);

    /**
     * 批量删除主体管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCaSubjectByIds(Long[] ids);
}
