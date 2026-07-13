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
    <el-container style="90%">
      <!-- Adjustable part on the left -->
      <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px' }" class="left-pane">
        <div class="left-tree">
          <div class="head-container">
            <el-input v-model="deptName" :placeholder="td('sys.system.user.deptNamePlaceholder')" clearable prefix-icon="Search"
              style="margin-bottom: 20px" />
          </div>
          <div class="head-container">
            <el-tree :data="deptOptions" :props="{ label: 'label', children: 'children' }"
              :filter-node-method="filterNode" ref="deptTreeRef" node-key="id" highlight-current default-expand-all
              @node-click="handleNodeClick">
              <template #default="{ node, data }">
                <span class="custom-tree-node">
                  <!-- first level -->
                  <el-icon class="iconimg colorxz" v-if="node.expanded && node.level === 1">
                    <FolderOpened />
                  </el-icon>
                  <el-icon class="iconimg colorxz" v-if="!node.expanded && node.level === 1">
                    <Folder />
                  </el-icon>
                  <!-- Level 2 -->
                  <el-icon class="iconimg colorxz" v-if="
                    node.expanded && node.childNodes.length && node.level == 2
                  ">
                    <FolderOpened />
                  </el-icon>
                  <el-icon class="iconimg colorxz" v-if="
                    !node.expanded &&
                    node.childNodes.length &&
                    node.level == 2
                  ">
                    <Folder />
                  </el-icon>
                  <!-- child -->
                  <el-icon class="zjiconimg colorwxz" v-show="!node.isCurrent && node.level == 3">
                    <Tickets />
                  </el-icon>
                  <el-icon class="zjiconimg colorxz" v-show="node.isCurrent && node.level == 3">
                    <Tickets />
                  </el-icon>

                  <span class="treelable" @click="getNode(node)">{{
                    node.label
                  }}</span>
                </span>
              </template>
            </el-tree>
          </div>
        </div>
      </el-aside>
      <!-- Drag strip -->
      <div class="resize-bar" @mousedown="startResize">
        <div class="resize-handle-sx">
          <span class="zjsx"></span>
          <el-icon v-if="leftWidth == 0" @click.stop="toggleCollapse" class="collapse-icon">
            <ArrowRight />
          </el-icon>
          <el-icon v-else class="collapse-icon" @click.stop="toggleCollapse">
            <ArrowLeft />
          </el-icon>
        </div>
      </div>
      <!-- right part -->
      <el-main>
        <!--User data-->
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true">
            <el-form-item :label="td('sys.system.user.userName')" prop="userName">
              <el-input v-model="queryParams.userName" :placeholder="td('sys.system.user.userNamePlaceholder')" clearable class="el-form-input-width"
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('sys.system.user.phone')" prop="phonenumber" :label-position="labelPosition">
              <el-input v-model="queryParams.phonenumber" :placeholder="td('sys.system.user.phonePlaceholder')" clearable class="el-form-input-width"
                @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status">
              <el-select v-model="queryParams.status" :placeholder="td('sys.system.user.userStatus')" clearable class="el-form-input-width">
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item :label="td('common.texts.createdTime')">
              <el-date-picker class="el-form-input-width" v-model="dateRange" value-format="YYYY-MM-DD" type="daterange"
                range-separator="-" :start-placeholder="td('common.form.startDatePlaceholder')" :end-placeholder="td('common.form.endDatePlaceholder')"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
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
                <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:user:add']">{{ td('common.button.add') }}
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="primary" plain icon="Edit" :disabled="single" @click="handleUpdate"
                  v-hasPermi="['system:user:edit']">{{ td('common.button.update') }}
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                  v-hasPermi="['system:user:remove']">{{ td('common.button.delete') }}
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['system:user:import']">{{ td('common.button.import') }}
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button type="warning" plain icon="Download" @click="handleExport"
                  v-hasPermi="['system:user:export']">{{ td('common.button.export') }}
                </el-button>
              </el-col>
            </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
          </div>

          <el-table stripe height="58vh" v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column :label="td('sys.system.user.userNo')" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
            <el-table-column :label="td('sys.system.user.userName')" align="center" key="userName" prop="userName" v-if="columns[1].visible"
              :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.system.user.userNickName')" align="center" key="nickName" prop="nickName" v-if="columns[2].visible"
              :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.system.user.dept')" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible"
              :show-overflow-tooltip="true" />
            <el-table-column :label="td('sys.system.user.phone')" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible"
              width="120" />
            <el-table-column :label="td('common.texts.status')" align="center" key="status" v-if="columns[5].visible">
              <template #default="scope">
                <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
                  @change="handleStatusChange(scope.row)"></el-switch>
              </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.createdTime')" align="center" prop="createTime" v-if="columns[6].visible" width="160">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
              <template #default="scope">
                <!-- <el-tooltip content="Modify" placement="top" v-if="scope.row.userId !== 1">
                                   <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']"></el-button>
                                </el-tooltip>
                                <el-tooltip content="Delete" placement="top" v-if="scope.row.userId !== 1">
                                   <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']"></el-button>
                                </el-tooltip>
                                <el-tooltip content="Reset password" placement="top" v-if="scope.row.userId !== 1">
                                   <el-button link type="primary" icon="Key" @click="handleResetPwd(scope.row)" v-hasPermi="['system:user:resetPwd']"></el-button>
                                </el-tooltip>
                                <el-tooltip content="Assign roles" placement="top" v-if="scope.row.userId !== 1">
                                   <el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row)" v-hasPermi="['system:user:edit']"></el-button>
                                </el-tooltip> -->
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:user:edit']" v-if="scope.row.userId !== 1">{{ td('common.button.update') }}
                </el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                  v-hasPermi="['system:user:remove']" v-if="scope.row.userId !== 1">{{ td('common.button.delete') }}
                </el-button>
                <el-popover placement="bottom" :width="150" trigger="click" v-if="scope.row.userId !== 1">
                  <template #reference>
                    <el-button link type="primary" icon="ArrowDown">{{ td('common.button.more') }}</el-button>
                  </template>
                  <div style="width: 90px" class="butgdlist">
                    <el-button style="padding-left: 14px" link type="primary" icon="Key"
                      @click="handleResetPwd(scope.row)" v-hasPermi="['system:user:resetPwd']">{{ td('sys.system.user.resetPassword') }}
                    </el-button>
                    <el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row)"
                      v-hasPermi="['system:user:edit']">
                      {{ td('sys.system.user.assignRole') }}
                    </el-button>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-main>
    </el-container>

    <!-- Add or modify user configuration dialog box -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-form :model="form" :rules="rules" ref="userRef" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.userNickNameLabel')" prop="nickName" :label-position="labelPosition">
              <el-input v-model="form.nickName" :placeholder="td('sys.system.user.userNickNamePlaceholder')" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.belongDept')" prop="deptId" :label-position="labelPosition">
              <el-tree-select v-model="form.deptId" :data="deptOptions"
                :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" :placeholder="td('sys.system.user.selectBelongDept')"
                check-strictly />
              <!--                     <treeselect v-model="form.deptId" :options="deptOptions" :flat="true" :show-count="true" placeholder="Please select the department you belong to"-->
              <!--                                  :noResultsText="{{td('common.noData')}}" :multiple="true"-->
              <!--                     />-->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.phone')" prop="phonenumber">
              <el-input v-model="form.phonenumber" :placeholder="td('sys.system.user.phonePlaceholder')" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.email')" prop="email" :label-position="labelPosition">
              <el-input v-model="form.email" :placeholder="td('sys.system.user.emailPlaceholder')" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" :label="td('sys.system.user.userNameLabel')" prop="userName" :label-position="labelPosition">
              <el-input v-model="form.userName" :placeholder="td('sys.system.user.userNameLabelPlaceholder')" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" :label="td('sys.system.user.userPassword')" prop="password" :label-position="labelPosition">
              <el-input v-model="form.password" :placeholder="td('sys.system.user.userPasswordPlaceholder')" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.userGender')" :label-position="labelPosition">
              <el-select v-model="form.sex" :placeholder="td('sys.system.user.selectPlaceholder')">
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" :label-position="labelPosition">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.post')" :label-position="labelPosition">
              <el-select v-model="form.postIds" multiple :placeholder="td('sys.system.user.selectPlaceholder')" class="selectlist">
                <el-option v-for="item in postOptions" :key="item.postId" :label="item.postName" :value="item.postId"
                  :disabled="item.status == 1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.system.user.role')" prop="roleIds" :label-position="labelPosition">
            <el-select v-model="form.roleIds" multiple :placeholder="td('sys.system.user.selectPlaceholder')" class="selectlist">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
                  :disabled="item.status == 1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.system.user.inputContent')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- User import dialog -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable
      destroy-on-close>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload">
          <upload-filled />
        </el-icon>
        <div class="el-upload__text" v-html="td('sys.system.user.dragOrClick')"></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />
              {{ td('sys.system.user.updateExistingData') }}
            </div>
            <span>{{ td('sys.system.user.importTip') }}</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">{{ td('sys.system.user.downloadTemplate') }}
            </el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="User">
