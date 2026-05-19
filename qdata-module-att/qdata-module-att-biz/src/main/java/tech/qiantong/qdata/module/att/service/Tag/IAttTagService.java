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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.service.Tag;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 标签管理Service接口
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagService extends IService<AttTagDO> {

    /**
     * 获得标签管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标签管理分页列表
     */
    PageResult<AttTagDO> getAttTagPage(AttTagPageReqVO pageReqVO);

    /**
     * 创建标签管理
     *
     * @param createReqVO 标签管理信息
     * @return 标签管理编号
     */
    Long createAttTag(AttTagSaveReqVO createReqVO);

    /**
     * 更新标签管理
     *
     * @param updateReqVO 标签管理信息
     */
    int updateAttTag(AttTagSaveReqVO updateReqVO);

    /**
     * 删除标签管理
     *
     * @param idList 标签管理编号
     */
    int removeAttTag(Collection<Long> idList);

    /**
     * 获得标签管理详情
     *
     * @param id 标签管理编号
     * @return 标签管理
     */
    AttTagRespVO getAttTagById(Long id);

    /**
     * 获得全部标签管理列表
     *
     * @return 标签管理列表
     */
    List<AttTagDO> getAttTagList();

    /**
     * 获得全部标签管理 Map
     *
     * @return 标签管理 Map
     */
    Map<Long, AttTagDO> getAttTagMap();


    /**
     * 导入标签管理数据
     *
     * @param importExcelList 标签管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTag(List<AttTagRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Long getCountByCatCode(String code);


    /**
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
     */
    int updateCatCode(String oldCatCode, String newCatCode);
}
