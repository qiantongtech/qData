package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据资产类目管理Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttAssetCatService extends IService<AttAssetCatDO> {

    /**
     * 获得数据资产类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产类目管理分页列表
     */
    PageResult<AttAssetCatDO> getAttAssetCatPage(AttAssetCatPageReqVO pageReqVO);

    /**
     * 创建数据资产类目管理
     *
     * @param createReqVO 数据资产类目管理信息
     * @return 数据资产类目管理编号
     */
    Long createAttAssetCat(AttAssetCatSaveReqVO createReqVO);

    /**
     * 更新数据资产类目管理
     *
     * @param updateReqVO 数据资产类目管理信息
     */
    int updateAttAssetCat(AttAssetCatSaveReqVO updateReqVO);

    /**
     * 删除数据资产类目管理
     *
     * @param idList 数据资产类目管理编号
     */
    int removeAttAssetCat(Collection<Long> idList);


    /**
     * 获得数据资产类目管理详情
     *
     * @param id 数据资产类目管理编号
     * @return 数据资产类目管理
     */
    AttAssetCatDO getAttAssetCatById(Long id);

    /**
     * 获得全部数据资产类目管理列表
     *
     * @return 数据资产类目管理列表
     */
    List<AttAssetCatDO> getAttAssetCatList();

    /**
     * 获得全部数据资产类目管理列表
     *
     * @return 数据资产类目管理列表
     */
    List<AttAssetCatDO> getAttAssetCatList(AttAssetCatPageReqVO reqVO);

    /**
     * 获得全部数据资产类目管理 Map
     *
     * @return 数据资产类目管理 Map
     */
    Map<Long, AttAssetCatDO> getAttAssetCatMap();


    /**
     * 导入数据资产类目管理数据
     *
     * @param importExcelList 数据资产类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importAttAssetCat(List<AttAssetCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
