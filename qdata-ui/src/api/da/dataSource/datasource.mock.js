/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *
 * 数据源管理（DA）前端联调 Mock
 */

const delay = (ms = 280) => new Promise((resolve) => setTimeout(resolve, ms))

const nowStr = () => new Date().toISOString().slice(0, 19).replace('T', ' ')
const clone = (obj) => JSON.parse(JSON.stringify(obj))

let datasourceSeq = 7

const mockDb = [
  {
    id: '1',
    name: 'crm-prod-mysql',
    description: 'CRM 生产库',
    datasourceType: 'MySQL',
    bizDomain: '客户域',
    env: 'prod',
    owner: 'zhangsan',
    tags: ['核心', '在线服务'],
    status: 'enabled',
    refCount: 2,
    createBy: 'admin',
    createTime: '2026-02-15 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-20 10:16:22',
    lastTestResult: 'success',
    lastTestTime: '2026-02-24 16:21:13',
    connection: {
      host: '10.10.10.11',
      port: 3306,
      database: 'crm_prod',
      username: 'readonly',
      passwordMasked: '********',
      authType: '',
      baseUrl: '',
      rootPath: ''
    },
    references: [
      { id: 'job-1001', jobName: 'CRM客户增量同步', system: 'DPP', env: 'prod' },
      { id: 'job-1002', jobName: 'CRM客户宽表构建', system: 'DP', env: 'prod' }
    ],
    auditLogs: [
      {
        id: 'log-1',
        operateTime: '2026-02-24 16:21:13',
        operatorName: 'admin',
        actionType: 'test',
        resultSummary: '连接测试成功（耗时 418ms）'
      },
      {
        id: 'log-2',
        operateTime: '2026-02-20 10:16:22',
        operatorName: 'admin',
        actionType: 'update',
        resultSummary: '更新连接参数：host'
      }
    ]
  },
  {
    id: '2',
    name: 'crm-dev-mysql',
    description: 'CRM 开发环境',
    datasourceType: 'MySQL',
    bizDomain: '客户域',
    env: 'non_prod',
    owner: 'lisi',
    tags: ['开发'],
    status: 'enabled',
    refCount: 0,
    createBy: 'admin',
    createTime: '2026-02-13 12:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-23 09:10:00',
    lastTestResult: 'fail',
    lastTestTime: '2026-02-23 09:10:00',
    connection: {
      host: '10.10.12.20',
      port: 3306,
      database: 'crm_dev',
      username: 'dev_user',
      passwordMasked: '********',
      authType: '',
      baseUrl: '',
      rootPath: ''
    },
    references: [],
    auditLogs: []
  },
  {
    id: '3',
    name: 'order-prod-pg',
    description: '订单域 PostgreSQL',
    datasourceType: 'PostgreSQL',
    bizDomain: '订单域',
    env: 'prod',
    owner: 'wangwu',
    tags: ['核心', '账务'],
    status: 'disabled',
    refCount: 1,
    createBy: 'admin',
    createTime: '2026-02-11 08:32:00',
    updateBy: 'admin',
    updateTime: '2026-02-22 21:33:00',
    lastTestResult: 'untested',
    lastTestTime: '',
    connection: {
      host: '10.10.20.30',
      port: 5432,
      database: 'order_prod',
      username: 'pg_ro',
      passwordMasked: '********',
      authType: '',
      baseUrl: '',
      rootPath: ''
    },
    references: [{ id: 'job-2001', jobName: '订单汇总离线任务', system: 'DPP', env: 'prod' }],
    auditLogs: []
  },
  {
    id: '4',
    name: 'risk-api',
    description: '风险评分 API',
    datasourceType: 'API',
    bizDomain: '风控域',
    env: 'prod',
    owner: 'zhaoliu',
    tags: ['API'],
    status: 'enabled',
    refCount: 0,
    createBy: 'admin',
    createTime: '2026-02-09 13:20:00',
    updateBy: 'admin',
    updateTime: '2026-02-21 18:01:00',
    lastTestResult: 'success',
    lastTestTime: '2026-02-24 15:10:30',
    connection: {
      host: '',
      port: null,
      database: '',
      username: '',
      passwordMasked: '',
      authType: 'Token',
      baseUrl: 'https://risk.example.com/api',
      rootPath: ''
    },
    references: [],
    auditLogs: []
  },
  {
    id: '5',
    name: 'lake-ods',
    description: '对象存储 ODS 目录',
    datasourceType: 'dataLake',
    bizDomain: '数据湖',
    env: 'non_prod',
    owner: 'sunqi',
    tags: ['离线'],
    status: 'enabled',
    refCount: 0,
    createBy: 'admin',
    createTime: '2026-02-08 09:45:00',
    updateBy: 'admin',
    updateTime: '2026-02-20 09:45:00',
    lastTestResult: 'untested',
    lastTestTime: '',
    connection: {
      host: '',
      port: null,
      database: '',
      username: '',
      passwordMasked: '',
      authType: '',
      baseUrl: '',
      rootPath: '/lake/ods/'
    },
    references: [],
    auditLogs: []
  },
  {
    id: '6',
    name: 'ops-sftp',
    description: '运维 SFTP 文件源',
    datasourceType: 'fileSystem',
    bizDomain: '运维域',
    env: 'non_prod',
    owner: 'admin',
    tags: ['文件'],
    status: 'enabled',
    refCount: 0,
    createBy: 'admin',
    createTime: '2026-02-07 11:08:00',
    updateBy: 'admin',
    updateTime: '2026-02-24 11:08:00',
    lastTestResult: 'success',
    lastTestTime: '2026-02-24 11:09:32',
    connection: {
      host: '',
      port: null,
      database: '',
      username: '',
      passwordMasked: '',
      authType: '',
      baseUrl: '',
      rootPath: '/incoming/'
    },
    references: [],
    auditLogs: []
  }
]

