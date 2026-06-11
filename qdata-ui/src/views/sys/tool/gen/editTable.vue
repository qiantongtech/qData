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
  <div class="dataBody">
    <el-card class="app-container">
      <el-tabs v-model="activeName">
        <el-tab-pane :label="td('sys.tool.genEdit.basicInfo')" name="basic">
          <basic-info-form ref="basicInfo" :info="info" />
        </el-tab-pane>
        <el-tab-pane :label="td('sys.tool.genEdit.fieldInfo')" name="columnInfo">
          <el-table stripe ref="dragTable" height="650px" :data="columns" row-key="columnId" :max-height="tableHeight">
            <el-table-column :label="td('sys.tool.genEdit.index')" type="index" width="80"/>
            <el-table-column
                    :label="td('sys.tool.genEdit.fieldColumnName')"
                    prop="columnName"
                    width="150"
                    :show-overflow-tooltip="true"
            />
            <el-table-column :label="td('sys.tool.genEdit.fieldDesc')" min-width="150" align="center">
              <template #default="scope">
                <el-input v-model="scope.row.columnComment"></el-input>
              </template>
            </el-table-column>
            <el-table-column
                    :label="td('sys.tool.genEdit.physicalType')"
                    prop="columnType"
                    width="150"
                    :show-overflow-tooltip="true"
                    align="center"
            />
            <el-table-column :label="td('sys.tool.genEdit.javaType')" width="150" align="center">
              <template #default="scope">
                <el-select v-model="scope.row.javaType">
                  <el-option label="Long" value="Long" />
                  <el-option label="String" value="String" />
                  <el-option label="Integer" value="Integer" />
                  <el-option label="Double" value="Double" />
                  <el-option label="BigDecimal" value="BigDecimal" />
                  <el-option label="Date" value="Date" />
                  <el-option label="Boolean" value="Boolean" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.javaProperty')" width="150" align="center">
              <template #default="scope">
                <el-input v-model="scope.row.javaField"></el-input>
              </template>
            </el-table-column>

            <!--          <el-table-column label="插入" width="60" align="center">
                        <template #default="scope">
                          <el-checkbox true-label="1" false-label="0" v-model="scope.row.isInsert"></el-checkbox>
                        </template>
                      </el-table-column>-->
            <el-table-column :label="td('sys.tool.genEdit.addEdit')" width="120" align="center">
              <template #default="scope">
                <el-checkbox true-label="1" false-label="0" v-model="scope.row.isEdit"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.list')" width="120" align="center">
              <template #default="scope">
                <el-checkbox true-label="1" false-label="0" v-model="scope.row.isList"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.query')" width="120" align="center">
              <template #default="scope">
                <el-checkbox true-label="1" false-label="0" v-model="scope.row.isQuery"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.queryMethod')" width="150" align="center">
              <template #default="scope">
                <el-select v-model="scope.row.queryType">
                  <el-option label="=" value="EQ" />
                  <el-option label="!=" value="NE" />
                  <el-option label=">" value="GT" />
                  <el-option label=">=" value="GTE" />
                  <el-option label="<" value="LT" />
                  <el-option label="<=" value="LTE" />
                  <el-option label="LIKE" value="LIKE" />
                  <el-option label="BETWEEN" value="BETWEEN" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.required')" width="60" align="center">
              <template #default="scope">
                <el-checkbox true-label="1" false-label="0" v-model="scope.row.isRequired"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.displayType')" min-width="150" align="center">
              <template #default="scope">
                <el-select v-model="scope.row.htmlType">
                  <el-option :label="td('sys.tool.genEdit.textbox')" value="input" />
                  <el-option :label="td('sys.tool.genEdit.textarea')" value="textarea" />
                  <el-option :label="td('sys.tool.genEdit.dropdown')" value="select" />
                  <el-option :label="td('sys.tool.genEdit.radio')" value="radio" />
                  <el-option :label="td('sys.tool.genEdit.checkbox')" value="checkbox" />
                  <el-option :label="td('sys.tool.genEdit.datePicker')" value="datetime" />
                  <el-option :label="td('sys.tool.genEdit.imageUpload')" value="imageUpload" />
                  <el-option :label="td('sys.tool.genEdit.fileUpload')" value="fileUpload" />
                  <el-option :label="td('sys.tool.genEdit.richText')" value="editor" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column :label="td('sys.tool.genEdit.dictType')" width="150" align="center">
              <template #default="scope">
                <el-select v-model="scope.row.dictType" clearable filterable :placeholder="td('sys.tool.genEdit.selectPlaceholder')">
                  <el-option
                          v-for="dict in dictOptions"
                          :key="dict.dictType"
                          :label="dict.dictName"
                          :value="dict.dictType">
                    <span style="float: left">{{ dict.dictName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="td('sys.tool.genEdit.genInfo')" name="genInfo">
          <gen-info-form ref="genInfo" :info="info" :tables="tables" />
        </el-tab-pane>
      </el-tabs>
      <el-form label-width="100px">
        <div style="text-align: center;margin-left:-100px;margin-top:10px;">
          <el-button @click="close()">{{ td('common.button.back') }}</el-button>
          <el-button type="primary" @click="submitForm()">{{ td('sys.tool.genEdit.submit') }}</el-button>
        </div>
      </el-form>
    </el-card>
  </div>

</template>

<script setup name="GenEdit">
import { getGenTable, updateGenTable } from "@/api/system/tool/gen.js";
import { optionselect as getDictOptionselect } from "@/api/system/system/dict/type.js";
import basicInfoForm from "./basicInfoForm.vue";
import genInfoForm from "./genInfoForm.vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const route = useRoute();
const { proxy } = getCurrentInstance();

const activeName = ref("columnInfo");
const tableHeight = ref(document.documentElement.scrollHeight - 245 + "px");
const tables = ref([]);
const columns = ref([]);
const dictOptions = ref([]);
const info = ref({});

/** 提交按钮 */
function submitForm() {
  const basicForm = proxy.$refs.basicInfo.$refs.basicInfoForm;
  const genForm = proxy.$refs.genInfo.$refs.genInfoForm;
  Promise.all([basicForm, genForm].map(getFormPromise)).then(res => {
    const validateResult = res.every(item => !!item);
    if (validateResult) {
      const genTable = Object.assign({}, info.value);
      genTable.columns = columns.value;
      genTable.params = {
        treeCode: info.value.treeCode,
        treeName: info.value.treeName,
        treeParentCode: info.value.treeParentCode,
        parentMenuId: info.value.parentMenuId
      };
      updateGenTable(genTable).then(res => {
        proxy.$modal.msgSuccess(res.msg);
        if (res.code === 200) {
          close();
        }
      });
    } else {
      proxy.$modal.msgError(td('sys.tool.genEdit.formValidationFailed'));
    }
  });
}

function getFormPromise(form) {
  return new Promise(resolve => {
    form.validate(res => {
      resolve(res);
    });
  });
}

function close() {
  const obj = { path: "/tool/gen", query: { t: Date.now(), pageNum: route.query.pageNum } };
  proxy.$tab.closeOpenPage(obj);
}

(() => {
  const tableId = route.params && route.params.tableId;
  if (tableId) {
    // 获取表详细信息
    getGenTable(tableId).then(res => {
      columns.value = res.data.rows;
      info.value = res.data.info;
      tables.value = res.data.tables;
    });
    /** 查询字典下拉列表 */
    getDictOptionselect().then(response => {
      dictOptions.value = response.data;
    });
  }
})();
</script>
<style scoped>
/*.pagecont{*/
/*  background: #ffffff;*/
/*  padding: 15px;*/
/*}*/
.dataBody{
  min-height: calc(100vh - 115px);
}
.app-container {
  background-color: white !important;
  min-height: auto;
}
</style>
