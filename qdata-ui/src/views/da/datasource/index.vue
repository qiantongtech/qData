<template>
  <div class="app-container" ref="appContainer">
    <el-result v-if="noPermission" icon="warning" title="无权限访问" sub-title="当前账号暂无数据源管理权限，请联系管理员开通。" />

    <template v-else>
      <el-row :gutter="14">
        <el-col :xs="24" :sm="24" :md="6" :lg="5" :xl="5">
          <div class="tree-panel">
            <div class="tree-panel__header">分组视图</div>
            <el-input
              v-model="treeFilterText"
              placeholder="搜索分组"
              clearable
              class="mb10"
            >
              <template #prefix>
                <i class="iconfont-mini icon-a-zu22377"></i>
              </template>
            </el-input>
            <el-tree
              ref="treeRef"
              node-key="key"
              :data="treeData"
              :props="treeProps"
              :filter-node-method="filterTreeNode"
              :expand-on-click-node="false"
              default-expand-all
              @node-click="handleTreeNodeClick"
            />
          </div>
        </el-col>

        <el-col :xs="24" :sm="24" :md="18" :lg="19" :xl="19">
          <div class="pagecont-top" v-show="showSearch">
            <el-form
              ref="queryRef"
              :model="queryParams"
              :inline="true"
              class="btn-style"
              label-width="75px"
              @submit.prevent
            >
              <el-form-item label="关键字" prop="keyword">
                <el-input
                  v-model="queryParams.keyword"
                  placeholder="名称/描述"
                  clearable
                  class="el-form-input-width"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
              <el-form-item label="类型" prop="datasourceType">
                <el-select
                  v-model="queryParams.datasourceType"
                  placeholder="请选择"
                  clearable
                  class="el-form-input-width"
                >
                  <el-option v-for="opt in datasourceTypeOptions" :key="opt" :label="opt" :value="opt" />
                </el-select>
              </el-form-item>
              <el-form-item label="业务域" prop="bizDomain">
                <el-select
                  v-model="queryParams.bizDomain"
                  placeholder="请选择"
                  clearable
                  class="el-form-input-width"
                >
                  <el-option v-for="opt in bizDomainOptions" :key="opt" :label="opt" :value="opt" />
                </el-select>
              </el-form-item>
              <el-form-item label="环境" prop="env">
                <el-select v-model="queryParams.env" placeholder="请选择" clearable class="el-form-input-width">
                  <el-option label="生产" value="prod" />
                  <el-option label="非生产" value="non_prod" />
                </el-select>
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select
                  v-model="queryParams.status"
                  placeholder="请选择"
                  clearable
                  class="el-form-input-width"
                >
                  <el-option label="启用" value="enabled" />
                  <el-option label="禁用" value="disabled" />
                </el-select>
              </el-form-item>
              <el-form-item label="标签" prop="tag">
                <el-select v-model="queryParams.tag" placeholder="请选择" clearable class="el-form-input-width">
                  <el-option v-for="opt in tagOptions" :key="opt" :label="opt" :value="opt" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" plain @click="handleQuery" :loading="loading">
                  <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
                <el-button @click="resetQuery" :disabled="!hasActiveFilters">
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
                    v-hasPermi="['da:dataSource:add']"
                  >
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增数据源
                  </el-button>
                </el-col>
              </el-row>
              <div class="justify-end top-right-btn">
                <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
              </div>
            </div>

            <div v-if="listError" class="error-wrap">
              <el-result icon="error" title="列表加载失败" :sub-title="errorText || '系统繁忙，请稍后重试'">
                <template #extra>
                  <el-button type="primary" @click="getList">重试</el-button>
                </template>
              </el-result>
            </div>

            <template v-else>
              <el-table
                stripe
                height="60vh"
                v-loading="loading"
                :data="datasourceList"
                :default-sort="{ prop: 'createTime', order: 'descending' }"
                @sort-change="handleSortChange"
              >
                <el-table-column label="名称" prop="name" min-width="160" show-overflow-tooltip>
                  <template #default="scope">
                    <el-button link type="primary" @click="handleDetail(scope.row)">{{ scope.row.name }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column label="类型" prop="datasourceType" width="120" />
                <el-table-column label="业务域" prop="bizDomain" width="110" />
                <el-table-column label="环境" prop="env" width="95">
                  <template #default="scope">
                    {{ scope.row.env === 'prod' ? '生产' : '非生产' }}
                  </template>
                </el-table-column>
                <el-table-column label="责任人" prop="owner" width="110" show-overflow-tooltip />
                <el-table-column label="状态" prop="status" width="90">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === 'enabled' ? 'success' : 'info'">
                      {{ scope.row.status === 'enabled' ? '启用' : '禁用' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="被引用数量" prop="refCount" width="110" />
                <el-table-column label="最近测试" prop="lastTestResult" width="110">
                  <template #default="scope">
                    <el-tag v-if="scope.row.lastTestResult === 'success'" type="success">成功</el-tag>
                    <el-tag v-else-if="scope.row.lastTestResult === 'fail'" type="danger">失败</el-tag>
                    <el-tag v-else type="info">未测试</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="最近测试时间" prop="lastTestTime" width="160">
                  <template #default="scope">{{ parseTime(scope.row.lastTestTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</template>
                </el-table-column>
                <el-table-column label="创建时间" prop="createTime" width="160" sortable="custom">
                  <template #default="scope">{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="330" fixed="right">
                  <template #default="scope">
                    <el-button
                      link
                      type="primary"
                      :loading="!!testLoadingMap[scope.row.id]"
                      :disabled="!!testLoadingMap[scope.row.id]"
                      @click="handleRowTest(scope.row)"
                      v-hasPermi="['da:dataSource:edit']"
                    >
                      连接测试
                    </el-button>
                    <el-button
                      link
                      type="primary"
                      @click="handleToggleStatus(scope.row)"
                      v-hasPermi="['da:dataSource:edit']"
                    >
                      {{ scope.row.status === 'enabled' ? '禁用' : '启用' }}
                    </el-button>
                    <el-button link type="primary" @click="handleEdit(scope.row)" v-hasPermi="['da:dataSource:edit']">
                      编辑
                    </el-button>
                    <el-button link type="primary" @click="handleDetail(scope.row)" v-hasPermi="['da:dataSource:query']">
                      详情
                    </el-button>
                    <el-button link type="danger" @click="handleDelete(scope.row)" v-hasPermi="['da:dataSource:remove']">
                      删除
                    </el-button>
                  </template>
                </el-table-column>

                <template #empty>
                  <div class="emptyBg">
                    <img src="@/assets/system/images/no_data/noData.png" alt="" />
                    <p>{{ emptyText }}</p>
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
            </template>
          </div>
        </el-col>
      </el-row>
    </template>

    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="860px" :append-to="$refs['appContainer']" draggable>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" @submit.prevent>
        <div class="form-section-title">基础信息</div>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" maxlength="64" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="datasourceType">
              <el-select v-model="form.datasourceType" placeholder="请选择" style="width: 100%">
                <el-option v-for="opt in datasourceTypeOptions" :key="opt" :label="opt" :value="opt" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="业务域" prop="bizDomain">
              <el-select v-model="form.bizDomain" placeholder="请选择" style="width: 100%">
                <el-option v-for="opt in bizDomainOptions" :key="opt" :label="opt" :value="opt" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="环境" prop="env">
              <el-select v-model="form.env" placeholder="请选择" style="width: 100%">
                <el-option label="生产" value="prod" />
                <el-option label="非生产" value="non_prod" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="责任人" prop="owner">
              <el-input v-model="form.owner" maxlength="64" placeholder="请输入责任人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签" prop="tags">
              <el-select v-model="form.tags" multiple collapse-tags collapse-tags-tooltip placeholder="请选择" style="width: 100%">
                <el-option v-for="opt in tagOptions" :key="opt" :label="opt" :value="opt" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="18">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="3" maxlength="256" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">连接配置</div>
        <template v-if="isDbType">
          <el-row :gutter="18">
            <el-col :span="12">
              <el-form-item label="主机地址" prop="host">
                <el-input v-model="form.host" placeholder="请输入主机地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口" prop="port">
                <el-input-number v-model="form.port" :min="1" :max="65535" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="18">
            <el-col :span="12">
              <el-form-item label="库名" prop="database">
                <el-input v-model="form.database" placeholder="请输入库名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="18">
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="form.password"
                  type="password"
                  show-password
                  :placeholder="form.id ? '留空则不修改' : '请输入密码'"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template v-else-if="form.datasourceType === 'API'">
          <el-row :gutter="18">
            <el-col :span="24">
              <el-form-item label="Base URL" prop="baseUrl">
                <el-input v-model="form.baseUrl" placeholder="请输入 Base URL" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="18">
            <el-col :span="12">
              <el-form-item label="认证方式" prop="authType">
                <el-input v-model="form.authType" placeholder="如 Token/AKSK" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="认证信息" prop="password">
                <el-input
                  v-model="form.password"
                  type="password"
                  show-password
                  :placeholder="form.id ? '留空则不修改' : '请输入 Token 或密钥'"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template v-else-if="isFileType">
          <el-row :gutter="18">
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
          <el-button :loading="formTestLoading" :disabled="!canTestForm" @click="handleFormTest">连接测试</el-button>
          <el-button @click="dialogOpen = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DaDatasourceIndex">
import { computed, getCurrentInstance, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { parseTime } from '@/utils/anivia'
import {
  addDaDatasourceMock,
  delDaDatasourceMock,
  getDaDatasourceMock,
  listDaDatasourceMock,
  listDatasourceFacetMock,
  testDaDatasourceFormMock,
  testDaDatasourceMock,
  toggleDaDatasourceStatusMock,
  updateDaDatasourceMock
} from '@/api/da/dataSource/datasource.mock'

const { proxy } = getCurrentInstance()
const router = useRouter()

const appContainer = ref(null)
const queryRef = ref(null)
const formRef = ref(null)
const treeRef = ref(null)

const noPermission = ref(false)
const showSearch = ref(true)
const loading = ref(false)
const listError = ref(false)
const errorText = ref('')

const datasourceList = ref([])
const total = ref(0)
const treeFilterText = ref('')

const datasourceTypeOptions = ref([])
const bizDomainOptions = ref([])
const tagOptions = ref([])

const treeData = ref([])
const treeProps = { children: 'children', label: 'label' }

const dialogOpen = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formTestLoading = ref(false)

const testLoadingMap = reactive({})

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  datasourceType: '',
  bizDomain: '',
  env: '',
  status: '',
  tag: '',
  orderByColumn: 'createTime',
  isAsc: 'desc'
})

const defaultForm = () => ({
  id: '',
  name: '',
  description: '',
  datasourceType: '',
  bizDomain: '',
  env: 'non_prod',
  owner: '',
  tags: [],
  host: '',
  port: 3306,
  database: '',
  username: '',
  password: '',
  authType: '',
  baseUrl: '',
  rootPath: ''
})

const form = reactive(defaultForm())

const rules = reactive({
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' },
    { min: 1, max: 64, message: '名称长度限制为 1-64 个字符', trigger: 'blur' }
  ],
  datasourceType: [{ required: true, message: '请选择数据源类型', trigger: 'change' }],
  bizDomain: [{ required: true, message: '请选择业务域', trigger: 'change' }],
  env: [{ required: true, message: '请选择环境', trigger: 'change' }],
  owner: [{ max: 64, message: '责任人长度不能超过 64', trigger: 'blur' }]
})

const dbTypes = ['MySQL', 'Oracle', 'PostgreSQL', 'Doris', 'Hive']
const fileTypes = ['fileSystem', 'dataLake']
const isDbType = computed(() => dbTypes.includes(form.datasourceType))
const isFileType = computed(() => fileTypes.includes(form.datasourceType))

const hasActiveFilters = computed(() => {
  const keys = ['keyword', 'datasourceType', 'bizDomain', 'env', 'status', 'tag']
  return keys.some((key) => queryParams[key])
})

const emptyText = computed(() => {
  if (hasActiveFilters.value) return '未找到符合当前筛选条件的数据源，请调整筛选条件后重试。'
  return '暂未配置任何数据源，请点击“新增数据源”开始配置。'
})

const canTestForm = computed(() => {
  if (!form.datasourceType) return false
  if (isDbType.value) {
    const hasPwd = form.id ? true : !!form.password
    return !!(form.host && form.port && form.database && form.username && hasPwd)
  }
  if (form.datasourceType === 'API') {
    const hasPwd = form.id ? true : !!form.password
    return !!(form.baseUrl && hasPwd)
  }
  if (isFileType.value) {
    return !!form.rootPath
  }
  return false
})

watch(treeFilterText, (val) => {
  treeRef.value?.filter(val)
})

watch(
  () => form.datasourceType,
  () => {
    formRef.value?.clearValidate?.()
  }
)

const buildTreeData = () => {
  const root = {
    key: 'all',
    label: '全部数据源',
    filter: { bizDomain: '', env: '' },
    children: []
  }

  bizDomainOptions.value.forEach((domain) => {
    root.children.push({
      key: `domain-${domain}`,
      label: domain,
      filter: { bizDomain: domain, env: '' },
      children: [
        { key: `domain-${domain}-prod`, label: '生产', filter: { bizDomain: domain, env: 'prod' } },
        { key: `domain-${domain}-non`, label: '非生产', filter: { bizDomain: domain, env: 'non_prod' } }
      ]
    })
  })

  treeData.value = [root]
}

const filterTreeNode = (value, data) => {
  if (!value) return true
  return data.label?.toLowerCase().includes(value.toLowerCase())
}

const checkPermission = () => {
  if (typeof proxy?.hasPermi === 'function') {
    noPermission.value = !proxy.hasPermi(['da:dataSource:list'])
  }
}

const loadFacet = async () => {
  const res = await listDatasourceFacetMock()
  if (res.code !== 200) return
  datasourceTypeOptions.value = res.data.datasourceTypeOptions || []
  bizDomainOptions.value = res.data.bizDomainOptions || []
  tagOptions.value = res.data.tagOptions || []
  buildTreeData()
}

const getList = async () => {
  loading.value = true
  listError.value = false
  errorText.value = ''

  try {
    const res = await listDaDatasourceMock(queryParams)
    if (res.code !== 200) {
      throw new Error(res.msg || '列表查询失败')
    }
    datasourceList.value = res.data?.rows || []
    total.value = res.data?.total || 0
  } catch (error) {
    listError.value = true
    errorText.value = error?.message || '系统繁忙，请稍后重试'
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  proxy?.resetForm?.('queryRef')
  Object.assign(queryParams, {
    pageNum: 1,
    pageSize: 10,
    keyword: '',
    datasourceType: '',
    bizDomain: '',
    env: '',
    status: '',
    tag: '',
    orderByColumn: 'createTime',
    isAsc: 'desc'
  })
  getList()
}

const handleTreeNodeClick = (node) => {
  queryParams.bizDomain = node.filter?.bizDomain || ''
  queryParams.env = node.filter?.env || ''
  handleQuery()
}

const handleSortChange = ({ prop, order }) => {
  queryParams.orderByColumn = prop || 'createTime'
  queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
  getList()
}

const resetFormModel = () => {
  Object.assign(form, defaultForm())
}

const handleAdd = () => {
  resetFormModel()
  dialogTitle.value = '新增数据源'
  dialogOpen.value = true
}

const handleEdit = async (row) => {
  const res = await getDaDatasourceMock(row.id)
  if (res.code !== 200 || !res.data) {
    proxy?.$modal?.msgError(res.msg || '查询详情失败')
    return
  }

  const data = res.data
  Object.assign(form, {
    id: data.id,
    name: data.name,
    description: data.description,
    datasourceType: data.datasourceType,
    bizDomain: data.bizDomain,
    env: data.env,
    owner: data.owner,
    tags: data.tags || [],
    host: data.connection?.host || '',
    port: data.connection?.port || 3306,
    database: data.connection?.database || '',
    username: data.connection?.username || '',
    password: '',
    authType: data.connection?.authType || '',
    baseUrl: data.connection?.baseUrl || '',
    rootPath: data.connection?.rootPath || ''
  })

  dialogTitle.value = '编辑数据源'
  dialogOpen.value = true
}

const validateConnectionPayload = () => {
  if (isDbType.value) {
    if (!form.host) return '主机地址不能为空'
    if (!form.port) return '端口不能为空'
    if (!form.database) return '库名不能为空'
    if (!form.username) return '用户名不能为空'
    if (!form.id && !form.password) return '密码不能为空'
  }

  if (form.datasourceType === 'API') {
    if (!form.baseUrl) return 'Base URL 不能为空'
    if (!form.id && !form.password) return '认证信息不能为空'
  }

  if (isFileType.value && !form.rootPath) {
    return '根路径不能为空'
  }

  return ''
}

const submitForm = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return

    const connError = validateConnectionPayload()
    if (connError) {
      proxy?.$modal?.msgError(connError)
      return
    }

    submitLoading.value = true
    try {
      const payload = {
        ...form,
        password: form.password || ''
      }
      const res = form.id ? await updateDaDatasourceMock(payload) : await addDaDatasourceMock(payload)
      if (res.code !== 200) {
        proxy?.$modal?.msgError(res.msg || '保存失败')
        return
      }
      proxy?.$modal?.msgSuccess(form.id ? '保存成功' : '新增成功')
      dialogOpen.value = false
      getList()
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDelete = async (row) => {
  if (row.refCount > 0) {
    proxy?.$modal?.msgError(`当前有 ${row.refCount} 个任务正在使用该数据源，请先迁移或解绑引用后再删除。`)
    return
  }

  try {
    await proxy?.$modal?.confirm('删除为软删操作，删除后将不再作为可用数据源展示，确认继续吗？')
    const res = await delDaDatasourceMock(row.id)
    if (res.code !== 200) {
      proxy?.$modal?.msgError(res.msg || '删除失败')
      return
    }
    proxy?.$modal?.msgSuccess('删除成功')
    getList()
  } catch {
    // cancel
  }
}

const handleToggleStatus = async (row) => {
  const target = row.status === 'enabled' ? '禁用' : '启用'
  const tip = row.status === 'enabled' && row.refCount > 0
    ? `当前有 ${row.refCount} 个任务正在使用该数据源，禁用可能影响任务运行，确认继续吗？`
    : `确认要${target}数据源「${row.name}」吗？`

  try {
    await proxy?.$modal?.confirm(tip)
    const res = await toggleDaDatasourceStatusMock(row.id)
    if (res.code !== 200) {
      proxy?.$modal?.msgError(res.msg || `${target}失败`)
      return
    }
    proxy?.$modal?.msgSuccess(res.msg || `${target}成功`)
    getList()
  } catch {
    // cancel
  }
}

const handleRowTest = async (row) => {
  testLoadingMap[row.id] = true
  try {
    const res = await testDaDatasourceMock(row.id)
    const result = res.data || {}
    if (result.success) {
      proxy?.$modal?.msgSuccess(result.message)
    } else {
      proxy?.$modal?.msgError(result.message || '连接测试失败')
    }
    await getList()
  } finally {
    testLoadingMap[row.id] = false
  }
}

const handleFormTest = async () => {
  if (!canTestForm.value) return
  formTestLoading.value = true
  try {
    const payload = {
      datasourceType: form.datasourceType,
      host: form.host,
      port: form.port,
      database: form.database,
      username: form.username,
      password: form.password,
      baseUrl: form.baseUrl,
      authType: form.authType,
      rootPath: form.rootPath
    }
    const res = await testDaDatasourceFormMock(payload)
    const result = res.data || {}
    if (result.success) {
      proxy?.$modal?.msgSuccess(result.message)
    } else {
      proxy?.$modal?.msgError(result.message || '连接测试失败')
    }
  } finally {
    formTestLoading.value = false
  }
}

const handleDetail = (row) => {
  router.push({ path: '/da/datasource/detail', query: { id: row.id } })
}

onMounted(async () => {
  checkPermission()
  if (noPermission.value) return
  await loadFacet()
  await getList()
})
</script>

<style lang="scss" scoped>
.tree-panel {
  min-height: 66vh;
  background: #fff;
  padding: 14px;
  border-radius: 2px;

  &__header {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 10px;
  }
}

.form-section-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.error-wrap {
  min-height: 60vh;
  background: #fff;
  border-radius: 2px;
}
</style>
