package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据开发类目管理Service接口
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttDataDevCatService extends IService<AttDataDevCatDO> {

    /**
     * 获得数据开发类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据开发类目管理分页列表
     */
    PageResult<AttDataDevCatDO> getAttDataDevCatPage(AttDataDevCatPageReqVO pageReqVO);

    /**
     * 创建数据开发类目管理
     *
     * @param createReqVO 数据开发类目管理信息
     * @return 数据开发类目管理编号
     */
    Long createAttDataDevCat(AttDataDevCatSaveReqVO createReqVO);

    /**
     * 更新数据开发类目管理
     *
     * @param updateReqVO 数据开发类目管理信息
     */
    int updateAttDataDevCat(AttDataDevCatSaveReqVO updateReqVO);

    /**
     * 删除数据开发类目管理
     *
     * @param idList 数据开发类目管理编号
     */
    int removeAttDataDevCat(Collection<Long> idList);

    /**
     * 获得数据开发类目管理详情
     *
     * @param id 数据开发类目管理编号
     * @return 数据开发类目管理
     */
    AttDataDevCatDO getAttDataDevCatById(Long id);

    /**
     * 获得全部数据开发类目管理列表
     *
     * @return 数据开发类目管理列表
     */
    List<AttDataDevCatDO> getAttDataDevCatList();

    /**
     * 获得全部数据开发类目管理列表
     *
     * @return 数据开发类目管理列表
     */
    List<AttDataDevCatDO> getAttDataDevCatList(AttDataDevCatPageReqVO reqVO);

    /**
     * 获得全部数据开发类目管理 Map
     *
     * @return 数据开发类目管理 Map
     */
    Map<Long, AttDataDevCatDO> getAttDataDevCatMap();


    /**
     * 导入数据开发类目管理数据
     *
     * @param importExcelList 数据开发类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttDataDevCat(List<AttDataDevCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
