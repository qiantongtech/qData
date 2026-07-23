<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="dialog"
    :title="title"
    destroy-on-close
    width="60%"
    :append-to="$refs['app-container']"
  >
    <el-form
      ref="daDiscoveryTaskRef"
      :model="form"
      :rules="
        title == td('dpp.integratioTask.taskDetail', 'Task Details') ? {} : rules
      "
      label-width="146px"
      @submit.prevent
      :disabled="title == td('dpp.integratioTask.taskDetail', 'Task Details')"
     :label-position="labelPosition">
      <div class="h2-title">
        {{ td("dpp.integratioTask.basicInfo", "Basic Info") }}
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskName', 'Task Name')"
            prop="name"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              v-model="form.name"
              :placeholder="
                td('dpp.integratioTask.inputTaskName', 'Please enter task name')
              "
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.taskCategory', 'Task Category')"
              prop="catCode"
              :label-position="labelPosition">
            <el-tree-select
                :default-expanded-keys="defaultExpandedCats"
                filterable
                v-model="form.catCode"
                :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="
                td('dpp.integratioTask.selectTaskCategory', 'Please select task category')
              "
                check-strictly
                @node-click="handleNodeClick"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.responsiblePerson', 'Responsible Person')"
              prop="personCharge"
              :label-position="labelPosition">
            <el-tree-select
                filterable
                v-model="form.personCharge"
                :data="userList"
                :props="{
                value: 'userId',
                label: 'nickName',
                children: 'children',
              }"
                value-key="ID"
                :placeholder="
                td('dpp.integratioTask.selectResponsiblePersonInfo', '任务维护责任人，用于异常跟进和追溯')
              "
                check-strictly
                @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.contactNumber', 'Contact Phone')"
              prop="contactNumber"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.integratioTask.taskDetail')"
                v-model="form.contactNumber"
                :placeholder="
                td('dpp.integratioTask.inputContactNumberInfo', '责任人联系电话，随责任人自动带出')
              "
                disabled
            >
            </el-input>
            <div class="form-readonly" v-else>{{ form.contactNumber }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.taskStatus', 'Task Status')"
              prop="releaseState"
              :label-position="labelPosition">
            <el-radio-group
                v-if="title != td('dpp.integratioTask.taskDetail')"
                v-model="form.releaseState"
                class="el-form-input-width"
            >
              <el-radio
                  v-for="dict in dpp_etl_task_status"
                  :key="dict.value"
                  :label="dict.value"
                  :disabled="dict.value == 1"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_status.find(
                    (item) => item.value == form.releaseState
                )?.label || "-"
              }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('common.texts.description')"
              prop="description"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.integratioTask.taskDetail')"
                v-model="form.description"
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
            />
            <div class="form-readonly" v-else>
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="h2-title execution-section-title">
        {{ td("dpp.integratioTask.executionEngineSetting", "执行设置") }}
        <p class="form-item-description" style="margin-left: 20px;">
          {{ td("dpp.integratioTask.executionEngineDescription", "选择任务运行的引擎，不同引擎适用于不同的计算场景与技术栈。") }}
        </p>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.integratioTask.executionEngine', '执行引擎')"
              prop="taskType"
              :label-position="labelPosition">
            <el-radio-group
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                :model-value="form.taskType"
                class="engine-card-group"
                :disabled="props.data.id"
                @change="handleExecutionEngineChange"
            >
              <el-radio
                  v-for="(item, index) in actuator_type"
                  :key="index"
                  :value="item.value"
                  class="option-card engine-card"
                  :disabled="item.value === 'FLINK'"
              >
                <img
                    class="option-card__icon engine-card__icon"
                    :src="getEngineMeta(item.value).icon"
                    :alt="item.label"
                />

                <span class="option-card__content">
                  <span class="option-card__heading">
                    <span class="option-card__name">{{ item.label }}</span>
                    <el-tag type="primary" :underline="false" class="task-cat-ellipsis" size="small">
                      {{ getEngineMeta(item.value).tag || "-" }}
                    </el-tag>
                  </span>
                  <span class="option-card__description">
                    {{ getEngineMeta(item.value).description }}
                  </span>
                </span>
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.taskType || "-" }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="engine-config-grid">
        <template v-if="form.taskType == 'DATAX'">
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXJvmXms', 'JVM初始内存')"
                prop="dataXJvmXms"
                :label-position="labelPosition"
            >
              <el-input
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="dataXJvmXmsInput"
                  inputmode="numeric"
                  :placeholder="td('dpp.integratioTask.inputDataXJvmXms', '请输入JVM初始内存')"
              >
                <template #append>MB</template>
              </el-input>
              <div class="form-readonly" v-else>
                {{ formatDataXMemoryMb(form.dataXJvmXms) }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXJvmXmsDescription", "DataX 进程启动时申请的 JVM 堆内存") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXJvmXmx', 'JVM最大内存')"
                prop="dataXJvmXmx"
                :label-position="labelPosition"
            >
              <el-input
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="dataXJvmXmxInput"
                  inputmode="numeric"
                  :placeholder="td('dpp.integratioTask.inputDataXJvmXmx', '请输入JVM最大内存')"
              >
                <template #append>MB</template>
              </el-input>
              <div class="form-readonly" v-else>
                {{ formatDataXMemoryMb(form.dataXJvmXmx) }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXJvmXmxDescription", "DataX 进程可使用的 JVM 最大堆内存") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXChannel', 'Channel并发数')"
                prop="dataXChannel"
                :label-position="labelPosition"
            >
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="form.dataXChannel"
                  controls-position="right"
                  :min="1"
                  :max="128"
                  style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.dataXChannel ?? "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXChannelDescription", "Reader 与 Writer 同时运行的数据传输通道数") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXByteSpeed', '字节限速')"
                prop="dataXByteSpeed"
                :label-position="labelPosition"
            >
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="form.dataXByteSpeed"
                  controls-position="right"
                  :min="0"
                  style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.dataXByteSpeed ?? "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXByteSpeedDescription", "每秒最大传输字节数，0 表示不限速") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXRecordSpeed', '记录限速')"
                prop="dataXRecordSpeed"
                :label-position="labelPosition"
            >
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="form.dataXRecordSpeed"
                  controls-position="right"
                  :min="0"
                  style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.dataXRecordSpeed ?? "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXRecordSpeedDescription", "每秒最大传输记录数，0 表示不限速") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.dataXErrorLimit', '脏数据上限')"
                prop="dataXErrorLimit"
                :label-position="labelPosition"
            >
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                  v-model="form.dataXErrorLimit"
                  controls-position="right"
                  :min="0"
                  style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.dataXErrorLimit ?? "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.dataXErrorLimitDescription", "允许的最大脏数据条数，超过后任务失败") }}
              </p>
            </el-form-item>
          </el-col>
        </template>
        <template v-if="form.taskType == 'SPARK'">
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.driverCores', 'Driver Cores')"
                prop="driverCores"
                :label-position="labelPosition">
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverCores',
                    'Please enter driver cores'
                  )
                "
                  v-model="form.driverCores"
                  controls-position="right"
                  :min="0"
                  style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.driverCores || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.driverCoresDescription", "Spark Driver 使用的 CPU 核心数") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.driverMemory', 'Driver Memory')"
                prop="driverMemory"
                :label-position="labelPosition">
              <el-input
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  v-model="form.driverMemory"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverMemory',
                    'Please enter driver memory'
                  )
                "
                  style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.driverMemory || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.driverMemoryDescription", "Spark Driver 可使用的内存大小") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.executorCount', 'Executor Count')"
                prop="numExecutors"
                :label-position="labelPosition">
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCount',
                    'Please enter executor count'
                  )
                "
                  v-model="form.numExecutors"
                  controls-position="right"
                  style="width: 100%"
                  :min="0"
              />
              <div class="form-readonly" v-else>
                {{ form.numExecutors || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.executorCountDescription", "Spark Executor 的进程数量") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.executorMemory', 'Executor Memory')"
                prop="executorMemory"
                :label-position="labelPosition">
              <el-input
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  v-model="form.executorMemory"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorMemory',
                    'Please enter executor memory'
                  )
                "
                  style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.executorMemory || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.executorMemoryDescription", "单个 Spark Executor 的内存大小") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.executorCores', 'Executor Cores')"
                prop="executorCores"
                :label-position="labelPosition">
              <el-input-number
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCores',
                    'Please enter executor cores'
                  )
                "
                  v-model="form.executorCores"
                  controls-position="right"
                  style="width: 100%"
                  :min="0"
              />
              <div class="form-readonly" v-else>
                {{ form.executorCores || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.executorCoresDescription", "单个 Executor 使用的 CPU 核心数") }}
              </p>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                :label="td('dpp.integratioTask.yarnQueue', 'Yarn Queue')"
                prop="yarnQueue"
                :label-position="labelPosition">
              <el-input
                  v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                  v-model="form.yarnQueue"
                  :placeholder="
                  td(
                    'dpp.integratioTask.inputYarnQueue',
                    'Please enter Yarn queue (optional)'
                  )
                "
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.yarnQueue || "-" }}
              </div>
              <p class="form-item-description">
                {{ td("dpp.integratioTask.yarnQueueDescription", "Spark 作业提交使用的 Yarn 资源队列") }}
              </p>
            </el-form-item>
          </el-col>
        </template>
      </el-row>

      <div class="h2-title scheduler-section-title">
        {{ td("dpp.integratioTask.schedulerSetting", "调度设置") }}
        <p class="form-item-description" style="margin-left: 20px;">
          {{ td("dpp.integratioTask.schedulerDescription", "设置任务的调度周期及调度系统。") }}
        </p>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.integratioTask.schedulerSystem', '调度系统')"
              prop="scheduler"
              :label-position="labelPosition"
          >
            <el-radio-group
                v-if="title != td('dpp.integratioTask.taskDetail')"
                class="scheduler-card-group"
                :model-value="form.scheduler"
                @change="handleSchedulerChange"
            >
              <el-radio
                  v-for="(item, index) in scheduler_type"
                  :key="index"
                  :value="item.value"
                  class="option-card"
              >
                <img
                    class="option-card__icon scheduler-card__icon"
                    :src="getSchedulerMeta(item.value).icon"
                    :alt="item.label"
                />
                <span class="option-card__content">
                  <span class="option-card__heading">
                    <span class="option-card__name">{{ item.label }}</span>
                    <el-tag type="primary" :underline="false" class="task-cat-ellipsis" size="small">
                      {{ getSchedulerMeta(item.value).tag || "-" }}
                    </el-tag>
                  </span>
                  <span class="option-card__description">
                    {{ getSchedulerMeta(item.value).description }}
                  </span>
                </span>
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.scheduler || "-" }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.scheduleCycle', '调度周期')"
              prop="crontab"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.integratioTask.taskDetail')"
                v-model="form.crontab"
                :placeholder="
                td('dpp.integratioTask.selectScheduleCycle', '请选择调度周期')
              "
            >
              <template #append>
                <el-button
                    type="primary"
                    @click="handleShowCron"
                    style="background-color: #2666fb; color: #fff"
                >
                  {{ td("dpp.integratioTask.configure", "配置") }}
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
            <p
                v-if="showCrontabDescription"
                class="form-item-description"
            >
              {{ td("dpp.integratioTask.scheduleCycleDescription", "定义任务自动触发的时间和频率") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.workerGroup', 'Worker分组')"
              prop="workerGroup"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                v-model="form.workerGroup"
                :placeholder="
                td('dpp.integratioTask.inputWorkerGroup', '请输入Worker分组')
              "
                disabled
            />
            <div class="form-readonly" v-else>
              {{ form.workerGroup ?? "-" }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.workerGroupDescription", "指定承载任务的 Worker 节点分组") }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="h2-title">
        {{ td("dpp.integratioTask.attributeInfo", "运行策略") }}
        <p class="form-item-description" style="margin-left: 20px;">
          {{ td("dpp.integratioTask.attributeDescription", "配置任务运行时的容错策略。") }}
        </p>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskPriority', '任务优先级')"
            prop="taskPriority"
           :label-position="labelPosition">
            <el-select
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              clearable
              v-model="form.taskPriority"
              :placeholder="
                td('dpp.integratioTask.selectTaskPriority', '请选择任务优先级')
              "
            >
              <el-option
                v-for="(item, index) in dpp_etl_task_priority"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_priority.find(
                  (item) => item.value == form.taskPriority
                )?.label || "-"
              }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.taskPriorityDescription", "用于资源竞争时确定任务调度顺序，优先级越高越优先执行") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.failRetryTimes', '失败重试次数')"
            prop="failRetryTimes"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.failRetryTimes"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryTimes',
                  '请输入失败重试次数'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.times", "次")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryTimes || "-" }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.failRetryTimesDescription", "定义任务失败后的最大自动重试次数") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.failRetryInterval', '失败重试间隔')"
            prop="failRetryInterval"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.failRetryInterval"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryInterval',
                  '请输入失败重试间隔'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.minutes", "分")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryInterval || "-" }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.failRetryIntervalDescription", "定义相邻两次失败重试的间隔分钟数") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.delayExecutionTime', '延迟执行时间')"
            prop="delayTime"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.delayTime"
              :placeholder="
                td('dpp.integratioTask.inputDelayTime', '请输入延迟执行时间')
              "
            >
              <template #append>分</template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.delayTime || "-" }}</div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.delayExecutionTimeDescription", "定义调度触发后的延迟执行分钟数") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integratioTask.executionStrategy', '执行策略')"
              prop="executionType"
              :label-position="labelPosition">
            <el-select
                v-if="title != td('dpp.integratioTask.taskDetail')"
                class="el-form-input-width"
                v-model="form.executionType"
                :placeholder="
                td(
                  'dpp.integratioTask.selectExecutionStrategy',
                  '请选择执行策略'
                )
              "
                style="width: 100%"
            >
              <el-option
                  v-for="dict in dpp_etl_task_execution_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              ></el-option>
            </el-select>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_execution_type.find(
                    (item) => item.value == form.executionType
                )?.label || "-"
              }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.integratioTask.executionStrategyDescription", "定义任务节点的执行组织方式") }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('common.texts.remark')" prop="remark" :label-position="labelPosition">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row> -->
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">{{
            td("common.button.close", "Close")
          }}</el-button>
          <el-button
            type="primary"
            v-if="!route.query.info"
            @click="saveClose"
            >{{ td("common.button.save", "Save") }}</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">{{
            td("dpp.integratioTask.onlySave", "Save Only")
          }}</el-button>
          <el-button type="primary" @click="saveData">{{
            td("dpp.integratioTask.saveAndConfigFlow", "Save and Configure Flow")
          }}</el-button>
        </template>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    :title="td('dpp.integratioTask.cronGenerator', 'Cron Expression Generator')"
    v-model="openCron"
    class="dialog"
    :append-to="$refs['app-container']"
    destroy-on-close
  >
    <!--    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression" :Crontab="false">-->
    <crontab
      ref="crontabRef"
      @hide="openCron = false"
      @fill="crontabFill"
      :expression="expression"
    >
    </crontab>
  </el-dialog>
