<template>
  <div class="app-container">
    <el-form :model="store.form" :rules="rules" ref="formRef" label-width="110">
      <div class="module-head">基础信息</div>
      <div class="module-body infotop column-form">
        <el-form-item label="所属库名" prop="dbId">
          <el-select
            v-model="store.form.dbId"
            :disabled="!!route.query.id"
            placeholder="请选择所属库名"
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
        <el-form-item label="来源系统" prop="sourceSystemName">
          <el-input
            v-model="store.form.sourceSystemName"
            :disabled="!!route.query.id"
            placeholder="自动获取来源系统"
          />
        </el-form-item>
        <el-form-item label="表名称" prop="tableName">
          <el-input
            clearable
            :disabled="!!route.query.id"
            v-model="store.form.tableName"
            placeholder="请输入表名称"
          />
        </el-form-item>

        <el-form-item label="表注释" prop="tableComment">
          <el-input
            clearable
            :disabled="!!route.query.id"
            v-model="store.form.tableComment"
            placeholder="请输入表注释"
          />
        </el-form-item>

        <el-form-item label="所属分层">
          <el-select
            clearable
            v-model="store.form.belongingLayer"
            disabled
            placeholder="请选择所属分层"
          >
            <el-option
              v-for="dict in toValue(dicts.meta_dw_layers)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="所属系统" prop="belongingSystem">
          <el-input
            clearable
            v-model="store.form.belongingSystem"
            disabled
            placeholder="请输入所属系统"
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

        <!-- <el-form-item label="状态" prop="status">
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

        <el-form-item label="备注" class="row-full">
          <el-input
            v-model="store.form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </div>

      <div class="module-head">技术信息</div>
      <div class="module-body infotop column-form">
        <el-form-item label="数据连接名称" prop="datasourceId">
          <el-select
            clearable
            v-model="store.form.datasourceId"
            placeholder="请选择数据连接名称"
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

        <el-form-item label="数据库类型" prop="dbType">
          <el-input
            clearable
            v-model="store.form.dbType"
            disabled
            placeholder="请输入数据库类型"
          />
        </el-form-item>

        <el-form-item label="IP" prop="ip">
          <el-input
            clearable
            v-model="store.form.ip"
            disabled
            placeholder="请输入ip"
          />
        </el-form-item>

        <el-form-item label="端口号" prop="port">
          <el-input
            clearable
            v-model="store.form.port"
            disabled
            placeholder="请输入端口号"
          />
        </el-form-item>

        <el-form-item label="账号" prop="username">
          <el-input
            clearable
            v-model="store.form.username"
            disabled
            placeholder="请输入账号"
          />
        </el-form-item>

        <el-form-item label="存储类型" prop="storageType">
          <el-input
            clearable
            v-model="store.form.storageType"
            placeholder="请输入存储类型"
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

        <el-form-item label="技术负责人">
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
            placeholder="请选择技术负责人"
            check-strictly
            @change="handleUserChange($event, 'techLeaderPhone')"
          />
        </el-form-item>

        <el-form-item label="技术负责人电话">
          <el-input
            clearable
            v-model="store.form.techLeaderPhone"
            placeholder="请输入技术负责人电话"
          />
        </el-form-item>
      </div>

      <div class="module-head">业务信息</div>
      <div class="module-body infotop column-form">
        <el-form-item label="业务负责人">
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
            placeholder="请选择业务负责人"
            check-strictly
            @change="handleUserChange($event, 'businessLeaderPhone')"
          />
        </el-form-item>

        <el-form-item label="业务负责人电话">
          <el-input
            clearable
            v-model="store.form.businessLeaderPhone"
            placeholder="请输入业务负责人电话"
          />
        </el-form-item>

        <el-form-item label="是否主表" prop="masterFlag">
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

        <el-form-item label="是否临时表" prop="tempFlag">
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

        <el-form-item label="描述" class="row-full">
          <el-input
            v-model="store.form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
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
            maxlength="500个字符"
          />
        </el-form-item> -->
      </div>
    </el-form>

    <div class="button-style">
      <el-button @click="handleDraftClick"> 暂存 </el-button>
      <el-button
        type="primary"
        plain
        class="fh_btn"
        @mousedown="(e) => e.preventDefault()"
        @click="router.back"
      >
        <svg-icon iconClass="fhs" />返回列表
      </el-button>
      <el-button type="primary" @click="handleConfirmClick">
        确认并退出
      </el-button>
    </div>
  </div>
</template>

<script setup name="TableHandle">
import { reactive, getCurrentInstance, toValue } from "vue";
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

const DEFAULT_FORM = {
  status: "0",
  masterFlag: "1",
  tempFlag: "0",
};

const rules = {
  dbId: [
    { required: true, message: "请选择所属库名", trigger: ["blur", "change"] },
  ],
  sourceSystemName: [
    { required: true, message: "来源系统不能为空", trigger: "change" },
  ],
  datasourceId: [
    { required: true, message: "请选择数据连接名称", trigger: "change" },
  ],
  tableName: [
    { required: true, message: "请输入表名称", trigger: ["blur", "change"] },
  ],
  dbType: [
    {
      required: true,
      message: "请输入数据库类型",
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    { required: true, message: "请输入数据库ip", trigger: ["blur", "change"] },
  ],
  port: [
    {
      required: true,
      message: "请输入数据库端口",
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: "请输入数据库用户名",
      trigger: ["blur", "change"],
    },
  ],
  updateMsg: [
    { required: true, message: "请输入变更说明", trigger: ["change", "blur"] },
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
  store.loading = true;
  const valid = await formRef.value.validate();
  store.loading = false;
  if (!valid) return;
  store.loading = true;
  const func = route.query.id ? updateTable : addTable;
  if (store.form.safetyLevelId == undefined) {
    store.form.safetyLevelId = null;
    store.form.safetyLevelName = null;
  }
  await func(store.form);
  store.loading = false;
  proxy.$modal.msgSuccess(`${route.query.id ? "修改" : "新增"}表元数据成功！`);
  router.back();
}

async function handleDraftClick() {
  store.loading = true;
  await draftTable(store.form);
  store.loading = false;
  proxy.$modal.msgSuccess(`暂存表元数据成功！`);
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
