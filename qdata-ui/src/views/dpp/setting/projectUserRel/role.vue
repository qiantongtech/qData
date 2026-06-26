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
  <div class="pagecont-top" v-show="showSearch">
    <el-form
      class="btn-style"
      :model="queryParams"
      ref="queryRef"
      v-show="showSearch"
      :inline="true"

    >
      <el-form-item
        :label="td('dpp.setting.projectUserRel.roleName')"
        prop="roleName"
      >
        <el-input
          v-model="queryParams.roleName"
          :placeholder="td('dpp.setting.projectUserRel.inputRoleName')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="td('dpp.setting.projectUserRel.roleKey')"
        prop="roleKey"
      >
        <el-input
          v-model="queryParams.roleKey"
          :placeholder="td('dpp.setting.projectUserRel.inputRoleKey')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="td('dpp.setting.projectUserRel.roleStatus')"
          clearable
          class="el-form-input-width"
        >
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="td('common.texts.createdTime')">
        <el-date-picker
          class="el-form-input-width"
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          :start-placeholder="td('common.form.startDatePlaceholder')"
          :end-placeholder="td('common.form.endDatePlaceholder')"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <!-- <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button> -->
        <el-button
          plain
          type="primary"
          @click="handleQuery"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i
          >{{ td("common.button.query") }}
        </el-button>
        <el-button icon="Refresh" @click="resetQuery">{{
          td("common.button.reset")
        }}</el-button>
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
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['att:project:role:add']"
            >{{ td("common.button.add") }}</el-button
          >
        </el-col>

        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['att:project:role:remove']"
            >{{ td("common.button.delete") }}</el-button
          >
        </el-col>
      </el-row>
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </div>

    <!-- 表格数据 -->
    <el-table
      stripe
      v-loading="loading"
      :data="roleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        :label="td('common.texts.number')"
        width="60"
        prop="roleId"
        align="center"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.roleName')"
        prop="roleName"
        align="center"
        :show-overflow-tooltip="{ effect: 'light' }"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.roleRemark')"
        prop="remark"
        align="center"
        :show-overflow-tooltip="{ effect: 'light' }"
        width="250"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.roleKey')"
        prop="roleKey"
        align="center"
        :show-overflow-tooltip="{ effect: 'light' }"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.roleSort')"
        prop="roleSort"
        align="center"
      />
      <el-table-column
        :label="td('common.texts.createdBy')"
        align="center"
        prop="createBy"
      >
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('common.texts.createdTime')"
        align="center"
        prop="createTime"
        width="160"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="td('common.texts.status')" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
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
            v-hasPermi="['att:project:role:edit']"
            v-if="scope.row.roleId !== 1 || scope.row.builtInOrNot"
            >{{ td("common.button.update") }}</el-button
          >
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['att:project:role:remove']"
            v-if="
              (scope.row.roleId !== 1 && scope.row.roleId !== 3) ||
              scope.row.builtInOrNot
            "
            >{{ td("common.button.delete") }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>

  <!-- 新增或修改角色配置对话框 -->
  <el-dialog
    :title="title"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
  >
    <el-form ref="roleRef" :model="form" :rules="rules" label-width="100px" :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.setting.projectUserRel.roleName')"
            prop="roleName"
           :label-position="labelPosition">
            <el-input
              v-model="form.roleName"
              :placeholder="td('dpp.setting.projectUserRel.inputRoleName')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="roleKey" :label-position="labelPosition">
            <template #label>
              <span>
                <el-tooltip
                  :content="td('dpp.setting.projectUserRel.roleKeyHint')"
                  placement="top"
                >
                  <!-- <el-icon style="color: #909399;"><InfoFilled /></el-icon> -->
                  <el-icon style="color: #909399">
                    <InfoFilled />
                  </el-icon>
                </el-tooltip>
                {{ td("dpp.setting.projectUserRel.roleKey") }}
              </span>
            </template>
            <el-input
              v-model="form.roleKey"
              :placeholder="td('dpp.setting.projectUserRel.inputRoleKey')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.setting.projectUserRel.roleSort')"
            prop="roleSort"
           :label-position="labelPosition">
            <el-input-number
              style="width: 100%"
              v-model="form.roleSort"
              controls-position="right"
              :min="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="td('common.texts.status')" :label-position="labelPosition">
            <el-radio-group v-model="form.status">
              <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :value="dict.value"
                >{{ dict.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="td('dpp.setting.projectUserRel.menuPermission')" :label-position="labelPosition">
        <el-checkbox
          v-model="menuExpand"
          @change="handleCheckedTreeExpand($event, 'menu')"
          >{{ td("common.button.un_fold") }}</el-checkbox
        >
        <el-checkbox
          v-model="menuNodeAll"
          @change="handleCheckedTreeNodeAll($event, 'menu')"
          >{{ td("common.button.selectAll") }}/{{
            td("common.button.unselectAll")
          }}</el-checkbox
        >
        <el-checkbox
          v-model="form.menuCheckStrictly"
          @change="handleCheckedTreeConnect($event, 'menu')"
          >{{ td("common.button.linkParentChild") }}</el-checkbox
        >
        <el-tree
          class="tree-border"
          :data="menuOptions"
          show-checkbox
          ref="menuRef"
          node-key="id"
          :default-expanded-keys="expandedKeys"
          :check-strictly="!form.menuCheckStrictly"
          :empty-text="td('common.button.loading')"
          :props="{ label: 'label', children: 'children' }"
        ></el-tree>
      </el-form-item>
      <el-form-item :label="td('dpp.setting.projectUserRel.roleRemark')" :label-position="labelPosition">
        <el-input
          v-model="form.remark"
          type="textarea"
          placeholder="请输入权限描述"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ td("common.button.cancel") }}</el-button>
        <el-button type="primary" @click="submitForm">{{
          td("common.button.confirm")
        }}</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 分配角色数据权限对话框 -->
  <el-dialog
    :title="title"
    v-model="openDataScope"
    width="500px"
    append-to-body
  >
    <el-form :model="form" label-width="80px" :label-position="labelPosition">
      <el-form-item :label="td('dpp.setting.projectUserRel.roleName')" :label-position="labelPosition">
        <el-input v-model="form.roleName" :disabled="true" />
      </el-form-item>
      <el-form-item :label="td('dpp.setting.projectUserRel.roleKey')" :label-position="labelPosition">
        <el-input v-model="form.roleKey" :disabled="true" />
      </el-form-item>
      <el-form-item :label="td('dpp.setting.projectUserRel.dataScope')" :label-position="labelPosition">
        <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
          <el-option
            v-for="item in dataScopeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        :label="td('dpp.setting.projectUserRel.dataPermission')"
        v-show="form.dataScope == 2"
       :label-position="labelPosition">
        <el-checkbox
          v-model="deptExpand"
          @change="handleCheckedTreeExpand($event, 'dept')"
          >{{ td("common.button.un_fold") }}</el-checkbox
        >
        <el-checkbox
          v-model="deptNodeAll"
          @change="handleCheckedTreeNodeAll($event, 'dept')"
          >{{ td("common.button.selectAll") }}/{{
            td("common.button.unselectAll")
          }}</el-checkbox
        >
        <el-checkbox
          v-model="form.deptCheckStrictly"
          @change="handleCheckedTreeConnect($event, 'dept')"
          >{{ td("common.button.linkParentChild") }}</el-checkbox
        >
        <el-tree
          class="tree-border"
          :data="deptOptions"
          show-checkbox
          default-expand-all
          ref="deptRef"
          node-key="id"
          :expanded-keys="expandedKeys"
          :check-strictly="!form.deptCheckStrictly"
          :empty-text="td('common.button.loading')"
          :props="{ label: 'label', children: 'children' }"
        ></el-tree>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">{{
          td("common.button.confirm")
        }}</el-button>
        <el-button @click="cancelDataScope">{{
          td("common.button.cancel")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="Role">
import {
  addRole,
  changeRoleStatus,
  dataScope,
  delRole,
  getRole,
  listRole,
  updateRole,
  deptTreeSelect,
} from "@/api/att/projectUserRel/attProjectUserRel.js";
import {
  roleMenuTreeselectDpp,
  treeselectDpp as menuTreeselect,
} from "@/api/system/system/menu.js";
import useUserStore from "@/store/system/user";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");
const userStore = useUserStore();
const roleList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const menuOptions = ref([]);
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const deptExpand = ref(true);
const deptNodeAll = ref(false);
const deptOptions = ref([]);
const openDataScope = ref(false);
const menuRef = ref(null);
const deptRef = ref(null);
const expandedKeys = ref([]);
/** 数据范围选项*/
const dataScopeOptions = computed(() => [
  { value: "1", label: td("dpp.setting.projectUserRel.allDataScope") },
  { value: "2", label: td("dpp.setting.projectUserRel.customDataScope") },
  { value: "3", label: td("dpp.setting.projectUserRel.deptDataScope") },
  { value: "4", label: td("dpp.setting.projectUserRel.deptAndBelowDataScope") },
  { value: "5", label: td("dpp.setting.projectUserRel.onlySelfDataScope") },
]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    roleName: undefined,
    roleKey: undefined,
    status: undefined,
  },
  rules: {
    roleName: [
      {
        required: true,
        message: td("dpp.setting.projectUserRel.roleNameRequired"),
        trigger: "blur",
      },
    ],
    roleKey: [
      {
        required: true,
        message: td("dpp.setting.projectUserRel.roleKeyRequired"),
        trigger: "blur",
      },
    ],
    roleSort: [
      {
        required: true,
        message: td("dpp.setting.projectUserRel.roleSortRequired"),
        trigger: "blur",
      },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);
// 监听 userStore 中的 projectId 变化
watch(
  () => userStore.projectId,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      queryParams.value.projectId = newValue;
      getList();
    }
  },
  { immediate: true }
);
/** 查询角色列表 */
function getList() {
  loading.value = true;
  if (queryParams.value.projectId) {
    listRole(proxy.addDateRange(queryParams.value, dateRange.value)).then(
      (response) => {
        roleList.value = response.rows;
        total.value = response.total;
        loading.value = false;
      }
    );
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row) {
  const roleIds = row.roleId || ids.value;
  proxy.$modal
    .confirm(
      td(
        "dpp.setting.projectUserRel.confirmDeleteRole",
        '是否确认删除角色编号为"{id}"的数据项？'
      ).replace("{id}", roleIds)
    )
    .then(function () {
      return delRole(roleIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "system/role/export",
    {
      ...queryParams.value,
    },
    `role_${new Date().getTime()}.xlsx`
  );
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.roleId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 角色状态修改 */
function handleStatusChange(row) {
  let text =
    row.status === "0"
      ? td("dpp.setting.projectUserRel.enable", "启用")
      : td("dpp.setting.projectUserRel.disable", "停用");
  proxy.$modal
    .confirm(
      td("dpp.setting.projectUserRel.confirmStatusChange", "确认要") +
        '"' +
        text +
        '"' +
        row.roleName +
        '"' +
        td("dpp.setting.projectUserRel.role", "角色吗?")
    )
    .then(function () {
      return changeRoleStatus(row.roleId, row.status);
    })
    .then(() => {
      proxy.$modal.msgSuccess(
        text + td("dpp.setting.projectUserRel.success", "成功")
      );
    })
    .catch(function () {
      row.status = row.status === "0" ? "1" : "0";
    });
}

/** 更多操作 */
function handleCommand(command, row) {
  switch (command) {
    case "handleDataScope":
      handleDataScope(row);
      break;
    case "handleAuthUser":
      handleAuthUser(row);
      break;
    default:
      break;
  }
}

/** 分配用户 */
function handleAuthUser(row) {
  router.push("/system/role-auth/user/" + row.roleId);
}

/** 查询菜单树结构 */
function getMenuTreeselect() {
  menuTreeselect().then((response) => {
    menuOptions.value = response.data;
    expandedKeys.value = response.data.map((item) => item.id);
  });
}

/** 所有部门节点数据 */
function getDeptAllCheckedKeys() {
  // 目前被选中的部门节点
  let checkedKeys = deptRef.value.getCheckedKeys();
  // 半选中的部门节点
  let halfCheckedKeys = deptRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

/** 重置新增的表单以及其他数据  */
function reset() {
  if (menuRef.value != undefined) {
    menuRef.value.setCheckedKeys([]);
  }
  menuExpand.value = false;
  menuNodeAll.value = false;
  deptExpand.value = true;
  deptNodeAll.value = false;
  form.value = {
    roleId: undefined,
    projectId: undefined,
    roleName: undefined,
    roleKey: undefined,
    roleSort: 0,
    status: "0",
    menuIds: [],
    deptIds: [],
    menuCheckStrictly: true,
    deptCheckStrictly: true,
    remark: undefined,
  };
  proxy.resetForm("roleRef");
}

/** 新增角色 */
function handleAdd() {
  reset();
  getMenuTreeselect();
  open.value = true;
  title.value = td("dpp.setting.projectUserRel.addRole", "新增角色");
}

/** 修改角色 */
function handleUpdate(row) {
  reset();
  const roleId = row.roleId || ids.value;
  const roleMenu = getRoleMenuTreeselect(roleId);
  getRole(roleId).then((response) => {
    form.value = response.data;
    form.value.roleSort = Number(form.value.roleSort);
    open.value = true;
    nextTick(() => {
      roleMenu.then((res) => {
        let checkedKeys = res.checkedKeys;
        checkedKeys.forEach((v) => {
          nextTick(() => {
            menuRef.value.setChecked(v, true, false);
          });
        });
      });
    });
    title.value = td("dpp.setting.projectUserRel.editRole", "修改角色");
  });
}

/** 根据角色ID查询菜单树结构 */
function getRoleMenuTreeselect(roleId) {
  return roleMenuTreeselectDpp(roleId).then((response) => {
    menuOptions.value = response.menus;
    expandedKeys.value = response.menus.map((item) => item.id);
    console.log(expandedKeys.value, "expandedKeys");
    return response;
  });
}

/** 根据角色ID查询部门树结构 */
function getDeptTree(roleId) {
  return deptTreeSelect(roleId).then((response) => {
    deptOptions.value = response.depts;
    return response;
  });
}

/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value, type) {
  if (type == "menu") {
    let treeList = menuOptions.value;
    for (let i = 0; i < treeList.length; i++) {
      menuRef.value.store.nodesMap[treeList[i].id].expanded = value;
    }
  } else if (type == "dept") {
    let treeList = deptOptions.value;
    for (let i = 0; i < treeList.length; i++) {
      deptRef.value.store.nodesMap[treeList[i].id].expanded = value;
    }
  }
}

/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value, type) {
  if (type == "menu") {
    menuRef.value.setCheckedNodes(value ? menuOptions.value : []);
  } else if (type == "dept") {
    deptRef.value.setCheckedNodes(value ? deptOptions.value : []);
  }
}

/** 树权限（父子联动） */
function handleCheckedTreeConnect(value, type) {
  if (type == "menu") {
    form.value.menuCheckStrictly = value ? true : false;
  } else if (type == "dept") {
    form.value.deptCheckStrictly = value ? true : false;
  }
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys() {
  // 目前被选中的菜单节点
  let checkedKeys = menuRef.value.getCheckedKeys();
  // 半选中的菜单节点
  let halfCheckedKeys = menuRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["roleRef"].validate((valid) => {
    if (valid) {
      if (form.value.roleId != undefined) {
        form.value.projectId = userStore.projectId;
        form.value.menuIds = getMenuAllCheckedKeys();
        updateRole(form.value).then((response) => {
          proxy.$modal.msgSuccess(td("common.message.editSuccess"));
          open.value = false;
          getList();
        });
      } else {
        form.value.menuIds = getMenuAllCheckedKeys();
        form.value.projectId = userStore.projectId;
        addRole(form.value).then((response) => {
          proxy.$modal.msgSuccess(td("common.message.addSuccess"));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 选择角色权限范围触发 */
function dataScopeSelectChange(value) {
  if (value !== "2") {
    deptRef.value.setCheckedKeys([]);
  }
}

/** 分配数据权限操作 */
function handleDataScope(row) {
  reset();
  const deptTreeSelect = getDeptTree(row.roleId);
  getRole(row.roleId).then((response) => {
    form.value = response.data;
    openDataScope.value = true;
    nextTick(() => {
      deptTreeSelect.then((res) => {
        nextTick(() => {
          if (deptRef.value) {
            deptRef.value.setCheckedKeys(res.checkedKeys);
          }
        });
      });
    });
    title.value = td(
      "dpp.setting.projectUserRel.assignDataScope",
      "分配数据权限"
    );
  });
}

/** 提交按钮（数据权限） */
function submitDataScope() {
  if (form.value.roleId != undefined) {
    form.value.deptIds = getDeptAllCheckedKeys();
    dataScope(form.value).then((response) => {
      proxy.$modal.msgSuccess(td("common.message.editSuccess"));
      openDataScope.value = false;
      getList();
    });
  }
}

/** 取消按钮（数据权限）*/
function cancelDataScope() {
  openDataScope.value = false;
  reset();
}
</script>

<style scoped lang="scss">
.app-container {
  .pagecont-bottom {
    min-height: calc(100vh - 240px);
  }
}
</style>