</template>

<script setup>
import { checkApi } from "@/api/ds/api/api.js";
import useDefaultLang from "@/composables/useDefaultLang";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
import dataXIcon from "@/assets/images/common/img-datax.png";
import sparkIcon from "@/assets/images/common/icon-spark-one.svg";
import flinkIcon from "@/assets/images/common/icon-flink-one.svg";
import quartzIcon from "@/assets/images/common/img-quartz.png";
import dolphinSchedulerIcon from "@/assets/images/common/img-ds.png";
const { proxy } = getCurrentInstance();
const {
  dpp_etl_task_execution_type,
  dpp_etl_task_status,
  dpp_etl_task_priority
} = proxy.useDict(
  "dpp_etl_task_execution_type",
  "dpp_etl_task_status",
  "dpp_etl_task_priority"
);
import { useRoute, useRouter } from "vue-router";

const { td } = useDefaultLang();
const route = useRoute();
const router = useRouter();

const actuator_type = [
  { label: "DataX", value: "DATAX" },
  { label: "Spark", value: "SPARK" },
  { label: "Flink", value: "FLINK" },
];

const scheduler_type = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];

const engineMeta = {
  DATAX: {
    icon: dataXIcon,
    tag: td("dpp.integratioTask.lightweight", "轻量级"),
    description: td(
      "dpp.integratioTask.dataXDescription",
      "轻量级数据同步引擎，单机运行，部署简单，适合关联数据抽取与导入场景。"
    ),
  },
  SPARK: {
    icon: sparkIcon,
    tag: td("dpp.integratioTask.distributed", "分布式"),
    description: td(
      "dpp.integratioTask.sparkDescription",
      "分布式计算引擎，适合大规模离线处理与复杂计算任务。"
    ),
  },
  FLINK: {
    icon: flinkIcon,
    tag: td("dpp.integratioTask.notOnline", "暂未上线"),
    description: td(
      "dpp.integratioTask.flinkDescription",
      "分布式流式计算引擎，适合实时处理与低延迟计算场景。"
    ),
  },
};

