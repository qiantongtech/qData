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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.qiantong.qdata.api.ds.api.etl.ds;

/**
 * process definition log
 */
public class ProcessDefinitionLog extends ProcessDefinition {

    public ProcessDefinitionLog() {
        super();
    }

    public ProcessDefinitionLog(ProcessDefinition processDefinition) {
        this.setCode(processDefinition.getCode());
        this.setName(processDefinition.getName());
        this.setVersion(processDefinition.getVersion());
        this.setReleaseState(processDefinition.getReleaseState());
        this.setProjectCode(processDefinition.getProjectCode());
        this.setDescription(processDefinition.getDescription());
        this.setCreateTime(processDefinition.getCreateTime());
        this.setUpdateTime(processDefinition.getUpdateTime());
//        this.setFlag(processDefinition.getFlag());
//        this.setUserId(processDefinition.getUserId());
//        this.setUserName(processDefinition.getUserName());
//        this.setProjectName(processDefinition.getProjectName());
        this.setLocations(processDefinition.getLocations());
//        this.setScheduleReleaseState(processDefinition.getScheduleReleaseState());
//        this.setTimeout(processDefinition.getTimeout());
//        this.setModifyBy(processDefinition.getModifyBy());
//        this.setWarningGroupId(processDefinition.getWarningGroupId());
        this.setExecutionType(processDefinition.getExecutionType());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
