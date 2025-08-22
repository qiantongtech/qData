package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据质量类目Service接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IAttQualityCatService extends IService<AttQualityCatDO> {

    /**
     * 获得数据质量类目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量类目分页列表
     */
    PageResult<AttQualityCatDO> getAttQualityCatPage(AttQualityCatPageReqVO pageReqVO);

    /**
     * 创建数据质量类目
     *
     * @param createReqVO 数据质量类目信息
     * @return 数据质量类目编号
     */
    Long createAttQualityCat(AttQualityCatSaveReqVO createReqVO);

    /**
     * 更新数据质量类目
     *
     * @param updateReqVO 数据质量类目信息
     */
    int updateAttQualityCat(AttQualityCatSaveReqVO updateReqVO);

    /**
     * 删除数据质量类目
     *
     * @param idList 数据质量类目编号
     */
    int removeAttQualityCat(Collection<Long> idList);

    /**
     * 获得数据质量类目详情
     *
     * @param id 数据质量类目编号
     * @return 数据质量类目
     */
    AttQualityCatDO getAttQualityCatById(Long id);

    /**
     * 获得全部数据质量类目列表
     *
     * @return 数据质量类目列表
     */
    List<AttQualityCatDO> getAttQualityCatList(AttQualityCatPageReqVO attQualityCat);

    /**
     * 获得全部数据质量类目 Map
     *
     * @return 数据质量类目 Map
     */
    Map<Long, AttQualityCatDO> getAttQualityCatMap();


    /**
     * 导入数据质量类目数据
     *
     * @param importExcelList 数据质量类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttQualityCat(List<AttQualityCatRespVO> importExcelList, boolean isUpdateSupport, String operName);    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

}
