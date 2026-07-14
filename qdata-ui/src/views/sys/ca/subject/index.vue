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
        <el-form-item :label="td('sys.subject.subjectName')" prop="name" :label-position="labelPosition">
          <el-input
            v-model="queryParams.name"
            :placeholder="td('sys.subject.subjectNamePlaceholder')"
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
            @click="handleAdd"
            v-hasPermi="['ca:subject:add']"
          >{{ td('common.button.add') }}</el-button>
        </el-col>
      </el-row>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="subjectList" @selection-change="handleSelectionChange">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column :label="td('sys.subject.subjectName')" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.subject.commonName')" align="center" prop="cn" :show-overflow-tooltip="true" />
        <el-table-column :label="td('sys.subject.dept')" align="center" prop="ou" />
        <el-table-column :label="td('sys.subject.orgName')" align="center" prop="o" />
        <el-table-column :label="td('sys.subject.cityName')" align="center" prop="l" />
        <el-table-column :label="td('sys.subject.provinceName')" align="center" prop="st" />
        <el-table-column :label="td('sys.subject.country')" align="center" prop="c" />
  <!--      <el-table-column :label="td('common.texts.remark')" align="center" prop="remark" />-->
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width"  fixed="right" width="240">
          <template #default="scope">
            <el-button
                link
                type="primary"
                icon="download"
                @click="downloadFiles(scope.row)"
                v-hasPermi="['ca:subject:remove']"
            >{{ td('common.button.download') }}</el-button>
  <!--          <el-button-->
  <!--            size="mini"-->
  <!--            type="text"-->
  <!--            icon="el-icon-edit"-->
  <!--            @click="handleUpdate(scope.row)"-->
  <!--            v-hasPermi="['ca:subject:edit']"-->
  <!--          >{{ td('common.button.update') }}</el-button>-->
            <el-button
                link
                type="primary"
                style="color: red"
                icon="Delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['ca:subject:remove']"
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

    <!-- Add or modify the subject management dialogue -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"   draggable destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.subjectName')" prop="name">
              <el-input v-model="form.name" :placeholder="td('sys.subject.subjectNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.commonName')" prop="cn" :label-position="labelPosition">
              <el-input v-model="form.cn" :placeholder="td('sys.subject.commonNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.deptName')" prop="ou" :label-position="labelPosition">
              <el-input v-model="form.ou" :placeholder="td('sys.subject.deptNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.orgName')" prop="o" :label-position="labelPosition">
              <el-input v-model="form.o" :placeholder="td('sys.subject.orgNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.cityName')" prop="l" :label-position="labelPosition">
              <el-input v-model="form.l" :placeholder="td('sys.subject.cityNamePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.province')" prop="st" :label-position="labelPosition">
              <el-input v-model="form.st" :placeholder="td('sys.subject.provincePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('sys.subject.countryName')" prop="c" :label-position="labelPosition">
              <el-input v-model="form.c" :placeholder="td('sys.subject.countryPlaceholder')" />
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
import { listSubject, getSubject, delSubject, addSubject, updateSubject } from "@/api/system/ca/subject.js";
import useDefaultLang from "@/composables/useDefaultLang";
import JSZip from 'jszip';
import {red} from "chalk";

const { td } = useDefaultLang();
export default {
  name: "Subject",
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
      // Master Management Table Data
      subjectList: [],
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
        cn: null,
        ou: null,
        o: null,
        l: null,
        st: null,
        c: null,
        certificate: null,
        privateKey: null,
        validFlag: null,
        creatorId: null,
      },
      // Form parameters
      form: {},
      // Form validation
      rules: {
        name: [
          { required: true, message: this.td('sys.subject.subjectNameRequired'), trigger: "blur" }
        ],
        cn: [
          { required: true, message: this.td('sys.subject.commonNameRequired'), trigger: "blur" }
        ],
        ou: [
          { required: true, message: this.td('sys.subject.orgUnitRequired'), trigger: "blur" }
        ],
        o: [
          { required: true, message: this.td('sys.subject.orgNameRequired'), trigger: "blur" }
        ],
        l: [
          { required: true, message: this.td('sys.subject.cityNameRequired'), trigger: "blur" }
        ],
        st: [
          { required: true, message: this.td('sys.subject.provinceNameRequired'), trigger: "blur" }
        ],
        c: [
          { required: true, message: this.td('sys.subject.countryRequired'), trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    red,
    /** Query Subject Management List */
    getList() {
      this.loading = true;
      listSubject(this.queryParams).then(response => {
        this.subjectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        saveAs(content, row.name + "_根证书" + '.zip');
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
        cn: null,
        ou: null,
        o: null,
        l: null,
        st: null,
        c: null,
        certificate: null,
        privateKey: null,
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
      this.title = this.td('sys.subject.addTitle');
    },
    /** Modify button operation */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSubject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.td('sys.subject.editTitle');
      });
    },
    /** Submit button */
    submitForm() {
      if (this.submitLoading) return;
      this.submitLoading = true;
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSubject(this.form).then(response => {
              this.$modal.msgSuccess(td('common.message.editSuccess'));
              this.open = false;
              this.getList();
              this.submitLoading = false;
            }).catch(() => {
              this.submitLoading = false;
            });
          } else {
            addSubject(this.form).then(response => {
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
      this.$modal.confirm(this.td('sys.subject.confirmDelete', { id: ids })).then(function() {
        return delSubject(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(td('common.message.deleteSuccess'));
      }).catch(() => {});
    },
    /** Export button operation */
    handleExport() {
      this.download('ca/subject/export', {
        ...this.queryParams
      }, `subject_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
