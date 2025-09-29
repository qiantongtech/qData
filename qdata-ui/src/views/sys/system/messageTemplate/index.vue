<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
        <el-form-item label="消息标题" prop="title">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.title"
              placeholder="请输入消息标题"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="消息类别" prop="category">
          <el-select v-model="queryParams.category" placeholder="请选择" class="el-form-input-width">
            <el-option
                v-for="dict in message_category"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="消息等级" prop="msgLevel">
          <el-select v-model="queryParams.msgLevel" placeholder="请选择" class="el-form-input-width">
            <el-option
                v-for="dict in message_level"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
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
        <el-row :gutter="15" class="justify-end btn-style">
          <el-col :span="1.5">
            <el-button
                type="primary"
                plain
                @click="handleAdd"
                v-hasPermi="['system:messageTemplate:add']"
                @mousedown="e => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="messageTemplateList" >
        <el-table-column label="模版ID" align="center" prop="id" />
        <el-table-column label="消息标题" align="center" prop="title">
          <template #default="scope">
            {{ scope.row.title || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="消息模板内容" align="center" prop="content" width="180"  :show-overflow-tooltip="true">
          <template #default="scope">
            {{ scope.row.content || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="消息类别" align="center" prop="category">
          <template #default="scope">
            <dict-tag :options="message_category" :value="scope.row.category" />
          </template>
        </el-table-column>
        <el-table-column label="消息等级" align="center" prop="msgLevel">
          <template #default="scope">
            <dict-tag :options="message_level" :value="scope.row.msgLevel" />
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:messageTemplate:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:messageTemplate:remove']">删除</el-button>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt="">
            <p>没有记录哦~</p>
          </div>
        </template>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改消息模板对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <!-- <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-popover placement="top-start" width="641px" trigger="hover">
            <div class="tips-content">
              <div>
                <el-icon size="20" style="color: #909399; font-size: 16px">
                  <InfoFilled />
                </el-icon>
                <span class="wxtstitle ml0">温馨提示!</span>
              </div>
              <div>
                <p>
                  xxxx
                </p>
              </div>
            </div>
            <template #reference>
              <el-icon size="20" style="color: #909399; font-size: 16px">
                <InfoFilled />
              </el-icon>
            </template>
          </el-popover>
        </span>
        <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
          <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
              <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
              </path>
            </svg></i>
        </button>
      </template> -->
      <el-form ref="messageTemplateRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="消息标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入消息标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="消息类别" prop="category">
              <el-select v-model="form.category" placeholder="请选择">
                <el-option
                    v-for="dict in message_category"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="消息类别" prop="category">
              <el-select v-model="form.category" placeholder="请选择">
                <el-option
                    v-for="dict in message_category"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="消息等级" prop="level">
              <el-select v-model="form.msgLevel" placeholder="请选择">
                <el-option
                    v-for="dict in message_level"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="消息模板" prop="content">
              <el-input v-model="form.content" type="textarea" placeholder="请输入内容" />
              <!--          <editor v-model="form.content" :min-height="192"/>-->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MessageTemplate">
import { listMessageTemplate, getMessageTemplate, delMessageTemplate, addMessageTemplate, updateMessageTemplate } from "@/api/system/system/message/messageTemplate";

const { proxy } = getCurrentInstance();
const { message_category, message_level } = proxy.useDict("message_category", "message_level");

const messageTemplateList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    category: null,
    msgLevel: null,
  },
  rules: {
    title: [
      { required: true, message: "消息标题不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "消息模板内容不能为空", trigger: "blur" }
    ],
    category: [
      { required: true, message: "消息类别不能为空", trigger: "blur" }
    ],
    msgLevel: [
      { required: true, message: "消息等级不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询消息模板列表 */
function getList() {
  loading.value = true;
  listMessageTemplate(queryParams.value).then(response => {
    messageTemplateList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    title: null,
    content: null,
    category: null,
    msgLevel: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("messageTemplateRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增消息模板";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getMessageTemplate(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改消息模板";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["messageTemplateRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateMessageTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addMessageTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除消息模板编号为"' + _ids + '"的数据项？').then(function() {
    return delMessageTemplate(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/messageTemplate/export', {
    ...queryParams.value
  }, `messageTemplate_${new Date().getTime()}.xlsx`)
}

getList();
</script>
