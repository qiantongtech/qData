<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="td('sys.system.messageTemplate.msgTitle')" prop="title" :label-position="labelPosition">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.title"
              :placeholder="td('sys.system.messageTemplate.msgTitlePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('sys.system.messageTemplate.msgCategory')" prop="category" :label-position="labelPosition">
          <el-select v-model="queryParams.category" :placeholder="td('sys.system.messageTemplate.selectPlaceholder')" class="el-form-input-width">
            <el-option
                v-for="dict in message_category"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="td('sys.system.messageTemplate.msgLevel')" prop="msgLevel">
          <el-select v-model="queryParams.msgLevel" :placeholder="td('sys.system.messageTemplate.selectPlaceholder')" class="el-form-input-width">
            <el-option
                v-for="dict in message_level"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div  class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="justify-end btn-style">
          <el-col :span="1.5">
            <el-button
                type="primary"
                plain
                @click="handleAdd"
                v-hasPermi="['system:messageTemplate:add']"
                @mousedown="e => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
            </el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="messageTemplateList" >
        <el-table-column :label="td('sys.system.messageTemplate.templateId')" align="center" prop="id" />
        <el-table-column :label="td('sys.system.messageTemplate.msgTitle')" align="center" prop="title">
          <template #default="scope">
            {{ scope.row.title || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.system.messageTemplate.msgTemplateContent')" align="center" prop="content" width="180"  :show-overflow-tooltip="true">
          <template #default="scope">
            {{ scope.row.content || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.system.messageTemplate.msgCategory')" align="center" prop="category">
          <template #default="scope">
            <dict-tag :options="message_category" :value="scope.row.category" />
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.system.messageTemplate.msgLevel')" align="center" prop="msgLevel">
          <template #default="scope">
            <dict-tag :options="message_level" :value="scope.row.msgLevel" />
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdBy')" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.remark')" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:messageTemplate:edit']">{{ td('common.button.update') }}</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:messageTemplate:remove']">{{ td('common.button.delete') }}</el-button>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/images/system/no_data/empty-nodata.png" alt="">
            <p>{{ td('sys.system.messageTemplate.noRecord') }}</p>
          </div>
        </template>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改消息模板对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <!-- <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-popover placement="top-start" width="641px" trigger="hover">
            <div class="tips-content">
              <div>
                <el-icon size="20" style="color: #909399; font-size: 16px">
                  <InfoFilled />
                </el-icon>
                <span class="wxtstitle ml0">温馨提示!</span>
              </div>
              <div>
                <p>
                  xxxx
                </p>
              </div>
            </div>
            <template #reference>
              <el-icon size="20" style="color: #909399; font-size: 16px">
                <InfoFilled />
              </el-icon>
            </template>
          </el-popover>
        </span>
        <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
          <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
              <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
              </path>
            </svg></i>
        </button>
      </template> -->
      <el-form ref="messageTemplateRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.messageTemplate.msgTitle')" prop="title">
              <el-input v-model="form.title" :placeholder="td('sys.system.messageTemplate.msgTitlePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.system.messageTemplate.msgCategory')" prop="category" :label-position="labelPosition">
              <el-select v-model="form.category" :placeholder="td('sys.system.messageTemplate.selectPlaceholder')">
                <el-option
                    v-for="dict in message_category"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('sys.system.messageTemplate.msgCategory')" prop="category">
              <el-select v-model="form.category" :placeholder="td('sys.system.messageTemplate.selectPlaceholder')">
                <el-option
                    v-for="dict in message_category"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.messageTemplate.msgLevel')" prop="level" :label-position="labelPosition">
              <el-select v-model="form.msgLevel" :placeholder="td('sys.system.messageTemplate.selectPlaceholder')">
                <el-option
                    v-for="dict in message_level"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('sys.system.messageTemplate.msgTemplate')" prop="content" :label-position="labelPosition">
              <el-input v-model="form.content" type="textarea" :placeholder="td('sys.system.messageTemplate.inputContent')" />
              <!--          <editor v-model="form.content" :min-height="192"/>-->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.system.messageTemplate.inputContent')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MessageTemplate">
import useDefaultLang from "@/composables/useDefaultLang";
import { listMessageTemplate, getMessageTemplate, delMessageTemplate, addMessageTemplate, updateMessageTemplate } from "@/api/system/system/message/messageTemplate";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { message_category, message_level } = proxy.useDict("message_category", "message_level");

const messageTemplateList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    category: null,
    msgLevel: null,
  },
  rules: {
    title: [
      { required: true, message: td('sys.system.messageTemplate.msgTitleRequired'), trigger: "blur" }
    ],
    content: [
      { required: true, message: td('sys.system.messageTemplate.msgTemplateRequired'), trigger: "blur" }
    ],
    category: [
      { required: true, message: td('sys.system.messageTemplate.msgCategoryRequired'), trigger: "blur" }
    ],
    msgLevel: [
      { required: true, message: td('sys.system.messageTemplate.msgLevelRequired'), trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询消息模板列表 */
function getList() {
  loading.value = true;
  listMessageTemplate(queryParams.value).then(response => {
    messageTemplateList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
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
    title: null,
    content: null,
    category: null,
    msgLevel: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("messageTemplateRef");
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = td('sys.system.messageTemplate.addTitle');
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getMessageTemplate(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('sys.system.messageTemplate.editTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["messageTemplateRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateMessageTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      } else {
        addMessageTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      }
    } else {
      submitLoading.value = false;
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm(td('sys.system.messageTemplate.confirmDelete', { id: _ids })).then(function() {
    return delMessageTemplate(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/messageTemplate/export', {
    ...queryParams.value
  }, `messageTemplate_${new Date().getTime()}.xlsx`)
}

getList();
</script>
