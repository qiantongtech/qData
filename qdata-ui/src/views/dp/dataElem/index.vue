<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入数据元类目'"
        @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
            v-show="showSearch" @submit.prevent>
            <el-form-item label="中文名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入中文名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="英文名称" prop="engName">
              <el-input class="el-form-input-width" v-model="queryParams.engName" placeholder="请输入英文名称" clearable
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="type">
              <el-select class="el-form-input-width" v-model="queryParams.type" placeholder="请选择类型">
                <el-option v-for="dict in dp_data_elem_code_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
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
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dp:dataElem:dataelem:add']"
                  @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="primary" plain :disabled="single" @click="handleUpdate"
                  v-hasPermi="['dp:dataElem:dataelem:edit']" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                  v-hasPermi="['dp:dataElem:dataelem:remove']" @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                </el-button>
              </el-col>
              <!--          <el-col :span="1.5">-->
              <!--            <el-button type="info" plain @click="handleImport" v-hasPermi="['dp:dataElem:dataelem:export']"-->
              <!--                       @mousedown="(e) => e.preventDefault()">-->
              <!--              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
              <!--            </el-button>-->
              <!--          </el-col>-->
              <!--          <el-col :span="1.5">-->
              <!--            <el-button type="warning" plain @click="handleExport" v-hasPermi="['dp:dataElem:dataelem:export']"-->
              <!--                       @mousedown="(e) => e.preventDefault()">-->
              <!--              <i class="iconfont-mini icon-download-line mr5"></i>导出-->
              <!--            </el-button>-->
              <!--          </el-col>-->
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe height="58vh" v-loading="loading" :data="dpDataElemList"
            @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" width="80" />
            <el-table-column v-if="getColumnVisibility(1)" label="中文名称" :show-overflow-tooltip="true" align="left"
              prop="name">
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" label="英文名称" :show-overflow-tooltip="true" align="left"
              prop="engName">
              <template #default="scope">
                {{ scope.row.engName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" width="100" label="类型" align="center" prop="type">
              <template #default="scope">
                {{ typeFormat(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(4)" label="数据元类目" width="140" :show-overflow-tooltip="true"
              align="left" prop="catCode">
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" width="100" label="状态" align="center" prop="status">
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" active-value="1"
                  inactive-value="0" @change="
                    (e) => handleStatusChange(scope.row.id, scope.row, e)
                  " />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" width="240" label="元描述" align="left" prop="description"
              :show-overflow-tooltip="true">
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                  v-hasPermi="['dp:dataElem:dataelem:edit']">修改
                </el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['dp:dataElem:dataelem:remove']">删除
                </el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                  v-hasPermi="['dp:dataElem:dataelem:edit']">详情
                </el-button>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
              </div>
            </template>
          </el-table>

          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <!-- 新增或修改数据元对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="dpDataElemRef" :model="form" :rules="rules" label-width="100px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="中文名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入中文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称" prop="engName">
              <el-input v-model="form.engName" placeholder="请输入英文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="form.type" :disabled="form.id">
                <el-radio v-for="dict in dp_data_elem_code_type" :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据元类目" prop="catCode">
              <el-tree-select v-model="form.catCode" :data="deptOptions"
                :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" placeholder="请选择所属类目"
                check-strictly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="字段类型" prop="columnType">
              <el-select v-model="form.columnType" placeholder="请选择字段类型">
                <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_disable" :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="personCharge">
              <!--                <el-input v-model="form.managerId" placeholder="请选择负责人" />-->
              <el-select v-model="form.personCharge" @change="handleChange" filterable placeholder="请选择">
                <el-option v-for="item in managerOptions" :key="item.userId" :label="item.nickName"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactNumber">
              <el-input disabled v-model="form.contactNumber" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="元描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入元描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload">
          <upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />
              是否更新已经存在的数据元数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">下载模板
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">取 消</el-button>
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!--        &lt;!&ndash;数据元字段详情&ndash;&gt;-->
    <!--        <data-elem-detail-dialog ref="detailDialog" />-->
    <!--        &lt;!&ndash;数据元代码详情&ndash;&gt;-->
    <!--        <data-elem-code-detail-dialog ref="detailCodeDialog" />-->
  </div>
</template>

<script setup name="DpDataElem">
import DeptTree from "@/components/DeptTree";
import {
  listDpDataElem,
  getDpDataElem,
  delDpDataElem,
  addDpDataElem,
  updateDpDataElem,
  updateStatusDpDataElem,
} from "@/api/dp/dataElem/dpDataElem";
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttDataElemCat } from "@/api/att/cat/attDataElemCat/attDataElemCat";
import { getToken } from "@/utils/auth.js";
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type"
);
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
/** 类型字典翻译 */
function typeFormat(row) {
  return proxy.selectDictLabel(dp_data_elem_code_type.value, row.type);
}

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "中文名称", visible: true },
  { key: 2, label: "英文名称", visible: true },
  { key: 3, label: "类型", visible: true },
  { key: 4, label: "数据元类目", visible: true },
  { key: 5, label: "当前状态", visible: true },
  { key: 6, label: "元描述", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedDpDataElemRuleRel = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/dp/dpDataElem/importData",
});

const data = reactive({
  form: { status: "0" },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    engName: null,
    catCode: null,
    type: null,
  },
  rules: {
    name: [{ required: true, message: "中文不能为空", trigger: "blur" }],
    engName: [
      { required: true, message: "英文不能为空", trigger: "blur" },
      {
        pattern: /^[a-zA-Z_]+$/,
        message: "只能包含英文字母和下划线",
        trigger: "blur",
      },
    ],
    catCode: [{ required: true, message: "类目编码不能为空", trigger: "blur" }],
    status: [{ required: true, message: "状态不能为空", trigger: "change" }],
    type: [{ required: true, message: "类型不能为空", trigger: "change" }],
    columnType: [
      { required: true, message: "字段类型不能为空", trigger: "change" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);
const managerOptions = ref([]);
/** 查询数据元列表 */
function getList() {
  loading.value = true;
  listDpDataElem(queryParams.value).then((response) => {
    dpDataElemList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
function handleChange(value) {
  const selectedManager = managerOptions.value.find(
    (item) => item.userId === form.value.personCharge
  );
  form.value.contactNumber = selectedManager.phonenumber; // 将完整对象存储到 form 中
}
// 取消按钮
function cancel() {
  open.value = false;
  reset();
}
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  handleQuery();
}
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => { });
  }
}; /** 查询部门下拉树结构 */
// 表单重置
function reset() {
  form.value = {
    id: null,
    code: null,
    name: null,
    engName: null,
    catCode: null,
    type: "1",
    personCharge: null,
    contactNumber: null,
    columnType: null,
    status: "0",
    description: null,
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
  dpDataElemRuleRelList.value = [];
  proxy.resetForm("dpDataElemRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);
/** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  reset();
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
function getDeptTree() {
  listAttDataElemCat().then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: "数据元类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  if (queryParams.value.catCode) {
    form.value.catCode = queryParams.value.catCode;
  }
  open.value = true;
  title.value = "新增数据元";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDpDataElem(_id).then((response) => {
    form.value = response.data;
    dpDataElemRuleRelList.value = response.data.dpDataElemRuleRelList;
    form.value.personCharge = Number(response.data.personCharge);
    open.value = true;
    title.value = "修改数据元";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  if (row.type == 1) {
    routeTo("/dp/dataElem/dataElemDetail", row);
  } else {
    routeTo("/dp/dataElem/dataElemCodeDetail", row);
  }
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dpDataElemRef"].validate((valid) => {
    if (valid) {
      form.value.dpDataElemRuleRelList = dpDataElemRuleRelList.value;
      if (form.value.id != null) {
        updateDpDataElem(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addDpDataElem(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
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
    .confirm('是否确认删除数据元编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDpDataElem(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

/** 数据元数据规则关联信息序号 */
function rowDpDataElemRuleRelIndex({ row, rowIndex }) {
  row.index = rowIndex + 1;
}

/** 数据元数据规则关联信息新增按钮操作 */
function handleAddDpDataElemRuleRel() {
  let obj = {};
  obj.ruleType = "";
  obj.ruleId = "";
  obj.ruleConfig = "";
  obj.remark = "";
  dpDataElemRuleRelList.value.push(obj);
}

/** 数据元数据规则关联信息删除按钮操作 */
function handleDeleteDpDataElemRuleRel() {
  if (checkedDpDataElemRuleRel.value.length == 0) {
    proxy.$modal.msgError("请先选择要删除的数据元数据规则关联信息数据");
  } else {
    const dpDataElemRuleRels = dpDataElemRuleRelList.value;
    const checkedDpDataElemRuleRels = checkedDpDataElemRuleRel.value;
    dpDataElemRuleRelList.value = dpDataElemRuleRels.filter(function (item) {
      return checkedDpDataElemRuleRels.indexOf(item.index) == -1;
    });
  }
}

/** 复选框选中数据 */
function handleDpDataElemRuleRelSelectionChange(selection) {
  checkedDpDataElemRuleRel.value = selection.map((item) => item.index);
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "dp/dpDataElem/export",
    {
      ...queryParams.value,
    },
    `dpDataElem_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "数据元导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `dpDataElem_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    "导入结果",
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? "启用" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据元吗？')
    .then(function () {
      updateStatusDpDataElem(id, row.status).then((response) => {
        proxy.$modal.msgSuccess("操作成功");
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}
// function handleStatusChange(row) {
//   let text = row.status === "0" ? "启用" : "停用";
//   proxy.$modal
//     .confirm('确认要"' + text + '""' + row.roleName + '"角色吗?')
//     .then(function () {
//       return changeRoleStatus(row.roleId, row.status);
//     })
//     .then(() => {
//       proxy.$modal.msgSuccess(text + "成功");
//     })
//     .catch(function () {
//       row.status = row.status === "0" ? "1" : "0";
//     });
// }
/** ---------------------------------**/

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
getDeptTree();
getList();
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//上传附件样式调整
::v-deep {

  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}
</style>
