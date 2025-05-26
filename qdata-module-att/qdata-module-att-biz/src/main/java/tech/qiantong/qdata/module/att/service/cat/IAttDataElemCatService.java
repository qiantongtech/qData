package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据元类目管理Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttDataElemCatService extends IService<AttDataElemCatDO> {

    /**
     * 获得数据元类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据元类目管理分页列表
     */
    PageResult<AttDataElemCatDO> getAttDataElemCatPage(AttDataElemCatPageReqVO pageReqVO);

    /**
     * 创建数据元类目管理
     *
     * @param createReqVO 数据元类目管理信息
     * @return 数据元类目管理编号
     */
    Long createAttDataElemCat(AttDataElemCatSaveReqVO createReqVO);

    /**
     * 更新数据元类目管理
     *
     * @param updateReqVO 数据元类目管理信息
     */
    int updateAttDataElemCat(AttDataElemCatSaveReqVO updateReqVO);

    /**
     * 删除数据元类目管理
     *
     * @param idList 数据元类目管理编号
     */
    int removeAttDataElemCat(Collection<Long> idList);

    /**
     * 获得数据元类目管理详情
     *
     * @param id 数据元类目管理编号
     * @return 数据元类目管理
     */
    AttDataElemCatDO getAttDataElemCatById(Long id);

    /**
     * 获得全部数据元类目管理列表
     *
     * @return 数据元类目管理列表
     */
    List<AttDataElemCatDO> getAttDataElemCatList();

    /**
     * 获得全部数据元类目管理列表
     *
     * @return 数据元类目管理列表
     */
    List<AttDataElemCatDO> getAttDataElemCatList(AttDataElemCatPageReqVO reqVO);

    /**
     * 获得全部数据元类目管理 Map
     *
     * @return 数据元类目管理 Map
     */
    Map<Long, AttDataElemCatDO> getAttDataElemCatMap();


    /**
     * 导入数据元类目管理数据
     *
     * @param importExcelList 数据元类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttDataElemCat(List<AttDataElemCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