const schedulerMeta = {
  QUARTZ: {
    icon: quartzIcon,
    tag: td("dpp.integratioTask.lightweight", "轻量级"),
    description: td(
      "dpp.integratioTask.quartzCardDescription",
      "轻量级调度，适用于简单定时任务。"
    ),
  },
  DOLPHINSCHEDULER: {
    icon: dolphinSchedulerIcon,
    tag: td("dpp.integratioTask.enterprise", "企业级"),
    description: td(
      "dpp.integratioTask.dolphinSchedulerCardDescription",
      "企业级调度，适用于复杂工作流编排。"
    ),
  },
};

const getEngineMeta = (value) => engineMeta[value] || engineMeta.DATAX;
const getSchedulerMeta = (value) =>
  schedulerMeta[value] || schedulerMeta.QUARTZ;
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  catCode: { type: String, default: "" },
  defaultScheduler: { type: String, default: "" },
});

const emit = defineEmits(["update:visible", "confirm", "save"]);

const dataXDefaultConfig = {
  dataXJvmXms: "1024m",
  dataXJvmXmx: "2048m",
  dataXChannel: 1,
  dataXByteSpeed: 0,
  dataXRecordSpeed: 0,
  dataXErrorLimit: 0,
};

const validateDataXMemory = (rule, value, callback) => {
  if (!/^\d+[mMgG]$/.test(value || "")) {
    callback(
      new Error(
        td(
          "dpp.integratioTask.dataXMemoryFormatError",
          "请输入正整数，单位为MB"
        )
      )
    );
    return;
  }
  callback();
};

