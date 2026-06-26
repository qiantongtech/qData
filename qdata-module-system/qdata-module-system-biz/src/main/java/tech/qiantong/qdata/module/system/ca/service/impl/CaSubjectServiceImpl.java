/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
