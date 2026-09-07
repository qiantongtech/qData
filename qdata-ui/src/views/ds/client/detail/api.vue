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
  <div ref="app-container">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef":config="{ fullContent: false, actions: { table: { search: false } } }">
      <template #actions-data>
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
        <template #startTime="{ row }">
          <span v-if="row.pvFlag == 1">{{ td('ds.client.details.permanent') }}</span>
          <div v-else>
            <span>{{ parseTime(row.startTime, "{y}-{m}-{d} ") }}</span>
            <span>- </span>
            <span>{{ parseTime(row.endTime, "{y}-{m}-{d} ") }}</span>
          </div>
        </template>

        <template #status="{ row }">
          <el-switch v-model="row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
            inactive-value="0" @change="(e) => handleStatusChange(row.id, row, e)" />
        </template>

        <template #action="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)">{{ td('common.button.update') }}</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(row)">{{ td('common.button.delete') }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

  <!-- Add or modify application API service association dialog box -->
  <el-dialog :title="title" v-model="open" class="dialog" :append-to="$refs['app-container']" draggable>
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="clientApiRelRef" :model="form" :rules="rules" @submit.prevent :label-position="labelPosition">
      <el-row >
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.apiService')" prop="apiName" :label-position="labelPosition">
            <el-autocomplete :disabled="form.id" v-model="form.apiName" :fetch-suggestions="remoteMethod"
              :placeholder="td('ds.client.details.apiServicePlaceholder')" @select="handleApiIdSelect" />
          </el-form-item>
        </el-col>
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.permanentValid')" prop="pvFlag" :label-position="labelPosition">
            <el-radio-group v-model="form.pvFlag" @change="handlePvFlagChange">
              <el-radio v-for="dict in sys_is_or_not" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col  :span="12" v-if="form.pvFlag == 0">
          <el-form-item :label="td('ds.client.details.validPeriod')" prop="dateRange" :label-position="labelPosition">
            <el-date-picker class="el-form-input-width" v-model="form.dateRange" value-format="YYYY-MM-DD"
              type="daterange" range-separator="-" :start-placeholder="td('common.form.startDatePlaceholder')" :end-placeholder="td('common.form.endDatePlaceholder')"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="24">
          <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
            <el-input type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" v-model="form.description" :min-height="192"   maxlength="256字符" show-word-limit/>
          </el-form-item>
        </el-col>
      </el-row>
      
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{td('common.button.cancel')}}</el-button>
        <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{td('common.button.confirm')}}</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- Application API service association details dialog box -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="clientApiRelRef" :model="form" label-width="80px" :label-position="labelPosition">
      <el-row >
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.appId')" prop="clientId" :label-position="labelPosition">
            <div>
              {{ form.clientId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.apiServiceId')" prop="apiId" :label-position="labelPosition">
            <div>
              {{ form.apiId }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.permanentValid')" prop="pvFlag" :label-position="labelPosition">
            <dict-tag :options="sys_is_or_not" :value="form.pvFlag" />
          </el-form-item>
        </el-col>
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.startTime')" prop="startTime" :label-position="labelPosition">
            <el-date-picker clearable style="width: 100%" v-model="form.startTime" type="date" value-format="YYYY-MM-DD"
              :placeholder="td('ds.client.details.startTimePlaceholder')"> </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col  :span="24">
          <el-form-item :label="td('ds.client.details.endTime')" prop="endTime" :label-position="labelPosition">
            <el-date-picker clearable style="width: 100%" v-model="form.endTime" type="date" value-format="YYYY-MM-DD"
              :placeholder="td('ds.client.details.endTimePlaceholder')"> </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col  :span="24">
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
  </div>
</template>

<script setup name="ClientApiRel">
import { listClientApiRel, getClientApiRel, delClientApiRel, addClientApiRel, updateClientApiRel } from "@/api/ds/client/clientApiRel";
import { selectByName } from "@/api/ds/api/api.js";
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, ref, watch, getCurrentInstance, toRefs } from "vue";
import { useRouter } from "vue-router";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
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
    tableStore.params.clientId = newValue.id;
    tableRef.value?.refresh();
  }
);

const tableRef = ref(null);
const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const router = useRouter();

const wrapConfig = reactive({
  actions: {
    table: {
      search: false,
    },
  },
});

const tableStore = reactive({
  func: listClientApiRel,
  params: {
    clientId: null,
  },
  config: {
    initResquest: false,
  },
  columns: [
    { label: td('common.texts.number'), prop: "id", width: 60, align: "center" },
    { label: td('ds.client.details.apiCode'), prop: "apiId", width: 120, align: "center", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.client.details.apiName'), prop: "apiName", width: 200, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.client.details.apiPath'), prop: "apiUrl", minWidth: 200, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('ds.client.details.requestMethod'), prop: "reqMethod", width: 120, align: "center", dict: "ds_api_bas_info_api_method_type" },
    { label: td('ds.client.details.validPeriod'), prop: "startTime", width: 260, align: "center", slot: "startTime" },
    { label: td('common.texts.description'), prop: "description", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('common.texts.createdBy'), prop: "createBy", width: 120, align: "center" },
    { label: td('common.texts.createdTime'), prop: "createTime", width: 160, align: "center", date: true },
    { label: td('ds.client.details.authStatus'), prop: "status", width: 100, align: "center", slot: "status" },
    { label: td('common.texts.remark'), prop: "remark", width: 256, align: "left", showOverflowTooltip: { effect: 'light' } },
    { label: td('common.texts.operation'), width: 140, align: "center", fixed: "right", slot: "action" }
  ]
});

const data = reactive({
  form: {
    pvFlag: "0",
    dateRange: [],
  },
  rules: {
    apiName: [{ required: true, message: td('ds.client.details.apiServiceRequired'), trigger: "change" }],
    pvFlag: [{ required: true, message: td('ds.client.details.permanentRequired'), trigger: "blur" }],
    dateRange: [{ required: true, message: td('ds.client.details.validPeriodRequired'), trigger: "blur" }],
  },
});

const { form, rules } = toRefs(data);

/** Change enabled status value */
function handleStatusChange(id, row, e) {
  const text = row.status == "1" ? td('ds.client.details.authorize') : td('ds.client.details.deauthorize');
  proxy.$modal
    .confirm(td('ds.client.details.confirmStatusChange') + text + td('ds.client.details.confirmStatusSuffix') + row.apiName + td('ds.client.details.confirmStatusSuffix2'))
    .then(function () {
      return updateClientApiRel({ ...row });
    })
    .then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
      }
    })
    .catch((error) => {
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

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

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

function handleAdd() {
  reset();
  open.value = true;
  title.value = td('ds.client.details.addApiAuth');
}

function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  form.value.dateRange = [form.value.startTime, form.value.endTime];
  open.value = true;
  title.value = td('ds.client.details.editApiAuth');
}

function handleDetail(row) {
  reset();
  const _id = row.id;
  getClientApiRel(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('ds.client.details.detailApiAuth');
  });
}

function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
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
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addClientApiRel(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            tableRef.value.refresh();
            submitLoading.value = false;
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

function handleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(td('ds.client.details.deleteApiRelConfirm') + _ids + td('ds.client.details.deleteApiRelConfirmSuffix'))
    .then(function () {
      return delClientApiRel(_ids);
    })
    .then(() => {
      tableRef.value.refresh();
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
