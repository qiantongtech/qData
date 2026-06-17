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
          <img src="@/assets/system/images/no_data/noData.png" alt="" />
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

  <!-- 新增或修改项目与用户关联关系对话框 -->
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
    <!--用户数据-->
    <el-form
      class="btn-style"
      :model="queryParamsUser"
      ref="queryRef"
      :inline="true"
      
     :label-position="labelPosition">
      <el-form-item
        :label="td('dpp.setting.projectUserRel.loginAccount')"
        prop="userName"
       :label-position="labelPosition">
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
       :label-position="labelPosition">
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
// 列显隐信息
const columns = ref([
  { key: 0, label: td('dpp.setting.projectUserRel.id'), visible: true },
  { key: 2, label: td('dpp.setting.projectUserRel.userId'), visible: true },
  { key: 7, label: td('dpp.setting.projectUserRel.createTime'), visible: true },
]);
const userList = ref([]);
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
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
// 监听 userStore 中的 projectId 变化
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
/** 查询项目与用户关联关系列表 */
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

    // 在表格加载完成后，设置之前选中的用户
    nextTick(() => {
      userList.value.forEach((user) => {
        if (form.value.userIdList.includes(user.userId)) {
          proxy.$refs.userTableRef.toggleRowSelection(user, true);
        }
      });
    });
  });
}
/** 搜索按钮操作 */
function handleQueryUser() {
  queryParamsUser.value.pageNum = 1;
  getListUser();
}
/** 重置按钮操作 */
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
/** 提交按钮操作 */
function submitFormUser() {
  form.value.userIdList = idsUser.value;
  form.value.userNameList = userName.value;
  openTwo.value = false;
}
// 多选框选中数据
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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
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

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  getRoleList();
  reset();
  open.value = true;
  title.value = td("dpp.setting.projectUserRel.addMember", "新增项目成员");
}

/** 修改按钮操作 */
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

/** 详情按钮操作 */
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

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AttProjectUserRelRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        editUserListAndRoleList(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(td("common.message.editSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        // 新增时增加额外验证
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

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const _userId =
    row.userId ||
    AttProjectUserRelList.value
      .filter((item) => ids.value.includes(item.id))
      .map((item) => item.userId);
  proxy.$modal
    .confirm(
      td(
        "dpp.setting.projectUserRel.confirmDelete",
        '是否确认移除编号为"{id}"的数据项？'
      ).replace("{id}", _userId)
    )
    .then(function () {
      return delAttProjectUserRel(_ids);
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
    "att/AttProjectUserRel/export",
    {
      ...queryParams.value,
    },
    `AttProjectUserRel_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = td('dpp.setting.projectUserRel.memberImport');
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `AttProjectUserRel_template_${new Date().getTime()}.xlsx`
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
  /* 竖排 */
  margin-top: 8px;
}

.checkbox-vertical .el-checkbox {
  display: block;
  margin-bottom: 0px;
  height: 15px !important;
}
</style>
