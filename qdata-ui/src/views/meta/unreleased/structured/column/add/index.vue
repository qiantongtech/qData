<template>
  <div class="app-container" :loading="store.loading">
    <el-form
      :model="store.form"
      ref="baseFormRef"
      :rules="rules"
      label-width="110"
    >
      <div class="module-head">基础信息</div>
      <div class="module-body infotop column-form">
        <el-form-item label="所属库名" prop="dbId">
          <el-select
            clearable
            v-model="store.form.dbId"
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
        <el-form-item label="所属表名" prop="tableId">
          <el-select
            clearable
            v-model="store.form.tableId"
            placeholder="请选择所属表名"
          >
            <el-option
              v-for="item in store.metaTables"
              :key="item.id"
              :label="item.tableName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="业务域" prop="domainId">
          <el-tree-select
            filterable
            v-model="store.form.domainId"
            :data="store.treeDomains"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择业务域"
            check-strictly
            default-expand-all
            disabled
          />
        </el-form-item>
        <el-form-item label="所属分层">
          <el-select
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
            v-model="store.form.belongingSystem"
            disabled
            placeholder="请输入所属系统"
          />
        </el-form-item>
      </div>
    </el-form>

    <div class="field-wrap">
      <div class="module-head">技术信息</div>
      <div class="module-body infotop technical-info">
        <div class="column-box">
          <div class="box-title">字段列表</div>
          <div class="box-content">
            <template v-if="store.fieldList.length">
              <template
                v-for="(item, index) in store.fieldList"
                :key="item.field"
              >
                <div
                  :class="[
                    'field-item',
                    item.field == store.activeField ? 'active' : '',
                  ]"
                  @click="store.activeField = item.field"
                  @dblclick="handleUpdateFieldClick(item, $event)"
                  v-show="!item.input"
                >
                  <div class="index">{{ index + 1 }}、</div>
                  <div class="field-name">{{ item.field }}</div>
                  <div
                    class="close-btn"
                    @click="handleCloseFieldClick(item.field, index)"
                  >
                    <el-icon><CircleClose /></el-icon>
                  </div>
                </div>
                <el-input
                  v-if="item.input"
                  clearable
                  v-model="item.newField"
                  placeholder="请输入字段名称"
                  @keyup.enter="handleConfirmUpdateField(item, $event)"
                  @blur="handleConfirmUpdateField(item, $event)"
                  class="field-input"
                />
              </template>
            </template>
            <el-input
              clearable
              v-model="store.addInputValue"
              placeholder="请输入字段名称"
              @keyup.enter="handleConfirmAddField"
              @blur="handleConfirmAddField"
              class="field-input"
              ref="addInputRef"
              v-show="!store.fieldList.length || store.showInput"
            />
            <el-button
              type="primary"
              class="field-btn"
              icon="Plus"
              @click="handleAddFieldClick"
            >
              新增字段
            </el-button>
          </div>
        </div>

        <div class="column-box">
          <div class="box-title">字段设置</div>
          <div class="box-content" v-if="activeForm">
            <!--  待优化 Vue渲染机制导致会重复触发. 后续应抽离成组件 做隔离 -->
            <el-form
              v-for="form in store.formList"
              :key="form.columnName"
              :class="[
                'field-form',
                form.columnName == store.activeField ? 'active' : '',
              ]"
              label-width="100"
              :rules="rules"
              :model="form"
              :ref="(el) => setupFormRefs(form.columnName, el)"
            >
              <div class="form-title">基础信息</div>
              <div class="column-form">
                <el-form-item label="字段注释" prop="columnComment">
                  <el-input
                    clearable
                    v-model="form.columnComment"
                    placeholder="请输入字段注释"
                  />
                </el-form-item>

                <!-- <el-form-item label="安全等级" prop="safetyLevelId">
                  <el-select
                    clearable
                    v-model="form.safetyLevelId"
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

                <!-- <el-form-item label="标准数据元" prop="dataElemId">
                  <el-select
                    clearable
                    v-model="form.dataElemId"
                    placeholder="请选择标准数据元"
                  >
                    <el-option
                      v-for="item in store.dataElemList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item> -->

                <!-- <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="form.status">
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
                    v-model="form.remark"
                    type="textarea"
                    placeholder="请输入备注"
                    :min-height="192"
                    show-word-limit
                    maxlength="500个字符"
                  />
                </el-form-item>
              </div>
              <div class="form-title">技术信息</div>
              <div class="column-form">
                <el-form-item label="字段类型" prop="columnType">
                  <el-select
                    clearable
                    v-model="form.columnType"
                    placeholder="请选择字段类型"
                  >
                    <el-option
                      v-for="dict in toValue(dicts.column_type)"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>

                <el-form-item label="字段长度" prop="columnLength">
                  <el-input-number
                    :min="0"
                    v-model="form.columnLength"
                    placeholder="请输入字段长度"
                    :controls="true"
                    class="number-input"
                    controls-position="right"
                  />
                </el-form-item>

                <el-form-item label="字段精度" prop="columnPrecision">
                  <el-input-number
                    :min="0"
                    v-model="form.columnPrecision"
                    placeholder="请输入字段精度"
                    :controls="true"
                    class="number-input"
                    controls-position="right"
                  />
                </el-form-item>

                <el-form-item label="字段小数位" prop="columnScale">
                  <el-input-number
                    :min="0"
                    v-model="form.columnScale"
                    placeholder="请输入字段小数位"
                    :controls="true"
                    class="number-input"
                    controls-position="right"
                  />
                </el-form-item>

                <el-form-item label="是否必填" prop="nullableFlag">
                  <el-radio-group v-model="form.nullableFlag">
                    <el-radio
                      v-for="dict in toValue(dicts.table_yes_no)"
                      :key="dict.value"
                      :value="dict.value"
                    >
                      {{ dict.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>

                <el-form-item label="默认值" prop="defaultValue">
                  <el-input
                    clearable
                    v-model="form.defaultValue"
                    placeholder="请输入默认值"
                  />
                </el-form-item>

                <el-form-item label="是否主键" prop="pkFlag">
                  <el-radio-group v-model="form.pkFlag">
                    <el-radio
                      v-for="dict in toValue(dicts.table_yes_no)"
                      :key="dict.value"
                      :value="dict.value"
                    >
                      {{ dict.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>

                <el-form-item label="是否外键" prop="fkFlag">
                  <el-radio-group v-model="form.fkFlag">
                    <el-radio
                      v-for="dict in toValue(dicts.table_yes_no)"
                      :key="dict.value"
                      :value="dict.value"
                    >
                      {{ dict.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>

              <div class="form-title">业务信息</div>
              <div class="column-form">
                <el-form-item label="业务定义" prop="businessDefinition">
                  <el-input
                    clearable
                    v-model="form.businessDefinition"
                    placeholder="请输入业务定义"
                  />
                </el-form-item>

                <el-form-item label="度量单位" prop="measuringUnit">
                  <el-input
                    clearable
                    v-model="form.measuringUnit"
                    placeholder="请输入度量单位"
                  />
                </el-form-item>

                <el-form-item label="描述" class="row-full">
                  <el-input
                    v-model="form.description"
                    type="textarea"
                    placeholder="请输入描述"
                    :min-height="192"
                    show-word-limit
                    maxlength="500个字符"
                  />
                </el-form-item>
              </div>
            </el-form>
            <div class="button-style">
              <el-button @click="handleDraftClick"> 暂存 </el-button>
              <el-button
                type="primary"
                plain
                class="fh_btn"
                @mousedown="(e) => e.preventDefault()"
              >
                <svg-icon iconClass="fhs" />返回列表
              </el-button>
              <el-button type="primary" @click="handleConfirmClick">
                确认并退出
              </el-button>
            </div>
          </div>
          <div class="emptyBg" v-else>
            <img src="@/assets/images/sys/error/no-data.png" alt="" />
            <p>暂无记录</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="AddColumn">
import {
  reactive,
  getCurrentInstance,
  toValue,
  computed,
  nextTick,
  shallowRef,
} from "vue";
import { listDb, getDb } from "@/api/mc/unreleased/db";
import { listTable } from "@/api/mc/unreleased/table";
import { addColumn, draftColumn } from "@/api/mc/unreleased/column";
import { listDomain } from "@/api/att/domain/domain.js";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";
// import { getDgDataElemList } from "@/api/dg/standard/dataElem.js";
import { useRouter } from "vue-router";

const BASE_URL = "/meta/unreleased/structured/column";

const DEFAULT_FORM = {
  status: "0",
  pkFlag: "0",
  fkFlag: "0",
  nullableFlag: "0",
  // columnLength: 0,
  // columnPrecision: 0,
  // columnScale: 0
};

const rules = {
  dbId: [{ required: true, message: "请选择所属库名", trigger: ["change"] }],
  domainId: [{ required: true, message: "请选择业务域", trigger: "change" }],
  tableId: [{ required: true, message: "请选择所属表名", trigger: "change" }],
  columnType: [
    { required: true, message: "请选择字段类型", trigger: "change" },
  ],
  pkFlag: [{ required: true, message: "请选择是否主键", trigger: "change" }],
  fkFlag: [{ required: true, message: "请选择是否外键", trigger: "change" }],
  nullableFlag: [
    { required: true, message: "请选择是否可空", trigger: "change" },
  ],
  columnScale: [
    {
      required: true,
      message: "请输入字段小数位",
      trigger: ["change", "blur"],
    },
  ],
  businessDefinition: [
    { required: true, message: "请输入业务定义", trigger: "blur" },
  ],
  columnLength: [
    { required: true, message: "请输入字段长度", trigger: ["change", "blur"] },
  ],
  columnPrecision: [
    { required: true, message: "请输入字段精度", trigger: ["change", "blur"] },
  ],
};

const formRefs = shallowRef({});
const baseFormRef = shallowRef(null);
const addInputRef = shallowRef(null);
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "meta_task_status",
  "meta_dw_layers",
  "table_yes_no",
  "column_type"
);

const router = useRouter();

const store = reactive({
  form: {},
  metaDatabases: [],
  domains: [],
  treeDomains: [],
  formList: [],
  fieldList: [],
  activeField: "",
  addInputValue: "",
  showInput: false,
  sensitiveLevels: [],
  dataElemList: [],
});

const activeForm = computed(() => {
  return store.formList.find((item) => item.columnName == store.activeField);
});

// 设置表单dom
// TODO: 待优化 Vue渲染机制导致会重复触发. 后续应抽离成组件 做隔离
function setupFormRefs(key, el) {
  if (!key || !key.trim()) return;
  formRefs.value[key] = el;
}

// 获取业务域列表
function getDomains() {
  listDomain().then((res) => {
    store.domains = [...res.data];
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.treeDomains.push(domains);
  });
}

// 获取库元素列表
function getMetaDatabases() {
  return listDb({ pageSize: 1000 }).then((res) => {
    store.metaDatabases = res.data.rows;
    return res;
  });
}

// 获取表元素列表
function getMetaTables(dbId) {
  return listTable({ pageSize: 1000, dbId }).then((res) => {
    store.metaTables = res.data.rows;
    return res;
  });
}

// 切换库元数据
function handleMetaDBChange(id) {
  getMetaTables(id);
  getDb(id).then((res) => {
    store.form.domainId = res.data.domainId;
    store.form.dbName = res.data.dbName;
    store.form.belongingLayer = res.data.belongingLayer;
    store.form.belongingSystem = res.data.belongingSystem;
    store.form.datasourceId = res.data.datasourceId;
    store.form.tableId = "";
  });
}

// 新增字段
function handleAddFieldClick() {
  if (!store.fieldList.length) return;
  store.showInput = true;
  nextTick(() => {
    addInputRef.value.focus();
  });
}

function validateItemField(field, event) {
  return new Promise((resolve) => {
    const regex = /^[a-zA-Z][a-zA-Z0-9_]*$/;
    const valid = regex.test(field);
    if (!valid) {
      ElMessage.error("字段名只能包含字母、数字和下划线，且必须以字母开头");
      event.target.focus();
      return resolve(false);
    }

    resolve(true);
  });
}

// 确定新增字段
async function handleConfirmAddField(event) {
  const field = store.addInputValue.trim();
  if (!field) {
    store.showInput = false;
    return;
  }
  const valid = await validateItemField(field, event);
  if (!valid) return;
  const hasField = store.fieldList.find((item) => item.field == field);
  if (hasField) {
    ElMessage.error("字段名已存在");
    event.target.focus();
    return;
  }
  store.fieldList.push({
    field,
  });
  store.formList.push({
    columnName: field,
    ...DEFAULT_FORM,
  });
  store.showInput = false;
  store.addInputValue = "";
  store.activeField = field;
  nextTick(removeInvalidRef);
}

// 删除无效dom
function removeInvalidRef() {
  for (let key in formRefs.value) {
    if (store.fieldList.includes(key)) continue;
    delete formRefs.value[key];
  }
}

// 点击修改字段
function handleUpdateFieldClick(row, event) {
  row.input = true;
  row.newField = row.field;
  nextTick(() => {
    event.target.nextElementSibling.querySelector("input").focus();
  });
}

// 修改字段
async function handleConfirmUpdateField(row, event) {
  const field = row.newField.trim();
  if (!field) {
    row.input = false;
    return;
  }
  const valid = await validateItemField(field, event);
  if (!valid) return;
  const hasField = store.fieldList.find(
    (item) => item.field == field && item.field != row.field
  );
  if (hasField) {
    ElMessage.error("字段名已存在");
    event.target.focus();
    return;
  }
  const targetForm = store.formList.find(
    (item) => item.columnName == row.field
  );
  targetForm.columnName = field;
  if (store.activeField == row.field) {
    store.activeField = field;
  }
  row.field = field;
  row.input = false;
  row.newField = "";

  // 删除无效的dom
  nextTick(removeInvalidRef);
}

// 删除字段
function handleCloseFieldClick(field, fieldIndex) {
  store.fieldList.splice(fieldIndex, 1);
  const formIndex = store.formList.findIndex(
    (item) => item.columnName == field
  );
  store.formList.splice(formIndex, 1);

  nextTick(() => {
    if (store.activeField == field) {
      store.activeField = store.fieldList[0].field;
    }
    removeInvalidRef();
  });
}

// 获取安全等级
function getSensitiveLevel() {
  listDgSensitiveLevel({ pageSize: 1000 }).then((res) => {
    store.sensitiveLevels = res.data.rows;
  });
}

// function getDataElem() {
//   getDgDataElemList().then((res) => {
//     store.dataElemList = res.data;
//   });
// }

// 表单验证
function validateGlobalForm() {
  let depend = store.fieldList.length + 1;
  const change = (vaild, resolve) => {
    if (!vaild) return resolve(false);
    depend--;
    if (!depend) return resolve(true);
  };
  return new Promise((resolve) => {
    baseFormRef.value.validate((vaild) => {
      change(vaild, resolve);
    });
    for (let i = 0; i < store.fieldList.length; i++) {
      const key = store.fieldList[i].field;
      nextTick(() => {
        formRefs.value[key].validate((vaild) => {
          change(vaild, resolve);
        });
      });
    }
  });
}

// 确认并退出
async function handleConfirmClick() {
  store.loading = true;
  const valid = await validateGlobalForm();
  store.loading = false;
  if (!valid) return proxy.$message.warning(`请完成表单填写！`);
  store.loading = true;
  const params = [];
  store.formList.forEach((item) => {
    if (item.safetyLevelId == undefined) {
      item.safetyLevelId = null;
      item.safetyLevelName = null;
    }
    params.push({
      ...item,
      ...store.form,
    });
  });
  const res = await addColumn(params);
  if (res.code !== 200) return;
  proxy.$message.success(`新增字段成功！`);
  store.loading = false;
  router.push({ path: BASE_URL });
}

// 暂存
async function handleDraftClick() {
  const params = [];
  store.loading = true;
  store.formList.forEach((item) => {
    if (item.safetyLevelId == undefined) {
      item.safetyLevelId = null;
      item.safetyLevelName = null;
    }
    params.push({
      ...item,
      ...store.form,
    });
  });
  const res = await draftColumn(params);
  if (res.code != 200) return;
  proxy.$message.success(`暂存字段成功！`);
  store.loading = false;
  router.push({ path: BASE_URL });
}

// getDomains();
getMetaDatabases();
// getSensitiveLevel();
</script>

<style lang="scss" scoped>
.app-container {
  background-color: #fff;
  padding: 15px 30px 30px;
  display: flex;
  flex-direction: column;

  .module-body {
    margin-bottom: 40px;
  }

  .field-wrap {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .button-style {
    display: flex;
    justify-content: flex-end;
  }

  .technical-info {
    display: flex;
    flex: 1;
    margin-bottom: 0;
    overflow: hidden;
  }

  .column-box {
    overflow: auto;
    border: 1px solid #e6e6e6;
    display: flex;
    flex-direction: column;
    &:first-child {
      width: 380px;
      border-right: 0;
    }

    &:last-child {
      flex: 1;
    }
  }

  .box-title {
    background-color: #f6f8fa;
    padding-left: 20px;
    font-size: 14px;
    line-height: 32px;
    font-weight: 600;
    color: #747678;
  }

  .box-content {
    flex: 1;
    padding: 10px 20px;
    // max-height: 621px;
    .column-form {
      margin-bottom: 25px;
    }
  }

  .field-btn {
    margin-top: 10px;
    width: 100%;
  }

  .field-input {
    margin-top: -1px;
  }

  .field-item {
    line-height: 32px;
    border: 1px solid #dcdfe6;
    border-bottom: none;
    padding-left: 11px;
    color: #878787;
    font-size: 14px;
    display: flex;
    cursor: pointer;
    position: relative;
    &:last-of-type {
      border-bottom: 1px solid #dcdfe6;
    }
    &:hover {
      background-color: var(--el-color-primary-light-9);
      .close-btn {
        display: flex;
      }
    }
    &.active {
      border: 1px solid var(--el-color-primary);
      color: var(--el-color-primary);
      background-color: var(--el-color-primary-light-9);
    }

    .close-btn {
      position: absolute;
      right: 0;
      padding: 0 11px;
      top: 50%;
      transform: translateY(-50%);
      display: none;
      align-items: center;
      height: 100%;
      cursor: pointer;
      .el-icon {
        display: block;
      }
    }
  }

  .field-form {
    display: none;
    padding: 6px 18px;

    &.active {
      display: block;
    }

    .form-title {
      font-size: 14px;
      font-weight: 600;
      color: #747678;
      margin-bottom: 10px;
    }
  }
}
</style>
