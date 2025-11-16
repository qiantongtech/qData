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

package tech.qiantong.qdata.module.system.ca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.module.system.ca.domain.CaSubject;
import tech.qiantong.qdata.module.system.ca.mapper.CaSubjectMapper;
import tech.qiantong.qdata.module.system.ca.service.ICaSubjectService;

import java.util.List;

/**
 * 主体管理Service业务层处理
 *
 * @author qdata
 * @date 2024-08-18
 */
@Service
public class CaSubjectServiceImpl implements ICaSubjectService
{
    @Autowired
    private CaSubjectMapper caSubjectMapper;

    /**
     * 查询主体管理
     *
     * @param id 主体管理主键
     * @return 主体管理
     */
    @Override
    public CaSubject selectCaSubjectById(Long id)
    {
        return caSubjectMapper.selectCaSubjectById(id);
    }

    /**
     * 查询主体管理列表
     *
     * @param caSubject 主体管理
     * @return 主体管理
     */
    @Override
    public List<CaSubject> selectCaSubjectList(CaSubject caSubject)
    {
        return caSubjectMapper.selectCaSubjectList(caSubject);
    }

    /**
     * 新增主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    @Override
    public int insertCaSubject(CaSubject caSubject)
    {
        caSubject.setCreateTime(DateUtils.getNowDate());
        return caSubjectMapper.insertCaSubject(caSubject);
    }

    /**
     * 修改主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    @Override
    public int updateCaSubject(CaSubject caSubject)
    {
        caSubject.setUpdateTime(DateUtils.getNowDate());
        return caSubjectMapper.updateCaSubject(caSubject);
    }

    /**
     * 批量删除主体管理
     *
     * @param ids 需要删除的主体管理主键
     * @return 结果
     */
    @Override
    public int deleteCaSubjectByIds(Long[] ids)
    {
        return caSubjectMapper.deleteCaSubjectByIds(ids);
    }

    /**
     * 删除主体管理信息
     *
     * @param id 主体管理主键
     * @return 结果
     */
    @Override
    public int deleteCaSubjectById(Long id)
    {
        return caSubjectMapper.deleteCaSubjectById(id);
    }
}
