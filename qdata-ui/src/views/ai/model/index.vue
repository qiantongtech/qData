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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->
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
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['ai:model:add']"
        >
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['ai:model:remove']"
        >
          删除
        </el-button>
      </template>
      <qt-table v-bind="tableStore" ref="tableRef">
        <template #platform="scope">
          <dict-tag :options="ai_model_platform" :value="scope.row.platform" />
        </template>
        <template #createTime="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
        <template #validFlag="scope">
          <el-switch
            v-model="scope.row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          />
        </template>
        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['ai:model:edit']"
            >修改</el-button
          >
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(row)"
            v-hasPermi="['ai:model:remove']"
            >删除</el-button
          >
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(row)"
            v-hasPermi="['ai:model:edit']"
            >详情</el-button
          >
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 添加或修改模型对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form
        ref="modelRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-form-item label="模型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模型名称" />
        </el-form-item>

        <el-form-item label="平台" prop="platform">
          <el-select v-model="form.platform" placeholder="请选择平台">
            <el-option
              v-for="dict in ai_model_platform"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="API地址" prop="apiUrl">
          <el-input v-model="form.apiUrl" placeholder="请输入API地址" />
        </el-form-item>
        <el-form-item label="API秘钥" prop="apiKey">
          <el-input
            v-model="form.apiKey"
            placeholder="请输入API秘钥"
            type="password"
          />
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <el-radio v-model="form.validFlag" :label="true">启用</el-radio>
          <el-radio v-model="form.validFlag" :label="false">禁用</el-radio>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number
            style="width: 100%"
            v-model="form.sortOrder"
            controls-position="right"
            :min="0"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <el-input
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            v-model="form.description"
            placeholder="请输入描述"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark" class="row-full">
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

    <!-- 模型详情对话框 -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form ref="modelRef" :model="form" label-width="90px">
        <el-form-item label="编号" prop="id">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item label="模型名称" prop="name">
          <div class="form-readonly">
            {{ form.name }}
          </div>
        </el-form-item>
        <el-form-item label="平台" prop="platform">
          <dict-tag :options="ai_model_platform" :value="form.platform" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <div class="form-readonly">
            {{ form.sortOrder || "-" }}
          </div>
        </el-form-item>
        <el-form-item label="API地址" prop="apiUrl">
          <div class="form-readonly" :title="form.apiUrl || '-'">
            {{ form.apiUrl ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="API秘钥" prop="apiKey">
          <div class="form-readonly">
            {{ form.apiKey ? "**********" : "-" }}
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <div class="form-readonly">
            {{ form.validFlag ? "启用" : "禁用" }}
          </div>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
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
          <el-button size="mini" @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox
                v-model="upload.updateSupport"
              />是否更新已经存在的模型数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >下载模板</el-link
            >
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
    <DeleteConfirmDialog
      ref="deleteConfirmDialog"
      @confirm-delete="handleDelete"
    />
  </div>
</template>

<script setup name="Model">
import {
  listModel,
  getModel,
  delModel,
  addModel,
  updateModel,
} from "@/api/ai/model";
import { getToken } from "@/utils/auth.js";
import { parseTime } from "@/utils/anivia.js";
import { encrypt, isDecrypted } from "@/utils/aesEncrypt";
import DeleteConfirmDialog from "@/components/DeleteConfirmDialog";
import { reactive, ref, toRefs, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";

const { proxy } = getCurrentInstance();
const { ai_model_platform } = proxy.useDict("ai_model_platform");

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
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
      selectable: function (row) {
        return true;
      },
    },
    { label: "编号", prop: "id", width: 60, sortable: true },
    { label: "模型名称", prop: "name", align: "left", width: 200 },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 200,
      showOverflowTooltip: true,
    },
    {
      label: "平台",
      prop: "platform",
      width: 120,
      slot: "platform",
    },
    {
      label: "API地址",
      prop: "apiUrl",
      align: "left",
      width: 200,
      showOverflowTooltip: true,
    },
    // {
    //   label: "API秘钥",
    //   prop: "apiKey",
    //   align: "left",
    //   width: 200,
    //   showOverflowTooltip: true,
    // },
    { label: "排序", prop: "sortOrder", width: 80, sortable: true },

    { label: "状态", prop: "validFlag", slot: "validFlag" },
    { label: "创建人", prop: "createBy", width: 120 },
    {
      label: "创建时间",
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    // {
    //   label: "备注",
    //   prop: "remark",
    //   align: "left",
    //   width: 200,
    //   showOverflowTooltip: true,
    // },
    { label: "操作", width: 280, fixed: "right", slot: "handle" },
  ],
  func: listModel,
  params: {},
  events: {},
});

