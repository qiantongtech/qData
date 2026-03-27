<template>
  <div class="app-container" ref="detailContainer">
    <el-result v-if="noPermission" icon="warning" title="无权限访问" sub-title="当前账号暂无数据源管理权限，请联系管理员开通。" />

    <template v-else>
      <div class="detail-header">
        <div>
          <div class="detail-title">{{ detail.name || '-' }}</div>
          <div class="detail-sub">
            {{ detail.datasourceType || '-' }}
            <el-divider direction="vertical" />
            {{ detail.env === 'prod' ? '生产环境' : '非生产环境' }}
            <el-divider direction="vertical" />
            <el-tag :type="detail.status === 'enabled' ? 'success' : 'info'">
              {{ detail.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </div>
        </div>
        <div class="detail-actions">
          <el-button @click="goBack">返回列表</el-button>
          <el-button type="primary" plain @click="openEdit">编辑</el-button>
          <el-button type="primary" plain @click="handleToggleStatus">
            {{ detail.status === 'enabled' ? '禁用' : '启用' }}
          </el-button>
          <el-button type="danger" plain @click="handleDelete">删除</el-button>
          <el-button
            :loading="testLoading"
            :disabled="testLoading"
            @click="handleTest"
          >
            连接测试
          </el-button>
        </div>
      </div>

      <el-result v-if="detailError" icon="error" title="详情加载失败" :sub-title="detailErrorText || '系统繁忙，请稍后重试'">
        <template #extra>
          <el-button type="primary" @click="loadDetail">重试</el-button>
        </template>
      </el-result>

      <div v-else v-loading="loading" class="detail-content">
        <el-card shadow="never" class="mb14">
          <template #header>基础信息</template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="名称">{{ detail.name || '-' }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ detail.datasourceType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              {{ detail.status === 'enabled' ? '启用' : '禁用' }}
            </el-descriptions-item>
            <el-descriptions-item label="业务域">{{ detail.bizDomain || '-' }}</el-descriptions-item>
            <el-descriptions-item label="环境">{{ detail.env === 'prod' ? '生产' : '非生产' }}</el-descriptions-item>
            <el-descriptions-item label="责任人">{{ detail.owner || '-' }}</el-descriptions-item>
            <el-descriptions-item label="标签" :span="3">{{ (detail.tags || []).join('、') || '-' }}</el-descriptions-item>
            <el-descriptions-item label="描述" :span="3">{{ detail.description || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ detail.createBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ parseTime(detail.createTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ parseTime(detail.updateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card shadow="never" class="mb14">
          <template #header>连接配置概要</template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="主机地址">{{ detail.connection?.host || '-' }}</el-descriptions-item>
            <el-descriptions-item label="端口">{{ detail.connection?.port || '-' }}</el-descriptions-item>
            <el-descriptions-item label="库名">{{ detail.connection?.database || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ detail.connection?.username || '-' }}</el-descriptions-item>
            <el-descriptions-item label="认证方式">{{ detail.connection?.authType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="Base URL">{{ detail.connection?.baseUrl || '-' }}</el-descriptions-item>
            <el-descriptions-item label="根路径">{{ detail.connection?.rootPath || '-' }}</el-descriptions-item>
            <el-descriptions-item label="敏感字段" :span="2">{{ detail.connection?.passwordMasked || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card shadow="never" class="mb14">
          <template #header>
            下游引用信息（{{ detail.refCount || 0 }}）
          </template>
          <el-table :data="detail.references || []" stripe>
            <el-table-column prop="jobName" label="任务名称" min-width="220" show-overflow-tooltip />
            <el-table-column prop="system" label="系统" width="120" />
            <el-table-column prop="env" label="环境" width="120">
              <template #default="scope">{{ scope.row.env === 'prod' ? '生产' : '非生产' }}</template>
            </el-table-column>
            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>当前暂无下游任务引用该数据源。</p>
              </div>
            </template>
          </el-table>
        </el-card>

        <el-card shadow="never">
          <template #header>审计摘要</template>
          <el-table :data="detail.auditLogs || []" stripe>
            <el-table-column prop="operateTime" label="操作时间" width="170">
              <template #default="scope">{{ parseTime(scope.row.operateTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作人" width="120" />
            <el-table-column prop="actionType" label="操作类型" width="110" />
            <el-table-column prop="resultSummary" label="摘要" min-width="320" show-overflow-tooltip />
            <template #empty>
              <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无相关审计记录。</p>
              </div>
            </template>
          </el-table>
        </el-card>
      </div>
    </template>

    <el-dialog title="编辑数据源" v-model="editOpen" width="860px" :append-to="$refs['detailContainer']" draggable>
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
                <el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" />
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
                <el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" />
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
          <el-button @click="editOpen = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitEdit">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DaDatasourceDetail">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { parseTime } from '@/utils/anivia'
import {
  delDaDatasourceMock,
  getDaDatasourceMock,
  listDatasourceFacetMock,
  testDaDatasourceFormMock,
  testDaDatasourceMock,
  toggleDaDatasourceStatusMock,
  updateDaDatasourceMock
} from '@/api/da/dataSource/datasource.mock'

const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()

const detailContainer = ref(null)
const formRef = ref(null)

const noPermission = ref(false)
const loading = ref(false)
const detailError = ref(false)
const detailErrorText = ref('')
const testLoading = ref(false)

const editOpen = ref(false)
const submitLoading = ref(false)
const formTestLoading = ref(false)

const datasourceTypeOptions = ref([])
const bizDomainOptions = ref([])
const tagOptions = ref([])

const detail = reactive({})

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

const canTestForm = computed(() => {
  if (!form.datasourceType) return false
  if (isDbType.value) {
    return !!(form.host && form.port && form.database && form.username)
  }
  if (form.datasourceType === 'API') {
    return !!form.baseUrl
  }
  if (isFileType.value) {
    return !!form.rootPath
  }
  return false
})

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
}

const loadDetail = async () => {
  const id = route.query.id
  if (!id) {
    detailError.value = true
    detailErrorText.value = '缺少数据源 ID 参数'
    return
  }

  loading.value = true
  detailError.value = false
  detailErrorText.value = ''
  try {
    const res = await getDaDatasourceMock(id)
    if (res.code !== 200 || !res.data) {
      throw new Error(res.msg || '详情查询失败')
    }
    Object.keys(detail).forEach((key) => delete detail[key])
    Object.assign(detail, res.data)
  } catch (error) {
    detailError.value = true
    detailErrorText.value = error?.message || '系统繁忙，请稍后重试'
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/da/datasource')
}

const handleToggleStatus = async () => {
  if (!detail.id) return
  const target = detail.status === 'enabled' ? '禁用' : '启用'
  const tip = detail.status === 'enabled' && detail.refCount > 0
    ? `当前有 ${detail.refCount} 个任务正在使用该数据源，禁用可能影响任务运行，确认继续吗？`
    : `确认要${target}数据源「${detail.name}」吗？`

  try {
    await proxy?.$modal?.confirm(tip)
    const res = await toggleDaDatasourceStatusMock(detail.id)
    if (res.code !== 200) {
      proxy?.$modal?.msgError(res.msg || `${target}失败`)
      return
    }
    proxy?.$modal?.msgSuccess(res.msg || `${target}成功`)
    await loadDetail()
  } catch {
    // cancel
  }
}

const handleDelete = async () => {
  if (!detail.id) return
  if (detail.refCount > 0) {
    proxy?.$modal?.msgError(`当前有 ${detail.refCount} 个任务正在使用该数据源，请先迁移或解绑引用后再删除。`)
    return
  }

  try {
    await proxy?.$modal?.confirm('删除为软删操作，删除后将不再作为可用数据源展示，确认继续吗？')
    const res = await delDaDatasourceMock(detail.id)
    if (res.code !== 200) {
      proxy?.$modal?.msgError(res.msg || '删除失败')
      return
    }
    proxy?.$modal?.msgSuccess('删除成功')
    goBack()
  } catch {
    // cancel
  }
}

const handleTest = async () => {
  if (!detail.id) return
  testLoading.value = true
  try {
    const res = await testDaDatasourceMock(detail.id)
    if (res.data?.success) {
      proxy?.$modal?.msgSuccess(res.data.message)
    } else {
      proxy?.$modal?.msgError(res.data?.message || '连接测试失败')
    }
    await loadDetail()
  } finally {
    testLoading.value = false
  }
}

const openEdit = () => {
  Object.assign(form, {
    id: detail.id,
    name: detail.name,
    description: detail.description,
    datasourceType: detail.datasourceType,
    bizDomain: detail.bizDomain,
    env: detail.env,
    owner: detail.owner,
    tags: detail.tags || [],
    host: detail.connection?.host || '',
    port: detail.connection?.port || 3306,
    database: detail.connection?.database || '',
    username: detail.connection?.username || '',
    password: '',
    authType: detail.connection?.authType || '',
    baseUrl: detail.connection?.baseUrl || '',
    rootPath: detail.connection?.rootPath || ''
  })
  editOpen.value = true
}

const validateConnectionPayload = () => {
  if (isDbType.value) {
    if (!form.host) return '主机地址不能为空'
    if (!form.port) return '端口不能为空'
    if (!form.database) return '库名不能为空'
    if (!form.username) return '用户名不能为空'
  }

  if (form.datasourceType === 'API' && !form.baseUrl) {
    return 'Base URL 不能为空'
  }

  if (isFileType.value && !form.rootPath) {
    return '根路径不能为空'
  }

  return ''
}

const submitEdit = () => {
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
      const res = await updateDaDatasourceMock(payload)
      if (res.code !== 200) {
        proxy?.$modal?.msgError(res.msg || '保存失败')
        return
      }
      proxy?.$modal?.msgSuccess('保存成功')
      editOpen.value = false
      await loadDetail()
    } finally {
      submitLoading.value = false
    }
  })
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

onMounted(async () => {
  checkPermission()
  if (noPermission.value) return
  await loadFacet()
  await loadDetail()
})
</script>

<style lang="scss" scoped>
.detail-header {
  background: #fff;
  border-radius: 2px;
  padding: 14px 16px;
  margin-bottom: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.detail-title {
  font-size: 20px;
  font-weight: 600;
  line-height: 30px;
}

.detail-sub {
  margin-top: 8px;
  display: flex;
  align-items: center;
  color: var(--el-text-color-secondary);
}

.detail-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.detail-content {
  min-height: 50vh;
}

.form-section-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.mb14 {
  margin-bottom: 14px;
}
</style>
