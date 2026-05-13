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
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
        >
          删除
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
            >修改</el-button
          >
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            :disabled="row.validFlag"
            >删除</el-button
          >
          <el-button link type="primary" icon="view" @click="handleDetail(row)"
            >详情</el-button
          >
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 添加或修改来源系统对话框 -->
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
      >
        <el-form-item label="系统名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入系统名称" />
        </el-form-item>

        <el-form-item label="系统类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择系统类型">
            <el-option
              v-for="dict in sys_source_system_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-select
            v-model="form.responsiblePerson"
            filterable
            placeholder="请选择负责人"
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

        <el-form-item label="对接人" prop="contactPerson">
          <el-select
            v-model="form.contactPerson"
            filterable
            placeholder="请选择对接人"
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
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number
            style="width: 100%"
            v-model="form.sortOrder"
            controls-position="right"
            :min="0"
          />
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
          <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            v-model="form.description"
            placeholder="请输入描述"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            v-model="form.remark"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm"
            >确 定</el-button
          >
        </div>
      </template>
    </el-dialog>

    <!-- 来源系统详情对话框 -->
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
      >
        <el-form-item label="编号" prop="id">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item label="系统名称" prop="name">
          <div class="form-readonly">
            {{ form.name }}
          </div>
        </el-form-item>
        <el-form-item label="系统类型" prop="type">
          <div class="form-readonly">
            {{ getDictLabel(sys_source_system_type, form.type) }}
          </div>
        </el-form-item>

        <el-form-item label="负责人" prop="responsiblePersonName">
          <div class="form-readonly">
            {{ form.responsiblePersonName }}
          </div>
        </el-form-item>
        <el-form-item label="对接人" prop="contactPersonName">
          <div class="form-readonly">
            {{ form.contactPersonName }}
          </div>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <div class="form-readonly">
            {{ form.sortOrder }}
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <div class="form-readonly">
            {{ form.validFlag ? "启用" : "禁用" }}
          </div>
        </el-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <div class="form-readonly textarea">
            {{ form.remark ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item label="创建人" prop="createBy">
          <div class="form-readonly">
            {{ form.createBy }}
          </div>
        </el-form-item>

        <el-form-item label="创建时间" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新人" prop="updateBy">
          <div class="form-readonly">
            {{ form.updateBy }}
          </div>
        </el-form-item>

        <el-form-item label="更新时间" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">关闭 </el-button>
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

const { proxy } = getCurrentInstance();
const { sys_source_system_type } = proxy.useDict("sys_source_system_type");

// 获取用户列表选项
const userOptions = ref([]);

// 加载用户列表
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

// 根据用户ID获取用户名
function getUserLabel(userId) {
  if (!userId) return "-";
  const user = userOptions.value.find((u) => u.value === userId);
  return user ? user.label : userId;
}

// 根据字典值获取标签
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
    { label: "编号", prop: "id", width: 60, sortable: true },
    { label: "系统名称", prop: "name", align: "left", width: 150 },
    {
      label: "系统类型",
      prop: "type",
      width: 120,
      slot: "type",
    },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 200,
      showOverflowTooltip: true,
    },
    {
      label: "状态",
      prop: "validFlag",
      width: 80,
      slot: "validFlag",
    },
    {
      label: "排序",
      prop: "sortOrder",
      sortableKey: "sort_order",
      width: 80,
      sortable: true,
    },
    {
      label: "负责人",
      prop: "responsiblePersonName",
    },
    {
      label: "对接人",
      prop: "contactPersonName",
    },
    { label: "创建人", prop: "createBy", width: 120 },
    {
      label: "创建时间",
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    { label: "操作", width: 240, fixed: "right", slot: "handle" },
  ],
  func: listSourceSystem,
  params: {},
  events: {},
});

const searchStore = reactive({
  items: [
    {
      label: "系统名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入系统名称" },
    },
    {
      label: "系统类型",
      prop: "type",
      component: {
        is: "select",
        placeholder: "请选择系统类型",
        options: sys_source_system_type,
      },
    },
    {
      label: "状态",
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: "请选择状态",
        options: [
          { value: true, label: "启用" },
          { value: false, label: "禁用" },
        ],
      },
    },
    {
      label: "负责人",
      prop: "responsiblePerson",
      component: {
        is: "select",
        placeholder: "请选择负责人",
        options: [],
      },
    },
    {
      label: "对接人",
      prop: "contactPerson",
      component: {
        is: "select",
        placeholder: "请选择对接人",
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
    name: [{ required: true, message: "系统名称不能为空", trigger: "blur" }],
    type: [{ required: true, message: "系统类型不能为空", trigger: "blur" }],
  },
});

const { form, rules } = toRefs(data);

// 点击查询
function handleQueryClick() {
  tableRef.value.getList();
}

// 重置查询
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? "启用" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"来源系统吗？')
    .then(function () {
      updateSourceSystem({ id: row.id, validFlag: row.validFlag }).then(
        (response) => {
          proxy.$modal.msgSuccess(text + "成功");
          tableRef.value.getList();
        }
      );
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增来源系统";
}

/** 修改按钮操作 */
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
    title.value = "修改来源系统";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id;
  getSourceSystem(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "来源系统详情";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["sourceSystemRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateSourceSystem(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {});
      } else {
        addSourceSystem(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {});
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const invalidIds = [];
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    // _ids = store.rows.map((item) => item.id).join(",");
    store.rows.forEach((item) => {
      // 当 validFlag 为 false 时，记录 id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
  }
  proxy.$modal
    .confirm(
      `可删除${invalidIds.length}个，不可删除${
        store.rows.length - invalidIds.length
      }个，是否删除可删部分`
    )
    .then(function () {
      return delSourceSystem(invalidIds);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {
      // 用户取消删除操作
    });
}

// 初始化
loadUserOptions();
</script>
