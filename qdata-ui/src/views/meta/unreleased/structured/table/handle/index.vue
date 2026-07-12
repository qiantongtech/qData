<template>
  <div class="app-container">
    <el-form :model="store.form" :rules="rules" ref="formRef" label-width="110">
      <div class="module-head">
        {{ td("meta.unreleased.structured.table.detail.basicInfo") }}
      </div>
      <div class="module-body infotop column-form">
        <el-form-item
          :label="td('meta.released.structured.table.dbName')"
          prop="dbId"
        >
          <el-select
            v-model="store.form.dbId"
            :disabled="!!route.query.id"
            :placeholder="
              td('meta.unreleased.structured.table.handle.selectDbName')
            "
            @change="handleMetaDBChange"
          >
            <el-option
              v-for="item in store.metaDatabases"
              :key="item.id"
              :label="item.dbName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="td('meta.released.structured.table.sourceSystem')"
          prop="sourceSystemName"
        >
          <el-input
            v-model="store.form.sourceSystemName"
            :disabled="!!route.query.id"
            :placeholder="
              td('meta.unreleased.structured.table.handle.autoGetSourceSystem')
            "
          />
        </el-form-item>
        <el-form-item
          :label="td('meta.released.structured.table.tableName')"
          prop="tableName"
        >
          <el-input
            clearable
            :disabled="!!route.query.id"
            v-model="store.form.tableName"
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputTableName')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.released.structured.table.tableComment')"
          prop="tableComment"
        >
          <el-input
            clearable
            :disabled="!!route.query.id"
            v-model="store.form.tableComment"
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputTableComment')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.belongingLayer')"
        >
          <el-select
            clearable
            v-model="store.form.belongingLayer"
            disabled
            :placeholder="
              td('meta.unreleased.structured.table.handle.selectBelongingLayer')
            "
          >
            <el-option
              v-for="dict in toValue(dicts.meta_dw_layers)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.belongingSystem')"
          prop="belongingSystem"
        >
          <el-input
            clearable
            v-model="store.form.belongingSystem"
            disabled
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputBelongingSystem')
            "
          />
        </el-form-item>

        <!-- <el-form-item label="安全等级" prop="safetyLevelId">
          <el-select
            clearable
            v-model="store.form.safetyLevelId"
            placeholder="请选择安全等级"
          >
            <el-option
              v-for="item in store.sensitiveLevels"
              :key="item.id"
              :label="item.sensitiveLevel"
              :value="item.id"
            />
          </el-select>
        </el-form-item> -->

        <!-- <el-form-item :label="t('common.texts.status')" prop="status">
          <el-radio-group v-model="store.form.status">
            <el-radio
              v-for="dict in toValue(dicts.meta_task_status)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item> -->

        <el-form-item :label="td('common.texts.remark')" class="row-full">
          <el-input
            v-model="store.form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>
      </div>

      <div class="module-head">
        {{ td("meta.unreleased.structured.table.detail.techInfo") }}
      </div>
      <div class="module-body infotop column-form">
        <el-form-item
          :label="td('meta.unreleased.structured.table.handle.datasourceName')"
          prop="datasourceId"
        >
          <el-select
            clearable
            v-model="store.form.datasourceId"
            :placeholder="
              td('meta.unreleased.structured.table.handle.selectDatasourceName')
            "
            @change="handleDatasourceChange"
            disabled
          >
            <el-option
              v-for="item in store.datasources"
              :key="item.id"
              :label="item.datasourceName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          :label="td('meta.released.structured.database.dbType')"
          prop="dbType"
        >
          <el-input
            clearable
            v-model="store.form.dbType"
            disabled
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputDbType')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.ip')"
          prop="ip"
        >
          <el-input
            clearable
            v-model="store.form.ip"
            disabled
            :placeholder="td('meta.unreleased.structured.table.handle.inputIp')"
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.port')"
          prop="port"
        >
          <el-input
            clearable
            v-model="store.form.port"
            disabled
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputPort')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.username')"
          prop="username"
        >
          <el-input
            clearable
            v-model="store.form.username"
            disabled
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputUsername')
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.storageType')"
          prop="storageType"
        >
          <el-input
            clearable
            v-model="store.form.storageType"
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputStorageType')
            "
          />
        </el-form-item>

        <!-- <el-form-item label="存储大小" prop="storageSize">
          <el-input-number
            :min="0"
            v-model="store.form.storageSize"
            placeholder="请输入存储大小"
            :controls="false"
            class="number-input"
          />
        </el-form-item> -->

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.techLeader')"
        >
          <el-tree-select
            clearable
            filterable
            v-model="store.form.techLeader"
            :data="store.userList"
            :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
            value-key="userId"
            :placeholder="
              td('meta.unreleased.structured.table.handle.selectTechLeader')
            "
            check-strictly
            @change="handleUserChange($event, 'techLeaderPhone')"
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.techLeaderPhone')"
        >
          <el-input
            clearable
            v-model="store.form.techLeaderPhone"
            :placeholder="
              td('meta.unreleased.structured.table.handle.inputTechLeaderPhone')
            "
          />
        </el-form-item>
      </div>

      <div class="module-head">
        {{ td("meta.unreleased.structured.table.detail.businessInfo") }}
      </div>
      <div class="module-body infotop column-form">
        <el-form-item
          :label="td('meta.unreleased.structured.table.handle.businessLeader')"
        >
          <el-tree-select
            clearable
            filterable
            v-model="store.form.businessLeader"
            :data="store.userList"
            :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
            value-key="userId"
            :placeholder="
              td('meta.unreleased.structured.table.handle.selectBusinessLeader')
            "
            check-strictly
            @change="handleUserChange($event, 'businessLeaderPhone')"
          />
        </el-form-item>

        <el-form-item
          :label="
            td('meta.unreleased.structured.table.handle.businessLeaderPhone')
          "
        >
          <el-input
            clearable
            v-model="store.form.businessLeaderPhone"
            :placeholder="
              td(
                'meta.unreleased.structured.table.handle.inputBusinessLeaderPhone'
              )
            "
          />
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.isMasterTable')"
          prop="masterFlag"
        >
          <el-radio-group v-model="store.form.masterFlag">
            <el-radio
              v-for="dict in toValue(dicts.table_yes_no)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          :label="td('meta.unreleased.structured.table.handle.isTempTable')"
          prop="tempFlag"
        >
          <el-radio-group v-model="store.form.tempFlag">
            <el-radio
              v-for="dict in toValue(dicts.table_yes_no)"
              :key="dict.value"
              :value="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="td('common.texts.description')" class="row-full">
          <el-input
            v-model="store.form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>

        <!-- <el-form-item
          label="变更说明"
          prop="updateMsg"
          class="row-full"
          v-if="store.form.id"
        >
          <el-input
            v-model="store.form.updateMsg"
            type="textarea"
            placeholder="请输入变更说明"
            :min-height="192"
            show-word-limit
            maxlength="500"
          />
        </el-form-item> -->
      </div>
    </el-form>

    <div class="button-style">
      <el-button :loading="confirmLoading" @click="handleDraftClick">{{
        td("meta.unreleased.structured.table.handle.saveDraft")
      }}</el-button>
      <el-button
        type="primary"
        plain
        class="fh_btn"
        @mousedown="(e) => e.preventDefault()"
        @click="router.back"
      >
        <svg-icon iconClass="fhs" />{{
          td("meta.unreleased.structured.table.handle.backToList")
        }}
      </el-button>
      <el-button type="primary" :loading="confirmLoading" @click="handleConfirmClick">
        {{ td("meta.unreleased.structured.table.handle.confirmAndExit") }}
      </el-button>
    </div>
  </div>
