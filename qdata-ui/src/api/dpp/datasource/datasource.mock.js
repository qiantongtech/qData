/*
 * Mock for dpp/datasource API（开发联调用，与 datasource.js 方法名一致）
 * 对接后端后可从 datasource.js 请求真实接口，本文件仅作占位与联调。
 */

const MOCK_DELAY = 300;

function delay(ms = MOCK_DELAY) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

const mockList = [
  {
    id: '1',
    datasourceName: '生产MySQL',
    code: 'ds_mysql_prod',
    description: '生产环境主库',
    datasourceCategory: 'relationDb',
    datasourceType: 'MySQL',
    deptName: '数据平台部',
    owner: '张三',
    envTag: 'prod',
    version: '1.0',
    status: 'enabled',
    lastTestResult: 'success',
    lastTestTime: '2025-02-12 10:00:00',
    lastTestDuration: '120ms',
    createBy: 'admin',
    createTime: '2025-02-01 09:00:00'
  },
  {
    id: '2',
    datasourceName: '数仓Hive',
    code: 'ds_hive_dw',
    description: '离线数仓',
    datasourceCategory: 'bigData',
    datasourceType: 'Hive',
    deptName: '数据平台部',
    owner: '李四',
    envTag: 'test',
    version: '1.0',
    status: 'enabled',
    lastTestResult: 'untested',
    lastTestTime: null,
    lastTestDuration: null,
    createBy: 'admin',
    createTime: '2025-02-05 14:00:00'
  }
];

export function listDatasource(query) {
  return delay().then(() => ({
    code: 200,
    msg: '查询成功',
    data: {
      total: mockList.length,
      rows: mockList,
      list: mockList
    }
  }));
}

export function getDatasource(id) {
  return delay().then(() => {
    const row = mockList.find((item) => String(item.id) === String(id));
    return {
      code: 200,
      msg: '查询成功',
      data: row ? { ...row } : {}
    };
  });
}

export function addDatasource(data) {
  return delay().then(() => ({
    code: 200,
    msg: '新增成功',
    data: { id: String(Date.now()) }
  }));
}

export function updateDatasource(data) {
  return delay().then(() => ({
    code: 200,
    msg: '修改成功',
    data: {}
  }));
}

export function removeDatasource(id) {
  return delay().then(() => ({
    code: 200,
    msg: '删除成功',
    data: {}
  }));
}

export function testDatasource(id) {
  return delay().then(() => ({
    code: 200,
    msg: '连接成功',
    data: { success: true, duration: 120, message: '连接成功' }
  }));
}

export function toggleDatasourceStatus(id, status) {
  return delay().then(() => ({
    code: 200,
    msg: '操作成功',
    data: {}
  }));
}
