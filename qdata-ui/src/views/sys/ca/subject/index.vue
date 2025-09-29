<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="主体名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入主体名称"
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
            @click="handleAdd"
            v-hasPermi="['ca:subject:add']"
          >新增</el-button>
        </el-col>
      </el-row>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="subjectList" @selection-change="handleSelectionChange">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="主体名称" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="通用名称" align="center" prop="cn" :show-overflow-tooltip="true" />
        <el-table-column label="组织部门" align="center" prop="ou" />
        <el-table-column label="组织名称" align="center" prop="o" />
        <el-table-column label="城市名称" align="center" prop="l" />
        <el-table-column label="省名称" align="center" prop="st" />
        <el-table-column label="国家" align="center" prop="c" />
  <!--      <el-table-column label="备注" align="center" prop="remark" />-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  fixed="right" width="240">
          <template #default="scope">
            <el-button
                link
                type="primary"
                icon="download"
                @click="downloadFiles(scope.row)"
                v-hasPermi="['ca:subject:remove']"
            >下载</el-button>
  <!--          <el-button-->
  <!--            size="mini"-->
  <!--            type="text"-->
  <!--            icon="el-icon-edit"-->
  <!--            @click="handleUpdate(scope.row)"-->
  <!--            v-hasPermi="['ca:subject:edit']"-->
  <!--          >修改</el-button>-->
            <el-button
                link
                type="primary"
                style="color: red"
                icon="Delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['ca:subject:remove']"
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

    <!-- 添加或修改主体管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"   draggable destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主体名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入主体名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通用名称" prop="cn">
              <el-input v-model="form.cn" placeholder="请输入通用名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="ou">
              <el-input v-model="form.ou" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织名称" prop="o">
              <el-input v-model="form.o" placeholder="请输入组织名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市名称" prop="l">
              <el-input v-model="form.l" placeholder="请输入城市名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="省份名称" prop="st">
              <el-input v-model="form.st" placeholder="请输入省名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家名称" prop="c">
              <el-input v-model="form.c" placeholder="请输入国家" />
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
import { listSubject, getSubject, delSubject, addSubject, updateSubject } from "@/api/system/ca/subject.js";
import JSZip from 'jszip';
import {red} from "chalk";

export default {
  name: "Subject",
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
      // 主体管理表格数据
      subjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "主体名称不能为空", trigger: "blur" }
        ],
        cn: [
          { required: true, message: "通用名称不能为空", trigger: "blur" }
        ],
        ou: [
          { required: true, message: "组织单位名称不能为空", trigger: "blur" }
        ],
        o: [
          { required: true, message: "组织名称不能为空", trigger: "blur" }
        ],
        l: [
          { required: true, message: "城市名称不能为空", trigger: "blur" }
        ],
        st: [
          { required: true, message: "省名称不能为空", trigger: "blur" }
        ],
        c: [
          { required: true, message: "国家不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    red,
    /** 查询主体管理列表 */
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

        // 自动获取文件名
        const fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        zip.file(fileName, blob);
      }

      zip.generateAsync({ type: 'blob' }).then(content => {
        saveAs(content, row.name + "_根证书" + '.zip');
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
      this.title = "新增主体";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSubject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改主体管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSubject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSubject(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除主体管理编号为"' + ids + '"的数据项？').then(function() {
        return delSubject(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ca/subject/export', {
        ...this.queryParams
      }, `subject_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
