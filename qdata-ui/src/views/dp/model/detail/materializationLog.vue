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
-->

<template>
  <qt-wrap
    :columns="tableStore.columns"
    :tableRef="tableRef"
    :config="{ fullContent: false, actions: { table: { search: false } } }"
  >
    <template #actions-data>
      <el-button
        type="primary"
        plain
        :disabled="row.status == 0"
        @click="handleMaterialization"
        v-hasPermi="['dp:model:edit']"
        @mousedown="(e) => e.preventDefault()"
      >
        <svg-icon
          iconClass="wh"
          style="font-size: 14px; margin-right: 6px"
          :class="{
            'icon-disabled': single,
            'icon-normal': !single,
          }"
        />{{ td('dp.materializedModel.publishModel') }}
      </el-button>
    </template>

    <qt-table v-bind="tableStore" ref="tableRef">
      <template #releaseMode="{ row }">
        <el-tag v-if="row.releaseMode == '1'" type="danger">{{ td('dp.materializedModel.deleteAndRecreate') }}</el-tag>
        <el-tag v-else-if="row.releaseMode == '2'" type="success">{{ td('dp.materializedModel.incrementalRelease') }}</el-tag>
        <span v-else>{{ row.releaseMode }}</span>
      </template>
    </qt-table>
  </qt-wrap>

  <!-- 新增或修改发布模型记录对话框 -->
  <el-dialog
    :title="title"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <el-form
      ref="dpModelMaterializedRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelCode')" prop="modelName">
            <el-input v-model="form.modelName" :placeholder="td('dp.materializedModel.modelCode')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelName')" prop="modelAlias">
            <el-input v-model="form.modelAlias" :placeholder="td('dp.materializedModel.modelName')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelTableId')" prop="modelId">
            <el-input v-model="form.modelId" :placeholder="td('dp.materializedModel.modelTableId')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('common.texts.status')" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio
                v-for="dict in dp_template_build_log_build_status"
                :key="dict.value"
                :label="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.materializedModel.executionLog')" prop="message">
            <el-input
              v-model="form.message"
              type="textarea"
              maxlength="500"
              show-word-limit
              :placeholder="td('dp.materializedModel.executionLog')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.sqlCommand')" prop="sqlCommand">
            <el-input
              v-model="form.sqlCommand"
              :placeholder="td('dp.materializedModel.sqlCommand')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.datasourceId')" prop="datasourceId">
            <el-input
              v-model="form.datasourceId"
              :placeholder="td('dp.materializedModel.datasourceId')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.modelForm.datasourceName')" prop="datasourceName">
            <el-input
              v-model="form.datasourceName"
              :placeholder="td('dp.modelForm.datasourceName')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.releaseMode')" prop="releaseMode">
            <el-radio-group v-model="form.releaseMode">
              <el-radio label="1">{{ td('dp.materializedModel.deleteAndRecreate') }}</el-radio>
              <el-radio label="2">{{ td('dp.materializedModel.incrementalRelease') }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.assetId')" prop="assetId">
            <el-input v-model="form.assetId" :placeholder="td('dp.materializedModel.assetId')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('common.texts.remark')" prop="remark">
            <el-input v-model="form.remark" :placeholder="td('common.form.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
          >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>

  <!-- 发布模型记录详情对话框 -->
  <el-dialog
    :title="title"
    v-model="openDetail"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <el-form ref="dpModelMaterializedRef" :model="form" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelCode')" prop="modelName">
            <div>
              {{ form.modelName }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelName')" prop="modelAlias">
            <div>
              {{ form.modelAlias }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.modelTableId')" prop="modelId">
            <div>
              {{ form.modelId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('common.texts.status')" prop="status">
            <dict-tag
              :options="dp_template_build_log_build_status"
              :value="form.status"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dp.materializedModel.executionLog')" prop="message">
            <div>
              {{ form.message }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.sqlCommand')" prop="sqlCommand">
            <div>
              {{ form.sqlCommand }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.datasourceId')" prop="datasourceId">
            <div>
              {{ form.datasourceId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.datasourceType')" prop="datasourceType">
            <div>
              {{ form.datasourceType }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.modelForm.datasourceName')" prop="datasourceName">
            <div>
              {{ form.datasourceName }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.releaseMode')" prop="releaseMode">
            <el-tag v-if="form.releaseMode == '1'" type="danger"
              >{{ td('dp.materializedModel.deleteAndRecreate') }}</el-tag
            >
            <el-tag v-else-if="form.releaseMode == '2'" type="success"
              >{{ td('dp.materializedModel.incrementalRelease') }}</el-tag
            >
            <span v-else>{{ form.releaseMode }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('dp.materializedModel.assetId')" prop="assetId">
            <div>
              {{ form.assetId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('common.texts.remark')" prop="remark">
            <div>
              {{ form.remark }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{ td('common.button.close') }}</el-button>
      </div>
    </template>
  </el-dialog>

  <MaterializationDialog
    :title="title"
    :visible="Materialization"
    @update:dialogFormVisible="Materialization = $event"
    :ids="modelIds"
    @confirm="getList"
  />
</template>

<script setup name="ComponentOne">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  listDpModelMaterialized,
  getDpModelMaterialized,
  delDpModelMaterialized,
  addDpModelMaterialized,
  updateDpModelMaterialized,
} from "@/api/dp/model/model";
import MaterializationDialog from "./materialization.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { dp_template_build_log_build_status } = proxy.useDict(
  "dp_template_build_log_build_status"
);
const dpModelMaterializedList = ref([]);
const props = defineProps({
  row: { type: Object, default: () => ({}) },
  modelId: { type: [String, Number], default: null },
});

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");

const data = reactive({
  dpModelMaterializedDetail: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelName: null,
    modelAlias: null,
    modelId: null,
    status: null,
    message: null,
    sqlCommand: null,
    datasourceId: null,
    datasourceType: null,
    datasourceName: null,
    releaseMode: null,
    assetId: null,
    createTime: null,
  },
  rules: {},
});

const { queryParams, form, dpModelMaterializedDetail, rules } = toRefs(data);
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, sortable: true },

    { label: td('dp.materializedModel.modelCode'), prop: "modelName", width: 240, align: "left" },
    {
      label: td('dp.materializedModel.modelName'),
      prop: "modelAlias",
      width: 240,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description'),
      prop: "description",
      align: "left",
      width: 250,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dp.materializedModel.datasourceType'),
      prop: "datasourceType",
      width: 160,
    },
    {
      label: td('dp.modelForm.datasourceName'),
      prop: "datasourceName",
      align: "left",
      width: 265,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dp.materializedModel.releaseMode'),
      prop: "releaseMode",
      width: 120,
      slot: "releaseMode",
    },
    {
      label: td('dp.materializedModel.executionLog'),
      prop: "message",
      align: "left",
      width: 220,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.status'),
      prop: "status",
      width: 80,
      dict: "dp_template_build_log_build_status",
    },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 120, align: "left" },
    {
      label: td('common.texts.createdTime'),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
  ],
  func: listWrapper,
  params: {},
});
function listWrapper(params) {
  const p = { ...params, modelId: modelId.value };
  return listDpModelMaterialized(p);
}
const route = useRoute();
const modelId = ref(props.modelId ?? route.query.id ?? 1);
watch(
  () => [props.modelId, route.query.id],
  ([pId, rId]) => {
    modelId.value = pId ?? rId ?? 1;
    getList();
  },
  { immediate: true }
);
const Materialization = ref(false);

/** 查询发布模型记录列表 */
function getList() {
  tableRef.value?.getList();
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
    modelName: null,
    modelAlias: null,
    modelId: null,
    status: null,
    message: null,
    sqlCommand: null,
    datasourceId: null,
    datasourceType: null,
    datasourceName: null,
    releaseMode: null,
    assetId: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("dpModelMaterializedRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  tableStore.params.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  tableStore.params.pageNum = 1;
  getList();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

// 排序由 qt-table 内部处理并写入 params
let modelIds = [];
/** 发布按钮操作 */
function handleMaterialization() {
  Materialization.value = true;
  title.value = td('dp.materializedModel.publishModelTitle');
  console.log("🚀 ~ handleMaterialization ~ modelId:", modelId);

  modelIds = [modelId.value];
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelMaterialized(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('dp.materializedModel.editRecord');
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDpModelMaterialized(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dp.materializedModel.recordDetail');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dpModelMaterializedRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDpModelMaterialized(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDpModelMaterialized(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.materializedModel.confirmDeleteRecord', { id: _ids }))
    .then(function () {
      return delDpModelMaterialized(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "dp/model/export",
    {
      ...queryParams.value,
    },
    `dpModelMaterialized_${new Date().getTime()}.xlsx`
  );
}

getList();
</script>