const getDataXMemoryMbValue = (value) => {
  const matched = String(value ?? "").trim().match(/^(\d+)([mMgG])?$/);
  if (!matched) return "";
  return matched[2]?.toLowerCase() === "g"
    ? String(Number(matched[1]) * 1024)
    : matched[1];
};

const formatDataXMemoryMb = (value) => {
  const memoryMb = getDataXMemoryMbValue(value);
  return memoryMb ? `${memoryMb} MB` : "-";
};

const showCrontabDescription = ref(true);
const validateCrontab = (_rule, value, callback) => {
  showCrontabDescription.value = Boolean(value);
  if (!value) {
    callback(
      new Error(
        td("dpp.integratioTask.selectScheduleCycle", "调度周期不能为空")
      )
    );
    return;
  }
  callback();
};

// Define form validation rules
const rules = {
  name: [
    {
      required: true,
      message: td("dpp.integratioTask.inputTaskName", "Please enter task name"),
      trigger: "change",
    },
  ],
  catCode: [
    {
      required: true,
      message: td("dpp.integratioTask.selectTaskCategory", "Please select task category"),
      trigger: "change",
    },
  ],
  executionType: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectExecutionStrategy",
        "Please select execution strategy"
      ),
      trigger: "change",
    },
  ],
  scheduler: [
    {
      required: true,
      message: td("dpp.integratioTask.selectScheduler", "Scheduler is required"),
      trigger: "change",
    },
  ],
  crontab: [
    {
      required: true,
      validator: validateCrontab,
      trigger: "blur",
    },
  ],

  // releaseState: [{ required: true, message: "Task status cannot be empty", trigger: "change" }],
  taskType: [
    {
      required: true,
      message: td("dpp.integratioTask.executionEngine", "Execution Engine"),
      trigger: "change",
    },
  ],
  personCharge: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectResponsiblePerson",
        "Please select responsible person"
      ),
      trigger: "change",
    },
  ],
};
const form = ref({
  catId: "",
  name: "",
  catCode: "", // Can be initialized to empty or set to default value
  executionType: "PARALLEL", // Initialized to empty or default value
  scheduler: "QUARTZ",
  actuator: "DATAX",
  crontab: "",
  releaseState: "0",
  description: "",
  contactNumber: "",
  personCharge: "",
  // Newly added
  taskPriority: dpp_etl_task_priority.value?.[0]?.value ?? "",
  workerGroup: "default",
  failRetryTimes: 0,
  failRetryInterval: 0,
  delayTime: 0,
  taskType: "DATAX",
  // DataX configuration
  ...dataXDefaultConfig,
  // Fink configuration
  jobManagerMemory: "1G",
  taskManagerMemory: "2G",
  slot: 1,
  taskManager: 2,
  parallelism: 1,
  // Spark configuration
  driverCores: 1,
  driverMemory: "512m",
  numExecutors: 1,
  executorMemory: "512m",
  executorCores: 1,
  yarnQueue: "",
});

