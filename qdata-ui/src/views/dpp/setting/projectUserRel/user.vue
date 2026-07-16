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
      :inline="true"
      v-show="showSearch"
      @submit.prevent
    >
      <el-form-item
        :label="td('dpp.setting.projectUserRel.userName')"
        prop="nickName"
      >
        <el-input
          class="el-form-input-width"
          v-model="queryParams.nickName"
          :placeholder="td('dpp.setting.projectUserRel.inputUserName')"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="td('dpp.setting.projectUserRel.phoneNumber')"
        prop="phoneNumber"
      >
        <el-input
          v-model="queryParams.phoneNumber"
          :placeholder="td('dpp.setting.projectUserRel.inputPhoneNumber')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.createdTime')">
        <el-date-picker
          @change="handleDateChange"
          class="el-form-input-width"
          v-model="createTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          :start-placeholder="td('common.form.startDatePlaceholder')"
          :end-placeholder="td('common.form.endDatePlaceholder')"
        ></el-date-picker>
      </el-form-item>

      <el-form-item :label-position="labelPosition">
        <el-button
          plain
          type="primary"
          @click="handleQuery"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i
          >{{ td("common.button.query") }}
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i
          >{{ td("common.button.reset") }}
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
            v-hasPermi="['att:projectUserRel:add']"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i
            >{{ td("common.button.add") }}
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['att:projectUserRel:remove']"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-shanchu-huise mr5"></i
            >{{ td("dpp.setting.projectUserRel.remove") }}
          </el-button>
        </el-col>
      </el-row>
      <div class="justify-end top-right-btn">
        <right-toolbar
          v-model:showSearch="showSearch"
          @queryTable="getList"
        ></right-toolbar>
      </div>
    </div>
    <el-table
      stripe
      v-loading="loading"
      :data="AttProjectUserRelList"
      @selection-change="handleSelectionChange"
      :default-sort="defaultSort"
      @sort-change="handleSortChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
      <el-table-column
        :label="td('dpp.setting.projectUserRel.userId')"
        width="80"
        align="center"
        prop="userId"
      >
        <template #default="scope">
          {{ scope.row.userId || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.setting.projectUserRel.userName')"
        align="center"
        prop="nickName"
      >
        <template #default="scope">
          {{ scope.row.nickName || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.setting.projectUserRel.role')"
        align="center"
        prop="roleStr"
        :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #default="scope">
          {{ scope.row.roleStr || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.setting.projectUserRel.dept')"
        align="center"
        prop="deptName"
      >
        <template #default="scope">
          {{ scope.row.deptName || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        :label="td('dpp.setting.projectUserRel.phoneNumber')"
        align="center"
        prop="phoneNumber"
      >
        <template #default="scope">
          {{ scope.row.phoneNumber || "-" }}
        </template>
      </el-table-column>
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
        v-if="getColumnVisibility(14)"
        :label="td('common.texts.createdTime')"
        align="center"
        prop="create_time"
        width="150"
        sortable="custom"
        column-key="create_time"
        :sort-orders="['descending', 'ascending']"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
          }}</span>
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
            v-hasPermi="['att:projectUserRel:edit']"
            >{{ td("common.button.update") }}</el-button
          >
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['att:projectUserRel:remove']"
            >{{ td("common.button.delete") }}</el-button
          >
        </template>
      </el-table-column>

      <template #empty>
        <div class="emptyBg">
          <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
          <p>{{ td("common.noData") }}</p>
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

  <!-- Add or modify project-user association dialog box -->
  <el-dialog
    :title="title"
    v-model="open"
    :append-to-body="false"
    class="warn-dialog-23012"
    width="700px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form
      ref="AttProjectUserRelRef"
      :model="form"
      :rules="rules"
      label-width="80px"
      @submit.prevent
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="24" v-if="form.id == null">
          <div class="hint-div">
            <el-icon color="#2A7BFD" size="16px">
              <InfoFilled />
            </el-icon>
            <span>
              {{ td("dpp.setting.projectUserRel.addUserHint")
              }}<a href="/system/user" style="color: #2a7bfd">{{
                td("dpp.setting.projectUserRel.userManagement")
              }}</a
              >{{ td("dpp.setting.projectUserRel.addUserHintEnd") }}
            </span>
          </div>
        </el-col>
        <el-col :span="24" v-if="form.id == null">
          <el-form-item
            :label="td('dpp.setting.projectUserRel.systemUser')"
            prop="userNameList"
           :label-position="labelPosition">
            <el-input
              style="width: 76%"
              v-model="form.userNameList"
              :placeholder="td('dpp.setting.projectUserRel.selectUser')"
              disabled
            >
            </el-input>
            <el-button
              style="margin-left: 12px"
              type="primary"
              @click="getListUser"
              >{{ td("dpp.setting.projectUserRel.selectUser") }}</el-button
            >
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.id != null">
          <el-form-item
            :label="td('dpp.setting.projectUserRel.systemUser')"
            prop="nickName"
           :label-position="labelPosition">
            <el-input
              v-model="form.nickName"
              :placeholder="td('dpp.setting.projectUserRel.selectUser')"
              disabled
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="td('dpp.setting.projectUserRel.userRole')"
            prop="roleIdList"
           :label-position="labelPosition">
            <el-checkbox-group
              v-model="form.roleIdList"
              class="checkbox-vertical"
            >
              <div
                v-for="item in roleList"
                :key="item.roleId"
                style="margin-bottom: 15px; height: 40px"
              >
                <el-checkbox :label="item.roleId">
                  {{ item.roleName }}
                </el-checkbox>
                <p
                  style="
                    display: flex;
                    align-items: center;
                    line-height: 1;
                    font-size: 12px;
                    color: #888;
                    margin-left: 23px;
                    margin-top: 10px;
                  "
                >
                  {{ item.remark }}
                </p>
              </div>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">{{
          td("common.button.cancel")
        }}</el-button>
        <el-button type="primary" size="mini" @click="submitForm">{{
          td("common.button.confirm")
        }}</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    :title="td('dpp.setting.projectUserRel.userSelect')"
    v-model="openTwo"
    width="1000px"
    class="user-select-tatble"
    draggable
  >
    <template>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ td("dpp.setting.projectUserRel.userSelect") }}
      </span>
    </template>
    <!--User data-->
    <el-form
      class="btn-style"
      :model="queryParamsUser"
      ref="queryRef"
      :inline="true"

     >
      <el-form-item
        :label="td('dpp.setting.projectUserRel.loginAccount')"
        prop="userName"
       >
        <el-input
          v-model="queryParamsUser.userName"
          :placeholder="td('dpp.setting.projectUserRel.inputLoginAccount')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQueryUser"
        />
      </el-form-item>
      <el-form-item
        :label="td('dpp.setting.projectUserRel.phoneNumber')"
        prop="phonenumber"
       >
        <el-input
          v-model="queryParamsUser.phonenumber"
          :placeholder="td('dpp.setting.projectUserRel.inputPhoneNumber')"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQueryUser"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          plain
          type="primary"
          @click="handleQueryUser"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i
          >{{ td("common.button.query") }}
        </el-button>
        <el-button
          @click="resetQueryUser"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22378 mr5"></i
          >{{ td("common.button.reset") }}
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="userTableRef"
      stripe
      v-loading="loadingUser"
      :data="userList"
      @selection-change="handleSelectionChangeUser"
    >
      <el-table-column type="selection" width="70" align="center" />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.userId')"
        width="80"
        align="center"
        key="userId"
        prop="userId"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.loginAccount')"
        align="center"
        key="userName"
        prop="userName"
        :show-overflow-tooltip="{ effect: 'light' }"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.userName')"
        align="center"
        key="nickName"
        prop="nickName"
        :show-overflow-tooltip="{ effect: 'light' }"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.dept')"
        width="180"
        align="center"
        key="deptName"
        prop="dept.deptName"
        :show-overflow-tooltip="{ effect: 'light' }"
      />
      <el-table-column
        :label="td('dpp.setting.projectUserRel.phoneNumber')"
        width="180"
        align="center"
        key="phonenumber"
        prop="phonenumber"
      />
      <el-table-column
        :label="td('common.texts.createdBy')"
        :show-overflow-tooltip="true"
        align="left"
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
        width="150"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
          }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="totalUser > 0"
      :total="totalUser"
      v-model:page="queryParamsUser.pageNum"
      v-model:limit="queryParamsUser.pageSize"
      @pagination="getListUser"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="openTwo = false">{{
          td("common.button.cancel")
        }}</el-button>
        <el-button type="primary" size="mini" @click="submitFormUser">{{
          td("common.button.confirm")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="AttProjectUserRel">
import { listUser } from "@/api/system/system/user.js";
import {
  listAttProjectUserRel,
  getAttProjectUserRel,
  delAttProjectUserRel,
  addAttProjectUserRel,
  updateAttProjectUserRel,
  editUserListAndRoleList,
  listRole,
  getRoleUser,
  addUserListAndRoleList,
} from "@/api/att/projectUserRel/attProjectUserRel";
import { getToken } from "@/utils/auth.js";
import useUserStore from "@/store/system/user";
import { addUserAndProject, noProjectUser } from "@/api/att/project/project.js";
import { ref } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);
const AttProjectUserRelList = ref([]);
const size = (ref < "default") | "large" | ("small" > "default");
const value1 = ref("");
const value2 = ref("");
const activeName = ref("first");
// Show hidden information
const columns = ref([
  { key: 0, label: td('dpp.setting.projectUserRel.id'), visible: true },
  { key: 2, label: td('dpp.setting.projectUserRel.userId'), visible: true },
  { key: 7, label: td('dpp.setting.projectUserRel.createTime'), visible: true },
]);
const userList = ref([]);
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // If the corresponding column configuration is not found, it will be displayed by default.
  if (!column) return true;
  // If the corresponding column configuration is found, the display is controlled based on the visible attribute.
  return column.visible;
};
const userStore = useUserStore();
const open = ref(false);
const openTwo = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const loadingUser = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const idsUser = ref([]);
const userName = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const totalUser = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const roleList = ref([]);
/*** User import parameters */
const upload = reactive({
  // Whether to display the pop-up layer (user import)
  open: false,
  // Popup layer title (user imported)
  title: "",
  // Whether to disable uploading
  isUploading: false,
  // Whether to update existing user data
  updateSupport: 0,
  // Set upload request headers
  headers: { Authorization: "Bearer " + getToken() },
  // Upload address
  url: import.meta.env.VITE_APP_BASE_API + "/att/AttProjectUserRel/importData",
});
const createTime = ref(null);
const data = reactive({
  form: {
    userIdList: [],
    userNameList: [],
    roleIdList: [],
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    userId: null,
    createTime: null,
    endTime: null,
    startTime: null,
  },
  queryParamsUser: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    userName: undefined,
    phoneNumber: undefined,
    status: undefined,
    deptId: undefined,
  },
  rules: {
    userNameList: [
      {
        required: true,
        message: td("dpp.setting.projectUserRel.userRequired", "请选择用户"),
        trigger: "change",
      },
    ],
    roleIdList: [
      {
        required: true,
        message: td("dpp.setting.projectUserRel.roleRequired", "请选择角色"),
        trigger: "change",
      },
    ],
  },
});

