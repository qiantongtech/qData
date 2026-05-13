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
 * 源代码的"可访问性"均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
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

package tech.qiantong.qdata.module.mc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 采集范围枚举
 *
 * @author qdata
 * @date 2026-04-27
 */
@Getter
@AllArgsConstructor
public enum CollectionScopeEnum {

    /**
     * 全部库
     */
    ALL("2", "全部库"),

    /**
     * 自定义库
     */
    CUSTOM("1", "自定义库");

    /**
     * 采集范围值
     */
    private final String scope;

    /**
     * 采集范围名称
     */
    private final String name;

    /**
     * 根据范围值获取枚举
     *
     * @param scope 范围值
     * @return 枚举对象
     */
    public static CollectionScopeEnum getByScope(String scope) {
        if (scope == null) {
            return null;
        }
        for (CollectionScopeEnum value : values()) {
            if (value.getScope().equals(scope)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为全部库
     *
     * @param scope 范围值
     * @return 是否为全部库
     */
    public static boolean isAll(String scope) {
        return ALL.getScope().equals(scope);
    }

    /**
     * 判断是否为自定义库
     *
     * @param scope 范围值
     * @return 是否为自定义库
     */
    public static boolean isCustom(String scope) {
        return CUSTOM.getScope().equals(scope);
    }
}