</template>

<script setup name="TableHandle">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, getCurrentInstance, toValue, ref } from "vue";
import { listDb, getDb } from "@/api/mc/unreleased/db";
import { deptUserTree } from "@/api/system/system/user.js";
import { listDaDatasource } from "@/api/mc/dataSource/dataSource";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";
import {
  getTable,
  updateTable,
  addTable,
  draftTable,
} from "@/api/mc/unreleased/table";
import { useRoute, useRouter } from "vue-router";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const confirmLoading = ref(false);
const DEFAULT_FORM = {
  status: "0",
  masterFlag: "1",
  tempFlag: "0",
};

const rules = {
  dbId: [
    { required: true, message: td('meta.unreleased.structured.table.handle.selectDbName'), trigger: ["blur", "change"] },
  ],
  sourceSystemName: [
    { required: true, message: td('meta.unreleased.structured.table.handle.sourceSystemRequired'), trigger: "change" },
  ],
  datasourceId: [
    { required: true, message: td('meta.unreleased.structured.table.handle.selectDatasourceName'), trigger: "change" },
  ],
  tableName: [
    { required: true, message: td('meta.unreleased.structured.table.handle.inputTableName'), trigger: ["blur", "change"] },
  ],
  dbType: [
    {
      required: true,
      message: td('meta.unreleased.structured.table.handle.inputDbType'),
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    { required: true, message: td('meta.unreleased.structured.table.handle.inputIp'), trigger: ["blur", "change"] },
  ],
  port: [
    {
      required: true,
      message: td('meta.unreleased.structured.table.handle.inputPort'),
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: td('meta.unreleased.structured.table.handle.inputUsername'),
      trigger: ["blur", "change"],
    },
  ],
  updateMsg: [
    { required: true, message: td('meta.unreleased.structured.table.handle.inputUpdateMsg'), trigger: ["change", "blur"] },
  ],
};

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "meta_task_status",
  "meta_dw_layers",
  "sys_yes_no",
  "table_yes_no"
);

