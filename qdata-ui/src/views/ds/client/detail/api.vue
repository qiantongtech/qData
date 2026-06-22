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
  <div ref="app-container">
    <div class="justify-between mb15">
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5">
          <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-table stripe v-loading="loading" :data="clientApiRelList" @selection-change="handleSelectionChange"
      :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column :label="td('common.texts.number')" type="index" align="center" width="50" :show-overflow-tooltip="{ effect: 'light' }" />
      <el-table-column :label="td('ds.client.details.apiCode')" align="center" prop="apiId" :show-overflow-tooltip="{ effect: 'light' }" />
      <el-table-column :label="td('ds.client.details.apiName')" align="center" prop="apiName" :show-overflow-tooltip="{ effect: 'light' }"
        width="150">
        <template #default="scope">
          {{ scope.row.apiName || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.details.apiPath')" align="center" prop="apiUrl" :show-overflow-tooltip="{ effect: 'light' }" width="150">
        <template #default="scope">
          {{ scope.row.apiUrl || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.details.requestMethod')" align="center" prop="reqMethod" :show-overflow-tooltip="{ effect: 'light' }">
        <template #default="scope">
          <dict-tag :options="ds_api_bas_info_api_method_type" :value="scope.row.reqMethod" />
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.details.validPeriod')" align="center" prop="startTime" width="260"
        :show-overflow-tooltip="{ effect: 'light' }">
        <template #default="scope">
          <span v-if="scope.row.pvFlag == 1">{{ td('ds.client.details.permanent') }}</span>
          <div v-else>
            <span>{{ parseTime(scope.row.startTime, "{y}-{m}-{d} ") }}</span>
            <span>- </span>
            <span>{{ parseTime(scope.row.endTime, "{y}-{m}-{d} ") }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.description')" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
        width="250">
        <template #default="scope">
          {{ scope.row.description || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.createdBy')" align="center" prop="createBy" :show-overflow-tooltip="{ effect: 'light' }" width="100">
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180"
        :show-overflow-tooltip="{ effect: 'light' }">
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="td('ds.client.details.authStatus')" align="center" prop="status" :show-overflow-tooltip="{ effect: 'light' }" width="160">
        <template #default="scope">
          <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
            inactive-value="0" @change="(e) => handleStatusChange(scope.row.id, scope.row, e)" />
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.remark')" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
        <template #default="scope">
          {{ scope.row.remark || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="140">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">{{ td('common.button.update') }}</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">{{ td('common.button.delete') }}</el-button>
        </template>
      </el-table-column>

      <template #empty>
        <div class="emptyBg">
          <img src="@/assets/images/system/images/no_data/noData.png" alt="" />
          <p>{{td('common.noData')}}</p>
        </div>
      </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>

  <!-- 添加或修改应用API服务关联对话框 -->
  <el-dialog :title="title" v-model="open" class="dialog" :append-to="$refs['app-container']" draggable>
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="clientApiRelRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.apiService')" prop="apiName" :label-position="labelPosition">
            <el-autocomplete :disabled="form.id" v-model="form.apiName" :fetch-suggestions="remoteMethod"
              :placeholder="td('ds.client.details.apiServicePlaceholder')" @select="handleApiIdSelect" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.permanentValid')" prop="pvFlag" :label-position="labelPosition">
            <el-radio-group v-model="form.pvFlag" @change="handlePvFlagChange">
              <el-radio v-for="dict in sys_is_or_not" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.pvFlag == 0">
          <el-form-item :label="td('ds.client.details.validPeriod')" prop="dateRange" :label-position="labelPosition">
            <el-date-picker class="el-form-input-width" v-model="form.dateRange" value-format="YYYY-MM-DD"
              type="daterange" range-separator="-" :start-placeholder="td('common.form.startDatePlaceholder')" :end-placeholder="td('common.form.endDatePlaceholder')"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
            <el-input type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" v-model="form.description" :min-height="192" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
            <el-input type="textarea" :placeholder="td('common.form.remarkPlaceholder')" v-model="form.remark" :min-height="192" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{td('common.button.cancel')}}</el-button>
        <el-button type="primary" size="mini" @click="submitForm">{{td('common.button.confirm')}}</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 应用API服务关联详情对话框 -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="clientApiRelRef" :model="form" label-width="80px" :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.appId')" prop="clientId" :label-position="labelPosition">
            <div>
              {{ form.clientId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.apiServiceId')" prop="apiId" :label-position="labelPosition">
            <div>
              {{ form.apiId }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.permanentValid')" prop="pvFlag" :label-position="labelPosition">
            <dict-tag :options="sys_is_or_not" :value="form.pvFlag" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.startTime')" prop="startTime" :label-position="labelPosition">
            <el-date-picker clearable style="width: 100%" v-model="form.startTime" type="date" value-format="YYYY-MM-DD"
              :placeholder="td('ds.client.details.startTimePlaceholder')"> </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.endTime')" prop="endTime" :label-position="labelPosition">
            <el-date-picker clearable style="width: 100%" v-model="form.endTime" type="date" value-format="YYYY-MM-DD"
              :placeholder="td('ds.client.details.endTimePlaceholder')"> </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('ds.client.details.authStatus')" prop="status" :label-position="labelPosition">
            <div>
              {{ form.status }}
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
</template>

<script setup name="ClientApiRel">
import { listClientApiRel, getClientApiRel, delClientApiRel, addClientApiRel, updateClientApiRel } from "@/api/ds/client/clientApiRel";
import { selectByName } from "@/api/ds/api/api.js";
import { status } from "nprogress";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_is_or_not, ds_api_bas_info_api_method_type } = proxy.useDict("sys_is_or_not", "ds_api_bas_info_api_method_type");

const props = defineProps({
  clientDetail: {
    type: Object,
    default: () => { },
  },
});
const clientId = ref(null);
watch(
  () => props.clientDetail,
  (newValue) => {
    clientId.value = newValue.id;
    getList();
  }
);
const clientApiRelList = ref([]);

const open = ref(false);
const openDetail = ref(false);
const loading = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

const data = reactive({
  form: {
    pvFlag: "0",
    dateRange: [],
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    clientId: null,
    apiId: null,
    pvFlag: null,
    startTime: null,
    endTime: null,
    status: null,
  },
  rules: {
    apiName: [{ required: true, message: td('ds.client.details.apiServiceRequired'), trigger: "change" }],
    pvFlag: [{ required: true, message: td('ds.client.details.permanentRequired'), trigger: "blur" }],
    dateRange: [{ required: true, message: td('ds.client.details.validPeriodRequired'), trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用API服务关联列表 */
function getList() {
  queryParams.value.clientId = clientId.value;
  loading.value = true;
  listClientApiRel(queryParams.value)
    .then((response) => {
      clientApiRelList.value = response.data.rows;
      total.value = response.data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 改变启用状态值 */
function handleStatusChange(id, row, e) {
  console.log(e);
  const text = row.status == "1" ? td('ds.client.details.authorize') : td('ds.client.details.deauthorize');
  // 弹出确认框
  proxy.$modal
    .confirm(td('ds.client.details.confirmStatusChange') + text + td('ds.client.details.confirmStatusSuffix') + row.apiName + td('ds.client.details.confirmStatusSuffix2'))
    .then(function () {
      loading.value = true; // 开始加载
      // 调用后台接口更新发布状态
      updateClientApiRel({ ...row })
        .then((res) => {
          if (res.code == 200) {
            proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
          }
        })
        .catch((error) => {
          console.log(error);
          // 失败时恢复状态
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false; // 无论成功失败都停止加载
        });
    })
    .catch((error) => {
      // 失败时恢复状态
      row.status = row.status === "1" ? "0" : "1";
    });
}

const handlePvFlagChange = (e) => {
  if (e == "1") {
    form.value.dateRange = [];
  }
};
const apiIdloading = ref(false);
const handleApiIdSelect = (row) => {
  form.value.apiId = row.id;
  form.value.reqMethod = row.reqMethod;
  form.value.apiUrl = row.apiUrl;
};
const remoteMethod = (queryString, cb) => {
  apiIdloading.value = true;
  selectByName(queryString || "")
    .then((res) => {
      if (res.code === 200 && Array.isArray(res.data)) {
        const results = res.data.map((item) => ({
          ...item,
          value: item.name,
        }));
        cb(results);
      } else {
        cb([]);
      }
    })
    .catch(() => {
      cb([]);
    })
    .finally(() => {
      apiIdloading.value = false;
    });
};

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
    clientId: null,
    apiId: null,
    pvFlag: "0",
    dateRange: [],
    startTime: null,
    endTime: null,
    status: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    REMARK: null,
  };
  proxy.resetForm("clientApiRelRef");
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
  open.value = true;
  title.value = td('ds.client.details.addApiAuth');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  form.value.dateRange = [form.value.startTime, form.value.endTime];
  open.value = true;
  title.value = td('ds.client.details.editApiAuth');
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getClientApiRel(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('ds.client.details.detailApiAuth');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["clientApiRelRef"].validate((valid) => {
    if (valid) {
      form.value.clientId = clientId.value;
      form.value.startTime = form.value.dateRange[0];
      form.value.endTime = form.value.dateRange[1];
      if (form.value.id != null) {
        updateClientApiRel(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addClientApiRel(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => { });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('ds.client.details.deleteApiRelConfirm') + _ids + td('ds.client.details.deleteApiRelConfirmSuffix'))
    .then(function () {
      return delClientApiRel(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

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
</script>
<style lang="scss" scoped></style>