import { getToken } from "@/utils/auth.js";
import Cookies from "js-cookie";
import {
  changeUserStatus,
  listUser,
  resetUserPwd,
  delUser,
  getUser,
  updateUser,
  addUser,
  deptTreeSelect,
} from "@/api/system/system/user.js";
import { computed } from "vue"
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);
import store from "@/store";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
const userId = ref(userStore.id);
// Computed property dynamically sets phonenumber rule
const phonenumberRules = computed(() => {
  const rules = [
    {
      pattern: /^1[3-9]\d{9}$/,
      message: td('sys.system.user.phoneRequired'),
      trigger: "blur",
    },
  ];
  if (userId.value != 1) {
    rules.unshift({
      required: true,
      message: td('sys.system.user.contactPhoneRequired'),
      trigger: "blur",
    });
  }

  return rules;
});

// Form validation rules
const rules = computed(() => ({
  userName: [
    { required: true, message: td('sys.system.user.userNameRequired'), trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: td('sys.system.user.userNameLengthRange'),
      trigger: "blur",
    },
  ],
  nickName: [{ required: true, message: td('sys.system.user.userNickNameRequired'), trigger: "blur" }],
  roleIds: [{ required: true, message: td('sys.system.user.roleRequired'), trigger: "change" }],
  deptId: [{ required: true, message: td('sys.system.user.deptRequired'), trigger: "change" }],
  password: [
    { required: true, message: td('sys.system.user.passwordRequired'), trigger: "blur" },
    {
      min: 8,
      max: 20,
      message: td('sys.system.user.passwordLengthRange'),
      trigger: "blur",
    },
    {
      pattern: /^[^<>"'|\\]+$/,
      message: td('sys.system.user.invalidChars'),
      trigger: "blur",
    },
    {
      validator: (rule, value, callback) => {
        const strengthRegex = {
          minLength: /^.{8,}$/,
          upperCase: /[A-Z]/,
          lowerCase: /[a-z]/,
          number: /\d/,
          specialChar: /[!@#$%^&*(),.?":{}|<>]/,
        };

        if (!strengthRegex.minLength.test(value)) {
          callback(new Error(td('sys.system.user.passwordMinLength')));
        } else if (!strengthRegex.upperCase.test(value)) {
          callback(new Error(td('sys.system.user.passwordUppercase')));
        } else if (!strengthRegex.lowerCase.test(value)) {
          callback(new Error(td('sys.system.user.passwordLowercase')));
        } else if (!strengthRegex.number.test(value)) {
          callback(new Error(td('sys.system.user.passwordDigit')));
        } else if (!strengthRegex.specialChar.test(value)) {
          callback(new Error(td('sys.system.user.passwordSpecial')));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  email: [
    {
      type: "email",
      message: td('sys.system.user.emailRequired'),
      trigger: ["blur", "change"],
    },
  ],
  phonenumber: phonenumberRules.value,
}));
const userList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const deptName = ref("");
const deptOptions = ref(undefined);
const initPassword = ref(undefined);
const postOptions = ref([]);
const roleOptions = ref([]);

const leftWidth = ref(300); // Initial left width
const isResizing = ref(false); // Determine whether dragging is in progress
let startX = 0; // Initial position when mouse is pressed

const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  // Use requestAnimationFrame to reduce redraw frequency
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};

const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => { });
  }
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
// Collapse and expand
const toggleCollapse = () => {
  if (leftWidth.value === 0) {
    leftWidth.value = 300;
  } else {
    leftWidth.value = 0;
  }
  emit("update:leftWidth", leftWidth.value);
};
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
  url: import.meta.env.VITE_APP_BASE_API + "/system/user/importData",
});
// Show hidden information
const columns = ref([
  { key: 0, label: td('sys.system.user.columnVisibility.userNo'), visible: true },
  { key: 1, label: td('sys.system.user.columnVisibility.userName'), visible: true },
  { key: 2, label: td('sys.system.user.columnVisibility.userName'), visible: true },
  { key: 3, label: td('sys.system.user.columnVisibility.dept'), visible: true },
  { key: 4, label: td('sys.system.user.columnVisibility.phone'), visible: true },
  { key: 5, label: td('common.display.status'), visible: true },
  { key: 6, label: td('common.display.createTime'), visible: true },
]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    phonenumber: undefined,
    status: undefined,
    deptId: undefined,
  },
});

