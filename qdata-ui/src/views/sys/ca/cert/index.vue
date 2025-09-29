<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="颁发者" prop="issuer">
          <el-input
            v-model="queryParams.issuer"
            placeholder="请输入颁发者"
            clearable
            class="el-form-input-width"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="所有者" prop="possessor">
          <el-input
            v-model="queryParams.possessor"
            placeholder="请输入所有者"
            class="el-form-input-width"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
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
          >新增</el-button>
        </el-col>
      </el-row>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table  stripe height="60vh" v-loading="loading" :data="certList" @selection-change="handleSelectionChange">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="名称" align="center" prop="name"  :show-overflow-tooltip="true" />
        <el-table-column label="主体名称" align="center" prop="subjectName"  :show-overflow-tooltip="true" />
        <el-table-column label="颁发者" align="center" prop="issuer"  :show-overflow-tooltip="true" />
        <el-table-column label="所有者" align="center" prop="possessor" :show-overflow-tooltip="true"/>
        <el-table-column label="有效期" align="center" prop="validTime">
          <template #default="scope">
            {{ scope.row.validTime }} 年
          </template>
        </el-table-column>
        <el-table-column label="生效时间" align="center" prop="createTime" :show-overflow-tooltip="true"/>
        <el-table-column label="备注" align="center" prop="remark"  :show-overflow-tooltip="true" >
          <template #default="scope">
            <span>{{ scope.row.remark || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作"  align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="download"
              @click="downloadFiles(scope.row)"
              v-hasPermi="['ca:cert:edit']"
            >下载</el-button>
            <el-button
              link
              type="danger"
              style="color: red"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['ca:cert:remove']"
            >删除</el-button>
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
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颁发主体" prop="issuer">
              <el-select v-model="form.subjectId" placeholder="请选择颁发主体" @change="subjectChange" :style="'width:100%'">
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
            <el-form-item label="颁发者" prop="issuer">
              <el-input v-model="form.issuer" disabled placeholder="请输入颁发者" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所有者" prop="possessor">
              <el-input v-model="form.possessor" placeholder="请输入所有者" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期" prop="validTime">
              <el-input v-model="form.validTime" type="number" :max="30" :min="1" placeholder="请输入有效期">
                <el-button slot="append">年</el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listCert, getCert, delCert, addCert, updateCert } from "@/api/system/ca/cert.js";
import {listSubject} from "@/api/system/ca/subject.js";
import JSZip from "jszip";

export default {
  name: "Cert",
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
          { required: true, message: "名称", trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: "主体id不能为空", trigger: "change" }
        ],
        subjectName: [
          { required: true, message: "主体名称不能为空", trigger: "change" }
        ],
        issuer: [
          { required: true, message: "颁发者不能为空", trigger: "blur" }
        ],
        possessor: [
          { required: true, message: "所有者不能为空", trigger: "blur" }
        ],
        validTime: [
          { required: true, message: "有效期不能为空", trigger: "blur" },
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
      this.title = "新增证书";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCert(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改证书";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCert(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCert(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
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
      this.$modal.confirm('是否确认删除证书编号为"' + ids + '"的数据项？').then(function() {
        return delCert(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
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
