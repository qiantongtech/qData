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
 * process task relation log
 */
public class ProcessTaskRelationLog extends ProcessTaskRelation {


    public ProcessTaskRelationLog() {
        super();
    }

    public ProcessTaskRelationLog(ProcessTaskRelation processTaskRelation) {
        super();
        this.setName(processTaskRelation.getName());
        this.setProcessDefinitionCode(processTaskRelation.getProcessDefinitionCode());
        this.setProcessDefinitionVersion(processTaskRelation.getProcessDefinitionVersion());
        this.setProjectCode(processTaskRelation.getProjectCode());
        this.setPreTaskCode(processTaskRelation.getPreTaskCode());
        this.setPreTaskVersion(processTaskRelation.getPreTaskVersion());
        this.setPostTaskCode(processTaskRelation.getPostTaskCode());
        this.setPostTaskVersion(processTaskRelation.getPostTaskVersion());
        this.setConditionType(processTaskRelation.getConditionType());
        this.setCreateTime(processTaskRelation.getCreateTime());
        this.setUpdateTime(processTaskRelation.getUpdateTime());
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
