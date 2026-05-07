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

package tech.qiantong.qdata.module.att.dal.mapper.sourceSystem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 来源系统Mapper接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface AttSourceSystemMapper extends BaseMapperX<AttSourceSystemDO> {


    default PageResult<AttSourceSystemDO> selectPage(AttSourceSystemPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .select("t2.NICK_NAME AS responsiblePersonName,t3.NICK_NAME AS contactPersonName")
                .leftJoin("SYSTEM_USER t2 on t.RESPONSIBLE_PERSON = t2.USER_ID AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.CONTACT_PERSON = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), AttSourceSystemDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getType()), AttSourceSystemDO::getType, reqVO.getType())
                .eq(reqVO.getValidFlag()!=null, AttSourceSystemDO::getValidFlag, reqVO.getValidFlag())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, AttSourceSystemDO.class, lambdaWrapper);
    }

    default AttSourceSystemDO selectById(Long id) {
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .select("t2.NICK_NAME AS responsiblePersonName,t3.NICK_NAME AS contactPersonName")
                .leftJoin("SYSTEM_USER t2 on t.RESPONSIBLE_PERSON = t2.USER_ID AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.CONTACT_PERSON = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(AttSourceSystemDO::getId, id);
        return selectOne(lambdaWrapper);
    }

  /*  default PageResult<AttSourceSystemDO> selectPage(AttSourceSystemPageReqVO reqVO) {
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .like(AttSourceSystemDO::getName, reqVO.getName())
                .eq(AttSourceSystemDO::getType, reqVO.getType())
                .eq(AttSourceSystemDO::getSortOrder, reqVO.getSortOrder())
                .eq(AttSourceSystemDO::getDescription, reqVO.getDescription())
                .eq(AttSourceSystemDO::getValidFlag, reqVO.getValidFlag())
                .eq(AttSourceSystemDO::getResponsiblePerson, reqVO.getResponsiblePerson())
                .eq(AttSourceSystemDO::getContactPerson, reqVO.getContactPerson())
                .eq(AttSourceSystemDO::getCreateTime, reqVO.getCreateTime());
        return selectJoinPage(reqVO, AttSourceSystemDO.class, lambdaWrapper);
    }*/
}
