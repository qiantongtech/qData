<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px">
        <el-form-item label="清洗规则类目名称" prop="name" label-width="130">
          <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入清洗规则类目名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="上级类目" prop="code">
          <el-tree-select class="el-form-input-width" v-model="queryParams.code" :data="attAssetCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" placeholder="请选择上级"
            check-strictly />
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
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['att:cleanCat:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="toggle-expand-all" type="primary" plain @click="toggleExpandAll">
              <svg-icon v-if="isExpandAll" icon-class="toggle" />
              <svg-icon v-else icon-class="expand" />
              <span>{{ isExpandAll ? "折叠" : "展开" }}</span>
            </el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table height="60vh" v-if="refreshTable" v-loading="loading" :data="AttCleanCatList" row-key="id"
        :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column label="清洗规则类目名称" align="left" prop="name" width="200"
          :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="描述" align="left" prop="description" :show-overflow-tooltip="{ effect: 'light' }"
          width="250">
          <template #default="scope">
            {{ scope.row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="排序" align="left" prop="sortOrder" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.sortOrder }}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="validFlag">
          <template #default="scope">
            <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

            <el-switch v-model="scope.row.validFlag" active-color="#13ce66" inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:cleanCat:edit']">修改</el-button>
            <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
              v-hasPermi="['att:cleanCat:add']">新增</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['att:cleanCat:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
    </div>

    <!-- 新增或修改清洗规则类目管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-form ref="attCleanCatRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入清洗规则类目名称" />
            </el-form-item>
          </el-col>
          <!--            <el-form-item label="类别排序" prop="sortOrder">-->
          <!--&lt;!&ndash;              <el-input v-model="form.sortOrder" placeholder="请输入类别排序" />&ndash;&gt;-->
          <!--              <el-input-number v-model="form.sortOrder"  steps="1" :min="0"  placeholder="请输入类别排序" />-->
          <!--            </el-form-item>-->
          <el-col :span="12">
            <el-form-item label="上级类目" prop="parentId">
              <el-tree-select :disabled="form.id" v-model="form.parentId" :data="attAssetCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id" placeholder="请选择上级"
                check-strictly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input type="textarea" placeholder="请输入描述" v-model="form.description" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序" prop="sortOrder">
              <el-input-number style="width: 100%" v-model="form.sortOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态" prop="validFlag">
              <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
              <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input type="textarea" placeholder="请输入备注" v-model="form.remark" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CleanCat">
import { listAttCleanCat, getAttCleanCat, delAttCleanCat, addAttCleanCat, updateAttCleanCat } from "@/api/att/cat/cleanCat/cleanCat.js";
import { getToken } from "@/utils/auth.js";
const { proxy } = getCurrentInstance();
const AttCleanCatList = ref([]);
const attAssetCatOptions = ref([]);
// 列显隐信息
const columns = ref([
  { key: 1, label: "类别名称", visible: true },
  { key: 2, label: "关联上级ID", visible: true },
  { key: 3, label: "类别排序", visible: true },
  { key: 4, label: "描述", visible: true },
  { key: 5, label: "层级编码", visible: true },
  { key: 8, label: "创建人", visible: true },
  { key: 10, label: "创建时间", visible: true },
  { key: 14, label: "备注", visible: true }
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
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
const refreshTable = ref(true);
const isExpandAll = ref(false);

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
  url: import.meta.env.VITE_APP_BASE_API + "/att/attCleanCat/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    parentId: null,
    sortOrder: null,
    description: null,
    code: null,
    createTime: null,
  },
  rules: {
    name: [{ required: true, message: '清洗规则类目名称不能为空', trigger: 'blur' }],
    parentId: [{ required: true, message: '上级类目不能为空', trigger: 'blur' }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询清洗规则类目列表 */
function getList() {
  loading.value = true;
  listAttCleanCat(queryParams.value).then(response => {
    AttCleanCatList.value = proxy.handleTree(response.data, 'id');
    // total.value = response.data.length;
    loading.value = false;
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
    parentId: null,
    sortOrder: 0,
    description: null,
    code: null,
    validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("attCleanCatRef");
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
  ids.value = selection.map(item => item.ID);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? '启用' : '禁用';
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"清洗规则类目吗？')
    .then(function () {
      updateAttCleanCat({ id: row.id, validFlag: row.validFlag }).then((response) => {
        proxy.$modal.msgSuccess(text + '成功');
        getList();
      }).catch((err) => {
        row.validFlag = !row.validFlag;
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  listAttCleanCat().then((response) => {
    attAssetCatOptions.value = [];
    const data = { id: 0, name: '顶级节点', children: [] };
    data.children = proxy.handleTree(response.data, 'id', 'parentId');
    attAssetCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加清洗规则类目";
}

function getDataTree() {
  listAttCleanCat().then((response) => {
    attAssetCatOptions.value = [];
    const data = { id: 0, name: '顶级节点', children: [] };
    data.children = proxy.handleTree(response.data, 'id', 'parentId');
    attAssetCatOptions.value.push(data);
  });
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  const response = await listAttCleanCat();
  attAssetCatOptions.value = [];
  // 过滤节点的计算属性
  const filteredDepts = response.data.filter((d) => {
    // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
    return d.ID !== row.id && !d.parentId.toString().split(',').includes(row.id.toString());
  });
  const data = { id: 0, name: '顶级节点', children: [] };
  data.children = proxy.handleTree(filteredDepts, 'id', 'parentId');
  attAssetCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getAttCleanCat(row.id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改清洗规则类目";
  });
}


/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _ID = row.ID || ids.value
  getAttCleanCat(_ID).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "清洗规则类目详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["attCleanCatRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateAttCleanCat(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addAttCleanCat(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除清洗规则类目编号为"' + ids + '"的数据项？').then(function () {
    return delAttCleanCat(ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('att/attCleanCat/export', {
    ...queryParams.value
  }, `AttCleanCat_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "清洗规则类目导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `AttCleanCat_template_${new Date().getTime()}.xlsx`)
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id
        }
      });
    }
  }
}

getList();
getDataTree();
</script>
