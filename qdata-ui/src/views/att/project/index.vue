<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="att/attProject.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
        v-show="showSearch" @submit.prevent>
        <el-form-item label="项目名称" prop="name">
          <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入项目名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="负责人" prop="managerId">
          <el-select v-model="queryParams.managerId" class="el-form-input-width" @change="handleChange" filterable
            placeholder="请选择负责人">
            <el-option v-for="item in managerOptions" :key="item.userId" :label="item.nickName" :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"
            v-hasPermi="['att:project:query']">
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:project:add']"
              @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <!-- <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            :disabled="single"
                            @click="handleUpdate"
                            v-hasPermi="['att:project:edit']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                            v-hasPermi="['att:project:remove']" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col> -->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="attProjectList" :default-sort="defaultSort"
        @sort-change="handleSortChange">
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <!--       <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" />-->
        <el-table-column label="编号" prop="id" width="80" align="center" v-if="getColumnVisibility(1)">
          <template #default="scope">
            {{ scope.row.id || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="项目名称" align="left" prop="name" v-if="getColumnVisibility(2)" width="200">
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="描述" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(3)" width="300">
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" prop="managerId" v-if="getColumnVisibility(4)">
          <template #default="scope">
            {{ scope.row.nickName || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="联系方式" align="center" prop="managerPhone" v-if="getColumnVisibility(5)">
          <template #default="scope">
            {{ scope.row.managerPhone || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="创建人" :show-overflow-tooltip="{ effect: 'light' }"
          align="left" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <!--   sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
        <el-table-column v-if="getColumnVisibility(6)" label="创建时间" align="center" prop="createTime" width="150">
          <template #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
              }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="validFlag" v-if="getColumnVisibility(8)">
          <template #default="scope">
            <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="left" width="200" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(9)">
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:project:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['att:project:remove']">删除</el-button>
            <el-button link type="primary" icon="view" v-hasPermi="['att:project:query']"
              @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 新增或修改项目对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="attProjectRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="managerId">
              <!--                <el-input v-model="form.managerId" placeholder="请选择负责人" />-->
              <el-select v-model="form.managerId" @change="handleChange" filterable placeholder="请选择负责人">
                <el-option v-for="item in managerOptions" :key="item.userId" :label="item.nickName"
                  :value="item.userId">
                </el-option>
              </el-select>
              <!--                <el-cascader :options="managerOptions" :show-all-levels="false"></el-cascader>-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="managerPhone">
              <el-input v-model="form.managerPhone" placeholder="请输入联系方式" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input type="textarea" v-model="form.description" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="状态" prop="validFlag">
              <el-radio-group v-model="form.validFlag">
                <el-radio :label="true">启用</el-radio>
                <el-radio :label="false">禁用</el-radio>
              </el-radio-group>

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
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
    <el-dialog :title="title" v-model="openDetail" width="1000px" :append-to="$refs['app-container']" draggable>
      <el-form ref="daAssetApplyRef" :model="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="编号:" prop="id">
              <div class="form-readonly">
                {{ form.id }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">

          <el-col :span="24">
            <el-form-item label="项目名称:" prop="name">
              <div class="form-readonly">
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人:" prop="assetTableName">
              <div class="form-readonly">
                {{ form.nickName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式:" prop="managerPhone">
              <div class="form-readonly">
                {{ form.managerPhone ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <div class="form-readonly textarea">
                {{ form.description ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建人:" prop="createBy">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间:" prop="createTime">
              <div class="form-readonly">
                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}

              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="更新人:" prop="createBy">
              <div class="form-readonly">
                {{ form.updateBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新时间:" prop="updateTime">
              <div class="form-readonly">
                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态:" prop="validFlag">
              <div class="form-readonly">
                {{ form.validFlag ? "启用" : "禁用" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <div class="form-readonly textarea">
                {{ form.remark ?? "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">关闭 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Project">
import {
  listAttProject,
  getAttProject,
  delAttProject,
  addAttProject,
  updateAttProject,
  editProjectStatus,
} from "@/api/att/project/project.js";
// import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import { deptUserTree } from "@/api/system/system/user.js";

const { proxy } = getCurrentInstance();
const { dp_model_status, } = proxy.useDict(
  "dp_model_status",

);

const attProjectList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "项目名称", visible: true },
  { key: 3, label: "项目描述", visible: true },
  { key: 4, label: "负责人", visible: true },
  { key: 5, label: "联系方式", visible: true },
  { key: 6, label: "创建时间", visible: true },
  { key: 7, label: "创建人", visible: true },
  { key: 8, label: "状态", visible: true },
  { key: 9, label: "备注", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const managerOptions = ref([]);

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
  url: import.meta.env.VITE_APP_BASE_API + "/att/attProject/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
  },
  rules: {
    managerId: [{ required: true, message: "负责人不能为空", trigger: "blur" }],
    name: [{ required: true, message: "项目名称不能为空", trigger: "blur" }],
    // managerId: [{ required: true, message: "责任人不能为空", trigger: "blur" }],
    // validFlag: [{ required: true, message: '是否有效不能为空', trigger: 'change' }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询项目列表 */
function getList() {
  loading.value = true;
  listAttProject(queryParams.value).then((response) => {
    attProjectList.value = response.data.rows;
    console.log(response.data.rows, "response.data.rows");
    total.value = response.data.total;
    loading.value = false;
  });
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
function handleChange(value) {
  const selectedManager = managerOptions.value.find(
    (item) => item.userId === form.value.managerId
  );
  form.value.managerPhone = selectedManager.phonenumber; // 将完整对象存储到 form 中
}
// function getUserTree(){
//    deptUserTree().then(response => {
//     managerOptions.value = response.data;
//   })
// }

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}
/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? "启用" : "禁用";
  const status = row.validFlag === true ? 1 : 0;
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"项目吗？')
    .then(function () {
      editProjectStatus(row.id, status).then((response) => {
        proxy.$modal.msgSuccess(text + "成功");
        getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    managerId: null,
    managerPhone: null,
    description: null,
    validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("attProjectRef");
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
  title.value = "新增项目";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  console.log(_id, "22");
  getAttProject(_id).then((response) => {
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = "修改项目";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttProject(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "项目详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["attProjectRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttProject(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");

            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addAttProject(form.value)
          .then((response) => {
            if (response.code === 200) {
              proxy.$modal.msgSuccess("新增成功");
              open.value = false;
              getList();
            }
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
    .confirm('是否确认删除项目编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delAttProject(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "att/attProject/export",
    {
      ...queryParams.value,
    },
    `attProject_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "项目导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `attProject_template_${new Date().getTime()}.xlsx`
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

getList();
// getUserTree();
</script>