const searchStore = reactive({
  items: [
    {
      label: "模型名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入模型名称" },
    },
    {
      label: "平台",
      prop: "platform",
      component: {
        is: "select",
        placeholder: "请选择平台",
        options: ai_model_platform,
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
  ],
});

const open = ref(false);
const openDetail = ref(false);
const title = ref("");

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
  url: import.meta.env.VITE_APP_BASE_API + "/ai/model/importData",
});

const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: "模型名称不能为空", trigger: "blur" }],
    platform: [{ required: true, message: "平台不能为空", trigger: "change" }],
    apiUrl: [
      {
        validator: (rule, value, callback) => {
          const apiKey = form.value.apiKey;
          if (!value && !apiKey) {
            callback(new Error("API地址和API秘钥至少填写一个"));
          } else {
            callback();
          }
        },
        trigger: "blur",
      },
    ],
    apiKey: [
      {
        validator: (rule, value, callback) => {
          const apiUrl = form.value.apiUrl;
          if (!value && !apiUrl) {
            callback(new Error("API地址和API秘钥至少填写一个"));
          } else {
            callback();
          }
        },
        trigger: "blur",
      },
    ],
  },
});

const { form, rules } = toRefs(data);

// 添加 old_apiKey 变量用于跟踪原始API秘钥
let old_apiKey = null;

/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? "启用" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"主题吗？')
    .then(function () {
      updateModel({ id: row.id, validFlag: row.validFlag }).then((response) => {
        proxy.$modal.msgSuccess(text + "成功");
        tableRef.value.getList();
      });
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}
// 点击查询
function handleQueryClick() {
  tableRef.value.getList();
}

// 重置查询
function handleResetQueryClick() {
  tableRef.value.resetQuery();
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
    platform: null,
    apiUrl: null,
    apiKey: null,
    sortOrder: 1,
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
  old_apiKey = null;
  proxy.resetForm("modelRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加模型";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id;
  getModel(_id).then((response) => {
    form.value = response.data;
    old_apiKey = response.data.apiKey; // 保存原始API秘钥
    open.value = true;
    title.value = "修改模型";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id;
  getModel(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "模型详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["modelRef"].validate((valid) => {
    if (valid) {
      const submitData = { ...form.value };

      // 处理API秘钥加密逻辑
      if (submitData.id != null) {
        // 修改时检查API秘钥是否需要重新加密
        if (
          old_apiKey !== submitData.apiKey ||
          !isDecrypted(submitData.apiKey)
        ) {
          submitData.apiKey = encrypt(submitData.apiKey);
        }
      } else {
        // 新增时直接加密API秘钥
        if (submitData.apiKey) {
          submitData.apiKey = encrypt(submitData.apiKey);
        }
      }

      if (submitData.id != null) {
        updateModel(submitData)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            tableRef.value.getList();
          })
          .catch((error) => {});
      } else {
        addModel(submitData)
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

let deleteConfirmDialog = ref(null);

/** 删除按钮操作 */
function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");
  }

  if (!_ids) return;

  proxy.$modal
    .confirm('是否确认删除模型编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delModel(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {
      // 用户取消删除操作
    });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "ai/model/export",
    {
      ...tableStore.params,
    },
    `model_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "模型导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "ai/model/importTemplate",
    {},
    `model_template_${new Date().getTime()}.xlsx`
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
  tableRef.value.getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    const router = useRouter();
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
</script>