const createDataXMemoryMbModel = (field) =>
  computed({
    get: () => getDataXMemoryMbValue(form.value[field]),
    set: (value) => {
      const memoryMb = String(value ?? "").replace(/\D/g, "");
      form.value[field] = memoryMb ? `${memoryMb}m` : "";
    },
  });

const dataXJvmXmsInput = createDataXMemoryMbModel("dataXJvmXms");
const dataXJvmXmxInput = createDataXMemoryMbModel("dataXJvmXmx");

const reset = () => {
  proxy.resetForm("daDiscoveryTaskRef");
  showCrontabDescription.value = true;
  form.value = {
    name: "",
    catId: "",
    scheduler: "QUARTZ",
    actuator: "DATAX",
    catCode: "", // Can be initialized to empty or set to default value
    executionType: "PARALLEL", // Initialized to empty or default value
    crontab: "",
    releaseState: "0",
    description: "",
    contactNumber: "",
    personCharge: "",
    // Newly added
    taskPriority: dpp_etl_task_priority.value?.[0]?.value ?? "",
    workerGroup: "default",
    failRetryTimes: 0,
    failRetryInterval: 0,
    delayTime: 0,
    taskType: "DATAX",
    // DataX configuration
    ...dataXDefaultConfig,
    // Fink configuration
    jobManagerMemory: "1G",
    taskManagerMemory: "2G",
    slot: 1,
    taskManager: 2,
    parallelism: 1,
    // Spark configuration
    driverCores: 1,
    driverMemory: "512m",
    numExecutors: 1,
    executorMemory: "512m",
    executorCores: 1,
    yarnQueue: "",
  };
};
watch(
  dpp_etl_task_priority,
  (options) => {
    if (
      options?.length &&
      (form.value.taskPriority === "" || form.value.taskPriority == null)
    ) {
      form.value.taskPriority = options[0].value;
    }
  },
  { immediate: true }
);
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      if (props.data.id) {
        let data = JSON.parse(JSON.stringify(props.data.taskConfig));
        console.log("🚀 ~ props.data.taskConfig:", props.data.taskConfig);
        let draftJson = JSON.parse(data.draftJson);
        form.value = { ...dataXDefaultConfig, ...data, ...draftJson };
        // Preserve DS and Spark as defaults for legacy tasks without scheduler or actuator values.
        form.value.scheduler = form.value.scheduler || "DOLPHINSCHEDULER";
        form.value.taskType = form.value.actuator == "DATAX" ? "DATAX" : form.value.taskType || "SPARK";
        syncActuatorByEngine();
        enforceQuartzDataX();
        form.value.personCharge = Number(form.value.personCharge) || "";
        form.value.crontab = props?.data.taskConfig?.crontab;
      } else {
        if (props.defaultScheduler) {
          form.value.scheduler = props.defaultScheduler;
          form.value.taskType =
            props.defaultScheduler === "QUARTZ" ? "DATAX" : "SPARK";
        }
        form.value.catCode = props?.catCode || "";
        syncActuatorByEngine();
      }
    } else {
      reset();
    }
  }
);
const handleNodeClick = (val) => {
  console.log("Task category changed; current value:", val);
  form.value.catId = val.id;
};

