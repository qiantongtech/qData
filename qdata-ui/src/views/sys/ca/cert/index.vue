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
      <el-form class="btn-style" :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item :label="td('common.texts.name')" prop="name" :label-position="labelPosition">
          <el-input
            v-model="queryParams.name"
            :placeholder="td('common.form.namePlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('sys.cert.issuer')" prop="issuer" :label-position="labelPosition">
          <el-input
            v-model="queryParams.issuer"
            :placeholder="td('sys.cert.issuerPlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('sys.cert.owner')" prop="possessor" :label-position="labelPosition">
          <el-input
            v-model="queryParams.possessor"
            :placeholder="td('sys.cert.ownerPlaceholder')"
            class="el-form-input-width"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
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
            icon="plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['ca:cert:add']"
          >{{ td('common.button.add') }}</el-button>
        </el-col>
      </el-row>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table  stripe height="60vh" v-loading="loading" :data="certList" @selection-change="handleSelectionChange">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column :label="td('common.texts.name')" align="center" prop="name"  :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.cert.subjectName')" align="center" prop="subjectName"  :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.cert.issuer')" align="center" prop="issuer"  :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.cert.owner')" align="center" prop="possessor" :show-overflow-tooltip="true"/>
        <el-table-column :label="td('sys.cert.validPeriod')" align="center" prop="validTime">
          <template #default="scope">
            {{ scope.row.validTime }} {{ td('sys.cert.year') }}
          </template>
        </el-table-column>
        <el-table-column :label="td('sys.cert.effectTime')" align="center" prop="createTime" :show-overflow-tooltip="true"/>
        <el-table-column :label="td('common.texts.remark')" align="center" prop="remark"  :show-overflow-tooltip="true" >
          <template #default="scope">
            <span>{{ scope.row.remark || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')"  align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="download"
              @click="downloadFiles(scope.row)"
              v-hasPermi="['ca:cert:edit']"
            >{{ td('common.button.download') }}</el-button>
            <el-button
              link
              type="danger"
              style="color: red"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['ca:cert:remove']"
            >{{ td('common.button.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- Add or modify the certificate dialogue -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.name')" prop="name">
              <el-input v-model="form.name" :placeholder="td('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.cert.issueSubject')" prop="issuer" :label-position="labelPosition">
              <el-select v-model="form.subjectId" :placeholder="td('sys.cert.selectIssueSubject')" @change="subjectChange" :style="'width:100%'">
                <el-option
                  v-for="item in subjectList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.cert.issuer')" prop="issuer">
              <el-input v-model="form.issuer" disabled :placeholder="td('sys.cert.issuerPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.cert.owner')" prop="possessor">
              <el-input v-model="form.possessor" :placeholder="td('sys.cert.ownerPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.cert.validPeriod')" prop="validTime" :label-position="labelPosition">
              <el-input v-model="form.validTime" type="number" :max="30" :min="1" :placeholder="td('sys.cert.validPeriodPlaceholder')">
                <el-button slot="append">{{ td('sys.cert.year') }}</el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <el-input v-model="form.remark" type="textarea" :placeholder="td('sys.cert.inputContent')" />
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

<script>
import { listCert, getCert, delCert, addCert, updateCert } from "@/api/system/ca/cert.js";
import useDefaultLang from "@/composables/useDefaultLang";
import {listSubject} from "@/api/system/ca/subject.js";
import JSZip from "jszip";

const { td } = useDefaultLang();
export default {
  name: "Cert",
  setup() {
    return { t };
  },
  data() {
    return {
      // Mask
      loading: true,
      // Selected array
      ids: [],
      // Not Disable by Single
      single: true,
      // Not Disable
      multiple: true,
      // Show search conditions
      showSearch: true,
      // Total number of articles
      total: 0,
      // Body List
      subjectList: [],
      // Certificate Form Data
      certList: [],
      // Popup Layer Title
      title: "",
      // Whether to show the eject layer
      open: false,
      // Submit buttons to avoid repeat clicks
      submitLoading: false,
      // Query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        subjectId: null,
        subjectName: null,
        certificate: null,
        privateKey: null,
        issuer: null,
        possessor: null,
        validTime: null,
        validFlag: null,
        creatorId: null,
      },
      // Form parameters
      form: {},
      // Form validation
      rules: {
        name: [
          { required: true, message: td('common.texts.name'), trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: this.td('sys.cert.subjectIdRequired'), trigger: "change" }
        ],
        subjectName: [
          { required: true, message: this.td('sys.cert.subjectNameRequired'), trigger: "change" }
        ],
        issuer: [
          { required: true, message: this.td('sys.cert.issuerRequired'), trigger: "blur" }
        ],
        possessor: [
          { required: true, message: this.td('sys.cert.ownerRequired'), trigger: "blur" }
        ],
        validTime: [
          { required: true, message: this.td('sys.cert.validPeriodRequired'), trigger: "blur" },
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getSubjectList();
  },
  methods: {
    /** Query Certificate List */
    getList() {
      this.loading = true;
      listCert(this.queryParams).then(response => {
        this.certList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** Query Theme List */
    getSubjectList() {
      listSubject({
        pageNum: 1,
        pageSize: 999999
      }).then(response => {
        this.subjectList = response.rows;
      });
    },
    subjectChange(e) {
      this.subjectList.forEach(item => {
        if (item.id === e) {
          this.form.subjectName = item.name;
          this.form.issuer = item.name;
        }
      });
    },
    // Cancel button
    cancel() {
      this.open = false;
      this.reset();
    },
    // Form Reset
    reset() {
      this.form = {
        id: null,
        name: null,
        subjectId: null,
        subjectName: null,
        certificate: null,
        privateKey: null,
        issuer: null,
        possessor: null,
        validTime: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** Search button operation */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** Reset button operations */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // Multiple box selected data
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** Add button operation */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.td('sys.cert.addTitle');
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCert(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.td('sys.cert.editTitle');
      });
    },
    /** Submit button */
    submitForm() {
      if (this.submitLoading) return;
      this.submitLoading = true;
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCert(this.form).then(response => {
              this.$modal.msgSuccess(td('common.message.editSuccess'));
              this.open = false;
              this.getList();
              this.submitLoading = false;
            }).catch(() => {
              this.submitLoading = false;
            });
          } else {
            addCert(this.form).then(response => {
              this.$modal.msgSuccess(td('common.message.addSuccess'));
              this.open = false;
              this.getList();
              this.submitLoading = false;
            }).catch(() => {
              this.submitLoading = false;
            });
          }
        } else {
          this.submitLoading = false;
        }
      });
    },
    /** Remove button operation */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.td('sys.cert.confirmDelete', { id: ids })).then(function() {
        return delCert(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(td('common.message.deleteSuccess'));
      }).catch(() => {});
    },
    async downloadFiles(row) {
      const zip = new JSZip();

      const files = [row.privateKey, row.certificate];

      for (let fileUrl of files) {
        const response = await fetch(fileUrl);
        const blob = await response.blob();

        // Automatically get filenames
        const fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        zip.file(fileName, blob);
      }

      zip.generateAsync({ type: 'blob' }).then(content => {
        saveAs(content, row.name + "_数字证书" + '.zip');
      });
    },
    /** Export button operation */
    handleExport() {
      this.download('ca/cert/export', {
        ...this.queryParams
      }, `cert_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
