<template>
  <div class="app-container" ref="app-container">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQueryClick"
          @reset="handleResetQueryClick"
        />
      </template>
      <template #actions-data>
        <el-button type="primary" plain icon="Plus" @click="handleAdd">
          {{ td('common.button.add') }}
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
        >
          {{ td('common.button.delete') }}
        </el-button>
      </template>
      <qt-table v-bind="tableStore" ref="tableRef">
        <template #type="scope">
          <dict-tag :options="sys_source_system_type" :value="scope.row.type" />
        </template>
        <template #validFlag="scope">
          <el-switch
            v-model="scope.row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          />
        </template>
        <template #responsiblePerson="scope">
          {{ getUserLabel(scope.row.responsiblePerson) }}
        </template>
        <template #contactPerson="scope">
          {{ getUserLabel(scope.row.contactPerson) }}
        </template>
        <template #handle="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)"
            >{{ td('common.button.update') }}</el-button
          >
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            :disabled="row.validFlag"
            >{{ td('common.button.delete') }}</el-button
          >
          <el-button link type="primary" icon="view" @click="handleDetail(row)"
            >{{ td('common.button.details') }}</el-button
          >
        </template>
      </qt-table>
    </qt-wrap>

    <!-- Add or modify source system dialog -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="sourceSystemRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
       :label-position="labelPosition">
        <el-form-item :label="td('att.sourceSystem.form.name')" prop="name" :label-position="labelPosition">
          <el-input v-model="form.name" :placeholder="td('att.sourceSystem.form.namePlaceholder')" />
        </el-form-item>

        <el-form-item :label="td('att.sourceSystem.form.type')" prop="type" :label-position="labelPosition">
          <el-select v-model="form.type" :placeholder="td('att.sourceSystem.form.typePlaceholder')">
            <el-option
              v-for="dict in sys_source_system_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.responsiblePerson')" prop="responsiblePerson" :label-position="labelPosition">
          <el-select
            v-model="form.responsiblePerson"
            filterable
            :placeholder="td('att.sourceSystem.form.responsiblePersonPlaceholder')"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="td('att.sourceSystem.form.contactPerson')" prop="contactPerson" :label-position="labelPosition">
          <el-select
            v-model="form.contactPerson"
            filterable
            :placeholder="td('att.sourceSystem.form.contactPersonPlaceholder')"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.sortOrder')" prop="sortOrder" :label-position="labelPosition">
          <el-input-number
            style="width: 100%"
            v-model="form.sortOrder"
            controls-position="right"
            :min="0"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <el-radio v-model="form.validFlag" :label="false">{{ td('att.sourceSystem.form.disable') }}</el-radio>
          <el-radio v-model="form.validFlag" :label="true">{{ td('att.sourceSystem.form.enable') }}</el-radio>
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
          <el-input
            type="textarea"
            :maxlength="500"
            show-word-limit
            v-model="form.description"
            :placeholder="td('common.form.descriptionPlaceholder')"
          />
        </el-form-item>

        <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
          <el-input
            type="textarea"
            :maxlength="500"
            show-word-limit
            v-model="form.remark"
            :placeholder="td('common.form.remarkPlaceholder')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm"
            >{{ td('common.button.confirm') }}</el-button
          >
        </div>
      </template>
    </el-dialog>

    <!-- Source system details dialog box -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form
        ref="sourceSystemRef"
        :model="form"
        label-width="90px"
        class="column-form"
       :label-position="labelPosition">
        <el-form-item :label="td('common.texts.number')" prop="id" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.name')" prop="name" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.name }}
          </div>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.type')" prop="type" :label-position="labelPosition">
          <div class="form-readonly">
            {{ getDictLabel(sys_source_system_type, form.type) }}
          </div>
        </el-form-item>

        <el-form-item :label="td('att.sourceSystem.form.responsiblePerson')" prop="responsiblePersonName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.responsiblePersonName }}
          </div>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.contactPerson')" prop="contactPersonName" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.contactPersonName }}
          </div>
        </el-form-item>
        <el-form-item :label="td('att.sourceSystem.form.sortOrder')" prop="sortOrder" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.sortOrder }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.validFlag ? td('att.sourceSystem.form.enable') : td('att.sourceSystem.form.disable') }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="description" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
          <div class="form-readonly textarea">
            {{ form.remark ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.createdBy')" prop="createBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.createBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.createdTime')" prop="createTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy" :label-position="labelPosition">
          <div class="form-readonly">
            {{ form.updateBy }}
          </div>
        </el-form-item>

        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime" :label-position="labelPosition">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">{{ td('common.button.close') }} </el-button>
        </div>
      </template>
    </el-dialog>

    <DeleteConfirmDialog
      ref="deleteConfirmDialog"
      @confirm-delete="handleDelete"
    />
  </div>
</template>

<script setup name="SourceSystem">
import { useI18n } from 'vue-i18n'
import useDefaultLang from "@/composables/useDefaultLang";
import {
  listSourceSystem,
  getSourceSystem,
  delSourceSystem,
  addSourceSystem,
  updateSourceSystem,
} from "@/api/att/sourceSystem/sourceSystem.js";
import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import DeleteConfirmDialog from "@/components/DeleteConfirmDialog";
import { reactive, ref, toRefs, getCurrentInstance, onMounted } from "vue";

const { t } = useI18n();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { sys_source_system_type } = proxy.useDict("sys_source_system_type");

// Get user list options
const userOptions = ref([]);

// Load user list
function loadUserOptions() {
  deptUserTree().then((response) => {
    const options = response.data.map((item) => ({
      label: item.nickName,
      value: item.userId,
      ...item,
    }));
    userOptions.value = options;
    const responsiblePersonItem = searchStore.items.find(
      (i) => i.prop === "responsiblePerson"
    );
    if (responsiblePersonItem?.component) {
      responsiblePersonItem.component.options = options;
    }

    const contactPersonItem = searchStore.items.find(
      (i) => i.prop === "contactPerson"
    );
    if (contactPersonItem?.component) {
      contactPersonItem.component.options = options;
    }
  });
}

// Get username based on user ID
function getUserLabel(userId) {
  if (!userId) return "-";
  const user = userOptions.value.find((u) => u.value === userId);
  return user ? user.label : userId;
}

// Get tags based on dictionary value
function getDictLabel(dictOptions, value) {
  if (!value) return "-";
  const dict = dictOptions.find((d) => d.value === value);
  return dict ? dict.label : value;
}

const store = reactive({
  rows: [],
});

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "desc" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    { label: computed(()=>td('common.texts.number')), prop: "id", width: 60, sortable: true },
    { label: computed(()=>td('att.sourceSystem.table.name')), prop: "name", align: "left", width: 150 },
    {
      label: computed(()=>td('att.sourceSystem.table.type')),
      prop: "type",
      width: 120,
      slot: "type",
    },
    {
      label: computed(()=>td('common.texts.description')),
      prop: "description",
      align: "left",
      width: 200,
      showOverflowTooltip: true,
    },
    {
      label: computed(()=>td('common.texts.status')),
      prop: "validFlag",
      width: 80,
      slot: "validFlag",
    },
    {
      label: computed(()=>td('common.texts.sortOrder')),
      prop: "sortOrder",
      sortableKey: "sort_order",
      width: 80,
      sortable: true,
    },
    {
      label: computed(()=>td('att.sourceSystem.table.responsiblePerson')),
      prop: "responsiblePersonName",
      width: 160,
    },
    {
      label: computed(()=>td('att.sourceSystem.table.contactPerson')),
      prop: "contactPersonName",
      width: 160
    },
    { label: computed(()=>td('common.texts.createdBy')), prop: "createBy", width: 120 },
    {
      label: computed(()=>td('common.texts.createdTime')),
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: computed(()=>td('common.texts.operation')), width: 240, fixed: "right", slot: "handle" },
  ],
  func: listSourceSystem,
  params: {},
  events: {},
});

