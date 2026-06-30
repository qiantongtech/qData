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
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <template #label>{{ td('sys.tool.genInfo.genTemplate') }}</template>
          <el-select v-model="info.tplCategory" @change="tplSelectChange">
            <el-option :label="td('sys.tool.genInfo.singleTable')" value="crud" />
            <el-option :label="td('sys.tool.genInfo.treeTable')" value="tree" />
            <el-option :label="td('sys.tool.genInfo.masterSubTable')" value="sub" />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="tplWebType">
          <template #label>{{ td('sys.tool.genInfo.frontendType') }}</template>
          <el-select v-model="info.tplWebType">
            <el-option :label="td('sys.tool.genInfo.vue2Template')" value="element-ui" />
            <el-option :label="td('sys.tool.genInfo.vue3Template')" value="element-plus" />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <template #label>
            {{ td('sys.tool.genInfo.genPackagePath') }}
            <el-tooltip :content="td('sys.tool.genInfo.genPackagePathTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-input v-model="info.packageName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <template #label>
            {{ td('sys.tool.genInfo.moduleName') }}
            <el-tooltip :content="td('sys.tool.genInfo.moduleNameTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-input v-model="info.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <template #label>
            {{ td('sys.tool.genInfo.genBusinessName') }}
            <el-tooltip :content="td('sys.tool.genInfo.genBusinessNameTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-input v-model="info.businessName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <template #label>
            {{ td('sys.tool.genInfo.genFunctionName') }}
            <el-tooltip :content="td('sys.tool.genInfo.genFunctionNameTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-input v-model="info.functionName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="genType">
          <template #label>
            {{ td('sys.tool.genInfo.genCodeMethod') }}
            <el-tooltip :content="td('sys.tool.genInfo.genCodeMethodTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-radio v-model="info.genType" value="0">{{ td('sys.tool.genInfo.zipPackage') }}</el-radio>
          <el-radio v-model="info.genType" value="1">{{ td('sys.tool.genInfo.customPath') }}</el-radio>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <template #label>
            {{ td('sys.tool.genInfo.parentMenu') }}
            <el-tooltip :content="td('sys.tool.genInfo.parentMenuTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <tree-select
           style="width:100%"
            v-model:value="info.parentMenuId"
            :options="menuOptions"
            :objMap="{ value: 'menuId', label: 'menuName', children: 'children' }"
            :placeholder="td('sys.tool.genInfo.selectSystemMenu')"
          />
        </el-form-item>
      </el-col>

      <el-col :span="24" v-if="info.genType == '1'">
        <el-form-item prop="genPath">
          <template #label>
            {{ td('sys.tool.genInfo.customPathLabel') }}
            <el-tooltip :content="td('sys.tool.genInfo.customPathTooltip')" placement="top">
              <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
            </el-tooltip>
          </template>
          <el-input v-model="info.genPath">
            <template #append>
              <el-dropdown>
                <el-button type="primary">
                  {{ td('sys.tool.genInfo.quickPathSelect') }}
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="info.genPath = '/'">{{ td('sys.tool.genInfo.restoreDefaultPath') }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <template v-if="info.tplCategory == 'tree'">
      <h4 class="form-header">{{ td('sys.tool.genInfo.otherInfo') }}</h4>
      <el-row v-show="info.tplCategory == 'tree'">
        <el-col :span="12">
          <el-form-item>
            <template #label>
              {{ td('sys.tool.genInfo.treeCode') }}
              <el-tooltip :content="td('sys.tool.genInfo.treeCodeTooltip')" placement="top">
                <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
              </el-tooltip>
            </template>
            <el-select v-model="info.treeCode" :placeholder="td('sys.tool.genInfo.selectPlaceholder')">
              <el-option
                v-for="(column, index) in info.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <template #label>
              {{ td('sys.tool.genInfo.treeParentCode') }}
              <el-tooltip :content="td('sys.tool.genInfo.treeParentCodeTooltip')" placement="top">
                <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
              </el-tooltip>
            </template>
            <el-select v-model="info.treeParentCode" :placeholder="td('sys.tool.genInfo.selectPlaceholder')">
              <el-option
                v-for="(column, index) in info.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <template #label>
              {{ td('sys.tool.genInfo.treeName') }}
              <el-tooltip :content="td('sys.tool.genInfo.treeNameTooltip')" placement="top">
                <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
              </el-tooltip>
            </template>
            <el-select v-model="info.treeName" :placeholder="td('sys.tool.genInfo.selectPlaceholder')">
              <el-option
                v-for="(column, index) in info.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </template>

    <template v-if="info.tplCategory == 'sub'">
      <h4 class="form-header">{{ td('sys.tool.genInfo.relationInfo') }}</h4>
      <el-row>
        <el-col :span="12">
          <el-form-item>
            <template #label>
              {{ td('sys.tool.genInfo.subTableName') }}
              <el-tooltip :content="td('sys.tool.genInfo.subTableNameTooltip')" placement="top">
                <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
              </el-tooltip>
            </template>
            <el-select v-model="info.subTableName" :placeholder="td('sys.tool.genInfo.selectPlaceholder')" @change="subSelectChange">
              <el-option
                v-for="(table, index) in tables"
                :key="index"
                :label="table.tableName + '：' + table.tableComment"
                :value="table.tableName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <template #label>
              {{ td('sys.tool.genInfo.subTableFkName') }}
              <el-tooltip :content="td('sys.tool.genInfo.subTableFkNameTooltip')" placement="top">
                <el-icon style="color: #909399;margin-top:9px"><InfoFilled /></el-icon>
              </el-tooltip>
            </template>
            <el-select v-model="info.subTableFkName" :placeholder="td('sys.tool.genInfo.selectPlaceholder')">
              <el-option
                v-for="(column, index) in subColumns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </template>

  </el-form>
</template>

<script setup>
import { listMenu } from "@/api/system/system/menu.js";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const subColumns = ref([]);
const menuOptions = ref([]);
const { proxy } = getCurrentInstance();

const props = defineProps({
  info: {
    type: Object,
    default: null
  },
  tables: {
    type: Array,
    default: null
  }
});

// 表单校验
const rules = ref({
  tplCategory: [{ required: true, message: td('sys.tool.genInfo.genTemplateRequired'), trigger: "blur" }],
  packageName: [{ required: true, message: td('sys.tool.genInfo.genPackagePathRequired'), trigger: "blur" }],
  moduleName: [{ required: true, message: td('sys.tool.genInfo.genModuleNameRequired'), trigger: "blur" }],
  businessName: [{ required: true, message: td('sys.tool.genInfo.genBusinessNameRequired'), trigger: "blur" }],
  functionName: [{ required: true, message: td('sys.tool.genInfo.genFunctionNameRequired'), trigger: "blur" }]
});

function subSelectChange(value) {
  props.info.subTableFkName = "";
}

function tplSelectChange(value) {
  if (value !== "sub") {
    props.info.subTableName = "";
    props.info.subTableFkName = "";
  }
}

function setSubTableColumns(value) {
  for (var item in props.tables) {
    const name = props.tables[item].tableName;
    if (value === name) {
      subColumns.value = props.tables[item].columns;
      break;
    }
  }
}

/** 查询菜单下拉树结构 */
function getMenuTreeselect() {
  listMenu().then(response => {
    menuOptions.value = proxy.handleTree(response.data, "menuId");
  });
}

watch(() => props.info.subTableName, val => {
  setSubTableColumns(val);
});

watch(() => props.info.tplWebType, val => {
  if (val === '') {
    props.info.tplWebType = "element-plus";
  }
});

getMenuTreeselect();
</script>
