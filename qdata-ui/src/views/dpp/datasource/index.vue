<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  、
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="da/daDatasource.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="100px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item
          :label="td('dpp.datasource.datasourceName')"
          prop="datasourceName"
        >
          <el-input
            class="el-form-input-width"
            v-model="queryParams.datasourceName"
            :placeholder="td('dpp.datasource.inputDatasourceName')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item
          :label="td('dpp.datasource.datasourceType')"
          prop="datasourceType"
        >
          <el-select
            class="el-form-input-width"
            v-model="queryParams.datasourceType"
            :placeholder="td('dpp.datasource.selectDatasourceType')"
            clearable
          >
            <el-option
              v-for="dict in datasource_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i
            >{{ td("common.button.query") }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i
            >{{ td("common.button.reset") }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['da:dataSource:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add") }}
            </el-button>
          </el-col>
          <!--         <el-col :span="1.5">-->
          <!--           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['da:dataSource:edit']"-->
          <!--                      @mousedown="(e) => e.preventDefault()">-->
          <!--             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
          <!--           </el-button>-->
          <!--         </el-col>-->
          <!--         <el-col :span="1.5">-->
          <!--           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['da:dataSource:remove']"-->
          <!--                      @mousedown="(e) => e.preventDefault()">-->
          <!--             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
          <!--           </el-button>-->
          <!--         </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar
            v-model:showSearch="showSearch"
            @queryTable="getList"
            :columns="columns"
          ></right-toolbar>
        </div>
      </div>
      <el-table
        stripe
        v-loading="loading"
        :data="daDatasourceList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column
          v-if="getColumnVisibility(1)"
          width="80"
          :label="td('dpp.datasource.number')"
          align="center"
          prop="id"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.id || "-" }}
          </template>
        </el-table-column>
        <!--       <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column
          v-if="getColumnVisibility(2)"
          width="250"
          :label="td('dpp.datasource.datasourceName')"
          align="left"
          prop="datasourceName"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.datasourceName || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(3)"
          :label="td('dpp.datasource.description')"
          width="240"
          align="left"
          prop="description"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>

        <el-table-column
          v-if="getColumnVisibility(4)"
          width="140"
          :label="td('dpp.datasource.datasourceType')"
          align="center"
          prop="datasourceType"
        >
          <template #default="scope">
            <dict-tag
              :options="datasource_type"
              :value="scope.row.datasourceType"
            />
          </template>
        </el-table-column>
        <!-- <el-table-column
            v-if="getColumnVisibility(2) && type == 1"
            width="120"
            :label="td('dpp.datasource.projectNameList')"
            align="center"
            prop="projectName"
        >
            <template #default="scope">
                {{ scope.row.projectName || '-' }}
            </template>
        </el-table-column> -->
        <el-table-column
          v-if="getColumnVisibility(5)"
          :label="td('dpp.datasource.createBy')"
          width="120"
          align="center"
          prop="createBy"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(6)"
          :label="td('dpp.datasource.createTime')"
          align="center"
          prop="createTime"
          width="160"
          sortable="custom"
          :sort-orders="['descending', 'ascending']"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          :label="td('dpp.datasource.status')"
          align="center"
          prop="validFlag"
          width="100"
        >
          <template #default="scope">
            <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

            <el-switch
              v-model="scope.row.validFlag"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(8)"
          :label="td('dpp.datasource.remark')"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(9)"
          :label="td('dpp.datasource.operation')"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="220"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Connection"
              @click="handleTestConnection(scope.row)"
              v-hasPermi="['da:dataSource:edit']"
              >{{ td("dpp.datasource.testConnection") }}
            </el-button>

            <el-button
              link
              type="primary"
              icon="view"
              @click="handleDetail(scope.row)"
              v-hasPermi="['da:dataSource:edit']"
              >{{ td("common.button.details") }}
            </el-button>
            <el-popover placement="bottom" :width="100" trigger="click">
              <template #reference>
                <el-button
                  link
                  type="primary"
                  :disabled="scope.row.isAdminAddTo == false"
                  icon="ArrowDown"
                >
                  <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="td('dpp.datasource.noPermission')"
                    placement="top"
                    :disabled="scope.row.isAdminAddTo != false"
                  >
                    {{ td("dpp.datasource.more") }}
                  </el-tooltip>
                </el-button>
              </template>
              <div class="butgdlist">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['da:dataSource:edit']"
                  >{{ td("common.button.update") }}
                </el-button>
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['da:dataSource:remove']"
                  >{{ td("common.button.delete") }}
                </el-button>
              </div>
            </el-popover>
            <!--           <el-button link type="primary" icon="view" @click="routeTo('/da/datasource/daDatasourceDetail',scope.row)"-->
            <!--                      v-hasPermi="['da:dataSource:edit']">复杂详情</el-button>-->
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt="" />
            <p>{{ td("common.noData") }}</p>
          </div>
        </template>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 新增或修改数据源对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="1000px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="daDatasourceRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
        :disabled="title == td('dpp.datasource.datasourceDetail')"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.datasource.datasourceName')"
              prop="datasourceName"
            >
              <el-input
                v-model="form.datasourceName"
                :placeholder="td('dpp.datasource.inputDatasourceName')"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              :label="td('dpp.datasource.datasourceType')"
              prop="datasourceType"
            >
              <el-select
                v-model="form.datasourceType"
                :placeholder="td('dpp.datasource.selectDatasourceType')"
                @change="handleDatasourceChange"
                :disabled="form.id"
              >
                <el-option
                  v-for="dict in datasource_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.datasourceType !== 'OSS-ALIYUN'">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.ip')" prop="ip">
              <el-input
                v-model="form.ip"
                :placeholder="td('dpp.datasource.inputIp')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.port')" prop="port">
              <el-input
                v-model="form.port"
                :placeholder="td('dpp.datasource.inputPort')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== 'Kafka' &&
            form.datasourceType !== 'HDFS' &&
            form.datasourceType !== 'OSS-ALIYUN'
          "
        >
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.datasource.username')"
              prop="username"
            >
              <el-input
                v-model="form.username"
                :placeholder="td('dpp.datasource.inputUsername')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.datasource.password')"
              prop="password"
            >
              <el-input
                type="password"
                v-model="form.password"
                :placeholder="td('dpp.datasource.inputPassword')"
                v-if="title === td('dpp.datasource.addDatasource')"
              />
              <el-input
                type="password"
                v-model="form.password"
                :placeholder="td('dpp.datasource.inputPassword')"
                v-if="title !== td('dpp.datasource.addDatasource')"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="form.datasourceType === 'OSS-ALIYUN'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.keyId')" prop="keyId">
                <el-input
                  v-model="form.keyId"
                  :placeholder="td('dpp.datasource.inputKeyId')"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :label="td('dpp.datasource.keySecret')"
                prop="keySecret"
              >
                <el-input
                  v-model="form.keySecret"
                  :placeholder="td('dpp.datasource.inputKeySecret')"
                  v-if="title === td('dpp.datasource.addDatasource')"
                />
                <el-input
                  type="password"
                  v-model="form.keySecret"
                  :placeholder="td('dpp.datasource.inputKeySecret')"
                  v-if="title !== td('dpp.datasource.addDatasource')"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.bucket')" prop="bucket">
                <el-input
                  v-model="form.bucket"
                  :placeholder="td('dpp.datasource.inputBucket')"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :label="td('dpp.datasource.endpoint')"
                prop="endpoint"
              >
                <el-input
                  v-model="form.endpoint"
                  :placeholder="td('dpp.datasource.inputEndpoint')"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="td('dpp.datasource.domain')" prop="domain">
                <el-input
                  v-model="form.domain"
                  :placeholder="td('dpp.datasource.inputDomain')"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== 'Kafka' &&
            form.datasourceType !== 'HDFS' &&
            form.datasourceType !== 'FTP' &&
            form.datasourceType !== 'OSS-ALIYUN'
          "
        >
          <el-col :span="12" v-if="form.datasourceType !== null">
            <el-form-item :label="td('dpp.datasource.dbname')" prop="dbname">
              <el-input
                v-model="form.dbname"
                :placeholder="td('dpp.datasource.inputDbname')"
                :disabled="form.id"
              />
            </el-form-item>
          </el-col>
          <el-col
            :span="12"
            v-if="
              form.datasourceType !== null &&
              (form.datasourceType == 'Oracle' ||
                form.datasourceType == 'Oracle11' ||
                form.datasourceType == 'Kingbase8' ||
                form.datasourceType == 'SQL_Server' ||
                form.datasourceType == 'SQL_Server2008' ||
                form.datasourceType == 'PostgreSQL')
            "
          >
            <el-form-item :label="td('dpp.datasource.sid')" prop="sid">
              <el-input
                v-model="form.sid"
                :placeholder="td('dpp.datasource.inputSid')"
                :disabled="form.id"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== null &&
            (form.datasourceType === 'Kafka' || form.datasourceType === 'HDFS')
          "
        >
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.config')" prop="config">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
                v-model="form.config"
                :placeholder="
                  form.datasourceType === 'Kafka'
                    ? td('dpp.datasource.kafkaConfigExample')
                    : td('dpp.datasource.hdfsConfigExample')
                "
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item
              :label="td('dpp.datasource.description')"
              prop="description"
            >
              <el-input
                type="textarea"
                :min-height="192"
                v-model="form.description"
                :placeholder="td('dpp.datasource.inputDescription')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="type == 0">
          <el-col :span="24">
            <el-form-item
              :label="td('dpp.datasource.projectNameList')"
              prop="projectNameList"
            >
              <el-input
                style="width: 83.5%"
                v-model="form.projectNameList"
                :placeholder="td('dpp.datasource.selectProject')"
                disabled
              >
              </el-input>
              <el-button
                style="margin-left: 11px"
                type="primary"
                @click="getListProject"
                >{{ td("dpp.datasource.selectProject") }}</el-button
              >
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.status')" prop="validFlag">
              <el-radio-group v-model="form.validFlag">
                <el-radio
                  v-for="dict in sys_disable"
                  :key="dict.value"
                  :label="dict.value === '1'"
                >
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.remark')">
              <el-input
                type="textarea"
                v-model="form.remark"
                :placeholder="td('common.form.remarkPlaceholder')"
                :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{
            td("common.button.close")
          }}</el-button>
          <el-button
            type="primary"
            size="mini"
            :loading="btnLoading"
            @click="submitForm"
            >{{ td("common.button.confirm") }}</el-button
          >
        </div>
      </template>
    </el-dialog>

    <!-- 详情 -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      width="1000px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="daDatasourceRef"
        :model="form"
        :rules="rules"
        label-width="110px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.datasourceName')">
              <div class="form-readonly">
                {{ form.datasourceName || "-" }}
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.datasourceType')">
              <div>
                <dict-tag
                  :options="datasource_type"
                  :value="form.datasourceType"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.datasourceType !== 'OSS-ALIYUN'">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.ip')">
              <div class="form-readonly">
                {{ form.ip || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.port')">
              <div class="form-readonly">
                {{ form.port || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== 'Kafka' &&
            form.datasourceType !== 'HDFS' &&
            form.datasourceType !== 'OSS-ALIYUN'
          "
        >
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.username')">
              <div class="form-readonly">
                {{ form.username || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.password')">
              <div class="form-readonly">***********</div>
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="form.datasourceType === 'OSS-ALIYUN'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.keyId')">
                <div class="form-readonly">
                  {{ form.keyId || "-" }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.keySecret')">
                <div class="form-readonly">
                  {{ form.keySecret || "-" }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.bucket')">
                <div class="form-readonly">
                  {{ form.bucket || "-" }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.endpoint')">
                <div class="form-readonly">
                  {{ form.endpoint || "-" }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="td('dpp.datasource.domain')">
                <div class="form-readonly">
                  {{ form.domain || "-" }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== 'Kafka' &&
            form.datasourceType !== 'HDFS' &&
            form.datasourceType !== 'FTP' &&
            form.datasourceType !== 'OSS-ALIYUN'
          "
        >
          <el-col :span="12" v-if="form.datasourceType !== null">
            <el-form-item :label="td('dpp.datasource.dbname')">
              <div class="form-readonly">
                {{ form.dbname || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col
            :span="12"
            v-if="
              form.datasourceType !== null &&
              (form.datasourceType == 'Oracle' ||
                form.datasourceType == 'Oracle11' ||
                form.datasourceType == 'Kingbase8' ||
                form.datasourceType == 'SQL_Server' ||
                form.datasourceType == 'SQL_Server2008' ||
                form.datasourceType == 'PostgreSQL')
            "
          >
            <el-form-item :label="td('dpp.datasource.sid')">
              <div class="form-readonly">
                {{ form.sid || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row
          :gutter="20"
          v-if="
            form.datasourceType !== null &&
            (form.datasourceType === 'Kafka' || form.datasourceType === 'HDFS')
          "
        >
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.config')">
              <div class="form-readonly">
                {{ form.config || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.description')">
              <div class="form-readonly textarea">
                {{ form.description || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="type == 0">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.projectNameList')">
              <div class="form-readonly">
                {{ form.projectNameListStr || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.status')">
              <dict-tag
                :options="sys_disable"
                :value="form.validFlag ? '1' : '0'"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.remark')">
              <div class="form-readonly textarea">
                {{ form.remark || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{
            td("common.button.close")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      :title="td('dpp.datasource.projectSelect')"
      v-model="openProject"
      width="1000px"
      draggable
    >
      <template>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ td("dpp.datasource.projectSelect") }}
        </span>
      </template>
      <!--用户数据-->
      <el-form
        class="btn-style"
        :model="queryParamsProject"
        ref="queryRef"
        :inline="true"
        label-width="68px"
      >
        <el-form-item :label="td('dpp.datasource.projectName')" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParamsProject.name"
            :placeholder="td('dpp.datasource.inputProjectName')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('dpp.datasource.manager')" prop="managerId">
          <el-select
            v-model="queryParamsProject.managerId"
            class="el-form-input-width"
            filterable
            :placeholder="td('dpp.datasource.selectManager')"
          >
            <el-option
              v-for="item in projectOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQueryProject"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i
            >{{ td("common.button.query") }}
          </el-button>
          <el-button
            @click="resetQueryProject"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22378 mr5"></i
            >{{ td("common.button.reset") }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="projectTableRef"
        stripe
        v-loading="loadingProject"
        :data="projectList"
        @selection-change="handleSelectionChangeProject"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="selectable"
          align="center"
        />
        <el-table-column
          :label="td('dpp.datasource.number')"
          prop="id"
          width="80"
        >
          <template #default="scope">
            {{ scope.row.id || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.datasource.projectName')"
          align="center"
          prop="name"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>

        <el-table-column
          :label="td('dpp.datasource.manager')"
          align="center"
          prop="managerId"
        >
          <template #default="scope">
            {{ scope.row.nickName || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('dpp.datasource.managerPhone')"
          align="center"
          prop="managerPhone"
        >
          <template #default="scope">
            {{ scope.row.managerPhone || "-" }}
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="totalProject > 0"
        :total="totalProject"
        v-model:page="queryParamsProject.pageNum"
        v-model:limit="queryParamsProject.pageSize"
        @pagination="getListProject"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openProject = false">{{
            td("common.button.cancel")
          }}</el-button>
          <el-button type="primary" size="mini" @click="submitFormProject">{{
            td("common.button.confirm")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DppDataSource">
import { ref, computed, watch } from "vue";
import {
  listDaDatasource,
  getDaDatasource,
  clientsTest,
  delDaDatasource,
  removeDppOrDa,
  addDaDatasource,
  updateDaDatasource,
  listDaDatasourceByProjectCode,
  editDatasourceStatus,
  noDppAdd,
} from "@/api/da/dataSource/dataSource";
import { encrypt, isDecrypted } from "@/utils/aesEncrypt";
import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";
import { config } from "ace-builds";
import useDefaultLang from "@/composables/useDefaultLang";

const userStore = useUserStore();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { datasource_type, sys_disable } = proxy.useDict(
  "datasource_type",
  "sys_disable"
);
const daDatasourceList = ref([]);

// 列显隐状态
const columnVisible = ref({
  1: true,
  2: true,
  3: true,
  4: true,
  5: true,
  6: true,
  7: true,
  8: true,
  9: true,
});

// 列配置（使用 ref，确保 RightToolbar 修改能持久化）
const columns = ref([
  {
    key: 1,
    label: td("dpp.datasource.number"),
    visible: columnVisible.value[1],
  },
  {
    key: 2,
    label: td("dpp.datasource.datasourceName"),
    visible: columnVisible.value[2],
  },
  {
    key: 3,
    label: td("dpp.datasource.description"),
    visible: columnVisible.value[3],
  },
  {
    key: 4,
    label: td("dpp.datasource.datasourceType"),
    visible: columnVisible.value[4],
  },
  {
    key: 5,
    label: td("dpp.datasource.createBy"),
    visible: columnVisible.value[5],
  },
  {
    key: 6,
    label: td("dpp.datasource.createTime"),
    visible: columnVisible.value[6],
  },
  {
    key: 7,
    label: td("dpp.datasource.status"),
    visible: columnVisible.value[7],
  },
  {
    key: 8,
    label: td("dpp.datasource.remark"),
    visible: columnVisible.value[8],
  },
  {
    key: 9,
    label: td("dpp.datasource.operation"),
    visible: columnVisible.value[9],
  },
]);

// 监听 RightToolbar 对 columns 的修改，同步到 columnVisible
watch(
  columns,
  (newColumns) => {
    newColumns.forEach((col) => {
      if (columnVisible.value[col.key] !== undefined) {
        columnVisible.value[col.key] = col.visible;
      }
    });
  },
  { deep: true }
);

const getColumnVisibility = (key) => {
  return columnVisible.value[key] !== undefined
    ? columnVisible.value[key]
    : true;
};

const open = ref(false);
const openProject = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const projectOptions = ref([]);
const projectList = ref([]);
const totalProject = ref(0);
const projectTableRef = ref(null);
const loadingProject = ref(false);
const projectIdAndCodeList = ref([]);
const route = useRoute();
let type = route.query.type || null;

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/da/daDatasource/importData",
});

const data = reactive({
  form: {
    projectNameListStr: "-",
    projectNameList: [],
    projectIdList: [],
    projectList: [],
  },
  queryParamsProject: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    managerId: null,
    datasourceId: null,
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasourceName: null,
    datasourceType: null,
    datasourceConfig: null,
    ip: null,
    port: null,
    listCount: null,
    syncCount: null,
    dataSize: null,
    description: null,
    createTime: null,
  },
  rules: {
    datasourceName: [
      {
        required: true,
        message: td("dpp.datasource.datasourceNameRequired"),
        trigger: "blur",
      },
    ],
    datasourceType: [
      {
        required: true,
        message: td("dpp.datasource.datasourceTypeRequired"),
        trigger: "change",
      },
    ],
    datasourceConfig: [
      {
        required: true,
        message: td("dpp.datasource.datasourceConfigRequired"),
        trigger: "blur",
      },
    ],
    ip: [
      {
        required: true,
        message: td("dpp.datasource.ipRequired"),
        trigger: "blur",
      },
      {
        pattern: /^[^\u4e00-\u9fa5]+$/,
        message: td("dpp.datasource.ipNoChinese"),
        trigger: "blur",
      },
    ],
    port: [
      {
        required: true,
        message: td("dpp.datasource.portRequired"),
        trigger: "blur",
      },
      {
        pattern: /^\d{1,9}$/,
        message: td("dpp.datasource.portPattern"),
        trigger: "blur",
      },
    ],
    username: [
      {
        required: true,
        message: td("dpp.datasource.usernameRequired"),
        trigger: "blur",
      },
    ],
    password: [
      {
        required: true,
        message: td("dpp.datasource.passwordRequired"),
        trigger: "blur",
      },
    ],
    keyId: [
      {
        required: true,
        message: td("dpp.datasource.keyIdRequired"),
        trigger: "blur",
      },
    ],
    keySecret: [
      {
        required: true,
        message: td("dpp.datasource.keySecretRequired"),
        trigger: "blur",
      },
    ],
    bucket: [
      {
        required: true,
        message: td("dpp.datasource.bucketRequired"),
        trigger: "blur",
      },
    ],
    endpoint: [
      {
        required: true,
        message: td("dpp.datasource.endpointRequired"),
        trigger: "blur",
      },
    ],
    dbname: [
      {
        required: true,
        message: td("dpp.datasource.dbnameRequired"),
        trigger: "blur",
      },
    ],
    sid: [
      {
        required: true,
        message: td("dpp.datasource.sidRequired"),
        trigger: "blur",
      },
    ],
    description: [
      {
        required: true,
        message: td("dpp.datasource.descriptionRequired"),
        trigger: "blur",
      },
    ],
    config: [
      {
        trigger: "blur",
        validator: (rule, value, callback) => {
          if (value === null || value === undefined || value === "") {
            callback();
            return;
          }
          var flag = false;
          if (typeof value === "string") {
            try {
              const obj = JSON.parse(value);
              if (typeof obj === "object" && obj) {
                flag = true;
              }
            } catch (e) {}
          }
          if (flag) {
            callback();
          } else {
            callback(td("dpp.datasource.jsonFormatError"));
          }
        },
      },
    ],
  },
});

const { queryParams, form, rules, queryParamsProject } = toRefs(data);
const selectable = (row) => !row.dppAssigned;
// 监听 id 变化
watch(
  () => userStore.projectCode,
  (newCode) => {
    getList();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function getProjectOptions() {
  deptUserTree().then((response) => {
    projectOptions.value = response.data;
  });
}

//数据连接类型change事件
function handleDatasourceChange(type) {
  if (type == "Hive") {
    rules.value.password[0].required = false;
  }
  if (type != "Hive") {
    rules.value.password[0].required = true;
  }
}

function getListProject() {
  openProject.value = true;
  loadingProject.value = true;
  noDppAdd(queryParamsProject.value).then((response) => {
    projectList.value = response.data.rows;
    totalProject.value = response.data.total;
    loadingProject.value = false;

    // 在表格加载完成后，设置之前选中的项目
    nextTick(() => {
      projectList.value.forEach((project) => {
        form.value.projectList.forEach((item) => {
          if (item.projectId === project.id) {
            proxy.$refs.projectTableRef.toggleRowSelection(project, true);
          }
        });
      });
    });
  });
}

function handleSelectionChangeProject(selection) {
  projectIdAndCodeList.value = [];
  for (let i = 0; i < selection.length; i++) {
    const element = selection[i];
    let project = {
      projectId: element.id,
      projectCode: element.code,
    };
    projectIdAndCodeList.value.push(project);
  }

  form.value.projectNameList = selection.map((item) => item.name);
}

function submitFormProject() {
  openProject.value = false;
  form.value.projectList = projectIdAndCodeList.value;
}

function handleQueryProject() {
  queryParamsProject.value.pageNum = 1;
  getListProject();
}

function resetQueryProject() {
  queryParamsProject.value.pageNum = 1;
  queryParamsProject.value.pageSize = 10;
  queryParamsProject.value.name = null;
  queryParamsProject.value.managerId = null;
  getListProject();
}

/** 查询数据源列表 */
function getList() {
  loading.value = true;
  if (type == 1) {
    queryParams.value.projectId = userStore.projectId;
    queryParams.value.projectCode = userStore.projectCode;
    listDaDatasourceByProjectCode(queryParams.value).then((response) => {
      daDatasourceList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  } else {
    listDaDatasource(queryParams.value).then((response) => {
      daDatasourceList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  }
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    projectNameList: [],
    projectIdList: [],
    projectList: [],
    datasourceName: null,
    datasourceType: null,
    datasourceConfig: null,
    ip: null,
    port: null,
    listCount: null,
    syncCount: null,
    dataSize: null,
    description: null,
    validFlag: false,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daDatasourceRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  if (type == 1) {
    form.value.isDaOrDpp = true;
    form.value.projectList = [
      {
        projectId: userStore.projectId,
        projectCode: userStore.projectCode,
        dppAssigned: true,
      },
    ];
  } else {
    form.value.isDaOrDpp = false;
    form.value.projectList = [];
  }
  open.value = true;
  title.value = td("dpp.datasource.addDatasource");
}

/** 修改按钮操作 */
let old_password;

function handleUpdate(row, type) {
  reset();
  const _id = row.id || ids.value;
  loading.value = true;
  getDaDatasource(_id)
    .then((response) => {
      form.value = response.data;
      form.value.projectIdList = form.value.projectList.map(
        (item) => item.projectId
      );
      form.value.projectNameList = form.value.projectList.map(
        (item) => item.projectName
      );

      // 拆解 datasourceConfig
      if (form.value.datasourceConfig) {
        const config = JSON.parse(form.value.datasourceConfig);
        form.value.username = config.username;
        form.value.password = config.password;
        form.value.dbname = config.dbname;
        form.value.sid = config.sid;
        if (config.keyId) form.value.keyId = config.keyId;
        if (config.keySecret) form.value.keySecret = config.keySecret;
        if (config.bucket) form.value.bucket = config.bucket;
        if (config.endpoint) form.value.endpoint = config.endpoint;
        if (config.domain) form.value.domain = config.domain;
        if (config.config) form.value.config = config.config;
      }
      form.value.projectListOld = form.value.projectIdList;
      queryParamsProject.value.datasourceId = form.value.id;
      open.value = true;
      if (type == 3) {
        title.value = td("dpp.datasource.datasourceDetail");
      } else {
        old_password = form.value.password;
        title.value = td("dpp.datasource.editDatasource");
      }
    })
    .finally(() => {
      loading.value = false; // 不管成功失败都结束loading
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaDatasource(_id).then((response) => {
    form.value = response.data;
    form.value.projectNameListStr = form.value.projectList
      .map((item) => item.projectName)
      .join(", ");
    if (form.value.datasourceConfig) {
      const config = JSON.parse(form.value.datasourceConfig);
      form.value.username = config.username;
      form.value.password = config.password;
      form.value.dbname = config.dbname;
      form.value.sid = config.sid;
      if (config.keyId) {
        form.value.keyId = config.keyId;
      }
      if (config.keySecret) {
        form.value.keySecret = config.keySecret;
      }
      if (config.bucket) {
        form.value.bucket = config.bucket;
      }
      if (config.endpoint) {
        form.value.endpoint = config.endpoint;
      }
      if (config.domain) {
        form.value.domain = config.domain;
      }
    }
    openDetail.value = true;
    title.value = td("dpp.datasource.datasourceDetail");
  });
}

/** 详情按钮操作 */
function handleTestConnection(row) {
  loading.value = true; // 开始加载
  reset();
  const _id = row.id || ids.value;
  clientsTest(_id)
    .then((response) => {
      console.log(response);
      proxy.$modal.msgSuccess(response.msg);
    })
    .finally(() => {
      loading.value = false; // 结束加载
    });
}
const btnLoading = ref(false);
/** 提交按钮 */
function submitForm() {
  proxy.$refs["daDatasourceRef"].validate((valid) => {
    if (valid) {
      btnLoading.value = true;
      if (form.value.id != null) {
        if (
          old_password !== form.value.password ||
          !isDecrypted(form.value.password)
        ) {
          form.value.password = encrypt(form.value.password);
        }
        form.value.datasourceConfig = JSON.stringify({
          username: form.value.username,
          password: form.value.password,
          dbname: form.value.dbname,
          sid: form.value.sid,
          keyId: form.value.keyId,
          keySecret: form.value.keySecret,
          bucket: form.value.bucket,
          endpoint: form.value.endpoint,
          domain: form.value.domain,
          config: form.value.config,
        });

        let projectListOld = [];
        form.value.projectListOld.forEach((item) => {
          if (!form.value.projectList.includes(item)) {
            projectListOld.push(item);
          }
        });
        form.value.projectListOld = projectListOld;
        updateDaDatasource(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("dpp.datasource.editSuccess"));
            open.value = false;
            getList();
          })
          .finally(() => {
            btnLoading.value = false;
          });
      } else {
        form.value.datasourceConfig = JSON.stringify({
          username: form.value.username,
          password: encrypt(form.value.password),
          dbname: form.value.dbname,
          sid: form.value.sid,
          keyId: form.value.keyId,
          keySecret: form.value.keySecret,
          bucket: form.value.bucket,
          endpoint: form.value.endpoint,
          domain: form.value.domain,
        });
        addDaDatasource(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("dpp.datasource.addSuccess"));
            open.value = false;
            getList();
          })
          .finally(() => {
            btnLoading.value = false;
          });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td("dpp.datasource.confirmDelete", { ids: _ids }))
    .then(function () {
      return removeDppOrDa(_ids, type);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("dpp.datasource.deleteSuccess"));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "da/daDatasource/export",
    {
      ...queryParams.value,
    },
    `daDatasource_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = td("dpp.datasource.datasourceImport");
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `daDatasource_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td("dpp.datasource.importResult"),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text =
    row.validFlag === true
      ? td("common.texts.enable")
      : td("common.texts.disable");
  const status = row.validFlag === true ? 1 : 0;
  proxy.$modal
    .confirm(
      td("common.message.confirmChangeStatus", {
        name: row.datasourceName,
        status: text,
      })
    )
    .then(function () {
      editDatasourceStatus(row.id, status).then((response) => {
        proxy.$modal.msgSuccess(td("common.message.operationSuccess"));
        getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
getProjectOptions();
</script>
