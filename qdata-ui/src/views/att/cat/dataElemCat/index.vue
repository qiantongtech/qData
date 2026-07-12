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
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
      >
        <el-form-item :label="td('att.common.dataElemCatName')" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            :placeholder="td('att.common.dataElemCatNamePlaceholder')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('att.common.parentCat')" prop="code">
          <el-tree-select
            filterable
            class="el-form-input-width"
            v-model="queryParams.code"
            :data="attDataElemCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            value-key="id"
            :placeholder="td('att.common.pleaseSelectParent')"
            check-strictly
          />
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i
              class="iconfont-mini icon-a-zu22377 mr5"
              v-hasPermi="['att:dataElemCat:query']"
            ></i
            >{{ td('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
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
              v-hasPermi="['att:dataElemCat:add']"
              >{{ td('common.button.add') }}</el-button
            >
          </el-col>

          <el-col :span="1.5">
            <el-button
              class="toggle-expand-all"
              type="primary"
              plain
              @click="toggleExpandAll"
            >
              <svg-icon v-if="isExpandAll" icon-class="toggle" />
              <svg-icon v-else icon-class="expand" />
              <span>{{ isExpandAll ? td('common.button.fold') : td('common.button.expand') }}</span>
            </el-button>
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
        :data="attDataElemCatList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column
          :label="td('att.common.dataElemCatName')"
          align="left"
          prop="name"
          width="200"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>

        <el-table-column
          :label="td('common.texts.description')"
          align="left"
          prop="description"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="250"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.sortOrder')"
          align="left"
          prop="sortOrder"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.sortOrder }}
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.createdBy')" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.createdTime')"
          align="center"
          prop="createTime"
          width="180"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.status')" align="center" prop="validFlag">
          <template #default="scope">
            <!--              <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>-->

            <el-switch
              v-model="scope.row.validFlag"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          :label="td('common.texts.remark')"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
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
          width="240"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['att:dataElemCat:edit']"
              >{{ td('common.button.update') }}</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['att:dataElemCat:add']"
              >{{ td('common.button.add') }}</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['att:dataElemCat:remove']"
              >{{ td('common.button.delete') }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 新增或修改数据元类目管理对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-form
        ref="attDataElemCatRef"
        :model="form"
        :rules="rules"
        label-width="80px"
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.categoryName')" prop="name" :label-position="labelPosition">
              <el-input
                v-model="form.name"
                :placeholder="td('att.common.dataElemCatNamePlaceholder')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('att.common.parentCat')" prop="parentId" :label-position="labelPosition">
              <el-tree-select
                filterable
                :disabled="form.id"
                v-model="form.parentId"
                :data="attDataElemCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                :placeholder="td('att.common.pleaseSelectParent')"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.description')" :label-position="labelPosition">
              <el-input
                type="textarea"
                :placeholder="td('common.form.descriptionPlaceholder')"
                v-model="form.description"
                :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('att.common.sortOrder')" prop="sortOrder" :label-position="labelPosition">
              <el-input-number
                style="width: 100%"
                v-model="form.sortOrder"
                controls-position="right"
                :min="0"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="validFlag" :label-position="labelPosition">
              <el-radio v-model="form.validFlag" :label="true">{{ td('att.common.enable') }}</el-radio>
              <el-radio v-model="form.validFlag" :label="false">{{ td('att.common.disable') }}</el-radio>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
              <el-input
                type="textarea"
                :placeholder="td('common.form.remarkPlaceholder')"
                v-model="form.remark"
                :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>

          <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataElemCat">
import { useI18n } from 'vue-i18n'
import useDefaultLang from "@/composables/useDefaultLang";
import {
  listAttDataElemCat,
  getAttDataElemCat,
  delAttDataElemCat,
  addAttDataElemCat,
  updateAttDataElemCat,
} from "@/api/att/cat/dataElemCat/dataElemCat.js";

const { t } = useI18n();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);

const attDataElemCatList = ref([]);
const attDataElemCatOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const total = ref(0);
const isExpandAll = ref(false);
const refreshTable = ref(true);

const data = reactive({
  form: {},
  queryParams: {
    name: null,
    parentId: null,
    createTime: null,
  },
  rules: {
    name: [
      { required: true, message: td('att.dataElemCat.validations.nameRequired'), trigger: "blur" },
    ],
    parentId: [
      { required: true, message: td('att.dataElemCat.validations.parentIdRequired'), trigger: "blur" },
    ],
    code: [{ required: true, message: td('att.common.codeRequired'), trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询数据元类目管理列表 */
function getList() {
  loading.value = true;
  listAttDataElemCat(queryParams.value).then((response) => {
    attDataElemCatList.value = proxy.handleTree(
      response.data,
      "id",
      "parentId"
    );
    // total.value = response.data.total;
    loading.value = false;
  });
}
function getDataTree() {
  listAttDataElemCat().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
}
/** 查询数据元类目管理下拉树结构1 */

// 取消按钮
function cancel() {
  open.value = false;
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
    remark: null,
  };
  proxy.resetForm("attDataElemCatRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
  proxy.$modal
    .confirm(td('att.common.confirmStatusChangeGeneric').replace('<status>', text).replace('<name>', row.name).replace('<type>', td('att.common.dataElemCatName')))
    .then(() => {
      updateAttDataElemCat({ id: row.id, validFlag: row.validFlag })
        .then((response) => {
          proxy.$modal.msgSuccess(td('att.common.statusSuccess').replace('<status>', text));
          getList();
        })
        .catch((err) => {
          row.validFlag = !row.validFlag;
        });
    })
    .catch(() => {
      row.validFlag = !row.validFlag;
    });
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  // getTreeselect();
  listAttDataElemCat().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: td('common.texts.topNode'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td('att.dataElemCat.title.add');
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
async function handleUpdate(row) {
  reset();
  // await getTreeselect();
  const response = await listAttDataElemCat();
  attDataElemCatOptions.value = [];
  // 过滤节点的计算属性
  const filteredDepts = response.data.filter((d) => {
    // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
    return (
      d.ID !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  const data = { id: 0, name: td('common.texts.topNode'), children: [] };
  data.children = proxy.handleTree(filteredDepts, "id", "parentId");
  attDataElemCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getAttDataElemCat(row.id).then((response) => {
    //把createTime过滤掉
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = td('att.dataElemCat.title.edit');
  });
}

/** 提交按钮 */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["attDataElemCatRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateAttDataElemCat(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(error => {
          submitLoading.value = false;
        });
      } else {
        addAttDataElemCat(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(error => {
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
  proxy.$modal
    .confirm(td('att.dataElemCat.messages.confirmDelete').replace('<name>', row.name))
    .then(function () {
      return delAttDataElemCat(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}

getList();
getDataTree();
</script>
