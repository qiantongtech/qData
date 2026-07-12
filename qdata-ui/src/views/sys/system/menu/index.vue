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
            <el-form-item :label="td('sys.system.menu.menuName')" prop="menuName" :label-position="labelPosition">
               <el-input
                  v-model="queryParams.menuName"
                  :placeholder="td('sys.system.menu.menuNamePlaceholder')"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
               />
            </el-form-item>
            <el-form-item :label="td('common.texts.status')" prop="status">
               <el-select v-model="queryParams.status" :placeholder="td('sys.system.menu.menuStatus')" clearable class="el-form-input-width">
                  <el-option
                     v-for="dict in sys_normal_disable"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item :label-position="labelPosition">
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
         <el-row :gutter="10" class="btn-style">
            <el-col :span="1.5">
               <el-button
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['system:menu:add']"
               >{{ td('common.button.add') }}</el-button>
            </el-col>
            <el-col :span="1.5">
               <el-button
                  type="info"
                  plain
                  icon="Sort"
                  @click="toggleExpandAll"
               >{{ td('common.button.un_fold') }}</el-button>
            </el-col>
         </el-row>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
         </div>

         <el-table
            v-if="refreshTable"
            height="60vh"
            v-loading="loading"
            :data="menuList"
            row-key="menuId"
            :default-expand-all="isExpandAll"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren'}"
         >
            <el-table-column prop="menuName" :label="td('sys.system.menu.menuName')" :show-overflow-tooltip="true" width="160"></el-table-column>
            <el-table-column prop="icon" :label="td('sys.system.menu.icon')" align="center" width="100">
               <template #default="scope">
                  <svg-icon :icon-class="scope.row.icon"/>
               </template>
            </el-table-column>
            <el-table-column prop="orderNum" :label="td('sys.system.menu.sort')" align="center" width="60"></el-table-column>
            <el-table-column prop="perms" :label="td('sys.system.menu.permission')" align="center" :show-overflow-tooltip="true">
               <template #default="scope">
                  <span>{{ scope.row.perms || "-" }}</span>
               </template>
            </el-table-column>
            <el-table-column prop="component" :label="td('sys.system.menu.componentPath')" align="center" :show-overflow-tooltip="true">
               <template #default="scope">
                  <span>{{ scope.row.component || "-" }}</span>
               </template>
            </el-table-column>
            <el-table-column prop="status" :label="td('common.texts.status')" width="80" align="center">
               <template #default="scope">
                  <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.createdTime')" align="center" width="160" prop="createTime">
               <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
               </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width"  fixed="right" width="240">
               <template #default="scope">
                  <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:menu:edit']">{{ td('common.button.update') }}</el-button>
                  <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['system:menu:add']">{{ td('common.button.add') }}</el-button>
                  <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:menu:remove']">{{ td('common.button.delete') }}</el-button>
               </template>
            </el-table-column>
         </el-table>
      </div>

      <!-- 添加或修改菜单对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
         <el-form ref="menuRef" :model="form" :rules="rules" label-width="100px" :label-position="labelPosition">
            <el-row :gutter="20">
               <el-col :span="24">
                  <el-form-item :label="td('sys.system.menu.parentMenu')" :label-position="labelPosition">
                     <el-tree-select
                        v-model="form.parentId"
                        :data="menuOptions"
                        :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                        value-key="menuId"
                        :placeholder="td('sys.system.menu.selectParentMenu')"
                        check-strictly
                     />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.menu.menuType')" prop="menuType" :label-position="labelPosition">
                     <el-radio-group v-model="form.menuType">
                        <el-radio value="M">{{ td('sys.system.menu.directory') }}</el-radio>
                        <el-radio value="C">{{ td('sys.system.menu.menu') }}</el-radio>
                        <el-radio value="F">{{ td('sys.system.menu.button') }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item :label="td('sys.system.menu.menuIcon')" prop="icon" :label-position="labelPosition">
                     <el-popover
                        placement="bottom-start"
                        :width="540"
                        trigger="click"
                     >
                        <template #reference>
                           <el-input v-model="form.icon" :placeholder="td('sys.system.menu.selectIcon')" @blur="showSelectIcon" readonly>
                              <template #prefix>
                                 <svg-icon
                                    v-if="form.icon"
                                    :icon-class="form.icon"
                                    class="el-input__icon"
                                    style="height: 32px;width: 16px;"
                                 />
                                 <el-icon v-else style="height: 32px;width: 16px;"><search /></el-icon>
                              </template>
                           </el-input>
                        </template>
                        <icon-select ref="iconSelectRef" @selected="selected" :active-icon="form.icon" />
                     </el-popover>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.menu.showSort')" prop="orderNum" :label-position="labelPosition">
                     <el-input-number style="width:100%" v-model="form.orderNum" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item :label="td('sys.system.menu.menuName')" prop="menuName">
                     <el-input v-model="form.menuName" :placeholder="td('sys.system.menu.menuNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item prop="routeName" :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.routeNameTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.routeName') }}
                        </span>
                     </template>
                     <el-input v-model="form.routeName" :placeholder="td('sys.system.menu.routeNamePlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item prop="path" :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.routePathTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.routePath') }}
                        </span>
                     </template>
                     <el-input v-model="form.path" :placeholder="td('sys.system.menu.routePathPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.isExternalLinkTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.isExternalLink') }}
                        </span>
                     </template>
                     <el-radio-group v-model="form.isFrame">
                        <el-radio value="0">{{ td('sys.system.menu.yes') }}</el-radio>
                        <el-radio value="1">{{ td('sys.system.menu.no') }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>

               <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item prop="component" :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.componentPathTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.componentPath') }}
                        </span>
                     </template>
                     <el-input v-model="form.component" :placeholder="td('sys.system.menu.componentPathPlaceholder')" />
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType != 'M'">
                  <el-form-item :label-position="labelPosition">
                     <el-input v-model="form.perms" :placeholder="td('sys.system.menu.permissionPlaceholder')" maxlength="100" />
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.permissionTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.permissionChar') }}
                        </span>
                     </template>
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item :label-position="labelPosition">
                     <el-input v-model="form.query" :placeholder="td('sys.system.menu.routeParamPlaceholder')" maxlength="255" />
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.routeParamTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.routeParam') }}
                        </span>
                     </template>
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType == 'C'">
                  <el-form-item :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.isCacheTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.isCache') }}
                        </span>
                     </template>
                     <el-radio-group v-model="form.isCache">
                        <el-radio value="0">{{ td('sys.system.menu.cache') }}</el-radio>
                        <el-radio value="1">{{ td('sys.system.menu.noCache') }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12" v-if="form.menuType != 'F'">
                  <el-form-item :label-position="labelPosition">
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.showStatusTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.showStatus') }}
                        </span>
                     </template>
                     <el-radio-group v-model="form.visible">
                        <el-radio
                           v-for="dict in sys_show_hide"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item>
                     <template #label>
                        <span>
                           <el-tooltip :content="td('sys.system.menu.menuStatusTooltip')" placement="top">
                              <el-icon style="color: #909399;"><InfoFilled /></el-icon>
                           </el-tooltip>
                           {{ td('sys.system.menu.menuStatus') }}
                        </span>
                     </template>
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in sys_normal_disable"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
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

<script setup name="Menu">
import useDefaultLang from "@/composables/useDefaultLang";
import { addMenu, delMenu, getMenu, listMenu, updateMenu } from "@/api/system/system/menu.js";
import SvgIcon from "@/components/SvgIcon/index.vue";
import IconSelect from "@/components/IconSelect/index.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const submitLoading = ref(false);
const { sys_show_hide, sys_normal_disable } = proxy.useDict("sys_show_hide", "sys_normal_disable");

const menuList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const menuOptions = ref([]);
const isExpandAll = ref(false);
const refreshTable = ref(true);
const iconSelectRef = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    menuName: undefined,
    visible: undefined
  },
  rules: {
    menuName: [{ required: true, message: td('sys.system.menu.menuNameRequired'), trigger: "blur" }],
    orderNum: [{ required: true, message: td('sys.system.menu.menuSortRequired'), trigger: "blur" }],
    path: [{ required: true, message: td('sys.system.menu.routePathRequired'), trigger: "blur" }]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询菜单列表 */
function getList() {
  loading.value = true;
  listMenu(queryParams.value).then(response => {
    menuList.value = proxy.handleTree(response.data, "menuId");
    loading.value = false;
  });
}

/** 查询菜单下拉树结构 */
function getTreeselect() {
  menuOptions.value = [];
  listMenu().then(response => {
    const menu = { menuId: 0, menuName: td('sys.system.menu.rootCategory'), children: [] };
    menu.children = proxy.handleTree(response.data, "menuId");
    menuOptions.value.push(menu);
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: "M",
    orderNum: 0,
    isFrame: "1",
    isCache: "0",
    visible: "0",
    status: "0"
  };
  proxy.resetForm("menuRef");
}

/** 展示下拉图标 */
function showSelectIcon() {
  iconSelectRef.value.reset();
}

/** 选择图标 */
function selected(name) {
  form.value.icon = name;
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = td('sys.system.menu.addTitle');
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
  await getTreeselect();
  getMenu(row.menuId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = td('sys.system.menu.editTitle');
  });
}

/** 提交按钮 */
function submitForm() {
  if (submitLoading.value) return;
  submitLoading.value = true;
  proxy.$refs["menuRef"].validate(valid => {
    if (valid) {
      if (form.value.menuId != undefined) {
        updateMenu(form.value).then(response => {
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
          open.value = false;
          getList();
          submitLoading.value = false;
        }).catch(() => {
          submitLoading.value = false;
        });
      } else {
        addMenu(form.value).then(response => {
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
  proxy.$modal.confirm(td('sys.system.menu.confirmDelete', { name: row.menuName })).then(function() {
    return delMenu(row.menuId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
  }).catch(() => {});
}

getList();
</script>
