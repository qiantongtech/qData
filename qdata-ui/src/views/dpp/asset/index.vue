<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree :deptOptions="deptOptions" :leftWidth="leftWidth" :placeholder="'请输入资产类目名称'" ref="DeptTreeRef"
                @node-click="handleNodeClick" />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                   v-show="showSearch" @submit.prevent>
            <el-form-item label="资产名称" prop="name">
              <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入资产名称" clearable
                        @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发布状态" prop="status">
              <el-select class="el-form-input-width" v-model="queryParams.status" placeholder="请选择发布状态" clearable>
                <el-option v-for="dict in da_assets_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="所属主题" prop="themeIdList">
              <el-select v-model="queryParams.themeIdList" collapse-tags multiple placeholder="请选择主题名称"
                         style="width: 240px">
                <el-option v-for="dict in themeList" :key="dict.id" :label="dict.name" :value="dict.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="资产类型" prop="status" v-if="type == 1">
              <el-select class="el-form-input-width" v-model="queryParams.type" placeholder="请选择资产类型" clearable>
                <el-option v-for="dict in da_asset_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        <div class="pagecont-bottom pagecont-bottoms">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dpp:asset:asset:add']"
                           @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
              <!--         <el-col :span="1.5">-->
              <!--           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['dpp:asset:asset:edit']"-->
              <!--                      @mousedown="(e) => e.preventDefault()">-->
              <!--             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
              <!--           </el-button>-->
              <!--         </el-col>-->
              <!--         <el-col :span="1.5">-->
              <!--           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['dpp:asset:asset:remove']"-->
              <!--                      @mousedown="(e) => e.preventDefault()">-->
              <!--             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
              <!--           </el-button>-->
              <!--         </el-col>-->
              <!--         <el-col :span="1.5">-->
              <!--           <el-button type="info" plain  @click="handleImport" v-hasPermi="['dpp:asset:asset:export']"-->
              <!--                      @mousedown="(e) => e.preventDefault()">-->
              <!--             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
              <!--           </el-button>-->
              <!--         </el-col>-->
              <!--         <el-col :span="1.5">-->
              <!--           <el-button type="warning" plain @click="handleExport" v-hasPermi="['dpp:asset:asset:export']"-->
              <!--                      @mousedown="(e) => e.preventDefault()">-->
              <!--             <i class="iconfont-mini icon-download-line mr5"></i>导出-->
              <!--           </el-button>-->
              <!--         </el-col>-->
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe height="58vh" v-loading="loading" :data="daAssetList"
                    @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <!--       <el-table-column type="selection" width="55" align="center" />-->
            <el-table-column v-if="getColumnVisibility(0)" width="80" label="编号" align="center" prop="id">
              <template #default="scope">
                {{ scope.row.id || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(1)" label="资产名称" align="left" prop="name" show-overflow-tooltip>
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(2)" label="资产描述" align="left" prop="description"
                             show-overflow-tooltip>
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(3)" label="资产类目" align="left" prop="catName" width="120"
                             show-overflow-tooltip>
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="资产类型" align="center" prop="source" width="120"
                             show-overflow-tooltip>
              <template #default="scope">
                <dict-tag :options="da_asset_type" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(5)" label="主题名称" align="left" show-overflow-tooltip>
              <template #default="scope">
                {{
                  scope.row.daAssetThemeRelList?.length
                      ? scope.row.daAssetThemeRelList.map(item => item.themeName).join(', ')
                      : '-'
                }}
              </template>
            </el-table-column>

            <!--            <el-table-column v-if="getColumnVisibility(6)" label="数据量(条)" align="center" prop="dataCount" width="90">-->
            <!--              <template #default="scope">-->
            <!--                {{ scope.row.dataCount || "-" }}-->
            <!--              </template>-->
            <!--            </el-table-column>-->

            <!--       <el-table-column v-if="getColumnVisibility(9)" label="发布状态" align="center" prop="STATUS">-->
            <!--         <template #default="scope">-->
            <!--           {{ scope.row.STATUS || '-' }}-->
            <!--         </template>-->
            <!--       </el-table-column>-->
            <el-table-column v-if="getColumnVisibility(7)" label="状态" align="center" prop="status" width="80">
              <template #default="scope">
                <el-switch :disabled="scope.row.projectId != userStore.projectId" v-model="scope.row.status"
                           active-color="#13ce66" inactive-color="#ff4949" active-value="1" inactive-value="0"
                           @change="handleStatusChange(scope.row)" />
              </template>
            </el-table-column>

            <!-- <el-table-column
                              v-if="getColumnVisibility(8)"
                              label="创建时间"
                              align="center"
                              prop="createTime"
                              width="160"
                              sortable="custom"
                              :sort-orders="['descending', 'ascending']"
                          >
                              <template #default="scope">
                                  <span>{{
                                      parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
                                  }}</span>
                              </template>
                          </el-table-column> -->
            <el-table-column v-if="getColumnVisibility(9)" label="更新时间" align="center" prop="updateTime" width="160">
              <template #default="scope">
                <span>{{
                    parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}:{s}")
                  }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="180">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                           v-if="scope.row.projectId == userStore.projectId" :disabled="scope.row.status == 1"
                           v-hasPermi="['dpp:asset:asset:edit']">修改</el-button>
                <!-- <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                                   v-hasPermi="['dpp:asset:asset:remove']">删除</el-button> -->
                <el-button link v-if="type != 1" type="primary" icon="Pointer" @click="handleApply(scope.row)"
                           v-hasPermi="['dpp:asset:asset:add']">申请</el-button>
                <el-button link v-if="scope.row.projectId == userStore.projectId" type="danger"
                           :disabled="scope.row.status == 1" icon="Delete" @click="handleDelete(scope.row)"
                           v-hasPermi="['dpp:asset:asset:remove']">删除</el-button>
                <el-button link type="primary" icon="view" @click="
                  routeTo(
                    type == 1
                      ? '/dpp/asset/daAssetDetail'
                      : '/da/asset/daAssetDetail',
                    scope.row
                  )
                  " v-hasPermi="['dpp:asset:asset:edit']">详情</el-button>
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

    <!-- 数据资产详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="daAssetRef" :model="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类目编码" prop="catCode">
              <div>
                {{ form.catCode }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主题id" prop="themeId">
              <div>
                {{ form.themeId }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据连接id" prop="datasourceId">
              <div>
                {{ form.datasourceId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="表名称" prop="tableName">
              <div>
                {{ form.tableName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表描述" prop="tableComment">
              <div>
                {{ form.tableComment }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据量(条)" prop="dataCount">
              <div>
                {{ form.dataCount }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段量" prop="fieldCount">
              <div>
                {{ form.fieldCount }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <div>
                {{ form.status }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描述" prop="description">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <div>
                {{ form.remark }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="small" @click="cancel">关 闭</el-button>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据资产数据
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
    <CreateEditModal :deptOptions="deptOptions" :visible="open" :title="title" @update:visible="open = $event"
                     @confirm="getList" :data="form" :type="0" />
    <!-- 申请数据资产对话框 -->
    <el-dialog :title="titleApply" v-model="openApply" width="800px" :append-to="$refs['app-container']" draggable>
      <el-form ref="daAssetApplyRef" :model="formApply" :rules="rulesApply" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产名称">
              <el-input v-model="formApply.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称">
              <el-input v-model="formApply.tableName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主题名称">
              <el-input v-model="formApply.themeName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据连接">
              <el-input v-model="formApply.datasourceName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库地址">
              <el-input v-model="formApply.datasourceIp" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型">
              <el-input v-model="formApply.datasourceType" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="资产描述">
              <el-input type="textarea" v-model="formApply.description" :rows="3" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请项目" prop="projectCode">
              <el-select v-model="formApply.projectCode" @change="handleSelectProject" placeholder="请选择申请项目">
                <el-option v-for="item in projectOptions" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formApply.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="申请事由" prop="applyReason">
              <el-input type="textarea" v-model="formApply.applyReason" :rows="3" placeholder="请输入申请事由" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openApply = false">取 消</el-button>
          <el-button type="primary" @click="submitApplyForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DaAsset">
import {
  listDaAsset,
  getDaAsset,
  delDaAsset,
  addDaAsset,
  updateDaAsset,
  listDppAsset,
} from "@/api/da/asset/daAsset";
import CreateEditModal from "./components/CreateEditModal";
import { currentUser } from "@/api/att/project/attProject";
import DeptTree from "@/components/DeptTree";
import { listAttAssetCat } from "@/api/att/cat/attAssetCat/attAssetCat.js";
import { getToken } from "@/utils/auth.js";
import { addDaAssetApply } from "@/api/da/daAssetApply/daAssetApply";
import useUserStore from "@/store/system/user";
import { getThemeList } from '@/api/att/theme/attTheme';
const { proxy } = getCurrentInstance();
const { da_assets_status, da_asset_source, da_asset_type } = proxy.useDict(
    "da_assets_status",
    "da_asset_source", "da_asset_type"
);

const daAssetList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "编号", visible: true },
  { key: 1, label: "资产名称", visible: true },
  { key: 2, label: "资产描述", visible: true },
  { key: 3, label: "资产类目", visible: true },
  { key: 9, label: '资产类型', visible: true },
  { key: 4, label: "主题名称", visible: true },
  { key: 5, label: "数据量(条)", visible: true },
  { key: 6, label: "状态", visible: true },
  { key: 7, label: "创建时间", visible: true },
  { key: 8, label: "更新时间", visible: true },
]);
let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
}
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
const open = ref(false);
const openDetail = ref(false);
const openApply = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const titleApply = ref("");
const projectOptions = ref([]);
const defaultSort = ref({ prop: "create_time", order: "desc" });
const router = useRouter();
const userStore = useUserStore();
const route = useRoute();
let type = route.query.type || null;
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
  url: import.meta.env.VITE_APP_BASE_API + "/da/daAsset/importData",
});
const options = [
  {
    value: 0,
    label: "申请资产",
  },
  {
    value: 1,
    label: "创建资产",
  },
];
const data = reactive({
  form: {},
  formApply: {
    projectCode: null,
    phone: null,
    applyReason: null,
  },
  queryParams: {
    themeIdList: [],
    pageNum: 1,
    pageSize: 10,
    name: null,
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    status: null,
    description: null,
    createTime: null,
    type: null,
    params: {
      sourceType: [0, 1],
    },
  },
  rules: {
    name: [{ required: true, message: "资产名称不能为空", trigger: "blur" }],
    catCode: [{ required: true, message: "类目编码不能为空", trigger: "blur" }],
    themeId: [{ required: true, message: "主题id不能为空", trigger: "blur" }],
    datasourceId: [
      { required: true, message: "数据连接id不能为空", trigger: "blur" },
    ],
    tableName: [{ required: true, message: "表名称不能为空", trigger: "blur" }],
  },
  rulesApply: {
    projectCode: [
      { required: true, message: "申请项目不能为空", trigger: "change" },
    ],
    phone: [{ required: true, message: "联系电话不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, formApply, rules, rulesApply } = toRefs(data);

watch(
    () => userStore.projectCode,
    (newCode) => {
      getList();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function submitApplyForm() {
  proxy.$refs["daAssetApplyRef"].validate((valid) => {
    if (valid) {
      formApply.value.id = null;
      formApply.value.updateBy = null;
      formApply.value.updaterId = null;
      formApply.value.updateTime = null;
      formApply.value.validFlag = null;
      formApply.value.delFlag = null;
      addDaAssetApply(formApply.value).then((response) => {
        proxy.$modal.msgSuccess("申请成功");
        openApply.value = false;
        getList();
      });
    }
  });
}

function handleApply(row) {
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    formApply.value = response.data;
    openApply.value = true;
    titleApply.value = "申请数据资产";
    formApply.value.phone = userStore.phonenumber;
    formApply.value.assetId = _id;
    formApply.value.assetName = response.data.name;
  });
  currentUser().then((response) => {
    projectOptions.value = response.data;
  });
}

function handleSelectProject(value) {
  formApply.value.projectCode = value;
  const project = projectOptions.value.find((item) => item.code === value);
  formApply.value.projectId = project.id;
  formApply.value.projectName = project.name;
}

/** 查询数据资产列表 */
function getList() {
  loading.value = true;
  console.log(type);

  if (type == 1) {
    queryParams.value.projectCode = userStore.projectCode;
    queryParams.value.projectId = userStore.projectId;
    listDppAsset(queryParams.value).then((response) => {
      daAssetList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  } else {
    listDaAsset(queryParams.value).then((response) => {
      daAssetList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  }
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
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    status: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    type: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daAssetRef");
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
  queryParams.value.params.sourceType = [0, 1];
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  queryParams.value.type = null;
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
/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
function getAssetCat() {
  listAttAssetCat().then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        label: "",
        name: "资产类目",
        value: "",
        children: deptOptions.value,
      },
    ];
  });
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增数据资产";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  loading.value = true
  getDaAsset(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据资产";
    loading.value = false
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "数据资产详情";
  });
}



/** 删除按钮操作 */
function handleDelete(row) {
  // proxy.$message.error("功能开发中....");
  const _ids = row.id || ids.value;
  proxy.$modal
      .confirm('是否确认删除数据资产编号为"' + _ids + '"的数据项？')
      .then(function () {
        return delDaAsset(_ids);
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
      "da/daAsset/export",
      {
        ...queryParams.value,
      },
      `daAsset_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "数据资产导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
      "system/user/importTemplate",
      {},
      `daAsset_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  handleQuery();
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

/** 启用禁用开关 */
function handleStatusChange(row) {
  const text = row.status === "1" ? "启用" : "停用";
  proxy.$modal
      .confirm("确认要" + text + '"' + row.name + '"资产吗？')
      .then(function () {
        updateDaAsset({ id: row.id, status: row.status }).then((response) => {
          proxy.$modal.msgSuccess(text + "成功");
          getList();
        });
      })
      .catch(function () {
        if (row.status === "1") {
          row.status = "0";
        } else {
          row.status = "1";
        }
      });
}
queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
getAssetCat();
getAssetThemeList()
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
