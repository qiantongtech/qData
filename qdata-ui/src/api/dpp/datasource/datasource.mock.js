/*
 * 数据源管理 Mock 接口
 * 与 datasource.js 同名的请求函数，返回 { code, msg, data } 结构，供前端联调使用。
 */

const delay = (ms = 300) => new Promise((r) => setTimeout(r, ms))

const dbTypes = ['MySQL', 'Oracle', 'PostgreSQL', 'Doris', 'Hive']
const otherTypes = ['API', 'fileSystem', 'dataLake']
const allTypes = [...dbTypes, ...otherTypes]

const statusOptions = [
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' }
]
const testResultOptions = [
  { label: '成功', value: 'success' },
  { label: '失败', value: 'fail' },
  { label: '未测试', value: 'untested' }
]

function buildList(total = 6) {
  const list = []
  for (let i = 1; i <= total; i++) {
    list.push({
      id: String(i),
      name: `数据源${i}`,
      description: i === 1 ? 'MySQL 生产库' : `描述${i}`,
      datasourceType: allTypes[(i - 1) % allTypes.length],
      deptName: ['数据研发部', '数据治理部', '平台部'][(i - 1) % 3],
      deptId: String((i % 3) + 1),
      status: i % 3 === 0 ? 'disabled' : 'enabled',
      lastTestResult: ['success', 'fail', 'untested'][(i - 1) % 3],
      lastTestTime: i % 2 === 0 ? '2025-02-10 14:30:00' : null,
      host: i <= 3 ? `192.168.1.${i}` : null,
      port: i <= 3 ? 3306 : null,
      dbOrSchema: i <= 3 ? 'qdata' : null,
      username: i <= 3 ? 'root' : null,
      createBy: 'admin',
      createTime: '2025-02-01 10:00:00'
    })
  }
  return list
}

const mockList = buildList()

export function listDppDatasource(query) {
  return delay().then(() => {
    const { name, datasourceType, status, deptId, pageNum = 1, pageSize = 10 } = query || {}
    let rows = [...mockList]
    if (name) rows = rows.filter((r) => r.name && r.name.indexOf(name) !== -1)
    if (datasourceType) rows = rows.filter((r) => r.datasourceType === datasourceType)
    if (status) rows = rows.filter((r) => r.status === status)
    if (deptId) rows = rows.filter((r) => r.deptId === deptId)
    const total = rows.length
    const start = (pageNum - 1) * pageSize
    rows = rows.slice(start, start + pageSize)
    return { code: 200, msg: '成功', data: { rows, total } }
  })
}

export function getDppDatasource(id) {
  return delay().then(() => {
    const row = mockList.find((r) => r.id === id)
    if (!row) return { code: 500, msg: '数据源不存在', data: null }
    const data = { ...row }
    if (data.password) data.password = '********'
    return { code: 200, msg: '成功', data }
  })
}

export function addDppDatasource(data) {
  return delay().then(() => {
    const id = String(mockList.length + 1)
    mockList.push({
      id,
      name: data.name,
      description: data.description,
      datasourceType: data.datasourceType,
      deptName: data.deptName || '',
      deptId: data.deptId,
      status: 'enabled',
      lastTestResult: 'untested',
      lastTestTime: null,
      ...data,
      createBy: 'admin',
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
    })
    return { code: 200, msg: '新增成功', data: { id } }
  })
}

export function updateDppDatasource(data) {
  return delay().then(() => {
    const idx = mockList.findIndex((r) => r.id === data.id)
    if (idx === -1) return { code: 500, msg: '数据源不存在', data: null }
    const row = mockList[idx]
    const update = { ...data }
    if (update.password === '' || update.password === '********') delete update.password
    Object.assign(row, update)
    return { code: 200, msg: '修改成功', data: { id: data.id } }
  })
}

export function delDppDatasource(id) {
  return delay().then(() => {
    if (id === '1') {
      return {
        code: 500,
        msg: '当前数据源被数据资产「用户表」、元数据采集任务「采集任务A」引用，禁止删除。',
        data: null
      }
    }
    const idx = mockList.findIndex((r) => r.id === id)
    if (idx !== -1) mockList.splice(idx, 1)
    return { code: 200, msg: '删除成功', data: null }
  })
}

export function testDppDatasource(id) {
  return delay(500).then(() => {
    const row = mockList.find((r) => r.id === id)
    const now = new Date().toISOString().slice(0, 19).replace('T', ' ')
    if (!row) return { code: 500, msg: '数据源不存在', data: null }
    const success = id !== '2'
    if (row) {
      row.lastTestResult = success ? 'success' : 'fail'
      row.lastTestTime = now
    }
    return {
      code: 200,
      msg: success ? '连接成功' : '连接失败',
      data: {
        success,
        message: success ? '连接成功' : 'Connection refused',
        lastTestTime: now,
        lastTestResult: success ? 'success' : 'fail'
      }
    }
  })
}

export function testDppDatasourceForm(data) {
  return delay(500).then(() => {
    return {
      code: 200,
      msg: '连接成功',
      data: { success: true, message: '连接成功' }
    }
  })
}

export function toggleDppDatasourceStatus(id) {
  return delay().then(() => {
    const row = mockList.find((r) => r.id === id)
    if (!row) return { code: 500, msg: '数据源不存在', data: null }
    row.status = row.status === 'enabled' ? 'disabled' : 'enabled'
    return { code: 200, msg: row.status === 'enabled' ? '已启用' : '已禁用', data: { status: row.status } }
  })
}