/**
 * DolphinScheduler
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async (value) => {
  const engineSwitchMap = {
    QUARTZ: {
      taskType: "DATAX",
      message: td(
        "dpp.integratioTask.switchQuartzEngineConfirm",
        "当前版本 Quartz 仅适配 DataX 执行引擎，切换后执行引擎将同步调整为 DataX，是否继续？"
      ),
    },
    DOLPHINSCHEDULER: {
      taskType: "SPARK",
      message: td(
        "dpp.integratioTask.switchDolphinSchedulerEngineConfirm",
        "当前版本 DolphinScheduler 仅适配 Spark 执行引擎，切换后执行引擎将同步调整为 Spark，是否继续？"
      ),
    },
  };
  const switchConfig = engineSwitchMap[value];

  if (switchConfig && form.value.taskType != switchConfig.taskType) {
    try {
      await proxy.$modal.confirm(switchConfig.message);
      form.value.scheduler = value;
      form.value.taskType = switchConfig.taskType;
      syncActuatorByEngine();
    } catch {
      // 取消时保持原调度器和执行引擎，不更新选中状态。
    }
    return;
  }

  form.value.scheduler = value;
  syncActuatorByEngine();
};

const handleExecutionEngineChange = async (value) => {
  const schedulerSwitchMap = {
    SPARK: {
      scheduler: "DOLPHINSCHEDULER",
      message: td(
        "dpp.integratioTask.switchSparkSchedulerConfirm",
        "当前版本 Spark 仅适配 DolphinScheduler 调度器，切换后调度器将同步调整为 DolphinScheduler，是否继续？"
      ),
    },
    DATAX: {
      scheduler: "QUARTZ",
      message: td(
        "dpp.integratioTask.switchDataXSchedulerConfirm",
        "当前版本 DataX 仅适配 Quartz 调度器，切换后调度器将同步调整为 Quartz，是否继续？"
      ),
    },
  };
  const switchConfig = schedulerSwitchMap[value];

  if (switchConfig && form.value.scheduler != switchConfig.scheduler) {
    try {
      await proxy.$modal.confirm(switchConfig.message);
      form.value.scheduler = switchConfig.scheduler;
      form.value.taskType = value;
      syncActuatorByEngine();
    } catch {
      // 取消时保持原执行引擎和调度器，不更新选中状态。
    }
    return;
  }

  form.value.taskType = value;
  syncActuatorByEngine();
};

const enforceQuartzDataX = () => {
  if (form.value.scheduler == "QUARTZ") {
    form.value.taskType = "DATAX";
  }
};

const syncActuatorByEngine = () => {
  form.value.actuator = form.value.taskType == "DATAX" ? "DATAX" : "SPARK";
};
// Computed property handling v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});
let daDiscoveryTaskRef = ref();
const closeDialog = () => {
  emit("update:visible", false);
};
const saveClose = async () => {
  if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
    return;
  }
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      syncActuatorByEngine();
      emit("save", form.value);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  });
};
// How to save data
const saveData = async () => {
  if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
    return;
  }
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      syncActuatorByEngine();
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  });
};

/**
 * check dolphinscheduler api
 * @returns {Promise<AxiosResponse<any>>}
 */
