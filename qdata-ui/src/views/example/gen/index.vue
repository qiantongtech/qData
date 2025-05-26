<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="75px"
      >
        <el-form-item label="ID" prop="id">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.id"
            placeholder="请输入ID"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入部门名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.leader"
            placeholder="请输入负责人"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.phone"
            placeholder="请输入联系电话"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.email"
            placeholder="请输入邮箱"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="部门状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择部门状态"
            clearable
            class="el-form-input-width"
          >
            <el-option
              v-for="dict in sys_notice_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
              v-hasPermi="['gen:dept:add']"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="toggleExpandAll"
              >展开/折叠</el-button
            >
          </el-col>
        </el-row>
        <right-toolbar
          v-model:showSearch="showSearch"
          @queryTable="getList"
        ></right-toolbar>
      </div>

      <el-table
        height="60vh"
        v-if="refreshTable"
        v-loading="loading"
        :data="deptList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column label="部门名称" prop="name" />
        <el-table-column label="负责人" align="center" prop="leader">
          <template #default="scope">
            {{ scope.row.leader || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="联系电话" align="center" prop="phone">
          <template #default="scope">
            {{ scope.row.phone || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="邮箱" align="center" prop="email">
          <template #default="scope">
            {{ scope.row.email || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="部门状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_notice_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['gen:dept:edit']"
              >修改</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['gen:dept:add']"
              >新增</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['gen:dept:remove']"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加或修改示例部门对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="父部门id" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择父部门id"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in sys_notice_status"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否有效" prop="validFlag">
              <el-input v-model="form.validFlag" placeholder="请输入是否有效" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="删除标志" prop="delFlag">
              <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Dept">
import {
  listDept,
  getDept,
  delDept,
  addDept,
  updateDept,
} from "@/api/example/gen/dept";

const { proxy } = getCurrentInstance();
const { sys_notice_status } = proxy.useDict("sys_notice_status");

const deptList = ref([]);
const deptOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(true);
const refreshTable = ref(true);
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    id: null,
    name: null,
    leader: null,
    phone: null,
    email: null,
    status: null,
  },
  rules: {
    parentId: [
      { required: true, message: "父部门id不能为空", trigger: "blur" },
    ],
    status: [
      { required: true, message: "部门状态不能为空", trigger: "change" },
    ],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" },
    ],
    updateTime: [
      { required: true, message: "更新时间不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询示例部门列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && "" != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && "" != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listDept(queryParams.value).then((response) => {
    deptList.value = proxy.handleTree(response.data, "id", "parentId");
    loading.value = false;
  });
}

/** 查询示例部门下拉树结构 */
function getTreeselect() {
  listDept().then((response) => {
    deptOptions.value = [];
    const data = { id: 0, name: "顶级节点", children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value.push(data);
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
    parentId: null,
    name: null,
    leader: null,
    phone: null,
    email: null,
    status: null,
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
  proxy.resetForm("deptRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "新增示例部门";
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** 修改按钮操作 */
// async function handleUpdate(row) {
//   reset();
//   await getTreeselect();
//   if (row != null) {
//     form.value.parentId = row.parentId;
//   }
//   getDept(row.id).then(response => {
//     form.value = response.data;
//     open.value = true;
//     title.value = "修改示例部门";
//   });
// }

async function handleUpdate(row) {
  reset();
  // await getTreeselect();
  const response = await listDept();
  deptOptions.value = [];
  // 过滤节点的计算属性
  const filteredDepts = response.data.filter((d) => {
    // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
    return (
      d.id !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  const data = { id: 0, name: "顶级节点", children: [] };
  data.children = proxy.handleTree(filteredDepts, "id", "parentId");
  deptOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getDept(row.id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改示例部门";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["deptRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDept(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addDept(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
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
    .confirm('是否确认删除示例部门编号为"' + row.id + '"的数据项？')
    .then(function () {
      return delDept(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getList();
</script>
