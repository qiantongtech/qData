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
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item :label="t('common.texts.name')" prop="name">
          <el-input
            v-model="queryParams.name"
            :placeholder="t('common.form.namePlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="t('sys.cert.issuer')" prop="issuer">
          <el-input
            v-model="queryParams.issuer"
            :placeholder="t('sys.cert.issuerPlaceholder')"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="t('sys.cert.owner')" prop="possessor">
          <el-input
            v-model="queryParams.possessor"
            :placeholder="t('sys.cert.ownerPlaceholder')"
            class="el-form-input-width"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ t('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ t('common.button.reset') }}
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
          >{{ t('common.button.add') }}</el-button>
        </el-col>
      </el-row>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table  stripe height="60vh" v-loading="loading" :data="certList" @selection-change="handleSelectionChange">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column :label="t('common.texts.name')" align="center" prop="name"  :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.cert.subjectName')" align="center" prop="subjectName"  :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.cert.issuer')" align="center" prop="issuer"  :show-overflow-tooltip="true" />
        <el-table-column :label="t('sys.cert.owner')" align="center" prop="possessor" :show-overflow-tooltip="true"/>
        <el-table-column :label="t('sys.cert.validPeriod')" align="center" prop="validTime">
          <template #default="scope">
            {{ scope.row.validTime }} {{ t('sys.cert.year') }}
          </template>
        </el-table-column>
        <el-table-column :label="t('sys.cert.effectTime')" align="center" prop="createTime" :show-overflow-tooltip="true"/>
        <el-table-column :label="t('common.texts.remark')" align="center" prop="remark"  :show-overflow-tooltip="true" >
          <template #default="scope">
            <span>{{ scope.row.remark || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.operation')"  align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="download"
              @click="downloadFiles(scope.row)"
              v-hasPermi="['ca:cert:edit']"
            >{{ t('common.button.download') }}</el-button>
            <el-button
              link
              type="danger"
              style="color: red"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['ca:cert:remove']"
            >{{ t('common.button.delete') }}</el-button>
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

    <!-- 添加或修改证书对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="t('common.texts.name')" prop="name">
              <el-input v-model="form.name" :placeholder="t('common.form.namePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="t('sys.cert.issueSubject')" prop="issuer">
              <el-select v-model="form.subjectId" :placeholder="t('sys.cert.selectIssueSubject')" @change="subjectChange" :style="'width:100%'">
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
            <el-form-item :label="t('sys.cert.issuer')" prop="issuer">
              <el-input v-model="form.issuer" disabled :placeholder="t('sys.cert.issuerPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="t('sys.cert.owner')" prop="possessor">
              <el-input v-model="form.possessor" :placeholder="t('sys.cert.ownerPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="t('sys.cert.validPeriod')" prop="validTime">
              <el-input v-model="form.validTime" type="number" :max="30" :min="1" :placeholder="t('sys.cert.validPeriodPlaceholder')">
                <el-button slot="append">{{ t('sys.cert.year') }}</el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="t('common.texts.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :placeholder="t('sys.cert.inputContent')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ t('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ t('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listCert, getCert, delCert, addCert, updateCert } from "@/api/system/ca/cert.js";
import {listSubject} from "@/api/system/ca/subject.js";
import JSZip from "jszip";
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
export default {
  name: "Cert",
  setup() {
    const { t } = useI18n();
    return { t };
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 主体列表
      subjectList: [],
      // 证书表格数据
      certList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: t('common.texts.name'), trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: this.t('sys.cert.subjectIdRequired'), trigger: "change" }
        ],
        subjectName: [
          { required: true, message: this.t('sys.cert.subjectNameRequired'), trigger: "change" }
        ],
        issuer: [
          { required: true, message: this.t('sys.cert.issuerRequired'), trigger: "blur" }
        ],
        possessor: [
          { required: true, message: this.t('sys.cert.ownerRequired'), trigger: "blur" }
        ],
        validTime: [
          { required: true, message: this.t('sys.cert.validPeriodRequired'), trigger: "blur" },
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getSubjectList();
  },
  methods: {
    /** 查询证书列表 */
    getList() {
      this.loading = true;
      listCert(this.queryParams).then(response => {
        this.certList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询主题列表 */
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.t('sys.cert.addTitle');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCert(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.t('sys.cert.editTitle');
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCert(this.form).then(response => {
              this.$modal.msgSuccess(t('common.message.editSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addCert(this.form).then(response => {
              this.$modal.msgSuccess(t('common.message.addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.t('sys.cert.confirmDelete', { id: ids })).then(function() {
        return delCert(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(t('common.message.deleteSuccess'));
      }).catch(() => {});
    },
    async downloadFiles(row) {
      const zip = new JSZip();

      const files = [row.privateKey, row.certificate];

      for (let fileUrl of files) {
        const response = await fetch(fileUrl);
        const blob = await response.blob();

        // 自动获取文件名
        const fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        zip.file(fileName, blob);
      }

      zip.generateAsync({ type: 'blob' }).then(content => {
        saveAs(content, row.name + "_数字证书" + '.zip');
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ca/cert/export', {
        ...this.queryParams
      }, `cert_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
