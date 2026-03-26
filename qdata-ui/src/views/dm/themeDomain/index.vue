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
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="75px"
      >
        <el-form-item label="主题域类目名称" prop="name" label-width="110">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入主题域类目名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="上级类目" prop="code">
          <el-tree-select
            filterable
            class="el-form-input-width"
            v-model="queryParams.code"
            :data="attDataElemCatOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择上级类目"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="数仓分层" prop="dataLayerId">
          <el-tree-select
            filterable
            class="el-form-input-width"
            v-model="queryParams.dataLayerId"
            :data="dataLayerOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择数仓分层"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="负责人" prop="ownerUserId">
          <el-select
            v-model="queryParams.ownerUserId"
            class="el-form-input-width"
            filterable
            placeholder="请选择负责人"
          >
            <el-option
              v-for="item in managerOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>
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
            >查询
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
              v-hasPermi="['att:dataElemCat:add']"
              >新增</el-button
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
              <span>{{ isExpandAll ? "折叠" : "展开" }}</span>
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
          label="主题域类目名称"
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
          label="描述"
          align="left"
          prop="description"
          :show-overflow-tooltip="{ effect: 'light' }"
          width="250"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="数仓分层" align="center" prop="validFlag">
          <template #default="scope">
            {{ scope.row.dataLayerName || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" prop="validFlag">
          <template #default="scope">
            {{ scope.row.ownerUserName || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="validFlag">
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
          label="备注"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
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

        <el-table-column
          label="操作"
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
              >修改</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['att:dataElemCat:add']"
              >新增</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['att:dataElemCat:remove']"
              >删除</el-button
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
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="themeDomainRef"
        :model="form"
        :rules="rules"
        label-width="130px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="主题域类目名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入主题域类目名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上级类目" prop="parentId">
              <el-tree-select
                filterable
                v-model="form.parentId"
                :data="attDataElemCatOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择上级类目"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="数仓分层" prop="dataLayerId">
              <el-tree-select
                filterable
                v-model="form.dataLayerId"
                :data="dataLayerOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择数仓分层"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="英文缩写" prop="engName">
              <el-input v-model="form.engName" placeholder="请输入英文缩写" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="负责人" prop="ownerUserId">
              <el-select
                v-model="form.ownerUserId"
                filterable
                placeholder="请选择负责人"
              >
                <el-option
                  v-for="item in managerOptions"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="256个字符"
                :min-height="256"
                show-word-limit
                placeholder="请输入描述"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                maxlength="500个字符"
                show-word-limit
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
  </div>
</template>

<script setup name="DataElemCat">
import {
  getThemeDomain,
  addThemeDomain,
  updateThemeDomain,
  listThemeDomain,
  delThemeDomain,
} from "@/api/dm/themeDomain/themeDomain.js";
import { deptUserTree } from "@/api/system/system/user.js";
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";

const { proxy } = getCurrentInstance();
const attDataElemCatList = ref([]);
const attDataElemCatOptions = ref([]);
const managerOptions = ref([]);
const dataLayerOptions = ref([]);
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
      { required: true, message: "主题域类目名称不能为空", trigger: "blur" },
    ],
    parentId: [
      { required: true, message: "上级类目不能为空", trigger: "blur" },
    ],
    engName: [{ required: true, message: "英文缩写不能为空", trigger: "blur" }],
    ownerUserId: [
      { required: true, message: "负责人不能为空", trigger: "blur" },
    ],
    dataLayerId: [
      { required: true, message: "数仓分层不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询主题域类目管理列表 */
function getList() {
  loading.value = true;
  listThemeDomain(queryParams.value).then((response) => {
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
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: "顶级节点", children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
}
function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}
function getDataLayerTree() {
  treeDataLayer().then((response) => {
    const disableRoot = (list) => {
      return list.map((item) => {
        const newItem = { ...item };
        if (!item.parentId || item.parentId === 0 || item.parentId === "0") {
          newItem.disabled = true;
        }
        if (item.children && item.children.length) {
          newItem.children = disableRoot(item.children);
        }
        return newItem;
      });
    };
    dataLayerOptions.value = disableRoot(response.data);
  });
}
/** 查询主题域类目管理下拉树结构1 */

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
    description: null,
    code: null,
    engName: null,
    ownerUserId: null,
    dataLayerId: null,
    // validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("themeDomainRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
/** 改变启用状态值 */
function handleStatusChange(row) {
  const text = row.validFlag === true ? "启用" : "禁用";
  proxy.$modal
    .confirm(`确认要"${text}","${row.name}"主题域类目吗？`)
    .then(() => {
      updateThemeDomain({ id: row.id, validFlag: row.validFlag })
        .then((response) => {
          proxy.$modal.msgSuccess(text + "成功");
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
  listThemeDomain().then((response) => {
    attDataElemCatOptions.value = [];
    const data = { id: 0, name: "顶级节点", children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    attDataElemCatOptions.value.push(data);
  });
  if (row != null && row.id) {
    form.value.parentId = row.id;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "新增主题域类目";
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
  const response = await listThemeDomain();
  attDataElemCatOptions.value = [];
  // 过滤节点的计算属性
  const filteredDepts = response.data.filter((d) => {
    // 过滤条件：去掉目标部门ID或者祖先中包含目标部门ID的项
    return (
      d.ID !== row.id &&
      !d.parentId.toString().split(",").includes(row.id.toString())
    );
  });
  const data = { id: 0, name: "顶级节点", children: [] };
  data.children = proxy.handleTree(filteredDepts, "id", "parentId");
  attDataElemCatOptions.value.push(data);
  if (row != null) {
    form.value.parentId = row.parentId;
  }
  getThemeDomain(row.id).then((response) => {
    //把createTime过滤掉
    delete response.data.createTime;
    delete response.data.updateTime;
    form.value = response.data;
    open.value = true;
    title.value = "修改主题域类目";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["themeDomainRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateThemeDomain(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addThemeDomain(form.value).then((response) => {
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
    .confirm('是否确认删除主题域类目管理编号为"' + row.name + '"的数据项？')
    .then(function () {
      return delThemeDomain(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getList();
getDataTree();
getManagerOptions();
getDataLayerTree();
</script>
