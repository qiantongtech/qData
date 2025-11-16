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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

/**
 * TODO <p>Need to optimize, why there are multiple task parameter variables:taskParams,dependence,switchResult</p>
 *
 */
public class ParametersNode {

    private String taskType;

    private String taskParams;

    private String dependence;

    private String switchResult;

    public static ParametersNodeBuilder builder() {
        return new ParametersNodeBuilder();
    }

    public static class ParametersNodeBuilder {

        private String taskType;

        private String taskParams;

        private String dependence;

        private String switchResult;

        public ParametersNodeBuilder taskType(String taskType) {
            this.taskType = taskType;
            return this;
        }

        public ParametersNodeBuilder taskParams(String taskParams) {
            this.taskParams = taskParams;
            return this;
        }

        public ParametersNodeBuilder dependence(String dependence) {
            this.dependence = dependence;
            return this;
        }

        public ParametersNodeBuilder switchResult(String switchResult) {
            this.switchResult = switchResult;
            return this;
        }

        public ParametersNode build() {
            return new ParametersNode(this.taskType, this.taskParams, this.dependence, this.switchResult);
        }

    }

    public ParametersNode() {

    }

    public ParametersNode(String taskType, String taskParams, String dependence, String switchResult) {
        this.taskType = taskType;
        this.taskParams = taskParams;
        this.dependence = dependence;
        this.switchResult = switchResult;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(String taskParams) {
        this.taskParams = taskParams;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    public String getSwitchResult() {
        return switchResult;
    }

    public void setSwitchResult(String switchResult) {
        this.switchResult = switchResult;
    }
}
