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
  <div class="app-container" ref="app-container">
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          :tableRef="tableRef"
          :visible-count="4"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dm:themeDomain:add']"
        >
          {{ td('common.button.add', '新增') }}
        </el-button>
        <el-button
          class="extend-btn"
          type="primary"
          plain
          @click="toggleExpandAll"
        >
          <svg-icon v-if="defaultExpandAll" icon-class="toggle" />
          <svg-icon v-else icon-class="expand" />
          <span>{{ defaultExpandAll ? td('common.button.fold', '折叠') : td('common.button.expand', '展开') }}</span>
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dm:themeDomain:edit']"
          >
            {{ td('common.button.update', '修改') }}
          </el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd(row)"
            v-hasPermi="['dm:themeDomain:add']"
          >
            {{ td('common.button.add', '新增') }}
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['dm:themeDomain:remove']"
          >
            {{ td('common.button.delete', '删除') }}
          </el-button>
        </template>

        <template #validFlag="{ row }">
          <el-switch
            v-model="row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(row)"
          >
          </el-switch>
        </template>
      </qt-table>
    </qt-wrap>
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="themeDomainRef"
        :model="form"
        :rules="rules"
        label-width="130px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.name', '主题域名称')" prop="name">
              <el-input v-model="form.name" :placeholder="td('dm.themeDomain.namePlaceholder', '请输入主题域名称')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.parentId', '上级主题域')" prop="parentId">
              <el-tree-select
                filterable
                v-model="form.parentId"
                :data="attDataElemCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dm.themeDomain.parentIdPlaceholder', '请选择上级主题域')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.dataLayerId', '数仓分层')" prop="dataLayerId">
              <el-tree-select
                filterable
                default-expand-all
                v-model="form.dataLayerId"
                :data="dataLayerOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('dm.themeDomain.dataLayerIdPlaceholder', '请选择数仓分层')"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.engName', '英文缩写')" prop="engName">
              <el-input v-model="form.engName" :placeholder="td('dm.themeDomain.engNamePlaceholder', '请输入英文缩写')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.ownerId', '负责人')" prop="ownerUserId">
              <el-select
                v-model="form.ownerUserId"
                filterable
                :placeholder="td('dm.themeDomain.ownerIdPlaceholder', '请选择负责人')"
                @change="handleOwnerChange"
              >
                <el-option
                  v-for="item in managerOptions"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('dm.themeDomain.ownerPhone', '负责人电话')" prop="ownerUserPhoneNumber">
              <el-input
                v-model="form.ownerUserPhoneNumber"
                :placeholder="td('dm.themeDomain.ownerPhonePlaceholder', '请输入负责人电话')"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description', '描述')" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="256"
                :min-height="256"
                show-word-limit
                :placeholder="td('common.form.descriptionPlaceholder', '请输入描述')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark', '备注')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="500"
                show-word-limit
                :placeholder="td('common.form.remarkPlaceholder', '请输入备注')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel', '取消') }}</el-button>
          <el-button type="primary" size="mini" @click="submitForm"
            >{{ td('common.button.confirm', '确定') }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataElemCat">
import useDefaultLang from "@/composables/useDefaultLang"
import {
  getThemeDomain,
  addThemeDomain,
  updateThemeDomain,
  listThemeDomain,
  delThemeDomain,
} from "@/api/dm/themeDomain/themeDomain.js";
import { deptUserTree, getUser } from "@/api/system/system/user.js";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import QtWrap from "@/components/QtWrap";
import QtTable from "@/components/QtTable";
import QtSearchBar from "@/components/QtSearchBar";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const attDataElemCatOptions = ref([]);
const managerOptions = ref([]);
const dataLayerOptions = ref([]);
const open = ref(false);
const title = ref("");
const tableRef = ref(null);

// 添加计算属性用于控制展开/折叠
const defaultExpandAll = computed({
  get() {
    return tableStore.config.table.defaultExpandAll;
  },
  set(val) {
    tableStore.config.table.defaultExpandAll = val;
  },
});

const data = reactive({
  form: {},
  queryParams: {
    name: null,
    code: null,
    dataLayerId: null,
    ownerUserId: null,
  },
  rules: {
    name: [{ required: true, message: td('dm.themeDomain.nameRequired', '主题域名称不能为空'), trigger: "blur" }],
    parentId: [
      { required: true, message: td('dm.themeDomain.parentIdRequired', '上级主题域不能为空'), trigger: "blur" },
    ],
    engName: [
      { required: true, message: td('dm.themeDomain.engNameRequired', '英文缩写不能为空'), trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: td('dm.themeDomain.englishOnly', '只能输入英文字符'), trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: td('dm.themeDomain.ownerRequired', '负责人不能为空'), trigger: "blur" },
    ],
    dataLayerId: [
      { required: true, message: td('dm.themeDomain.dataLayerIdRequired', '数仓分层不能为空'), trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

const tableStore = reactive({
  config: {
    stripe: true,
    notPagination: true,
    notPaginationParams: true,
    table: {
      rowKey: "id",
      defaultExpandAll: false,
      lazy: false,
      load: null,
      treeProps: { children: "children", hasChildren: "hasChildren" },
      defaultSort: { prop: "createTime", order: "descending" },
    },
  },
  columns: [
    {
      label: td('dm.themeDomain.name', '主题域名称'),
      prop: "name",
      width: 200,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('common.texts.description', '描述'),
      prop: "description",
      width: 250,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td('dm.themeDomain.engName', '英文缩写'),
      prop: "engName",
      width: 200,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    { label: td('dm.themeDomain.dataLayerId', '数仓分层'), prop: "dataLayerName", align: "left", width: 140 },
    { label: td('dm.themeDomain.ownerId', '负责人'), prop: "ownerUserName", align: "left" },
    {
      label: td('dm.themeDomain.ownerPhone', '负责人电话'),
      prop: "ownerUserPhoneNumber",
      width: 140,
      align: "left",
    },
    { label: td('common.texts.status', '状态'), prop: "validFlag", slot: "validFlag", width: 100 },
    { label: td('common.texts.remark', '备注'), prop: "remark", align: "left" },

    { label: td('common.texts.createdBy', '创建人'), prop: "createBy", align: "left" },
    {
      label: td('common.texts.createdTime', '创建时间'),
      prop: "createTime",
      sortable: true,
      date: true,
      width: 180,
    },
    { label: td('common.texts.operation', '操作'), width: 220, slot: "action", fixed: "right" },
  ],
  func: listThemeDomain,
  params: queryParams,
  events: {
    formatData: (data) => proxy.handleTree(data, "id", "parentId"),
  },
});

const searchStore = reactive({
  items: [
    {
      label: td('dm.themeDomain.name', '主题域名称'),
      prop: "name",
      component: { is: "input", placeholder: td('dm.themeDomain.namePlaceholder', '请输入主题域名称') },
    },
    {
      label: td('dm.themeDomain.parentId', '上级主题域'),
      prop: "code",
      component: {
        is: "tree-select",
        data: attDataElemCatOptions,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dm.themeDomain.parentIdPlaceholder', '请选择上级主题域'),
        checkStrictly: true,
      },
    },
    {
      label: td('dm.themeDomain.dataLayerId', '数仓分层'),
      prop: "dataLayerId",
      component: {
        is: "tree-select",
        data: dataLayerOptions,
        props: { value: "id", label: "name", children: "children" },
        valueKey: "id",
        placeholder: td('dm.themeDomain.dataLayerIdPlaceholder', '请选择数仓分层'),
        checkStrictly: true,
      },
    },
    {
      label: td('dm.themeDomain.ownerId', '负责人'),
      prop: "ownerUserId",
      component: {
        is: "select",
        options: computed(() =>
          managerOptions.value.map((item) => ({
            label: item.nickName,
            value: item.userId,
          }))
        ),
        placeholder: td('dm.themeDomain.ownerIdPlaceholder', '请选择负责人'),
      },
    },
  ],
});

/** 查询主题域管理列表 */
function getList() {
  tableRef.value?.getList();
}

function getDataTree() {
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('dm.themeDomain.topNode', '顶级节点'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
}

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function getDataLayerTree() {
  treeDataLayer().then((response) => {
    const disableRoot = (list) => {
      return list.map((item) => {
        const newItem = { ...item };
        if (!item.parentId || item.parentId === 0 || item.parentId === "0") {
          newItem.disabled = true;
        }
        if (item.children && item.children.length) {
          newItem.children = disableRoot(item.children);
        }
        return newItem;
      });
    };
    dataLayerOptions.value = disableRoot(response.data);
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    parentId: null,
    description: null,
    code: null,
    engName: null,
    ownerUserId: null,
    ownerUserPhoneNumber: null,
    dataLayerId: null,
    // validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("themeDomainRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('dm.themeDomain.enableText', '启用') : td('dm.themeDomain.disableText', '禁用');
  proxy.$modal
    .confirm(td('dm.themeDomain.confirmStatusChange', '确认要"<text>","<name>"主题域吗？').replace('<text>', text).replace('<name>', row.name))
    .then(() => {
      updateThemeDomain({
        id: row.id,
        parentId: row.parentId,
        validFlag: row.validFlag,
      })
        .then((response) => {
          proxy.$modal.msgSuccess(td('common.message.operationSuccess', '操作成功'));
          getList();
        })
        .catch((err) => {
          row.validFlag = !row.validFlag;
        });
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
    });
}

/** 重置按钮操作 */
function resetQuery() {
  Object.keys(queryParams.value).forEach((key) => {
    queryParams.value[key] = null;
  });
  handleQuery();
}

/** 当负责人改变时，更新电话号码 */
const handleOwnerChange = (selectedValue) => {
  const selectedUser = managerOptions.value.find(
    (user) => user.userId == selectedValue
  );
  form.value.ownerUserPhoneNumber = selectedUser?.phonenumber || "";
};

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  // getTreeselect();
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('dm.themeDomain.topNode', '顶级节点'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td('dm.themeDomain.addTitle', '新增主题域');
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  defaultExpandAll.value = !defaultExpandAll.value;
  tableRef.value.reload();
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  // await getTreeselect();
  const response = await listThemeDomain();
  attDataElemCatOptions.value = [];
  // 过滤节点的计算属性
  const filteredDepts = response.data.filter((d) => {
    // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
    return (
      d.ID !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  const data = { id: 0, name: td('dm.themeDomain.topNode', '顶级节点'), children: [] };
  data.children = proxy.handleTree(filteredDepts, "id", "parentId");
  attDataElemCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getThemeDomain(row.id).then((response) => {
    //把createTime过滤掉
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;

    open.value = true;
    title.value = td('dm.themeDomain.editTitle', '修改主题域');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["themeDomainRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateThemeDomain(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess', '修改成功'));
          open.value = false;
          getList();
        });
      } else {
        addThemeDomain(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess', '新增成功'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal
    .confirm(td('dm.themeDomain.confirmDelete', '是否确认删除主题域管理编号为"<name>"的数据项？').replace('<name>', row.name))
    .then(function () {
      return delThemeDomain(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess', '删除成功'));
    })
    .catch(() => {});
}

// 初始化数据
onMounted(() => {
  getDataTree();
  getManagerOptions();
  getDataLayerTree();
});
</script>
<style scoped lang="scss">
.extend-btn {
  .svg-icon {
    font-size: 12px;
    margin-right: 6px;
  }
}
</style>