const { queryParams, form } = toRefs(data);

/** Filter nodes by condition  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** Filter department tree by name */
watch(deptName, (val) => {
  proxy.$refs["deptTreeRef"].filter(val);
});

/** Query department drop-down tree structure */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    deptOptions.value = response.data;
  });
}

/** Query user list */
function getList() {
  loading.value = true;
  listUser(proxy.addDateRange(queryParams.value, dateRange.value)).then(
    (res) => {
      loading.value = false;
      userList.value = res.rows;
      total.value = res.total;
    }
  );
}

// // Function to customize rendering content
// const renderContent = (h, { node }) => {
//    console.log(node.level,node.label,"===========node.level")
//   // Determine the node type and select different icons
// //   const icon = node.level === 1 ? 'el-icon-folder' : 'el-icon-document';
// //   return (
// //     <span>
// //     <i class={icon}></i>
// //       {node.label}
// //     </span>
// //   );
// };

/** Node click event */
function handleNodeClick(data) {
  queryParams.value.deptId = data.id;
  handleQuery();
}

function getNode(node) {
  console.log(node, "============node");
}

/** Search button action */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** reset button action */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  queryParams.value.deptId = undefined;
  proxy.$refs.deptTreeRef.setCurrentKey(null);
  handleQuery();
}