const router = useRouter();
const route = useRoute();
const formRef = ref();
const store = reactive({
  form: { ...DEFAULT_FORM },
  metaDatabases: [],
  sensitiveLevels: [],
  userList: [],
  loading: false,
});

// 获取库元素列表
function getMetaDatabases() {
  return listDb({ pageSize: 1000 }).then((res) => {
    store.metaDatabases = res.data.rows;
    if (route.query.dbId) {
      store.form.dbId = route.query.dbId - 0;
      handleMetaDBChange(route.query.dbId - 0);
    }
    return res;
  });
}

// 获取安全等级
function getSensitiveLevel() {
  listDgSensitiveLevel({ pageSize: 1000 }).then((res) => {
    store.sensitiveLevels = res.data.rows;
  });
}

// 获取数据源列表
function getDatasources() {
  return listDaDatasource().then((res) => {
    res.data.rows.forEach((item) => {
      item.datasourceConfig = item.datasourceConfig
        ? JSON.parse(item.datasourceConfig)
        : {};
    });
    store.datasources = res.data.rows;
    return res.data.rows;
  });
}

// 获取用户列表
function getUserList() {
  return deptUserTree().then((res) => {
    store.userList = res.data;
    return res.data;
  });
}

// 切换数据源
function handleDatasourceChange(id) {
  const data = store.datasources?.find((item) => item.id === id);
  store.form.ip = data.ip;
  store.form.port = data.port;
  store.form.username = data.datasourceConfig?.username;
  store.form.dbType = data.datasourceType;
}

// 切换用户
function handleUserChange(id, key) {
  const data = store.userList.find((item) => item.userId === id);
  store.form[key] = data.phonenumber;
}

// 切换库元数据
function handleMetaDBChange(id) {
  getDb(id).then((res) => {
    store.form.domainId = res.data.domainId;
    store.form.sourceSystemName = res.data.sourceSystemName;
    store.form.dbName = res.data.dbName;
    store.form.datasourceId = res.data.datasourceId;
    store.form.belongingLayer = res.data.belongingLayer;
    store.form.belongingSystem = res.data.belongingSystem;
    handleDatasourceChange(res.data.datasourceId);
  });
}

// 确认新增/修改
async function handleConfirmClick() {
  if (confirmLoading.value) return;
  confirmLoading.value = true;
  const valid = await formRef.value.validate();
  if (!valid) {
    confirmLoading.value = false;
    return;
  }
  const func = route.query.id ? updateTable : addTable;
  if (store.form.safetyLevelId == undefined) {
    store.form.safetyLevelId = null;
    store.form.safetyLevelName = null;
  }
  await func(store.form);
  confirmLoading.value = false;
  proxy.$modal.msgSuccess(
    `${
      route.query.id ? td("common.button.update") : td("common.button.add")
    }${td("meta.unreleased.structured.table.handle.tableMetadataSuccess")}`
  );
  router.back();
}

async function handleDraftClick() {
  if (confirmLoading.value) return;
  confirmLoading.value = true;
  await draftTable(store.form);
  confirmLoading.value = false;
  proxy.$modal.msgSuccess(
    td("meta.unreleased.structured.table.handle.draftSuccess")
  );
}

// 获取详情
async function getDetail() {
  if (!route.query.id) return;
  getTable(route.query.id).then((res) => {
    const {
      createBy,
      createTime,
      delFlag,
      updateBy,
      updateTime,
      updaterId,
      auditTime,
      auditStatus,
      ...form
    } = res.data;
    store.form = form;
    handleMetaDBChange(res.data.dbId);
  });
}

getMetaDatabases();
// getSensitiveLevel();
getDatasources();
getUserList();
getDetail();
</script>

<style lang="scss" scoped>
.app-container {
  background-color: #fff;
  padding: 15px 30px 30px;

  .module-body {
    margin-bottom: 40px;
  }

  .button-style {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
