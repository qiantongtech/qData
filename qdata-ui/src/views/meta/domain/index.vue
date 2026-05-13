<template>
  <div class="app-container" v-loading="store.loading">
    <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStroe.params"
          :tableRef="tableRef"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAddClick"
          v-hasPermi="['md:domain:add']"
        >
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDeleteColumnClick"
          v-hasPermi="['md:domain:remove']"
        >
          删除
        </el-button>
        <el-button
          class="toggle-expand-all"
          type="primary"
          plain
          @click="handleToggleExpandClick"
        >
          <svg-icon v-if="defaultExpandAll" icon-class="toggle" />
          <svg-icon v-else icon-class="expand" />
          <span>{{ defaultExpandAll ? "折叠" : "展开" }}</span>
        </el-button>
      </template>
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #valid-flag="{ row }">
          <el-switch
            v-model="row.validFlag"
            @change="handleStatusChange(row, $event)"
          />
        </template>
        <template #action="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleEditClick(row)"
            v-hasPermi="['md:domain:edit']"
          >
            修改
          </el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAddClick(row)"
            v-hasPermi="['md:domain:add']"
          >
            新增
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag"
            @click="handleDeleteClick(row)"
            v-hasPermi="['md:domain:remove']"
          >
            删除
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 新增/修改弹窗 -->
    <el-dialog
      v-model="dialog.open"
      :title="dialog.title"
      width="800"
      v-loading="dialog.loading"
    >
      <el-form
        :model="dialog.form"
        :rules="rules"
        ref="formRef"
        label-width="110px"
      >
        <el-form-item label="上级类目" prop="parentId">
          <el-tree-select
            filterable
            v-model="dialog.form.parentId"
            :data="store.treeDomains"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择上级类目"
            check-strictly
            default-expand-all
          />
        </el-form-item>
        <el-form-item label="业务域名称" prop="name">
          <el-input v-model="dialog.form.name" placeholder="请输入业务域名称" />
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <el-radio-group v-model="dialog.form.validFlag">
            <el-radio :label="true">启用</el-radio>
            <el-radio :label="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number
            v-model="dialog.form.sortOrder"
            controls-position="right"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述" class="row-full">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
        <el-form-item label="备注" class="row-full">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick">取消</el-button>
          <el-button type="primary" @click="handleConfirmClick">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MetaDomain">
import {
  listDomain,
  addDomain,
  delDomain,
  updateDomain,
  updateDomainStatus,
  getDomain,
  batchDeleteCheck,
} from "@/api/att/domain/domain.js";
import { ElMessage } from "element-plus";
import { computed, getCurrentInstance, reactive, ref } from "vue";

const DEFAULT_FORM = {
  validFlag: true,
  sortOrder: 0,
};

// 表单验证规则
const rules = {
  parentId: [{ required: true, message: "请选择上级类目", trigger: "change" }],
  name: [{ required: true, message: "请输入业务域名称", trigger: "blur" }],
};

const formRef = ref(null);
const tableRef = ref(null);
const store = reactive({
  rows: [],
  treeDomains: [],
  loading: false,
});

const { proxy } = getCurrentInstance();
const tableStroe = reactive({
  config: {
    stripe: true,
    notPagination: true,
    sort: true,
    table: {
      rowKey: "id",
      defaultSort: { prop: "sortOrder", order: "ascending" },
      treeProps: { children: "children", hasChildren: "hasChildren" },
      defaultExpandAll: true,
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
      // selectable: function (row) {
      //     return row.validFlag == true ? false : true;
      // }
    },
    { label: "业务域名称", prop: "name", width: 240, align: "left" },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    { label: "状态", prop: "validFlag", slot: "valid-flag", align: "center" },
    {
      label: "排序",
      prop: "sortOrder",
      sortable: true,
    },
    { label: "备注", prop: "remark" },
    { label: "创建人", prop: "createBy" },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: "操作", width: 240, slot: "action", align: "center" },
  ],
  func: getTreeData,
  params: {},
});

const searchStore = reactive({
  items: [
    {
      label: "业务域名称",
      prop: "name",
      component: { is: "input" },
    },
    {
      label: "上级类目",
      prop: "code",
      component: {
        is: "tree-select",
        data: store.treeDomains,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        checkStrictly: true,
      },
    },
  ],
});

// 新增/修改弹窗
const dialog = reactive({
  open: false,
  title: "",
  form: { ...DEFAULT_FORM },
});

const defaultExpandAll = computed({
  get() {
    return tableStroe.config.table.defaultExpandAll;
  },
  set(val) {
    tableStroe.config.table.defaultExpandAll = val;
  },
});

// 获取列表数据
function getTreeData(param) {
  return listDomain(param).then((res) => {
    const data = proxy.handleTree(res.data, "id", "parentId");
    if (Object.keys(tableStroe.params).length) return { data };
    // 查询全部的时候更新数据
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: JSON.parse(JSON.stringify(data)),
    };
    store.treeDomains.push(domains);
    return { data };
  });
}

// 点击新增
function handleAddClick(row) {
  if (row) {
    dialog.form.parentId = row.id;
  }
  dialog.title = "新增业务域";
  dialog.open = true;
  dialog.func = addDomain;
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  const ids = store.rows.map((item) => item.id);
  store.loading = true;
  batchDeleteCheck(ids).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    store.loading = false;
    ElMessageBox.confirm(
      `可删除${canDeleteCount}个，不可删除${cannotDeleteCount}个，是否删除可删部分`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success("删除成功");
          return;
        }
        return delDomain(canDeleteIds.toString());
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success("删除成功");
        tableRef.value.getList();
      });
  });
}

// 取消新增/修改
function handleCancelClick() {
  formRef.value.resetFields();
  dialog.form = {
    ...DEFAULT_FORM,
  };
  dialog.loading = false;
  dialog.open = false;
}

// 确认新增/修改
async function handleConfirmClick() {
  dialog.loading = true;
  const valid = await formRef.value.validate();
  dialog.loading = false;
  if (!valid) return;
  await dialog.func(dialog.form);
  proxy.$modal.msgSuccess(
    `${dialog.form.id ? "修改" : "新增"}业务域任务成功！`
  );
  handleCancelClick();
  tableRef.value.getList();
}

// 切换状态
function handleStatusChange(row, status) {
  ElMessageBox.confirm(
    `是否确认${status ? "上线" : "下线"}数据编号为${row.id}的业务域吗？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      return updateDomainStatus(row.id, status);
    })
    .then(() => {
      ElMessage.success(
        `编号为${row.id}的业务域${status ? "上线" : "下线"}成功!`
      );
      row.validFlag = status;
    })
    .catch(() => {
      row.validFlag = status ? false : true;
    });
}

// 打开修改弹窗
function handleEditClick(row) {
  dialog.open = true;
  dialog.func = updateDomain;
  dialog.title = "修改任务";
  getDomain(row.id).then((res) => {
    dialog.form = res.data;
  });
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(`是否确认删除编号为${row.id}的数据项？`, "系统提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      return delDomain(row.id);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}

// 展开
function handleToggleExpandClick() {
  defaultExpandAll.value = !defaultExpandAll.value;
  tableRef.value.reload();
}
</script>
