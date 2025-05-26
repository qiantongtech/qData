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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProcessTaskRelation {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * process version
     */
    private int processDefinitionVersion;

    /**
     * project code
     */
    private String projectCode;

    /**
     * process code
     */
    private String processDefinitionCode;

    /**
     * pre task code
     */
    private String preTaskCode;

    /**
     * pre node version
     */
    private int preTaskVersion;

    /**
     * post task code
     */
    private String postTaskCode;

    /**
     * post node version
     */
    private int postTaskVersion;

    /**
     * condition type
     */
    private String conditionType;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;


    public ProcessTaskRelation(ProcessTaskRelationLog processTaskRelationLog) {
        this.name = processTaskRelationLog.getName();
        this.processDefinitionVersion = processTaskRelationLog.getProcessDefinitionVersion();
        this.projectCode = processTaskRelationLog.getProjectCode();
        this.processDefinitionCode = processTaskRelationLog.getProcessDefinitionCode();
        this.preTaskCode = processTaskRelationLog.getPreTaskCode();
        this.preTaskVersion = processTaskRelationLog.getPreTaskVersion();
        this.postTaskCode = processTaskRelationLog.getPostTaskCode();
        this.postTaskVersion = processTaskRelationLog.getPostTaskVersion();
        this.conditionType = processTaskRelationLog.getConditionType();

        this.createTime = processTaskRelationLog.getCreateTime();
        this.updateTime = new Date();
    }

}