const sortByField = (arr, orderByColumn = 'createTime', isAsc = 'desc') => {
  const list = [...arr]
  list.sort((a, b) => {
    const av = a[orderByColumn] ?? ''
    const bv = b[orderByColumn] ?? ''
    if (av === bv) return 0
    if (isAsc === 'asc') return av > bv ? 1 : -1
    return av > bv ? -1 : 1
  })
  return list
}

export function listDaDatasourceMock(query = {}) {
  const {
    pageNum = 1,
    pageSize = 10,
    keyword,
    datasourceType,
    status,
    bizDomain,
    env,
    tag,
    orderByColumn = 'createTime',
    isAsc = 'desc'
  } = query

  let rows = mockDb.filter((item) => {
    const hitKeyword = !keyword
      || item.name.toLowerCase().includes(String(keyword).toLowerCase())
      || (item.description || '').toLowerCase().includes(String(keyword).toLowerCase())
    const hitType = !datasourceType || item.datasourceType === datasourceType
    const hitStatus = !status || item.status === status
    const hitDomain = !bizDomain || item.bizDomain === bizDomain
    const hitEnv = !env || item.env === env
    const hitTag = !tag || item.tags.includes(tag)
    return hitKeyword && hitType && hitStatus && hitDomain && hitEnv && hitTag
  })

  rows = sortByField(rows, orderByColumn, isAsc)
  const total = rows.length
  const start = (Number(pageNum) - 1) * Number(pageSize)
  const pageRows = rows.slice(start, start + Number(pageSize))

  return delay().then(() => ({
    code: 200,
    msg: '查询成功',
    data: { rows: clone(pageRows), total }
  }))
}

export function getDaDatasourceMock(id) {
  return delay().then(() => {
    const row = mockDb.find((item) => item.id === String(id))
    if (!row) {
      return { code: 404, msg: '数据源不存在', data: null }
    }
    return { code: 200, msg: '查询成功', data: clone(row) }
  })
}

export function addDaDatasourceMock(data) {
  return delay().then(() => {
    const id = String(datasourceSeq++)
    const current = nowStr()
    mockDb.push({
      id,
      name: data.name,
      description: data.description || '',
      datasourceType: data.datasourceType,
      bizDomain: data.bizDomain,
      env: data.env,
      owner: data.owner || 'admin',
      tags: data.tags || [],
      status: 'enabled',
      refCount: 0,
      createBy: 'admin',
      createTime: current,
      updateBy: 'admin',
      updateTime: current,
      lastTestResult: 'untested',
      lastTestTime: '',
      connection: {
        host: data.host || '',
        port: data.port || null,
        database: data.database || '',
        username: data.username || '',
        passwordMasked: data.password ? '********' : '',
        authType: data.authType || '',
        baseUrl: data.baseUrl || '',
        rootPath: data.rootPath || ''
      },
      references: [],
      auditLogs: [
        {
          id: `log-${Date.now()}`,
          operateTime: current,
          operatorName: 'admin',
          actionType: 'create',
          resultSummary: '新增数据源'
        }
      ]
    })

    return { code: 200, msg: '新增成功', data: { id } }
  })
}

