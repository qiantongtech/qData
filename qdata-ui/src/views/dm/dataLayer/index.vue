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
    <el-container>
      <!-- 左侧树 -->
      <DeptTree
        :deptOptions="layerTreeOptions"
        :leftWidth="leftWidth"
        :placeholder="'请输入数仓分层名称'"
        ref="layerTreeRef"
        @node-click="handleNodeClick"
        title="数仓分层"
      />

      <!-- 右侧列表 -->
      <el-main class="main-content">
        <!-- 顶部信息卡片 -->
        <div
          v-if="currentLayer"
          class="layer-info-card mb20"
          style="
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            border: 1px solid #e4e7ed;
          "
        >
          <h3
            style="
              margin-top: 0;
              color: #409eff;
              display: flex;
              align-items: center;
            "
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>
            {{ currentLayer.name }}
            {{ currentLayer.engName ? "(" + currentLayer.engName + ")" : "" }}
          </h3>
          <div
            style="
              display: flex;
              flex-direction: column;
              gap: 10px;
              font-size: 13px;
              color: #606266;
            "
          >
            <div style="display: flex">
              <span style="min-width: 70px; color: #909399">业务定义：</span>
              <span>{{ currentLayer.description || "暂无描述" }}</span>
            </div>
            <div style="display: flex" v-if="currentLayer.engName">
              <span style="min-width: 90px; color: #909399"
                >命名核心标准：</span
              >
              <span
                style="
                  background: #e8f4ff;
                  color: #1890ff;
                  padding: 2px 8px;
                  border-radius: 4px;
                "
                >表名前缀固定为 {{ currentLayer.engName }}_</span
              >
            </div>
          </div>
        </div>

        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            label-width="75px"
            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item label="表前缀" prop="prefixName">
              <el-input
                class="el-form-input-width"
                v-model="queryParams.prefixName"
                placeholder="请输入表前缀"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                plain
                type="primary"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
              </el-button>
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
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
                  v-hasPermi="['dm:dataLayerSpecification:add']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['dm:dataLayerSpecification:edit']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['dm:dataLayerSpecification:remove']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
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
            height="50vh"
            v-loading="loading"
            :data="specificationList"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column
              v-if="getColumnVisibility(0)"
              label="ID"
              align="center"
              prop="id"
            />
            <el-table-column
              v-if="getColumnVisibility(1)"
              label="表前缀"
              align="center"
              prop="prefixName"
            />
            <el-table-column
              v-if="getColumnVisibility(2)"
              label="业务大类英文缩写"
              align="center"
              prop="businessEngName"
            />
            <el-table-column
              v-if="getColumnVisibility(3)"
              label="负责人名称"
              align="center"
              prop="ownerUserName"
            />
            <el-table-column
              v-if="getColumnVisibility(4)"
              label="状态"
              align="center"
              prop="status"
            >
              <template #default="scope">
                <dict-tag
                  :options="sys_normal_disable"
                  :value="scope.row.status"
                />
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(5)"
              label="描述"
              align="center"
              prop="description"
            />
            <el-table-column
              v-if="getColumnVisibility(6)"
              label="创建时间"
              align="center"
              prop="createTime"
              width="180"
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d}")
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
              width="160"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['dm:dataLayerSpecification:edit']"
                  >修改</el-button
                >
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['dm:dataLayerSpecification:remove']"
                  >删除</el-button
                >
              </template>
            </el-table-column>
            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
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

    <!-- 添加或修改规范管理对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="600px"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form
        ref="specificationRef"
        :model="form"
        :rules="rules"
        label-width="140px"
        @submit.prevent
      >
        <el-form-item label="表前缀" prop="prefixName">
          <el-input v-model="form.prefixName" placeholder="请输入表前缀" />
        </el-form-item>
        <el-form-item label="业务大类英文缩写" prop="businessEngName">
          <el-input
            v-model="form.businessEngName"
            placeholder="请输入业务大类英文缩写"
          />
        </el-form-item>
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
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
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
import { deptUserTree } from "@/api/system/system/user.js";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const leftWidth = ref(300); // 初始左侧宽度
const specificationList = ref([]);
const layerTreeOptions = ref([]);
const currentLayer = ref(null);
const layerTreeRef = ref(null);
const managerOptions = ref([]);

function getManagerOptions() {
  deptUserTree().then((response) => {
    managerOptions.value = response.data;
  });
}

function handleOwnerChange(val) {
  const selected = managerOptions.value.find((item) => item.userId === val);
  if (selected) {
    form.value.ownerUserName = selected.nickName;
  }
}

// 列显隐信息
const columns = ref([
  { key: 0, label: "ID", visible: false },
  { key: 1, label: "表前缀", visible: true },
  { key: 2, label: "业务大类英文缩写", visible: true },
  { key: 3, label: "负责人名称", visible: true },
  { key: 4, label: "状态", visible: true },
  { key: 5, label: "描述", visible: true },
  { key: 6, label: "创建时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  return column ? column.visible : true;
};

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
    dataLayerId: null,
    prefixName: null,
  },
  rules: {
    prefixName: [
      { required: true, message: "表前缀不能为空", trigger: "blur" },
    ],
    businessEngName: [
      { required: true, message: "业务大类英文缩写不能为空", trigger: "blur" },
    ],
    ownerUserId: [
      { required: true, message: "负责人不能为空", trigger: "blur" },
    ],
    status: [{ required: true, message: "状态不能为空", trigger: "change" }],
    description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

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
        queryParams.value.dataLayerId = targetNode.id;
        getList();
      } else if (layerTreeOptions.value.length > 0) {
        const firstNode = layerTreeOptions.value[0];
        if (layerTreeRef.value && layerTreeRef.value.setCurrentKey) {
          layerTreeRef.value.setCurrentKey(firstNode.id);
        }
        currentLayer.value = firstNode;
        queryParams.value.dataLayerId = firstNode.id;
        getList();
      }
    });
  });
}

/** 节点单击事件 */
function handleNodeClick(data) {
  currentLayer.value = data;
  queryParams.value.dataLayerId = data.id;
  handleQuery();
}

/** 查询列表 */
function getList() {
  if (!queryParams.value.dataLayerId) return;
  loading.value = true;
  listDataLayerSpecification(queryParams.value).then((response) => {
    specificationList.value = response.data.rows;
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
    dataLayerId: currentLayer.value ? currentLayer.value.id : null,
    prefixName: null,
    businessEngName: null,
    ownerUserId: null,
    ownerUserName: null,
    status: "0",
    description: null,
  };
  proxy.resetForm("specificationRef");
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  if (!currentLayer.value) {
    proxy.$modal.msgError("请先选择左侧数仓分层");
    return;
  }
  open.value = true;
  title.value = "添加规范管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
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
    title.value = "修改规范管理";
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
          getList();
        });
      } else {
        addDataLayerSpecification(form.value).then(() => {
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
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除规范管理编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDataLayerSpecification(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getTree();
getManagerOptions();
</script>

<style scoped lang="scss">
</style>
