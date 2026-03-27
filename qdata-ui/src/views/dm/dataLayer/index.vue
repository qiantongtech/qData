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
    <GuideTip tip-id="dm/dataLayer.list" />
    <el-container>
      <!-- 左侧树 -->
      <DeptTree
        :deptOptions="layerTreeOptions"
        :leftWidth="leftWidth"
        :placeholder="'请输入数仓分层名称'"
        ref="layerTreeRef"
        @node-click="handleNodeClick"
        title="数仓分层"
      >
      </DeptTree>

      <!-- 右侧列表 -->
      <el-main class="main-content">
        <!-- 顶部信息卡片 -->
        <layerInfoCard v-if="currentLayer" class="mb20" :layer="currentLayer" />

        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              :tableRef="tableRef"
            />
          </template>
          <template #actions-data>
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
              v-hasPermi="['dm:dataLayer:add']"
            >
              新增
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(row)"
                v-hasPermi="['dm:dataLayer:edit']"
              >
                修改
              </el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDelete(row)"
                v-hasPermi="['dm:dataLayer:remove']"
              >
                删除
              </el-button>
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleDetail(row)"
                v-hasPermi="['dm:dataLayer:edit']"
              >
                详情
              </el-button>
            </template>

            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-value="1"
                inactive-value="0"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="handleStatusChange(row)"
              />
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- 添加或修改规范对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      :append-to="$refs['app-container']"
      draggable
      width="800px"
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="specificationRef"
        :model="form"
        :rules="rules"
        label-width="140px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="表前缀" prop="prefixName">
              <el-input v-model="form.prefixName" placeholder="请输入表前缀" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业务英文缩写" prop="businessEngName">
              <el-input
                v-model="form.businessEngName"
                placeholder="请输入业务英文缩写"
              />
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
                @change="handleOwnerChange"
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
          <el-col :span="24">
            <el-form-item label="负责人电话" prop="ownerUserPhoneNumber">
              <el-input
                v-model="form.ownerUserPhoneNumber"
                placeholder="请输入负责人电话"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入描述"
                :min-height="192"
                show-word-limit
                maxlength="500个字符"
              />
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

    <!-- 规范详情对话框 -->
    <el-dialog
      title="规范详情"
      v-model="openDetail"
      :append-to="$refs['app-container']"
      draggable
      width="800px"
    >
      <el-form ref="specificationDetailRef" :model="form" label-width="140px">
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">
            {{ form.id }}
          </div>
        </el-form-item>
        <el-form-item label="表前缀" prop="prefixName">
          <div class="form-readonly">{{ form.prefixName ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="业务英文缩写" prop="businessEngName">
          <div class="form-readonly">{{ form.businessEngName ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="负责人" prop="ownerUserName">
          <div class="form-readonly">{{ form.ownerUserName || "-" }}</div>
        </el-form-item>
        <el-form-item label="负责人电话" prop="ownerUserPhoneNumber">
          <div class="form-readonly">
            {{ form.ownerUserPhoneNumber || "-" }}
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <dict-tag :options="sys_normal_disable" :value="form.status" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="创建人" prop="createBy">
              <div class="form-readonly">
                {{ form.createBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="创建时间" prop="createTime">
              <div class="form-readonly">
                {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="更新人" prop="createBy">
              <div class="form-readonly">
                {{ form.updateBy }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="更新时间" prop="updateTime">
              <div class="form-readonly">
                {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelDetail">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataLayer">
import { treeDataLayer } from "@/api/dm/dataLayer/dataLayer.js";
import {
  listDataLayerSpecification,
  getDataLayerSpecification,
  addDataLayerSpecification,
  updateDataLayerSpecification,
  delDataLayerSpecification,
} from "@/api/dm/dataLayerSpecification/dataLayerSpecification.js";
import DeptTree from "@/components/DeptTree";
import { deptUserTree, getUser } from "@/api/system/system/user.js";
import layerInfoCard from "./components/layerInfoCard.vue";
import {
  computed,
  getCurrentInstance,
  nextTick,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "vue";

// 导入必要的图标组件
import { FolderOpened, Folder, Tickets } from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");
const leftWidth = ref(300); // 初始左侧宽度
const layerTreeOptions = ref([]);
const currentLayer = ref(null);
const layerTreeRef = ref(null);
const managerOptions = ref([]);
const tableRef = ref(null);
const activeDropdownNodeId = ref(null);

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function handleOwnerChange(val) {
  const selected = managerOptions.value.find((item) => item.userId === val);
  if (selected) {
    form.value.ownerUserName = selected.nickName;
    // 更新负责人电话
    form.value.ownerUserPhoneNumber = selected.phonenumber || "";
  }
}

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const tableStore = reactive({
  config: {
    stripe: true,
    table: {
      rowKey: "id",
      defaultSort: { prop: "createTime", order: "descending" },
      onSelectionChange: function (selection) {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length !== 1;
        multiple.value = !selection.length;
      },
    },
  },
  columns: [
    // { type: "selection", width: 55, align: "left" },
    { label: "编号", prop: "id", width: 60, sortable: true },
    {
      label: "表前缀",
      prop: "prefixName",
      align: "left",
      width: 180,
      showOverflowTooltip: true,
    },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "业务英文缩写",
      prop: "businessEngName",
      align: "left",
      width: 100,
    },

    {
      label: "状态",
      prop: "status",
      align: "left",
      width: 100,
      slot: "status",
    },
    { label: "负责人", prop: "ownerUserName", align: "left", width: 120 },
    {
      label: "负责人电话",
      prop: "ownerUserPhoneNumber",
      align: "left",
      width: 140,
    },

    {
      label: "创建人",
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },
    {
      label: "操作",
      width: 220,
      slot: "action",
      fixed: "right",
    },
  ],
  func: listDataLayerSpecification,
  params: {
    dataLayerId: computed(() => currentLayer.value?.id || null),
  },
});

const searchStore = reactive({
  items: [
    {
      label: "表前缀",
      prop: "prefixName",
      component: { is: "input", placeholder: "请输入表前缀" },
    },
    {
      label: "业务英文缩写",
      prop: "businessEngName",
      component: { is: "input", placeholder: "请输入业务英文缩写" },
    },
    {
      label: "负责人",
      prop: "ownerUserId",
      component: {
        is: "tree-select",
        data: managerOptions,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "ID",
        placeholder: "请选择负责人",
        checkStrictly: true,
      },
    },
  ],
});

const open = ref(false);
const openDetail = ref(false);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    prefixName: [
      { required: true, message: "表前缀不能为空", trigger: "blur" },
    ],
    businessEngName: [
      { required: true, message: "业务英文缩写不能为空", trigger: "blur" },
      { pattern: /^[a-zA-Z]+$/, message: "只能输入英文字符", trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: "负责人不能为空", trigger: "blur" },
    ],
  },
});

const { form, rules } = toRefs(data);

/** 查询树 */
function getTree() {
  treeDataLayer().then((response) => {
    layerTreeOptions.value = response.data;
    nextTick(() => {
      let targetNode = null;
      const findNode = (nodes) => {
        for (let node of nodes) {
          if (node.name === "操作数据层") {
            targetNode = node;
            return;
          }
          if (node.children && node.children.length > 0) {
            findNode(node.children);
          }
        }
      };
      findNode(layerTreeOptions.value);

      if (targetNode) {
        if (layerTreeRef.value && layerTreeRef.value.setCurrentKey) {
          layerTreeRef.value.setCurrentKey(targetNode.id);
        }
        currentLayer.value = targetNode;
        tableRef.value?.getList();
      } else if (layerTreeOptions.value.length > 0) {
        const firstNode = layerTreeOptions.value[0];
        if (layerTreeRef.value && layerTreeRef.value.setCurrentKey) {
          layerTreeRef.value.setCurrentKey(firstNode.id);
        }
        currentLayer.value = firstNode;
        tableRef.value?.getList();
      }
    });
  });
}

/** 节点单击事件 */
function handleNodeClick(data) {
  if (data.parentId != 0) {
    currentLayer.value = data;
    tableRef.value?.getList();
  }
}

// 处理树节点点击
function handleTreeNodeClick(node) {
  // 如果是一级节点，只展开/折叠，不触发选中
  if (node.level === 1) {
    if (node.expanded) {
      node.collapse();
    } else {
      node.expand();
    }
  } else {
    // 如果不是一级节点，执行正常的节点点击逻辑
    currentLayer.value = node.data;
    tableRef.value?.getList();
  }
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 关闭详情
function cancelDetail() {
  openDetail.value = false;
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    dataLayerId: currentLayer.value ? currentLayer.value.id : null,
    prefixName: null,
    businessEngName: null,
    ownerUserId: null,
    ownerUserName: null,
    ownerUserPhoneNumber: null,
    status: "0",
    description: null,
  };
  proxy.resetForm("specificationRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  if (!currentLayer.value) {
    proxy.$modal.msgError("请先选择左侧数仓分层");
    return;
  }
  open.value = true;
  title.value = "添加规范";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row?.id || ids.value[0];
  getDataLayerSpecification(_id).then((response) => {
    form.value = response.data;
    if (form.value.ownerUserName && !form.value.ownerUserId) {
      const selected = managerOptions.value.find(
        (item) => item.nickName === form.value.ownerUserName
      );
      if (selected) {
        form.value.ownerUserId = selected.userId;
      }
    }
    open.value = true;
    title.value = "修改规范";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row?.id || ids.value[0];
  getDataLayerSpecification(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["specificationRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDataLayerSpecification(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          tableRef.value?.getList();
        });
      } else {
        addDataLayerSpecification(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          tableRef.value?.getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row?.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除规范编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDataLayerSpecification(_ids);
    })
    .then(() => {
      tableRef.value?.getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用";
  proxy.$modal
    .confirm("确认要" + text + '规范"' + row.id + '"吗?')
    .then(function () {
      return updateDataLayerSpecification({ id: row.id, status: row.status });
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功");
    })
    .catch(function () {
      // 恢复switch状态
      row.status = row.status === "0" ? "1" : "0";
    });
}

onMounted(() => {
  getTree();
  getManagerOptions();
});
</script>

<style scoped lang="scss">
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.treelabel {
  flex: 1;
  cursor: pointer;

  &:hover {
    color: var(--el-color-primary);
  }
}

.iconimg,
.zjiconimg {
  margin-right: 6px;
  font-size: 16px;
}

.colorxz {
  color: var(--el-color-primary);
}

.colorwxz {
  color: var(--el-text-color-secondary);
}

.operation-trigger {
  padding: 4px;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: var(--el-fill-color-light);
  }

  &.is-active {
    background-color: var(--el-fill-color);
  }
}

.action-icon {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}
</style>