/** Delete button action */
function handleDelete(row) {
  const userIds = row.userId || ids.value;
  proxy.$modal
    .confirm(td('sys.system.user.confirmDelete', { id: userIds }))
    .then(function () {
      return delUser(userIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
}

/** Export button action */
function handleExport() {
  proxy.download(
    "system/user/export",
    {
      ...queryParams.value,
    },
    `user_${new Date().getTime()}.xlsx`
  );
}

/** User status modification  */
function handleStatusChange(row) {
  let text = row.status === "0" ? td('sys.system.user.enable') : td('sys.system.user.disable');
  proxy.$modal
    .confirm(td('sys.system.user.confirmStatusChange', { text, name: row.userName }))
    .then(function () {
      return changeUserStatus(row.userId, row.status);
    })
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.updateSuccess'));
    })
    .catch(function () {
      row.status = row.status === "0" ? "1" : "0";
    });
}

/** More actions */
function handleCommand(command, row) {
  switch (command) {
    case "handleResetPwd":
      handleResetPwd(row);
      break;
    case "handleAuthRole":
      handleAuthRole(row);
      break;
    default:
      break;
  }
}

/** Jump to role assignment */
function handleAuthRole(row) {
  const userId = row.userId;
  router.push("/system/user-auth/role/" + userId);
}

/** Reset password button action */
function handleResetPwd(row) {
  proxy
    .$prompt(td('sys.system.user.resetPasswordPrompt', { name: row.userName }), td('common.message.prompt'), {
      confirmButtonText: td('common.button.confirm'),
      cancelButtonText: td('common.button.cancel'),
      closeOnClickModal: false,
      inputType: 'password',
      inputPattern: /^.{8,20}$/, // Password length requirement is between 8 and 20
      inputErrorMessage: td('sys.system.user.resetPasswordInputError'),
      inputValidator: (value) => {
        // Verify that the password contains illegal characters
        if (/<|>|"|'|\||\\/.test(value)) {
          return td('sys.system.user.invalidChars');
        }
        // Check password strength
        const strengthRegex = {
          upperCase: /[A-Z]/, // at least one capital letter
          lowerCase: /[a-z]/, // at least one lowercase letter
          number: /\d/, // at least one number
          specialChar: /[!@#$%^&*(),.?":{}|<>]/, // at least one special character
        };

        if (!strengthRegex.upperCase.test(value)) {
          return td('sys.system.user.passwordUppercase');
        }
        if (!strengthRegex.lowerCase.test(value)) {
          return td('sys.system.user.passwordLowercase');
        }
        if (!strengthRegex.number.test(value)) {
          return td('sys.system.user.passwordDigit');
        }
        if (!strengthRegex.specialChar.test(value)) {
          return td('sys.system.user.passwordSpecial');
        }
      },
    })
    .then(({ value }) => {
      resetUserPwd(row.userId, value).then((response) => {
        proxy.$modal.msgSuccess(td('sys.system.user.resetPasswordSuccess', { password: value }));
      });
    })
    .catch(() => {
      // Handling cancellation operations
    });
}

/** Select the number of items  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.userId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** Import button actions */
function handleImport() {
  upload.title = td('sys.system.user.userImport');
  upload.open = true;
}

/** Download template operation */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `user_template_${new Date().getTime()}.xlsx`
  );
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
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    "</div>",
    td('sys.system.user.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** Submit upload file */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/** Reset action form */
function reset() {
  form.value = {
    userId: undefined,
    deptId: undefined,
    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    email: undefined,
    sex: undefined,
    status: "0",
    remark: undefined,
    postIds: [],
    roleIds: [],
  };
  proxy.resetForm("userRef");
}

/** Cancel button */
function cancel() {
  open.value = false;
  reset();
}

/** Add button operation */
function handleAdd() {
  reset();
  getUser().then((response) => {
    postOptions.value = response.posts;
    roleOptions.value = response.roles;
    open.value = true;
    title.value = td('sys.system.user.addUser');
    form.value.password = initPassword.value;
  });
}

/** Modify button actions */
function handleUpdate(row) {
  reset();
  const userId = row.userId || ids.value;
  getUser(userId).then((response) => {
    form.value = response.data;
    postOptions.value = response.posts;
    roleOptions.value = response.roles;
    form.value.postIds = response.postIds;
    form.value.roleIds = response.roleIds;
    open.value = true;
    title.value = td('sys.system.user.editUser');
    form.password = "";
  });
}

/** submit button */
function submitForm() {
  proxy.$refs["userRef"].validate((valid) => {
    if (valid) {
      if (form.value.userId != undefined) {
        updateUser(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
        });
      } else {
        addUser(form.value).then((response) => {
          proxy.$modal.msgSuccess(td('common.message.addSuccess'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

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

.left-pane {
  background-color: #ffffff;
  overflow: hidden;
  transition: width 0s;
  /* Transition time can be adjusted as needed */
}

.app-container {
  margin: 13px 15px;

  .pagecont-bottom {
    flex: 1;
    min-height: calc(100vh - 250px);
    padding: 13px 15px;
    background-color: #ffffff;
    border-radius: 2px;
    box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
  }
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

.el-aside {
  padding: 2px 0px;
  margin-bottom: 0px;
  background-color: #f0f2f5;
}

.custom-tree-node {
  display: flex;
  align-items: center;
}

.treelable {
  margin-left: 5px;
}

.zjiconimg {
  font-size: 12px;
}

.colorxz {
  color: #358cf3;
}

.colorwxz {
  color: #afd1fa;
}

.iconimg {
  font-size: 15px;
}

//Upload attachment style adjustment
::v-deep {

  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}

.resize-bar {
  cursor: ew-resize;
  background-color: #f0f2f5;
  height: 86vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle-sx {
  width: 15px;
  text-align: center;
  position: relative;
  /* Must be added to locate collapse-icon */
}

.zjsx {
  display: none;
  width: 5px;
  height: 50px;
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
}

.collapse-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* true center */
  font-size: 28px;
  color: #aaa;
  cursor: pointer;
  z-index: 10;
  padding: 5px;
}
</style>
