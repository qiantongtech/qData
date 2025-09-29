<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="att/client.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
        v-show="showSearch" @submit.prevent>
        <el-form-item label="编号" prop="id">
          <el-input class="el-form-input-width" v-model="queryParams.id" placeholder="请输入编号" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="应用名称" prop="name">
          <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入应用名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="应用类型" prop="type">
          <el-select class="el-form-input-width" v-model="queryParams.type" placeholder="请选择应用类型" clearable>
            <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否公开" prop="publicFlag">
          <el-select class="el-form-input-width" v-model="queryParams.publicFlag" placeholder="请选择是否公开" clearable>
            <el-option v-for="dict in auth_public" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" v-hasPermi="['att:client:query']" @click="handleQuery"
            @mousedown="(e) => e.preventDefault()">
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:client:add']"
              @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <!--         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['att:client:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['att:client:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="info" plain  @click="handleImport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="warning" plain @click="handleExport" v-hasPermi="['att:client:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-download-line mr5"></i>导出
           </el-button>
         </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="clientList" @selection-change="handleSelectionChange"
        :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column v-if="getColumnVisibility(0)" width="50" label="编号" align="center" prop="id" />
        <el-table-column v-if="getColumnVisibility(1)" width="200" label="应用名称"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column width="100" v-if="getColumnVisibility(3)" label="应用类型" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="auth_app_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" :show-overflow-tooltip="{ effect: 'light' }" label="描述"
          align="left" prop="description" width="300">
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(16)" width="80" label="应用图标"
          :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="name">
          <template #default="scope">
            <div class="clientInfo">
              <div>
                <image-preview :src="scope.row.logo || noDataImg" :width="50" :height="50" />

              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column width="100" v-if="getColumnVisibility(4)" label="是否公开" align="center" prop="publicFlag">
          <template #default="scope">
            <dict-tag :options="auth_public" :value="scope.row.publicFlag" />
          </template>
        </el-table-column>
        <!--       <el-table-column v-if="getColumnVisibility(5)" label="允许授权的url" align="center" prop="allowUrl">
         <template #default="scope">
           {{ scope.row.allowUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(6)" label="同步地址" align="center" prop="syncUrl">
         <template #default="scope">
           {{ scope.row.syncUrl || '-' }}
         </template>
       </el-table-column>-->
        <!--       <el-table-column v-if="getColumnVisibility(7)" label="应用图标" align="center" prop="logo" width="100">
         <template #default="scope">
           <image-preview :src="scope.row.logo" :width="50" :height="50"/>
         </template>
       </el-table-column>-->

        <el-table-column v-if="getColumnVisibility(12)" label="创建人" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" label="创建时间" align="center" prop="createTime" width="150"
          sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"> <template
            #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }"
          v-if="getColumnVisibility(15)">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="280">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['att:client:edit']">修改</el-button>
            <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
              v-hasPermi="['att:client:query']">详情</el-button>
            <el-popover placement="bottom" :width="150" trigger="click">
              <template #reference>
                <el-button link type="primary" icon="ArrowDown">更多</el-button>
              </template>
              <div style="width: 100px" class="butgdlist">
                <el-button link style="padding-left: 14px" type="primary" icon="Refresh" @click="handleReset(scope.row)"
                  v-hasPermi="['att:client:edit']">重置秘钥</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['att:client:remove']">删除</el-button>
              </div>
            </el-popover>
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

    <!-- 新增或修改应用对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入应用名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择应用类型">
                <el-option v-for="dict in auth_app_type" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主页地址" prop="homepageUrl">
              <el-input v-model="form.homepageUrl" placeholder="请输入主页地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步地址" prop="syncUrl">
              <el-input v-model="form.syncUrl" placeholder="请输入同步地址" />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="应用图标" prop="logo">
              <image-upload v-model="form.logo" limit="1" :fileType="pdf" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否公开" prop="publicFlag">
              <el-radio-group v-model="form.publicFlag">
                <el-radio v-for="dict in auth_public" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
    <!-- 应用详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="clientRef" :model="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="编号" prop="id">
              <div>{{ form.id || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用秘钥" prop="secret">
              <div>{{ form.secret || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用名称" prop="name">
              <div>{{ form.name || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用图标" prop="logo">
              <image-preview :src="form.logo || noDataImg" :width="50" :height="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用类型" prop="type">
              <dict-tag :options="auth_app_type" :value="form.type" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否公开" prop="publicFlag">
              <dict-tag :options="auth_public" :value="form.publicFlag" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主页地址" prop="homepageUrl">
              <div>{{ form.homepageUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步地址" prop="syncUrl">
              <div>{{ form.syncUrl || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授权路径" prop="allowUrl">
              <div>{{ form.allowUrl || "-" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <div>{{ form.description || "-" }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的应用数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">下载模板</el-link>
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
  </div>
</template>

<script setup name="Client">
import {
  listClient,
  getClient,
  delClient,
  addClient,
  updateClient,
  resetSecret,
} from "@/api/ds/client/client";
import { getToken } from "@/utils/auth.js";

const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict(
  "auth_public",
  "auth_app_type"
);
const noDataImg = new URL('../../../assets/system/images/D.png', import.meta.url).href
const clientList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "应用名称", visible: true },
  { key: 3, label: "应用类型", visible: true },
  { key: 2, label: "描述", visible: true },
  { key: 16, label: "应用图标", visible: true },
  // { key: 5, label: "允许授权的url", visible: true },
  { key: 4, label: "是否公开", visible: true },
  { key: 12, label: "创建人", visible: true },
  { key: 14, label: "创建时间", visible: true },
  { key: 15, label: "备注", visible: true },
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
  url: import.meta.env.VITE_APP_BASE_API + "/att/client/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    name: null,
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
    createTime: null,
  },
  rules: {
    name: [{ required: true, message: "应用名称不能为空", trigger: "blur" }],
    type: [{ required: true, message: "应用类型不能为空", trigger: "change" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用列表 */
function getList() {
  loading.value = true;
  listClient(queryParams.value).then((response) => {
    clientList.value = response.data.rows;
    total.value = response.data.total;
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
    type: null,
    secret: null,
    homepageUrl: null,
    allowUrl: null,
    syncUrl: null,
    logo: null,
    description: null,
    publicFlag: null,
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
  proxy.resetForm("clientRef");
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
  title.value = "新增应用";

  data.form.publicFlag = "1";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getClient(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改应用";
  });
}

/** 重置秘钥按钮操作 */
function handleReset(row) {
  const _id = row.id || ids.value;

  proxy.$modal
    .confirm("是否确认重置秘钥，秘钥重置后请使用新秘钥访问。")
    .then(function () {
      resetSecret(_id).then((res) => {
        proxy.$modal.msgSuccess("新秘钥为：" + res.data);
        getList();
      });
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
  // reset();
  // const _id = row.id || ids.value;
  // getClient(_id).then((response) => {
  //   form.value = response.data;
  //   openDetail.value = true;
  //   title.value = "应用详情";
  // });
  routeTo("/ds/client/clientDetail", row);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["clientRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateClient(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        addClient(form.value)
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
    .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delClient(_ids);
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
    "att/client/export",
    {
      ...queryParams.value,
    },
    `client_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "应用导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `client_template_${new Date().getTime()}.xlsx`
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
</script>

<style scoped lang="scss">
.clientInfo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

// :deep {
//   .el-popper.is-dark {
//     max-width: 900px !important;
//     max-height: 400px;
//     font-size: 14px;
//     text-align: start;
//   }
// }</style>