const checkDSUpStart = async () => {
  const resp = await checkApi();
  if (!resp.data) {
    proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "Please start the DolphinScheduler scheduler！"));
  }
  return resp.data;
}

let openCron = ref(false);
const expression = ref("");
/** Scheduling cycle button operation */
function handleShowCron() {
  expression.value = form.value.crontab;
  openCron.value = true;
}
/** Return value after confirmation */
function crontabFill(value) {
  form.value.crontab = value;
  daDiscoveryTaskRef.value?.validateField("crontab");
}
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
const defaultExpandedCats = computed(() => {
  return props.deptOptions.map((item) => item.id);
});
</script>
<style lang="scss" scoped>
.blue-text {
  color: var(--el-color-primary);
}

.form-item-description {
  display: flex;
  align-items: center;
  color: #888;
  font-size: 12px;
  line-height: 1.5;
  margin: 0;
}

:deep(.el-form-item__content .form-item-description){
  margin-bottom: 0;
}

:deep(.el-select) {
  .el-select__wrapper.is-disabled {
    cursor: default;
    background-color: #fcfcfc;
    --el-select-disabled-color: #333;

    .el-select__suffix {
      display: none;
    }
  }
}
:deep(.el-divider--horizontal){
  margin: 8px 0 12px;
}