const searchStore = reactive({
  items: [
    {
      label: computed(()=>td('att.sourceSystem.form.name')),
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: computed(()=>td('att.sourceSystem.form.namePlaceholder')) },
    },
    {
      label: computed(()=>td('att.sourceSystem.form.type')),
      prop: "type",
      component: {
        is: "select",
        placeholder: computed(()=>td('att.sourceSystem.form.typePlaceholder')),
        options: sys_source_system_type,
      },
    },
    {
      label: computed(()=>td('common.texts.status')),
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: computed(()=>td('common.form.statusPlaceholder')),
        options: [
          { value: true, label: td('att.sourceSystem.form.enable') },
          { value: false, label: td('att.sourceSystem.form.disable') },
        ],
      },
    },
    {
      label: computed(()=>td('att.sourceSystem.form.responsiblePerson')),
      prop: "responsiblePerson",
      component: {
        is: "select",
        placeholder: computed(()=>td('att.sourceSystem.form.responsiblePersonPlaceholder')),
        options: [],
      },
    },
    {
      label: computed(()=>td('att.sourceSystem.form.contactPerson')),
      prop: "contactPerson",
      component: {
        is: "select",
        placeholder: computed(()=>td('att.sourceSystem.form.contactPersonPlaceholder')),
        options: [],
      },
    },
  ],
});