const { queryParams, queryParamsUser, form, rules } = toRefs(data);
let addUserAdnProject = ref(false);
// Monitor projectId changes in userStore
watch(
  () => userStore.projectId,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      console.log(userStore.projectCode, "userStore.projectCode");

      queryParams.value.projectId = newValue;
      queryParamsUser.value.projectId = newValue;
      getList();
    }
  },
  { immediate: true }
);
function handleDateChange(value) {
  queryParams.value.startTime = value[0];
  queryParams.value.endTime = value[1];
}
/** Query the list of relationships between projects and users */
function getList() {
  loading.value = true;
  if (queryParams.value.projectId) {
    listAttProjectUserRel(queryParams.value).then((response) => {
      AttProjectUserRelList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
    addUserAndProject(queryParams.value.projectId).then((response) => {
      addUserAdnProject.value = response.data;
    });
  }
}

function getListUser() {
  loadingUser.value = true;
  noProjectUser(queryParamsUser.value).then((response) => {
    userList.value = response.rows;
    openTwo.value = true;
    totalUser.value = response.total;
    loadingUser.value = false;
    console.log(userList.value, "userList");

    // After the table is loaded, set the previously selected user
    nextTick(() => {
      userList.value.forEach((user) => {
        if (form.value.userIdList.includes(user.userId)) {
          proxy.$refs.userTableRef.toggleRowSelection(user, true);
        }
      });
    });
  });
}
/** Search button action */
function handleQueryUser() {
  queryParamsUser.value.pageNum = 1;
  getListUser();
}
/** reset button action */
function resetQueryUser() {
  queryParamsUser.value = {
    pageNum: 1,
    pageSize: 10,
    projectId: userStore.projectId,
    userName: undefined,
    phoneNumber: undefined,
    status: undefined,
    deptId: undefined,
  };
  handleQueryUser();
}
/** Submit button action */
function submitFormUser() {
  form.value.userIdList = idsUser.value;
  form.value.userNameList = userName.value;
  openTwo.value = false;
}
// Multiple selection box selected data
function handleSelectionChangeUser(selection) {
  idsUser.value = selection.map((item) => item.userId);
  userName.value = selection.map((item) => item.nickName);
}
function getRoleList() {
  if (queryParams.value.projectId) {
    listRole(queryParams.value).then((response) => {
      roleList.value = response.rows;
      console.log(roleList.value, "roleList");
    });
  }
}
// Cancel button
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// form reset
function reset() {
  form.value = {
    id: null,
    projectId: null,
    userId: null,
    userIdList: [],
    userName: null,
    userNameList: [],
    roleIdList: [],
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
  proxy.resetForm("AttProjectUserRelRef");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** reset button action */
function resetQuery() {
  createTime.value = null;
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    projectId: userStore.projectId,
    userId: null,
    createTime: null,
    endTime: null,
    startTime: null,
  };
  // proxy.resetForm('queryRef');
  handleQuery();
}

// Multiple selection box selected data
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Sorting trigger events */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** Add button operation */
function handleAdd() {
  getRoleList();
  reset();
  open.value = true;
  title.value = td("dpp.setting.projectUserRel.addMember", "新增项目成员");
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getRoleList();
  getRoleUser(_id).then((response) => {
    form.value = response.data;
    console.log(form.value, "form");
    open.value = true;
    title.value = td("dpp.setting.projectUserRel.editMember", "修改项目成员");
  });
}

/** Detail button operation */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttProjectUserRel(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td(
      "dpp.setting.projectUserRel.memberDetail",
      "项目与用户关联关系详情"
    );
  });
}

/** submit button */
function submitForm() {
  proxy.$refs["AttProjectUserRelRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        proxy.$modal
          .confirm('修改角色后，该成员可能无法继续维护部分任务，请确认。')
          .then(() => editUserListAndRoleList(form.value))
          .then(() => {
              proxy.$modal.msgSuccess(td("common.message.editSuccess"));
              open.value = false;
              getList();
          })
          .catch(() => {});
      } else {
        // Add additional verification when adding
        if (!form.value.userIdList || form.value.userIdList.length === 0) {
          proxy.$modal.msgWarning(
            td(
              "dpp.setting.projectUserRel.noUserSelected",
              "未选择用户，请选择用户后重试"
            )
          );
          return;
        }
        form.value.projectId = userStore.projectId;
        addUserListAndRoleList(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.addSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const selectedRows = row.id
    ? [row]
    : AttProjectUserRelList.value.filter((item) => ids.value.includes(item.id));
  const selectedUserIds = selectedRows.map((item) => item.userId);
  const removingSelf = selectedUserIds.includes(userStore.id);
  const confirmText = removingSelf
    ? '确认移除自己的项目权限吗？操作后可能无法继续管理项目。'
    : selectedRows.length > 1
      ? `本次将移除${selectedRows.length}名成员，请确认。`
      : td(
          "dpp.setting.projectUserRel.confirmDelete",
          '是否确认移除编号为"{id}"的数据项？'
        ).replace("{id}", selectedUserIds[0]);
  proxy.$modal
    .confirm(confirmText)
    .then(function () {
      return delAttProjectUserRel(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess"));
    })
    .catch(() => {});
}

/** Export button action */
function handleExport() {
  proxy.download(
    "att/AttProjectUserRel/export",
    {
      ...queryParams.value,
    },
    `AttProjectUserRel_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- Import related operations ------------------**/
/** Import button actions */
function handleImport() {
  upload.title = td('dpp.setting.projectUserRel.memberImport');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `AttProjectUserRel_template_${new Date().getTime()}.xlsx`
  );
}

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**File upload is being processed */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** File upload successfully processed */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alertd(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('dpp.setting.projectUserRel.importResult'),
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
</script>
<style lang="scss" scoped>
.hint-div {
  margin: 0px 0px 20px 12px;
  /* border-top: 1px solid rgba(204, 204, 204, 0.5); */
  border-right: 1px solid rgba(204, 204, 204, 0.5);
  border-bottom: 1px solid #e5f1f8;
  border-left: 1px solid #e5f1f8;
  border-radius: 8px;
  background-color: #ecf5ff;
  padding: 10px;
  box-shadow: -1px 1px 2px #e5f1f8;
  display: flex;
  align-items: center;

  span {
    margin-left: 5px;
  }
}
</style>
<style scoped lang="scss">
.app-container {
  .pagecont-bottom {
    min-height: calc(100vh - 240px);
  }
}
</style>
<style lang="scss">
.warn-dialog-23012 {
  .el-dialog__body {
    overflow: auto;
    height: 500px !important;
    padding: 20px 40px !important;
  }
}

.user-select-tatble {
  .el-dialog__body {
    height: 600px !important;
  }
}

.checkbox-vertical {
  display: flex;
  flex-direction: column;
  /* Vertical */
  margin-top: 8px;
}

.checkbox-vertical .el-checkbox {
  display: block;
  margin-bottom: 0px;
  height: 15px !important;
}
</style>
