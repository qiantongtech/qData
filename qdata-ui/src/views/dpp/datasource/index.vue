<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
  *
  数据源管理：列表、新增/编辑/详情、连接测试、启用/禁用。
  当前对接 Mock 接口，对接后端时请将下方 import 改为 @/api/dpp/datasource/datasource
-->

<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="75px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="类型" prop="datasourceType">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.datasourceType"
            placeholder="请选择类型"
            clearable
          >
            <el-option
              v-for="opt in datasourceTypeOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.status"
            placeholder="请选择状态"
            clearable
          >
            <el-option
              v-for="opt in statusOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.deptId"
            placeholder="请选择所属部门"
            clearable
          >
            <el-option label="数据研发部" value="1" />
            <el-option label="数据治理部" value="2" />
            <el-option label="平台部" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['dpp:datasource:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              :disabled="single"
              @click="handleUpdate(selectedRow)"
              v-hasPermi="['dpp:datasource:edit']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              :disabled="multiple"
              @click="handleDelete()"
              v-hasPermi="['dpp:datasource:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
        </div>
      </div>

      <el-table
        stripe
        height="60vh"
        v-loading="loading"
        :data="datasourceList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" width="80" />
        <el-table-column label="名称" align="center" prop="name" min-width="140" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.name || '-' }}</template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="description" min-width="160" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.description || '-' }}</template>
        </el-table-column>
        <el-table-column label="数据源类型" align="center" prop="datasourceType" width="120">
          <template #default="scope">{{ getDatasourceTypeLabel(scope.row.datasourceType) || '-' }}</template>
        </el-table-column>
        <el-table-column label="所属部门" align="center" prop="deptName" width="120" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.deptName || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'enabled' ? 'success' : 'info'">
              {{ scope.row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最近测试结果" align="center" prop="lastTestResult" width="110">
          <template #default="scope">
            <el-tag v-if="scope.row.lastTestResult === 'success'" type="success">成功</el-tag>
            <el-tag v-else-if="scope.row.lastTestResult === 'fail'" type="danger">失败</el-tag>
            <el-tag v-else type="info">未测试</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最近测试时间" align="center" prop="lastTestTime" width="160">
          <template #default="scope">
            {{ parseTime(scope.row.lastTestTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy" width="100">
          <template #default="scope">{{ scope.row.createBy || '-' }}</template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160" sortable="custom">
          <template #default="scope">
            {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="320">
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="handleTest(scope.row)"
              v-hasPermi="['dpp:datasource:edit']"
            >
              连接测试
            </el-button>
            <el-button
              link
              type="primary"
              @click="handleToggleStatus(scope.row)"
              v-hasPermi="['dpp:datasource:edit']"
            >
              {{ scope.row.status === 'enabled' ? '禁用' : '启用' }}
            </el-button>
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['dpp:datasource:edit']"
            >
              修改
            </el-button>
            <el-button
              link
              type="primary"
              icon="View"
              @click="handleDetail(scope.row)"
              v-hasPermi="['dpp:datasource:query']"
            >
              详情
            </el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['dpp:datasource:remove']"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt="" />
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">{{ title }}</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" @submit.prevent>
        <div class="form-section-title">基础信息</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="datasourceType">
              <el-select v-model="form.datasourceType" placeholder="请选择类型" style="width: 100%">
                <el-option
                  v-for="opt in datasourceTypeOptions"
                  :key="opt.value"
                  :label="opt.label"
                  :value="opt.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择所属部门" clearable style="width: 100%">
                <el-option label="数据研发部" value="1" />
                <el-option label="数据治理部" value="2" />
                <el-option label="平台部" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" />
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入描述"
                :rows="3"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">连接配置</div>
        <template v-if="isDbType">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="主机地址" prop="host">
                <el-input v-model="form.host" placeholder="请输入主机地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口" prop="port">
                <el-input-number v-model="form.port" :min="1" :max="65535" controls-position="right" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="库/Schema" prop="dbOrSchema">
                <el-input v-model="form.dbOrSchema" placeholder="请输入库名或 Schema" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="form.password"
                  type="password"
                  :placeholder="form.id ? '留空则不修改' : '请输入密码'"
                  show-password
                  autocomplete="new-password"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <template v-else-if="form.datasourceType === 'API'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="Base URL" prop="baseUrl">
                <el-input v-model="form.baseUrl" placeholder="请输入 Base URL" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="认证方式" prop="authType">
                <el-input v-model="form.authType" placeholder="如 Token、AK/SK" />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <template v-else-if="form.datasourceType === 'fileSystem' || form.datasourceType === 'dataLake'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="根路径" prop="rootPath">
                <el-input v-model="form.rootPath" placeholder="请输入根路径" />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="default" @click="cancel">取 消</el-button>
          <el-button size="default" type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
          <el-button
            v-if="form.datasourceType && (isDbType || form.datasourceType === 'API')"
            size="default"
            :loading="testLoading"
            @click="handleTestForm"
          >
            连接测试
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="数据源详情" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <el-descriptions :column="2" border>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">名称</div></template>
          {{ detailData.name ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">数据源类型</div></template>
          {{ getDatasourceTypeLabel(detailData.datasourceType) ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">所属部门</div></template>
          {{ detailData.deptName ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">状态</div></template>
          {{ detailData.status === 'enabled' ? '启用' : '禁用' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content" :span="2">
          <template #label><div class="cell-item">描述</div></template>
          {{ detailData.description ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">最近测试结果</div></template>
          <span v-if="detailData.lastTestResult === 'success'">成功</span>
          <span v-else-if="detailData.lastTestResult === 'fail'">失败</span>
          <span v-else>未测试</span>
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">最近测试时间</div></template>
          {{ parseTime(detailData.lastTestTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">创建人</div></template>
          {{ detailData.createBy ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label-class-name="base-label" class-name="base-content">
          <template #label><div class="cell-item">创建时间</div></template>
          {{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="default" @click="openDetail = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Datasource">
import { getCurrentInstance, ref, reactive, computed, toRefs, onMounted } from 'vue'
import { parseTime } from '@/utils/anivia'
import {
  listDppDatasource,
  getDppDatasource,
  addDppDatasource,
  updateDppDatasource,
  delDppDatasource,
  testDppDatasource,
  testDppDatasourceForm,
  toggleDppDatasourceStatus
} from '@/api/dpp/datasource/datasource.mock'

const { proxy } = getCurrentInstance()

const datasourceList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const open = ref(false)
const openDetail = ref(false)
const title = ref('')
const submitLoading = ref(false)
const testLoading = ref(false)
const total = ref(0)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const selectedRow = ref(null)
const defaultSort = ref({ prop: 'createTime', order: 'descending' })

const datasourceTypeOptions = [
  { label: 'MySQL', value: 'MySQL' },
  { label: 'Oracle', value: 'Oracle' },
  { label: 'PostgreSQL', value: 'PostgreSQL' },
  { label: 'Doris', value: 'Doris' },
  { label: 'Hive', value: 'Hive' },
  { label: 'API', value: 'API' },
  { label: '文件系统', value: 'fileSystem' },
  { label: '数据湖', value: 'dataLake' }
]
const statusOptions = [
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' }
]

const dbTypes = ['MySQL', 'Oracle', 'PostgreSQL', 'Doris', 'Hive']
const isDbType = computed(() => form.datasourceType && dbTypes.includes(form.datasourceType))

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    datasourceType: null,
    status: null,
    deptId: null
  },
  rules: {
    name: [
      { required: true, message: '名称不能为空', trigger: 'blur' },
      { max: 100, message: '名称长度不能超过100个字符', trigger: 'blur' }
    ],
    datasourceType: [{ required: true, message: '请选择数据源类型', trigger: 'change' }],
    port: [
      { type: 'number', min: 1, max: 65535, message: '端口范围 1-65535', trigger: 'blur', transform: (v) => Number(v) }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)
const detailData = ref({})

function getDatasourceTypeLabel(val) {
  const opt = datasourceTypeOptions.find((o) => o.value === val)
  return opt ? opt.label : val
}

/** 查询列表 */
function getList() {
  loading.value = true
  listDppDatasource(queryParams.value)
    .then((response) => {
      datasourceList.value = response.data?.rows || response.data?.list || []
      total.value = response.data?.total ?? 0
    })
    .finally(() => {
      loading.value = false
    })
}

/** 搜索 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置 */
function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

/** 排序 */
function handleSortChange({ prop, order }) {
  queryParams.value.orderByColumn = prop
  queryParams.value.isAsc = order === 'ascending' ? 'asc' : 'desc'
  getList()
}

function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
  selectedRow.value = selection.length === 1 ? selection[0] : null
}

/** 新增 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '新增数据源'
}

/** 修改 */
function handleUpdate(row) {
  reset()
  const id = row?.id ?? ids.value?.[0]
  if (!id) return
  getDppDatasource(id).then((response) => {
    const data = response.data || response
    form.value = { ...data }
    if (form.value.password) form.value.password = ''
    open.value = true
    title.value = '修改数据源'
  })
}

/** 取消/关闭 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    name: null,
    description: null,
    datasourceType: null,
    deptId: null,
    deptName: null,
    status: 'enabled',
    host: null,
    port: 3306,
    dbOrSchema: null,
    username: null,
    password: null,
    authType: null,
    baseUrl: null,
    rootPath: null
  }
  proxy.resetForm('formRef')
}

/** 提交 */
function submitForm() {
  proxy.$refs['formRef'].validate((valid) => {
    if (!valid) return
    submitLoading.value = true
    const submitData = { ...form.value }
    if (submitData.password === '' || submitData.password === '********') delete submitData.password
    const api = submitData.id ? updateDppDatasource : addDppDatasource
    api(submitData)
      .then((response) => {
        proxy.$modal.msgSuccess(submitData.id ? '修改成功' : '新增成功')
        open.value = false
        getList()
      })
      .catch(() => {})
      .finally(() => {
        submitLoading.value = false
      })
  })
}

/** 详情 */
function handleDetail(row) {
  const id = row?.id ?? ids.value?.[0]
  if (!id) return
  getDppDatasource(id).then((response) => {
    detailData.value = response.data || response
    openDetail.value = true
  })
}

/** 删除 */
function handleDelete(row) {
  const _ids = row?.id ?? ids.value
  if (!_ids) return
  proxy.$modal
    .confirm('确认删除该数据源吗？删除后不可恢复。')
    .then(() => delDppDatasource(_ids))
    .then((res) => {
      if (res && res.code === 200) {
        proxy.$modal.msgSuccess('删除成功')
        getList()
      } else if (res && (res.msg || res.refInfo)) {
        proxy.$modal.msgError(res.msg || res.refInfo)
      }
    })
    .catch((err) => {
      if (err && err.message) proxy.$modal.msgError(err.message)
    })
}

/** 连接测试（列表） */
function handleTest(row) {
  testDppDatasource(row.id).then((res) => {
    const data = res.data || res
    if (data && data.success) {
      proxy.$modal.msgSuccess(data.message || '连接成功')
    } else {
      proxy.$modal.msgError(data?.message || res.msg || '连接失败')
    }
    getList()
  }).catch((err) => {
    proxy.$modal.msgError(err?.message || '连接失败')
    getList()
  })
}

/** 连接测试（表单内） */
function handleTestForm() {
  testLoading.value = true
  const payload = {
    datasourceType: form.value.datasourceType,
    host: form.value.host,
    port: form.value.port,
    dbOrSchema: form.value.dbOrSchema,
    username: form.value.username,
    password: form.value.password,
    baseUrl: form.value.baseUrl
  }
  testDppDatasourceForm(payload)
    .then((res) => {
      const data = res.data || res
      if (data && data.success !== false) {
        proxy.$modal.msgSuccess(data?.message || '连接成功')
      } else {
        proxy.$modal.msgError(data?.message || res.msg || '连接失败')
      }
    })
    .catch((err) => {
      proxy.$modal.msgError(err?.message || '连接失败')
    })
    .finally(() => {
      testLoading.value = false
    })
}

/** 启用/禁用 */
function handleToggleStatus(row) {
  const text = row.status === 'enabled' ? '禁用' : '启用'
  proxy.$modal
    .confirm(`确认要${text}数据源「${row.name}」吗？`)
    .then(() => {
      return toggleDppDatasourceStatus(row.id)
    })
    .then((res) => {
      proxy.$modal.msgSuccess(res.msg || `${text}成功`)
      getList()
    })
    .catch(() => {})
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.form-section-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}
:deep(.base-label) {
  width: 120px;
  .cell-item {
    font-weight: 500;
  }
}
</style>
