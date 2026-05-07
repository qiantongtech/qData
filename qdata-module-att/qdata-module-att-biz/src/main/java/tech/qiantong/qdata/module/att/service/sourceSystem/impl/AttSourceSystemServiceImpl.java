/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qData Data Middle Platform (Commercial Edition)
 * Software Copyright Registration No. 16069171
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qData 数据中台（商业版） | 软著登字第16069171号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

package tech.qiantong.qdata.module.att.service.sourceSystem.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.dal.mapper.sourceSystem.AttSourceSystemMapper;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来源系统Service业务层处理
 *
 * @author qdata
 * @date 2026-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttSourceSystemServiceImpl  extends ServiceImpl<AttSourceSystemMapper,AttSourceSystemDO> implements IAttSourceSystemService {
    @Resource
    private AttSourceSystemMapper attSourceSystemMapper;

    @Override
    public PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO) {
        return attSourceSystemMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO) {
        AttSourceSystemDO dictType = BeanUtils.toBean(createReqVO, AttSourceSystemDO.class);
        attSourceSystemMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO) {
        // 相关校验

        // 更新来源系统
        AttSourceSystemDO updateObj = BeanUtils.toBean(updateReqVO, AttSourceSystemDO.class);
        return attSourceSystemMapper.updateById(updateObj);
    }
    @Override
    public int removeAttSourceSystem(Collection<Long> idList) {
        //判断validFlag是否为true
        if (idList.stream().anyMatch(id -> attSourceSystemMapper.selectById(id).getValidFlag() == true)) {
            throw new IllegalArgumentException("已启用的来源系统，不能删除！");
        }
        // 批量删除来源系统
        return attSourceSystemMapper.deleteBatchIds(idList);
    }

    @Override
    public AttSourceSystemDO getAttSourceSystemById(Long id) {
        return attSourceSystemMapper.selectById(id);
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemList() {
        return attSourceSystemMapper.selectList();
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag) {
        return attSourceSystemMapper.selectList(new LambdaQueryWrapperX<AttSourceSystemDO>().eqIfPresent(AttSourceSystemDO::getValidFlag, validFlag));
    }

    @Override
    public Map<Long, AttSourceSystemDO> getAttSourceSystemMap() {
        List<AttSourceSystemDO> attSourceSystemList = attSourceSystemMapper.selectList();
        return attSourceSystemList.stream()
                .collect(Collectors.toMap(
                        AttSourceSystemDO::getId,
                        attSourceSystemDO -> attSourceSystemDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入来源系统数据
         *
         * @param importExcelList 来源系统数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttSourceSystemRespVO respVO : importExcelList) {
                try {
                    AttSourceSystemDO attSourceSystemDO = BeanUtils.toBean(respVO, AttSourceSystemDO.class);
                    Long attSourceSystemId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attSourceSystemId != null) {
                            AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectById(attSourceSystemId);
                            if (existingAttSourceSystem != null) {
                                attSourceSystemMapper.updateById(attSourceSystemDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + attSourceSystemId + " 的来源系统记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + attSourceSystemId + " 的来源系统记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<AttSourceSystemDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attSourceSystemId);
                        AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectOne(queryWrapper);
                        if (existingAttSourceSystem == null) {
                            attSourceSystemMapper.insert(attSourceSystemDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + attSourceSystemId + " 的来源系统记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + attSourceSystemId + " 的来源系统记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }
}
