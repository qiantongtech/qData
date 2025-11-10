/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.service.sensitiveLevel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 敏感等级Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDaSensitiveLevelService extends IService<DaSensitiveLevelDO> {

    /**
     * 获得敏感等级分页列表
     *
     * @param pageReqVO 分页请求
     * @return 敏感等级分页列表
     */
    PageResult<DaSensitiveLevelDO> getDaSensitiveLevelPage(DaSensitiveLevelPageReqVO pageReqVO);

    /**
     * 创建敏感等级
     *
     * @param createReqVO 敏感等级信息
     * @return 敏感等级编号
     */
    Long createDaSensitiveLevel(DaSensitiveLevelSaveReqVO createReqVO);

    /**
     * 更新敏感等级
     *
     * @param updateReqVO 敏感等级信息
     */
    int updateDaSensitiveLevel(DaSensitiveLevelSaveReqVO updateReqVO);

    /**
     * 删除敏感等级
     *
     * @param idList 敏感等级编号
     */
    int removeDaSensitiveLevel(Collection<Long> idList);

    /**
     * 获得敏感等级详情
     *
     * @param id 敏感等级编号
     * @return 敏感等级
     */
    DaSensitiveLevelDO getDaSensitiveLevelById(Long id);

    /**
     * 获得全部敏感等级列表
     *
     * @return 敏感等级列表
     */
    List<DaSensitiveLevelDO> getDaSensitiveLevelList();

    /**
     * 获得全部敏感等级 Map
     *
     * @return 敏感等级 Map
     */
    Map<Long, DaSensitiveLevelDO> getDaSensitiveLevelMap();


    /**
     * 导入敏感等级数据
     *
     * @param importExcelList 敏感等级数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaSensitiveLevel(List<DaSensitiveLevelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 修改状态
     * @param id 主键
     * @param status 状态值
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