export function updateDaDatasourceMock(data) {
  return delay().then(() => {
    const row = mockDb.find((item) => item.id === String(data.id))
    if (!row) {
      return { code: 404, msg: '数据源不存在', data: null }
    }

    row.name = data.name
    row.description = data.description || ''
    row.datasourceType = data.datasourceType
    row.bizDomain = data.bizDomain
    row.env = data.env
    row.owner = data.owner || row.owner
    row.tags = data.tags || []
    row.updateBy = 'admin'
    row.updateTime = nowStr()

    row.connection = {
      ...row.connection,
      host: data.host || '',
      port: data.port || null,
      database: data.database || '',
      username: data.username || '',
      passwordMasked: data.password ? '********' : row.connection.passwordMasked,
      authType: data.authType || '',
      baseUrl: data.baseUrl || '',
      rootPath: data.rootPath || ''
    }

    row.auditLogs.unshift({
      id: `log-${Date.now()}`,
      operateTime: row.updateTime,
      operatorName: 'admin',
      actionType: 'update',
      resultSummary: '编辑数据源'
    })

    return { code: 200, msg: '修改成功', data: { id: row.id } }
  })
}

export function delDaDatasourceMock(id) {
  return delay().then(() => {
    const idx = mockDb.findIndex((item) => item.id === String(id))
    if (idx < 0) {
      return { code: 404, msg: '数据源不存在', data: null }
    }

    const row = mockDb[idx]
    if (row.refCount > 0) {
      return {
        code: 409,
        msg: `当前有 ${row.refCount} 个任务正在使用该数据源，请先迁移或解绑引用后再删除。`,
        data: { refCount: row.refCount }
      }
    }

    mockDb.splice(idx, 1)
    return { code: 200, msg: '删除成功', data: null }
  })
}

export function toggleDaDatasourceStatusMock(id) {
  return delay().then(() => {
    const row = mockDb.find((item) => item.id === String(id))
    if (!row) {
      return { code: 404, msg: '数据源不存在', data: null }
    }

    row.status = row.status === 'enabled' ? 'disabled' : 'enabled'
    row.updateTime = nowStr()
    row.updateBy = 'admin'
    row.auditLogs.unshift({
      id: `log-${Date.now()}`,
      operateTime: row.updateTime,
      operatorName: 'admin',
      actionType: 'status',
      resultSummary: `状态变更为 ${row.status}`
    })

    return {
      code: 200,
      msg: row.status === 'enabled' ? '启用成功' : '禁用成功',
      data: { status: row.status }
    }
  })
}

const buildTestResult = ({ passed, durationMs, errorCode }) => ({
  success: passed,
  durationMs,
  message: passed ? `连接测试成功（耗时 ${durationMs} ms）` : `连接测试失败，请检查主机地址/端口/账号密码等配置。（错误码：${errorCode}）`,
  errorCode: passed ? '' : errorCode
})

export function testDaDatasourceMock(id) {
  return delay(600).then(() => {
    const row = mockDb.find((item) => item.id === String(id))
    if (!row) {
      return { code: 404, msg: '数据源不存在', data: null }
    }

    const shouldFail = row.name.includes('dev') || row.connection.host.includes('12.20')
    const durationMs = shouldFail ? 1186 : 426
    const result = buildTestResult({
      passed: !shouldFail,
      durationMs,
      errorCode: shouldFail ? 'DS-CONN-1001' : ''
    })

    row.lastTestResult = result.success ? 'success' : 'fail'
    row.lastTestTime = nowStr()
    row.auditLogs.unshift({
      id: `log-${Date.now()}`,
      operateTime: row.lastTestTime,
      operatorName: 'admin',
      actionType: 'test',
      resultSummary: result.message
    })

    return {
      code: 200,
      msg: result.success ? '连接成功' : '连接失败',
      data: result
    }
  })
}

export function testDaDatasourceFormMock(payload) {
  return delay(600).then(() => {
    const host = String(payload.host || '').toLowerCase()
    const baseUrl = String(payload.baseUrl || '').toLowerCase()
    const shouldFail = host.includes('fail') || baseUrl.includes('fail') || String(payload.password || '').includes('wrong')

    const result = buildTestResult({
      passed: !shouldFail,
      durationMs: shouldFail ? 1320 : 388,
      errorCode: shouldFail ? 'DS-CONN-1002' : ''
    })

    return {
      code: 200,
      msg: result.success ? '连接成功' : '连接失败',
      data: result
    }
  })
}

export function listDatasourceFacetMock() {
  const bizDomainOptions = [...new Set(mockDb.map((item) => item.bizDomain))]
  const tagOptions = [...new Set(mockDb.flatMap((item) => item.tags || []))]

  return Promise.resolve({
    code: 200,
    msg: '查询成功',
    data: {
      bizDomainOptions,
      tagOptions,
      envOptions: ['prod', 'non_prod'],
      statusOptions: ['enabled', 'disabled'],
      datasourceTypeOptions: ['MySQL', 'Oracle', 'PostgreSQL', 'Doris', 'Hive', 'API', 'fileSystem', 'dataLake']
    }
  })
}