const open = ref(false);
const openDetail = ref(false);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: td('att.sourceSystem.message.nameRequired'), trigger: "blur" }],
    type: [{ required: true, message: td('att.sourceSystem.message.typeRequired'), trigger: "blur" }],
  },
});

const { form, rules } = toRefs(data);

// Click to query
function handleQueryClick() {
  tableRef.value.getList();
}

// Reset query
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

/** Change enabled status value */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('att.sourceSystem.form.enable') : td('att.sourceSystem.form.disable');
  proxy.$modal
    .confirm(td('att.sourceSystem.message.confirmStatus').replace("<status>", text).replace("<name>", row.name))
    .then(function () {
      updateSourceSystem({ id: row.id, validFlag: row.validFlag }).then(
        (response) => {
          proxy.$modal.msgSuccess(td('att.sourceSystem.message.statusSuccess').replace("<status>", text));
          tableRef.value.getList();
        }
      );
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    name: null,
    type: null,
    sortOrder: 0,
    description: null,
    validFlag: false,
    responsiblePerson: null,
    contactPerson: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("sourceSystemRef");
}

/** Add button operation */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('att.sourceSystem.title.add');
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id;
  getSourceSystem(_id).then((response) => {
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    form.value.responsiblePerson = Number(response.data.responsiblePerson);
    form.value.contactPerson = Number(response.data.contactPerson);
    open.value = true;
    title.value = td('att.sourceSystem.title.edit');
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id;
  getSourceSystem(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('att.sourceSystem.title.detail');
  });
}
/** submit button */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["sourceSystemRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateSourceSystem(form.value)
          .then((response) => {
            submitLoading.value = false;
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {
            submitLoading.value = false;
          });
      } else {
        addSourceSystem(form.value)
          .then((response) => {
            submitLoading.value = false;
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            tableRef.value.getList();
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
/** Delete button action */
function handleDelete(row) {
  const invalidIds = [];
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    // _ids = store.rows.map((item) => item.id).join(",");
    store.rows.forEach((item) => {
      // When validFlag is false, record id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
  }
  proxy.$modal
    .confirm(
      td('att.sourceSystem.message.deleteConfirm').replace("<deletable>", invalidIds.length).replace("<undeletable>", store.rows.length - invalidIds.length)
    )
    .then(function () {
      return delSourceSystem(invalidIds);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {
      // User cancels deletion operation
    });
}

// initialization
loadUserOptions();
</script>
