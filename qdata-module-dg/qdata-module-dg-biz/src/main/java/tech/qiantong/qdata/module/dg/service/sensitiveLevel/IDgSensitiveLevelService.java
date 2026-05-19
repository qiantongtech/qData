package tech.qiantong.qdata.module.dg.service.sensitiveLevel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;

import java.util.Collection;

/**
 * 敏感等级Service接口
 *
 * @author Chaos
 * @date 2025-01-21
 */
public interface IDgSensitiveLevelService extends IService<DgSensitiveLevelDO> {

    /**
     * 获得敏感等级分页列表
     *
     * @param pageReqVO 分页请求
     * @return 敏感等级分页列表
     */
    PageResult<DgSensitiveLevelDO> getDgSensitiveLevelPage(DgSensitiveLevelPageReqVO pageReqVO);

    /**
     * 创建敏感等级
     *
     * @param createReqVO 敏感等级信息
     * @return 敏感等级编号
     */
    Long createDgSensitiveLevel(DgSensitiveLevelSaveReqVO createReqVO);

    /**
     * 更新敏感等级
     *
     * @param updateReqVO 敏感等级信息
     */
    int updateDgSensitiveLevel(DgSensitiveLevelSaveReqVO updateReqVO);

    /**
     * 删除敏感等级
     *
     * @param idList 敏感等级编号
     */
    int removeDgSensitiveLevel(Collection<Long> idList);

    /**
     * 获得敏感等级详情
     *
     * @param id 敏感等级编号
     * @return 敏感等级
     */
    DgSensitiveLevelDO getDgSensitiveLevelById(Long id);

    /**
     * 修改状态
     *
     * @param id     主键
     * @param status 状态值
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
