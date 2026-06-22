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
    <el-container style="90%">
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('dp.document.selectStandardCategory')"
        @node-click="handleNodeClick"
      />

      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item :label="td('dp.document.standardCode')" prop="code">
              <el-input
                class="el-form-input-width"
                v-model="queryParams.code"
                :placeholder="td('dp.document.standardCodePlaceholder')"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item :label="td('dp.document.standardName')" prop="name">
              <el-input
                class="el-form-input-width"
                v-model="queryParams.name"
                :placeholder="td('dp.document.standardNamePlaceholder')"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item :label="td('dp.document.standardStatus')" prop="status">
              <el-select
                class="el-form-input-width"
                v-model="queryParams.status"
                :placeholder="td('dp.document.standardStatusPlaceholder')"
              >
                <el-option
                  v-for="dict in dp_document_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                plain
                type="primary"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('dp.common.query') }}
              </el-button>
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('dp.common.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="pagecont-bottom">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="handleAdd"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('dp.common.add') }}
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar
                v-model:showSearch="showSearch"
                @queryTable="getList"
                :columns="columns"
              ></right-toolbar>
            </div>
          </div>
          <el-table
            stripe
            v-loading="loading"
            :data="dpDataElemList"
            @selection-change="handleSelectionChange"
            :default-sort="defaultSort"
            @sort-change="handleSortChange"
          >
            <el-table-column
              v-if="getColumnVisibility(0)"
              :label="td('common.texts.number')"
              align="left"
              prop="id"
              width="60"
              sortable
            />
            <el-table-column
              v-if="getColumnVisibility(1)"
              :label="td('dp.document.standardCode')"
              :show-overflow-tooltip="{ effect: 'light' }"
              align="left"
              prop="name"
              width="120"
            >
              <template #default="scope">
                {{ scope.row.code || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(2)"
              :label="td('dp.document.standardName')"
              :show-overflow-tooltip="{ effect: 'light' }"
              align="left"
              prop="name"
              width="200"
            >
              <template #default="scope">
                {{ scope.row.name || "-" }}
              </template>
            </el-table-column>

            <el-table-column
              v-if="getColumnVisibility(7)"
              width="240"
              :label="td('common.texts.description')"
              align="left"
              prop="description"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(4)"
              :label="td('dp.document.standardCategory')"
              :show-overflow-tooltip="{ effect: 'light' }"
              align="left"
              prop="catCode"
              width="180"
            >
              <template #default="scope">
                {{ scope.row.catName || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(10)"
              :label="td('common.texts.createdBy')"
              :show-overflow-tooltip="{ effect: 'light' }"
              align="left"
              prop="createBy"
              width="100"
            >
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <!--  sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']" -->
            <el-table-column
              v-if="getColumnVisibility(11)"
              :label="td('common.texts.createdTime')"
              align="left"
              prop="createTime"
              width="150"
              sortable
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(3)"
              :label="td('dp.document.standardStatus')"
              align="left"
              prop="status"
            >
              <template #default="scope">
                <dict-tag
                  :options="dp_document_status"
                  :value="scope.row.status"
                />
              </template>
            </el-table-column>
            <el-table-column
              :label="td('common.texts.remark')"
              align="left"
              prop="remark"
              :show-overflow-tooltip="{ effect: 'light' }"
              v-if="getColumnVisibility(15)"
            >
              <template #default="scope">
                {{ scope.row.remark || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              :label="td('common.texts.operation')"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
              width="200"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  >{{ td('dp.common.edit') }}
                </el-button>
                <el-button
                  link
                  type="primary"
                  icon="view"
                  @click="handleDetail(scope.row)"
                  >{{ td('dp.common.details') }}
                </el-button>
                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button link type="primary" icon="ArrowDown"
                      >{{ td('dp.document.more') }}</el-button
                    >
                  </template>
                  <div style="width: 100px" class="butgdlist">
                    <el-button
                      link
                      style="padding-left: 14px"
                      type="primary"
                      icon="View"
                      @click="handleFilePreview(scope.row.fileUrl)"
                      :disabled="!scope.row.fileUrl"
                      >{{ td('dp.document.previewBtn') }}</el-button
                    >
                    <el-button
                      link
                      type="primary"
                      icon="Download"
                      :disabled="!scope.row.fileUrl"
                      @click="handleDownload(scope.row)"
                      >{{ td('dp.document.downloadBtn') }}</el-button
                    >

                    <el-button
                      link
                      type="danger"
                      icon="Delete"
                      @click="handleDelete(scope.row)"
                      >{{ td('dp.common.delete') }}
                    </el-button>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/images/system/images/no_data/empty-nodata.png" alt="" />
                <p>{{td('common.noData')}}</p>
              </div>
            </template>
          </el-table>

          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </el-main>
    </el-container>
    <!-- 标准弹窗 -->
    <StandardModal ref="standardModalRef" @update-success="handleQuery" />
  </div>
</template>

<script setup name="Industry">
import DeptTree from "@/components/DeptTree";
import handleFilePreview from "@/utils/filePreview.js";
import {
  listDpDocument,
  delDpDocument,
  addDpDocument,
  listAttDocumentCat,
} from "@/api/dp/document/document";
import StandardModal from "../components/add";
import { deptUserTree } from "@/api/system/system/user.js";
import { getToken } from "@/utils/auth.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_document_status } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_document_status"
);
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度

const dpDataElemList = ref([]);
const dpDataElemRuleRelList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('dp.document.standardCode'), visible: true },
  { key: 2, label: td('dp.document.standardName'), visible: true },
  { key: 7, label: td('common.texts.description'), visible: true },
  { key: 3, label: td('dp.document.standardCategory'), visible: true },
  { key: 10, label: td('common.texts.createdBy'), visible: true },
  { key: 11, label: td('common.texts.createdTime'), visible: true },
  { key: 3, label: td('dp.document.standardStatusColumn'), visible: true },
  { key: 15, label: td('common.texts.remark'), visible: true },
]);
/** 预览文件 */
function handlePreview(row) {
  if (!row.fileUrl) {
    proxy.$message.warning(td('dp.document.previewNotAvailable'));
    return;
  }
  // 使用 window.open 打开文件，支持 pdf、图片等
  window.open(row.fileUrl, "_blank");
}
const handleDownload = (row) => {
  const baseUrl = import.meta.env.VITE_APP_BASE_API;
  const fullUrl = `${baseUrl}${row.fileUrl.trim()}`; // 去除可能的前后空格
  const a = document.createElement("a");
  a.href = fullUrl;
  a.download = row.fileName;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
};
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
const defaultSort = ref({ prop: "create_time", order: "descending" });
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
  url: import.meta.env.VITE_APP_BASE_API + "/dp/dataElem/importData",
});

const data = reactive({
  form: { status: "0" },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    name: null,
    catCode: null,
    type: 2,
  },
  rules: {
    code: [{ required: true, message: td('dp.document.standardCodeRequired'), trigger: "blur" }],
    name: [
      { required: true, message: td('dp.document.standardNameRequired'), trigger: "blur" },
      {
        pattern: /^[a-zA-Z_]+$/,
        message: td('dp.document.standardNamePattern'),
        trigger: "blur",
      },
    ],
    catCode: [{ required: true, message: td('dp.document.standardCategoryRequired'), trigger: "blur" }],
    status: [{ required: true, message: td('dp.document.standardStatusRequired'), trigger: "change" }],
    type: [{ required: true, message: td('dp.document.typeRequired'), trigger: "change" }],
    columnType: [
      { required: true, message: td('dp.document.columnTypeRequired'), trigger: "change" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);
const managerOptions = ref([]);
/** 查询行业标准列表 */
function getList() {
  loading.value = true;
  listDpDocument(queryParams.value).then((response) => {
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
    requestAnimationFrame(() => {});
  }
}; /** 查询部门下拉树结构 */
// 表单重置
function reset() {
  form.value = {
    ID: null,
    code: null,
    name: null,
    catCode: null,
    type: null,
    status: "0",
    issuingAgency: null,
    version: null,
    releaseDate: null,
    implementationDate: null,
    abolitionDate: null,
    standardUrl: null,
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
  proxy.resetForm("dpDocumentRef");
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
  queryParams.value.orderByColumn = defaultSort.value.prop;
  queryParams.value.isAsc = defaultSort.value.order;
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
  queryParams.value.orderByColumn =
    column.prop == "createTime" ? "create_time" : column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
function getDeptTree() {
  listAttDocumentCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('dp.document.treeRootName'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
const standardModalRef = ref(null);
/** 新增按钮操作 */
function handleAdd() {
  standardModalRef.value.openModal(
    {},
    deptOptions.value,
    queryParams.value.type
  );
}

/** 修改按钮操作 */
function handleUpdate(row) {
  standardModalRef.value.openModal(
    row,
    deptOptions.value,
    queryParams.value.type
  );
}

/** 详情按钮操作 */
function handleDetail(row) {
  routeTo("/dm/document/industry/detail", row);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dpDataElemRef"].validate((valid) => {
    form.value.type = 1;
    if (valid) {
      if (form.value.id != null) {
        updateDpDocument(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDpDocument(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dp.document.confirmDelete').replace('<id>', _ids))
    .then(function () {
      return delDpDocument(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

/** 国家标准数据规则关联信息序号 */
function rowDpDataElemRuleRelIndex({ row, rowIndex }) {
  row.index = rowIndex + 1;
}

/** 国家标准数据规则关联信息新增按钮操作 */
function handleaddDpDocumentRuleRel() {
  let obj = {};
  obj.ruleType = "";
  obj.ruleId = "";
  obj.ruleConfig = "";
  obj.remark = "";
  dpDataElemRuleRelList.value.push(obj);
}

/** 国家标准数据规则关联信息删除按钮操作 */
function handleDeleteDpDataElemRuleRel() {
  if (checkedDpDataElemRuleRel.value.length == 0) {
    proxy.$modal.msgWarning(td('dp.document.selectToDeleteWarning'));
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
  upload.title = td('dp.document.importTitle');
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
    td('dp.document.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** 启用禁用开关 */
function handleStatusChange(id, row, e) {
  const text = e === "1" ? td('dp.document.enableText') : td('dp.document.disableText');
  proxy.$modal
    .confirm(td('dp.document.confirmStatusChange').replace('<text>', text).replace('<name>', row.name))
    .then(function () {
      updateStatusDpDataElem(id, row.status).then((response) => {
        proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
      });
    })
    .catch(function () {
      row.status = row.status === "1" ? "0" : "1";
    });
}
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
queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
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