.h2-title {
  display: flex;
  align-items: center;
}

.h2-title > .form-item-description {
  align-self: center;
}

.scheduler-section-title {
  margin-top: 4px;
  padding-top: 16px;
}

.engine-card-group,
.scheduler-card-group {
  --option-card-width: max(210px, calc((100% - 34px) / 3));

  display: flex;
  flex-wrap: nowrap;
  gap: 17px;
  width: 100%;
}

:deep(.engine-card-group .option-card.el-radio),
:deep(.scheduler-card-group .option-card.el-radio) {
  flex: 0 0 var(--option-card-width);
  width: var(--option-card-width);
  margin-right: 0;
}

:deep(.option-card.el-radio) {
  position: relative;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  min-width: 0;
  padding: 13px 30px 13px 15px;
  white-space: normal;
  transition: border-color 0.2s, box-shadow 0.2s;
  height: 86px;
  background: #FFFFFF;
  border-radius: 2px;
  border: 1px solid #D6DAE1;

  &:hover:not(.is-disabled) {
    border-color: var(--el-color-primary);
  }

  &.is-checked {
    border-color: var(--el-color-primary);
  }

  &.is-disabled {
    background: var(--el-fill-color-lighter);
  }

  .el-radio__input {
    position: absolute;
    top: 14px;
    right: 12px;
  }

  .el-radio__label {
    display: flex;
    flex: 1;
    align-self: stretch;
    align-items: center;
    min-width: 0;
    width: 100%;
    padding-left: 0;
    color: var(--el-text-color-primary);
  }
}

.option-card__icon {
  flex: 0 0 auto;
  width: 36px;
  margin-right: 12px;
  object-fit: contain;
}

.engine-card__icon {
  width: 36px;
}

.option-card__content,
.option-card__heading,
.option-card__description {
  display: block;
}

.option-card__content {
  flex: 1;
  min-width: 0;
  width: 0;
}

.option-card__heading {
  display: flex;
  align-items: center;
  min-width: 0;
  margin-bottom: 4px;
  width: 100%;

  .task-cat-ellipsis {
    min-width: 60px;
  }
}

.option-card__name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 20px;
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: var(--color-third-party-Langfuse);
  line-height: 20px;
  text-align: left;
  font-style: normal;
  margin-right: 8px;
  max-width: calc(100% - 60px);
}

.option-card__description {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  width: 108%;
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: rgba(0,0,0,0.65);
  line-height: 18px;
  text-align: left;
  font-style: normal;
}

:deep(.option-card.el-radio.is-disabled) {
  .option-card__icon,
  .option-card__content {
    opacity: 0.55;
  }
}

.engine-config-grid {
  margin-top: 2px;

  :deep(.el-form-item) {
    margin-bottom: 12px;
  }
}

</style>
