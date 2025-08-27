package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;

import java.util.List;
import java.util.Map;

/**
 * 清洗规则类目Service接口
 *
 * @author qdata
 * @date 2025-08-11
 */
public interface IAttCleanCatService extends IService<AttCleanCatDO> {

    /**
     * 获得清洗规则类目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 清洗规则类目分页列表
     */
    PageResult<AttCleanCatDO> getAttCleanCatPage(AttCleanCatPageReqVO pageReqVO);

    /**
     * 创建清洗规则类目
     *
     * @param createReqVO 清洗规则类目信息
     * @return 清洗规则类目编号
     */
    Long createAttCleanCat(AttCleanCatSaveReqVO createReqVO);

    /**
     * 更新清洗规则类目
     *
     * @param updateReqVO 清洗规则类目信息
     */
    int updateAttCleanCat(AttCleanCatSaveReqVO updateReqVO);

    /**
     * 删除清洗规则类目
     *
     * @param idList 清洗规则类目编号
     */
    int removeAttCleanCat(Long idList);

    /**
     * 获得清洗规则类目详情
     *
     * @param id 清洗规则类目编号
     * @return 清洗规则类目
     */
    AttCleanCatDO getAttCleanCatById(Long id);

    /**
     * 获得全部清洗规则类目列表
     *
     * @return 清洗规则类目列表
     */
    List<AttCleanCatDO> getAttCleanCatList(AttCleanCatPageReqVO attCleanCat);
    List<AttCleanCatDO> getAttCleanCatList();

    /**
     * 获得全部清洗规则类目 Map
     *
     * @return 清洗规则类目 Map
     */
    Map<Long, AttCleanCatDO> getAttCleanCatMap();


    /**
     * 导入清洗规则类目数据
     *
     * @param importExcelList 清洗规则类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttCleanCat(List<AttCleanCatRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

}
