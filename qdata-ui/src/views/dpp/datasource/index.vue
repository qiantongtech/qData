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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="da/daDatasource.list" />

    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <!-- 搜索栏插槽 -->
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQuery"
          @reset="resetQuery"
          :tableRef="tableRef"
        />
      </template>

      <!-- 数据操作按钮插槽 (如：新增、导出) -->
      <template #actions-data>
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['da:dataSource:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td("common.button.add") }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <!-- 表格组件 -->
      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <!-- 状态切换插槽 -->
        <template #status="{ row }">
          <el-switch
            v-model="row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :loading="statusLoadingMap[row.id] === true"
            @change="handleStatusChange(row)"
          />
        </template>

        <!-- 操作列插槽 -->
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Connection"
            :loading="testConnectionLoadingMap[row.id] === true"
            @click="handleTestConnection(row)"
            v-hasPermi="['da:dataSource:edit']"
          >
            {{ td("dpp.datasource.testConnection") }}
          </el-button>

          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['da:dataSource:edit']"
          >
            {{ td("common.button.details") }}
          </el-button>

          <el-popover placement="bottom" :width="100" trigger="click">
            <template #reference>
              <el-button
                link
                type="primary"
                :disabled="row.isAdminAddTo == false"
                icon="ArrowDown"
              >
                <el-tooltip
                  class="box-item"
                  effect="dark"
                  :content="td('dpp.datasource.noPermission')"
                  placement="top"
                  :disabled="row.isAdminAddTo != false"
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
                @click="handleUpdate(row)"
                v-hasPermi="['da:dataSource:edit']"
              >
                {{ td("common.button.update") }}
              </el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['da:dataSource:remove']"
              >
                {{ td("common.button.delete") }}
              </el-button>
            </div>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify the data source dialogue -->
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
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.datasource.datasourceName')"
              prop="datasourceName"
             :label-position="labelPosition">
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
             :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.ip')" prop="ip" :label-position="labelPosition">
              <el-input
                v-model="form.ip"
                :placeholder="td('dpp.datasource.inputIp')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.port')" prop="port" :label-position="labelPosition">
              <el-input
                v-model="form.port"
                :placeholder="td('dpp.datasource.inputPort')"
                @input="form.port = $event.replace(/\D/g, '')"
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
             :label-position="labelPosition">
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
             :label-position="labelPosition">
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
              <el-form-item :label="td('dpp.datasource.keyId')" prop="keyId" :label-position="labelPosition">
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
               :label-position="labelPosition">
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
              <el-form-item :label="td('dpp.datasource.bucket')" prop="bucket" :label-position="labelPosition">
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
               :label-position="labelPosition">
                <el-input
                  v-model="form.endpoint"
                  :placeholder="td('dpp.datasource.inputEndpoint')"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="td('dpp.datasource.domain')" prop="domain" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.dbname')" prop="dbname" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.sid')" prop="sid" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.config')" prop="config" :label-position="labelPosition">
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
             :label-position="labelPosition">
              <el-input
                type="textarea"
                :min-height="192"
                v-model="form.description"
                :placeholder="td('dpp.datasource.inputDescription')"
                 maxlength="256字符"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="type == 0">
          <el-col :span="24">
            <el-form-item
              :label="td('dpp.datasource.projectNameList')"
              prop="projectNameList"
             :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.status')" prop="validFlag" :label-position="labelPosition">
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

    <!-- Details -->
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
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.datasourceName')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.datasourceName || "-" }}
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.datasourceType')" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.ip')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.ip || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.port')" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.username')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.username || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.password')" :label-position="labelPosition">
              <div class="form-readonly">***********</div>
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="form.datasourceType === 'OSS-ALIYUN'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.keyId')" :label-position="labelPosition">
                <div class="form-readonly">
                  {{ form.keyId || "-" }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.keySecret')" :label-position="labelPosition">
                <div class="form-readonly">
                  {{ form.keySecret || "-" }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.bucket')" :label-position="labelPosition">
                <div class="form-readonly">
                  {{ form.bucket || "-" }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="td('dpp.datasource.endpoint')" :label-position="labelPosition">
                <div class="form-readonly">
                  {{ form.endpoint || "-" }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="td('dpp.datasource.domain')" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.dbname')" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.sid')" :label-position="labelPosition">
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
            <el-form-item :label="td('dpp.datasource.config')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.config || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.description')" :label-position="labelPosition">
              <div class="form-readonly textarea">
                {{ form.description || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="type == 0">
          <el-col :span="24">
            <el-form-item :label="td('dpp.datasource.projectNameList')" :label-position="labelPosition">
              <div class="form-readonly">
                {{ form.projectNameListStr || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.datasource.status')" :label-position="labelPosition">
              <dict-tag
                :options="sys_disable"
                :value="form.validFlag ? '1' : '0'"
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
      <!--User Data-->
      <el-form
        class="btn-style"
        :model="queryParamsProject"
        ref="queryRef"
        :inline="true"

       :label-position="labelPosition">
        <el-form-item :label="td('dpp.datasource.projectName')" prop="name" :label-position="labelPosition">
          <el-input
            class="el-form-input-width"
            v-model="queryParamsProject.name"
            :placeholder="td('dpp.datasource.inputProjectName')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('dpp.datasource.manager')" prop="managerId" :label-position="labelPosition">
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
import { ref, computed, watch, onActivated, onBeforeUnmount, reactive, toRefs, getCurrentInstance, nextTick } from "vue";
import { useRoute, useRouter, onBeforeRouteLeave } from "vue-router";
import { ElMessageBox } from "element-plus";
import {
  listDaDatasource,
  getDaDatasource,
  clientsTest,
  delDaDatasource,
  removeDppOrDa,
  addDaDatasource,
  testDatasourceConnection,
  updateDaDatasource,
  listDaDatasourceByProjectCode,
  editDatasourceStatus,
  noDppAdd,
} from "@/api/da/dataSource/dataSource";
import { encrypt, isDecrypted } from "@/utils/aesEncrypt";
import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";
import useDefaultLang from "@/composables/useDefaultLang";

const userStore = useUserStore();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { datasource_type, sys_disable } = proxy.useDict(
  "datasource_type",
  "sys_disable"
);

const tableRef = ref(null);
const DeptTreeRef = ref(null);

const searchStore = reactive({
  items: [
    {
      label: td("dpp.datasource.datasourceName"),
      prop: "datasourceName",
      component: {
        is: "input",
        placeholder: td("dpp.datasource.inputDatasourceName"),
      },
    },
    {
      label: td("dpp.datasource.datasourceType"),
      prop: "datasourceType",
      component: {
        is: "select",
        options: datasource_type,
        placeholder: td("dpp.datasource.selectDatasourceType"),
      },
    },
  ],
});

const tableStore = reactive({
  config: {
    initResquest: true,
  },
  columns: [
    {
      label: td("dpp.datasource.number"),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td("dpp.datasource.datasourceName"),
      prop: "datasourceName",
      width: 260,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("dpp.datasource.description"),
      prop: "description",
      width: 256,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("dpp.datasource.datasourceType"),
      prop: "datasourceType",
      width: 160,
      align: "left",
      dict: "datasource_type",
    },
    {
      label: td("dpp.datasource.createBy"),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("dpp.datasource.createTime"),
      prop: "createTime",
      width: 160,
      align: "left",
      sortable: true,
      date: true,
    },
    {
      label: td("dpp.datasource.status"),
      prop: "validFlag",
      width: 100,
      align: "left",
      slot: "status",
    },
    {
      label: td("dpp.datasource.operation"),
      slot: "action",
      width: 280,
      align: "center",
      fixed: "right",
    },
  ],
  func: (params) => {
    if (type == 1) {
      params.projectId = userStore.projectId;
      params.projectCode = userStore.projectCode;
      return listDaDatasourceByProjectCode(params);
    }
    return listDaDatasource(params);
  },
  params: {
    pageNum: 1,
    pageSize: 10,
    datasourceName: null,
    datasourceType: null,
    orderByColumn: "createTime",
    isAsc: "descending",
  },
});

const open = ref(false);
const openProject = ref(false);
const openDetail = ref(false);
const title = ref("");
const router = useRouter();
const projectOptions = ref([]);
const projectList = ref([]);
const totalProject = ref(0);
const projectTableRef = ref(null);
const loadingProject = ref(false);
const projectIdAndCodeList = ref([]);
const route = useRoute();
let type = route.query.type || null;
let isDatasourcePageActive = true;

onActivated(() => {
  isDatasourcePageActive = true;
});

onBeforeRouteLeave(() => {
  isDatasourcePageActive = false;
});

onBeforeUnmount(() => {
  isDatasourcePageActive = false;
});

/*** User Import Parameters */
const upload = reactive({
  // Whether to show the eject layer (user import)
  open: false,
  // Popup Layer Title (User Import)
  title: "",
  // Disable Upload
  isUploading: false,
  // Update existing user data
  updateSupport: 0,
  // Set the head of the request for upload
  headers: { Authorization: "Bearer " + getToken() },
  // Uploading Address
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
        pattern: /^(?:((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)|(?![\d.]+$)(?=.{1,253}$)[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)$/,
        message: td('da.datasource.ipOrHostnameInvalid'),
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

// Watch id changes
watch(
  () => userStore.projectCode,
  (newCode) => {
    tableRef.value?.refresh();
  },
  { immediate: false }
);

function getProjectOptions() {
  deptUserTree().then((response) => {
    projectOptions.value = response.data;
  });
}

//Data Connection Type Change Event
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

    // Set the selected item before the table is loaded
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

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// Form Reset
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
    skipConnectionValidation: false,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
  };
  proxy.resetForm("daDatasourceRef");
}

/** Search button operation */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** Reset button operations */
function resetQuery() {
  DeptTreeRef.value?.resetTree?.();
  tableStore.params.datasourceName = null;
  tableStore.params.datasourceType = null;
  handleQuery();
}

/** Add button operation */
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

/** Modify button operation */
let old_password;

function handleUpdate(row, type) {
  reset();
  const _id = row.id;
  getDaDatasource(_id)
    .then((response) => {
      form.value = response.data;
      form.value.projectIdList = form.value.projectList.map(
        (item) => item.projectId
      );
      form.value.projectNameList = form.value.projectList.map(
        (item) => item.projectName
      );

      // Disassembly
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
    });
}

/** Details button operation */
function handleDetail(row) {
  reset();
  const _id = row.id;
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

/** Details button operation */
function handleTestConnection(row) {
  const _id = row.id;
  testConnectionLoadingMap.value[_id] = true;
  clientsTest(_id, { hideErrorMessage: true })
    .then((response) => {
      console.log(response);
      proxy.$modal.msgSuccess(response.msg);
    })
    .catch((error) => {
      proxy.$modal.msgWarning(error.message);
    })
    .finally(() => {
      testConnectionLoadingMap.value[_id] = false;
    });
}
const btnLoading = ref(false);
const testConnectionLoadingMap = ref({});
const statusLoadingMap = ref({});
/** Submit button */
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
            tableRef.value.refresh();
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
        testDatasourceConnection(form.value)
          .then((response) => response.data === true)
          .catch(() => false)
          .then((isConnected) => {
            if (isConnected) {
              form.value.skipConnectionValidation = false;
              return true;
            }
            return proxy.$modal
              .confirm(
                td(
                  "dpp.datasource.connectionValidationFailedConfirm",
                  "数据源校验未通过，确认继续新增吗？若点击确认，该数据源启用状态将自动置为禁用"
                )
              )
              .then(() => {
                form.value.skipConnectionValidation = true;
                form.value.validFlag = false;
              });
          })
          .then(() => addDaDatasource(form.value))
          .then((response) => {
            proxy.$modal.msgSuccess(td("dpp.datasource.addSuccess"));
            open.value = false;
            tableRef.value.refresh();
          })
          .finally(() => {
            btnLoading.value = false;
          });
      }
    }
  });
}

/** Remove button operation */
function handleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(td("dpp.datasource.confirmDelete", { ids: _ids }))
    .then(function () {
      return removeDppOrDa(_ids, type);
    })
    .then(() => {
      tableRef.value.refresh();
      proxy.$modal.msgSuccess(td("dpp.datasource.deleteSuccess"));
    })
    .catch(() => {});
}

/** Export button operation */
function handleExport() {
  proxy.download(
    "da/daDatasource/export",
    {
      ...tableStore.params,
    },
    `daDatasource_${new Date().getTime()}.xlsx`
  );
}

/** - - - Import-related operations - - - - **/
/** Import button operation */
function handleImport() {
  upload.title = td("dpp.datasource.datasourceImport");
  upload.open = true;
}

/** Download Template Operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `daDatasource_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit Upload File */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**Uploading and processing files */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload successfully processed */
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
  tableRef.value.refresh();
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

/** Change Enabled Value */
function handleStatusChange(row) {
  const isEnabling = row.validFlag === true;
  const text =
    isEnabling
        ? td("common.texts.enable")
        : td("common.texts.disable");
  const status = isEnabling ? 1 : 0;
  proxy.$modal
      .confirm(
          td("da.datasource.confirmStatusChange", '', {
            text,
            name: row.datasourceName,
          })
      )
    .then(function () {
      statusLoadingMap.value[row.id] = true;
      return editDatasourceStatus(row.id, status, {
        hideErrorMessage: isEnabling,
      })
        .then(() => {
          if (isDatasourcePageActive) {
            proxy.$modal.msgSuccess(td("da.datasource.statusSuccess", '', { text: text }));
          }
        })
        .catch((error) => {
          if (!isEnabling) {
            return Promise.reject(error);
          }
          if (!isDatasourcePageActive) {
            return Promise.reject(error);
          }
          return ElMessageBox.alert(
            "数据连接启动失败,请查看数据库连接信息",
            "数据连接",
            {
              type: "warning",
              confirmButtonText: td("common.button.confirm"),
              closeOnClickModal: false,
              closeOnPressEscape: false,
              showClose: false,
            }
          ).then(() => Promise.reject(error));
        });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    })
    .finally(function () {
      statusLoadingMap.value[row.id] = false;
      if (isDatasourcePageActive) {
        tableRef.value.refresh();
      }
    });
}

getProjectOptions();
</script>
