<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
  More information: https://qdata.qiantong.tech/business.html
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
        <el-form-item label="名称" prop="datasourceName">
          <el-input
            v-model="queryParams.datasourceName"
            placeholder="请输入名称"
            clearable
            class="el-form-input-width"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input
            v-model="queryParams.code"
            placeholder="请输入编码"
            clearable
            class="el-form-input-width"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="类型" prop="datasourceType">
          <el-select
            v-model="queryParams.datasourceType"
            placeholder="请选择类型"
            clearable
            class="el-form-input-width"
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
            v-model="queryParams.status"
            placeholder="请选择状态"
            clearable
            class="el-form-input-width"
          >
            <el-option
              v-for="opt in statusOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="环境" prop="envTag">
          <el-select
            v-model="queryParams.envTag"
            placeholder="请选择环境"
            clearable
            class="el-form-input-width"
          >
            <el-option
              v-for="opt in envTagOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
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
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns" />
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
        <el-table-column v-if="getColumnVisibility(1)" width="80" label="编号" align="center" prop="id">
          <template #default="scope">{{ scope.row.id || '-' }}</template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(2)"
          min-width="140"
          label="名称"
          align="left"
          prop="datasourceName"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">{{ scope.row.datasourceName || '-' }}</template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(3)"
          width="120"
          label="编码"
          align="center"
          prop="code"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">{{ scope.row.code || '-' }}</template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(4)"
          min-width="160"
          label="描述"
          align="left"
          prop="description"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">{{ scope.row.description || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" width="120" label="数据源大类" align="center" prop="datasourceCategory">
          <template #default="scope">{{ getCategoryLabel(scope.row.datasourceCategory) || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" width="120" label="数据源类型" align="center" prop="datasourceType">
          <template #default="scope">{{ scope.row.datasourceType || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" width="120" label="所属部门" align="center" prop="deptName" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">{{ scope.row.deptName || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" width="100" label="负责人" align="center" prop="owner">
          <template #default="scope">{{ scope.row.owner || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" width="90" label="环境标签" align="center" prop="envTag">
          <template #default="scope">{{ getEnvTagLabel(scope.row.envTag) || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" width="90" label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'enabled' ? 'success' : 'info'">
              {{ scope.row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(11)" width="110" label="最近测试结果" align="center" prop="lastTestResult">
          <template #default="scope">
            <el-tag v-if="scope.row.lastTestResult === 'success'" type="success">成功</el-tag>
            <el-tag v-else-if="scope.row.lastTestResult === 'fail'" type="danger">失败</el-tag>
            <span v-else>{{ scope.row.lastTestResult === 'untested' ? '未测试' : (scope.row.lastTestResult || '-') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(12)" width="160" label="最近测试时间" align="center" prop="lastTestTime">
          <template #default="scope">{{ parseTime(scope.row.lastTestTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(13)" width="100" label="最近测试耗时" align="center" prop="lastTestDuration">
          <template #default="scope">{{ scope.row.lastTestDuration || '-' }}</template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" width="100" label="创建人" align="center" prop="createBy">
          <template #default="scope">{{ scope.row.createBy || '-' }}</template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(15)"
          width="160"
          label="创建时间"
          align="center"
          prop="createTime"
          sortable="custom"
          :sort-orders="['descending', 'ascending']"
        >
          <template #default="scope">{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(16)"
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="280"
        >
          <template #default="scope">
            <el-button link type="primary" icon="Connection" @click="handleTest(scope.row)" v-hasPermi="['dpp:datasource:edit']">
              连接测试
            </el-button>
            <el-button link type="primary" @click="handleToggleStatus(scope.row)" v-hasPermi="['dpp:datasource:edit']">
              {{ scope.row.status === 'enabled' ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="primary" icon="View" @click="handleDetail(scope.row)" v-hasPermi="['dpp:datasource:query']">
              详情
            </el-button>
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['dpp:datasource:edit']">
              修改
            </el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['dpp:datasource:remove']">
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
    <el-dialog :title="title" v-model="open" width="900px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">{{ title }}</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent>
        <div class="form-section-title">基础信息</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称" prop="datasourceName">
              <el-input v-model="form.datasourceName" placeholder="请输入名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入编码（唯一）" :disabled="!!form.id" maxlength="64" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源大类" prop="datasourceCategory">
              <el-select v-model="form.datasourceCategory" placeholder="请选择大类" :disabled="!!form.id" @change="onCategoryChange" style="width: 100%">
                <el-option v-for="opt in categoryOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="datasourceType">
              <el-select v-model="form.datasourceType" placeholder="请选择类型" :disabled="!!form.id" style="width: 100%">
                <el-option v-for="opt in filteredTypeOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入描述" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入所属部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="owner">
              <el-input v-model="form.owner" placeholder="请输入负责人" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="环境标签" prop="envTag">
              <el-select v-model="form.envTag" placeholder="请选择环境" style="width: 100%">
                <el-option v-for="opt in envTagOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="version">
              <el-input v-model="form.version" placeholder="请输入版本号" maxlength="32" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="enabled">启用</el-radio>
                <el-radio label="disabled">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 数据库类连接配置 -->
        <template v-if="showDbConnection">
          <div class="form-section-title">连接配置</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="主机地址" prop="host">
                <el-input v-model="form.host" placeholder="请输入主机地址" maxlength="256" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口" prop="port">
                <el-input-number v-model="form.port" :min="1" :max="65535" placeholder="端口" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="数据库名/服务名" prop="dbName">
                <el-input v-model="form.dbName" placeholder="请输入数据库名或服务名" maxlength="128" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Schema" prop="schema">
                <el-input v-model="form.schema" placeholder="PG/Oracle 专用" maxlength="64" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" maxlength="64" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="form.password"
                  type="password"
                  :placeholder="form.id ? '不修改请留空' : '请输入密码'"
                  maxlength="128"
                  show-word-limit
                  autocomplete="new-password"
                />
                <span v-if="form.id" class="form-tip">留空表示不修改，否则覆盖原密码</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="驱动类" prop="driverClass">
                <el-input v-model="form.driverClass" placeholder="可选" maxlength="256" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="JDBC URL 后缀" prop="jdbcSuffix">
                <el-input v-model="form.jdbcSuffix" placeholder="如 useSSL=false" maxlength="512" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="最大连接数" prop="maxPoolSize">
                <el-input-number v-model="form.maxPoolSize" :min="1" :max="1000" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="连接超时(ms)" prop="connTimeout">
                <el-input-number v-model="form.connTimeout" :min="0" :max="300000" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <!-- 文件/API 类连接配置 -->
        <template v-if="showFileApiConnection">
          <div class="form-section-title">连接配置</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Base URL" prop="baseUrl">
                <el-input v-model="form.baseUrl" placeholder="接口或存储地址" maxlength="512" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="根路径" prop="rootPath">
                <el-input v-model="form.rootPath" placeholder="根路径" maxlength="256" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <template v-if="form.datasourceCategory === 'fileStorage'">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="主机地址" prop="host">
                  <el-input v-model="form.host" placeholder="FTP/SFTP 主机" maxlength="256" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="端口" prop="port">
                  <el-input-number v-model="form.port" :min="1" :max="65535" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="form.username" placeholder="用户名" maxlength="64" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="密码" prop="password">
                  <el-input
                    v-model="form.password"
                    type="password"
                    :placeholder="form.id ? '不修改请留空' : '请输入密码'"
                    maxlength="128"
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="认证方式" prop="authType">
                <el-select v-model="form.authType" placeholder="请选择" style="width: 100%">
                  <el-option label="AccessKey/SecretKey" value="aksk" />
                  <el-option label="Token" value="token" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <template v-if="form.authType === 'aksk'">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="AccessKey" prop="accessKey">
                  <el-input v-model="form.accessKey" placeholder="AccessKey" maxlength="128" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Secret Key" prop="secretKey">
                  <el-input
                    v-model="form.secretKey"
                    type="password"
                    :placeholder="form.id ? '不修改请留空' : '请输入 Secret Key'"
                    maxlength="256"
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>
          <template v-if="form.authType === 'token'">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="Token" prop="token">
                  <el-input
                    v-model="form.token"
                    type="password"
                    :placeholder="form.id ? '不修改请留空' : '请输入 Token'"
                    maxlength="512"
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>
        </template>

        <el-row :gutter="20" v-if="open">
          <el-col :span="24">
            <el-form-item>
              <el-button type="primary" @click="handleTestInDialog" :loading="testLoading">连接测试</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情弹窗（el-descriptions） -->
    <el-dialog title="数据源详情" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <el-descriptions :column="2" border>
        <el-descriptions-item
          v-for="(item, index) in detailDescList"
          :key="index"
          label-class-name="base-label"
          :span="item.span || 1"
          class-name="base-content"
        >
          <template #label>
            <div class="cell-item">{{ item.label }}</div>
          </template>
          <div v-if="item.type === 'datetime'">{{ parseTime(detailData[item.key], '{y}-{m}-{d} {h}:{i}') || '-' }}</div>
          <div v-else-if="item.key === 'status'">{{ detailData[item.key] === 'enabled' ? '启用' : '禁用' }}</div>
          <div v-else-if="item.key === 'datasourceCategory'">{{ getCategoryLabel(detailData[item.key]) || '-' }}</div>
          <div v-else-if="item.key === 'envTag'">{{ getEnvTagLabel(detailData[item.key]) || '-' }}</div>
          <div v-else-if="item.key === 'lastTestResult'">
            {{ detailData[item.key] === 'success' ? '成功' : detailData[item.key] === 'fail' ? '失败' : (detailData[item.key] === 'untested' ? '未测试' : (detailData[item.key] || '-')) }}
          </div>
          <div v-else-if="item.sensitive">{{ detailData[item.key] ? '******' : '-' }}</div>
          <div v-else>{{ detailData[item.key] ?? '-' }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="openDetail = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Datasource">
import { listDatasource, getDatasource, addDatasource, updateDatasource, removeDatasource, testDatasource, toggleDatasourceStatus } from '@/api/dpp/datasource/datasource.mock';

const { proxy } = getCurrentInstance();
const datasourceList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const open = ref(false);
const openDetail = ref(false);
const title = ref('');
const submitLoading = ref(false);
const testLoading = ref(false);
const defaultSort = ref({ prop: 'createTime', order: 'descending' });

const columns = ref([
  { key: 1, label: '编号', visible: true },
  { key: 2, label: '名称', visible: true },
  { key: 3, label: '编码', visible: true },
  { key: 4, label: '描述', visible: true },
  { key: 5, label: '数据源大类', visible: true },
  { key: 6, label: '数据源类型', visible: true },
  { key: 7, label: '所属部门', visible: true },
  { key: 8, label: '负责人', visible: true },
  { key: 9, label: '环境标签', visible: true },
  { key: 10, label: '状态', visible: true },
  { key: 11, label: '最近测试结果', visible: true },
  { key: 12, label: '最近测试时间', visible: true },
  { key: 13, label: '最近测试耗时', visible: true },
  { key: 14, label: '创建人', visible: true },
  { key: 15, label: '创建时间', visible: true },
  { key: 16, label: '操作', visible: true }
]);

function getColumnVisibility(key) {
  const column = columns.value.find((col) => col.key === key);
  return column ? column.visible : true;
}

const categoryOptions = [
  { label: '关系型数据库', value: 'relationDb' },
  { label: '大数据/数仓', value: 'bigData' },
  { label: '文件/对象存储', value: 'fileStorage' },
  { label: '接口类型', value: 'api' }
];

const datasourceTypeOptions = [
  { label: 'MySQL', value: 'MySQL' },
  { label: 'Oracle', value: 'Oracle' },
  { label: 'PostgreSQL', value: 'PostgreSQL' },
  { label: 'SQL Server', value: 'SQL Server' },
  { label: 'DB2', value: 'DB2' },
  { label: 'Doris', value: 'Doris' },
  { label: 'Hive', value: 'Hive' },
  { label: 'ClickHouse', value: 'ClickHouse' },
  { label: 'MaxCompute', value: 'MaxCompute' },
  { label: 'FTP', value: 'FTP' },
  { label: 'SFTP', value: 'SFTP' },
  { label: 'HDFS', value: 'HDFS' },
  { label: 'S3兼容', value: 'S3' },
  { label: 'Restful API', value: 'Restful API' }
];

const typeByCategory = {
  relationDb: ['MySQL', 'Oracle', 'PostgreSQL', 'SQL Server', 'DB2'],
  bigData: ['Doris', 'Hive', 'ClickHouse', 'MaxCompute'],
  fileStorage: ['FTP', 'SFTP', 'HDFS', 'S3'],
  api: ['Restful API']
};

const filteredTypeOptions = computed(() => {
  const cat = form.datasourceCategory;
  if (!cat) return datasourceTypeOptions;
  const types = typeByCategory[cat];
  return datasourceTypeOptions.filter((opt) => types.includes(opt.value));
});

const statusOptions = [
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' }
];

const envTagOptions = [
  { label: '开发', value: 'dev' },
  { label: '测试', value: 'test' },
  { label: '生产', value: 'prod' }
];

function getCategoryLabel(val) {
  const opt = categoryOptions.find((o) => o.value === val);
  return opt ? opt.label : val;
}
function getEnvTagLabel(val) {
  const opt = envTagOptions.find((o) => o.value === val);
  return opt ? opt.label : val;
}

const showDbConnection = computed(() => {
  const cat = form.datasourceCategory;
  return cat === 'relationDb' || cat === 'bigData';
});
const showFileApiConnection = computed(() => {
  const cat = form.datasourceCategory;
  return cat === 'fileStorage' || cat === 'api';
});

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  datasourceName: undefined,
  code: undefined,
  datasourceType: undefined,
  status: undefined,
  envTag: undefined,
  deptId: undefined
});

const form = ref({});
const formRef = ref(null);
const queryRef = ref(null);

const rules = ref({
  datasourceName: [
    { required: true, message: '名称不能为空', trigger: 'blur' },
    { max: 100, message: '名称长度不能超过100个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '编码不能为空', trigger: 'blur' },
    { max: 64, message: '编码长度不能超过64个字符', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_\-]+$/, message: '编码只能包含英文、数字、下划线和中划线', trigger: 'blur' }
  ],
  datasourceCategory: [{ required: true, message: '请选择数据源大类', trigger: 'change' }],
  datasourceType: [{ required: true, message: '请选择数据源类型', trigger: 'change' }],
  description: [{ max: 500, message: '描述长度不能超过500个字符', trigger: 'blur' }],
  owner: [{ max: 50, message: '负责人长度不能超过50个字符', trigger: 'blur' }],
  version: [{ max: 32, message: '版本号长度不能超过32个字符', trigger: 'blur' }],
  port: [{ type: 'number', min: 1, max: 65535, message: '端口号范围 1-65535', trigger: 'blur', transform: Number }],
  host: [{ max: 256, message: '主机地址长度不能超过256个字符', trigger: 'blur' }],
  dbName: [{ max: 128, message: '长度不能超过128个字符', trigger: 'blur' }],
  schema: [{ max: 64, message: 'Schema长度不能超过64个字符', trigger: 'blur' }],
  username: [{ max: 64, message: '用户名长度不能超过64个字符', trigger: 'blur' }],
  password: [{ max: 128, message: '密码长度不能超过128个字符', trigger: 'blur' }],
  driverClass: [{ max: 256, message: '驱动类长度不能超过256个字符', trigger: 'blur' }],
  jdbcSuffix: [{ max: 512, message: '长度不能超过512个字符', trigger: 'blur' }],
  maxPoolSize: [{ type: 'number', min: 1, max: 1000, message: '范围 1-1000', trigger: 'blur', transform: Number }],
  connTimeout: [{ type: 'number', min: 0, max: 300000, message: '单位毫秒，范围 0-300000', trigger: 'blur', transform: Number }],
  baseUrl: [{ max: 512, message: 'Base URL长度不能超过512个字符', trigger: 'blur' }],
  rootPath: [{ max: 256, message: '根路径长度不能超过256个字符', trigger: 'blur' }],
  accessKey: [{ max: 128, message: 'AccessKey长度不能超过128个字符', trigger: 'blur' }],
  secretKey: [{ max: 256, message: 'Secret Key长度不能超过256个字符', trigger: 'blur' }],
  token: [{ max: 512, message: 'Token长度不能超过512个字符', trigger: 'blur' }]
});

const detailData = ref({});
const detailDescList = ref([
  { key: 'datasourceName', label: '名称' },
  { key: 'code', label: '编码' },
  { key: 'description', label: '描述', span: 2 },
  { key: 'datasourceCategory', label: '数据源大类' },
  { key: 'datasourceType', label: '数据源类型' },
  { key: 'deptName', label: '所属部门' },
  { key: 'owner', label: '负责人' },
  { key: 'envTag', label: '环境标签' },
  { key: 'version', label: '版本号' },
  { key: 'status', label: '状态' },
  { key: 'host', label: '主机地址' },
  { key: 'port', label: '端口' },
  { key: 'dbName', label: '数据库名/服务名' },
  { key: 'schema', label: 'Schema' },
  { key: 'username', label: '用户名' },
  { key: 'password', label: '密码', sensitive: true },
  { key: 'baseUrl', label: 'Base URL', span: 2 },
  { key: 'rootPath', label: '根路径' },
  { key: 'lastTestResult', label: '最近测试结果' },
  { key: 'lastTestTime', label: '最近测试时间', type: 'datetime' },
  { key: 'lastTestDuration', label: '最近测试耗时' },
  { key: 'createBy', label: '创建人' },
  { key: 'createTime', label: '创建时间', type: 'datetime' }
]);

function onCategoryChange() {
  form.value.datasourceType = undefined;
}

function getList() {
  loading.value = true;
  listDatasource(queryParams.value)
    .then((res) => {
      const data = res.data || res;
      datasourceList.value = Array.isArray(data.rows) ? data.rows : (data.list || []);
      total.value = data.total ?? datasourceList.value.length;
    })
    .finally(() => (loading.value = false));
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}
function handleSortChange({ prop, order }) {
  defaultSort.value = { prop: prop || 'createTime', order: order === 'ascending' ? 'ascending' : 'descending' };
  getList();
}
function handleSelectionChange(selection) {}

function resetFormData() {
  form.value = {
    id: undefined,
    datasourceName: '',
    code: '',
    description: '',
    datasourceCategory: undefined,
    datasourceType: undefined,
    deptName: '',
    owner: '',
    envTag: undefined,
    version: '',
    status: 'enabled',
    host: undefined,
    port: undefined,
    dbName: '',
    schema: '',
    username: '',
    password: '',
    driverClass: '',
    jdbcSuffix: '',
    maxPoolSize: undefined,
    connTimeout: undefined,
    baseUrl: '',
    rootPath: '',
    authType: undefined,
    accessKey: '',
    secretKey: '',
    token: ''
  };
}

function handleAdd() {
  resetFormData();
  title.value = '新增数据源';
  open.value = true;
}
function handleUpdate(row) {
  resetFormData();
  const id = row.id;
  getDatasource(id).then((res) => {
    const data = res.data || res;
    form.value = { ...data };
    form.value.password = '';
    form.value.secretKey = '';
    form.value.token = '';
  });
  title.value = '编辑数据源';
  open.value = true;
}
function cancel() {
  open.value = false;
  resetFormData();
}
function submitForm() {
  formRef.value?.validate((valid) => {
    if (!valid) return;
    submitLoading.value = true;
    const data = { ...form.value };
    if (data.id && !data.password) delete data.password;
    if (data.id && !data.secretKey) delete data.secretKey;
    if (data.id && !data.token) delete data.token;
    const fn = data.id ? updateDatasource : addDatasource;
    fn(data)
      .then(() => {
        proxy.$modal.msgSuccess(data.id ? '修改成功' : '新增成功');
        open.value = false;
        getList();
      })
      .finally(() => (submitLoading.value = false));
  });
}

function handleDetail(row) {
  getDatasource(row.id).then((res) => {
    const data = res.data || res;
    detailData.value = { ...data };
  });
  openDetail.value = true;
}

function handleTest(row) {
  testDatasource(row.id)
    .then((res) => {
      const data = res.data || res;
      if (data && data.success !== false) {
        proxy.$modal.msgSuccess(data.message || '连接成功');
      } else {
        proxy.$modal.msgError(data?.message || '连接失败');
      }
      getList();
    })
    .catch((err) => {
      proxy.$modal.msgError(err?.msg || err?.message || '连接测试失败');
      getList();
    });
}
function handleTestInDialog() {
  if (!form.value.id) {
    proxy.$modal.msgWarning('请先保存后再测试连接');
    return;
  }
  testLoading.value = true;
  testDatasource(form.value.id)
    .then((res) => {
      const data = res.data || res;
      if (data && data.success !== false) {
        proxy.$modal.msgSuccess(data.message || '连接成功');
      } else {
        proxy.$modal.msgError(data?.message || '连接失败');
      }
      getList();
    })
    .catch((err) => {
      proxy.$modal.msgError(err?.msg || err?.message || '连接测试失败');
    })
    .finally(() => (testLoading.value = false));
}

function handleToggleStatus(row) {
  const nextStatus = row.status === 'enabled' ? 'disabled' : 'enabled';
  const action = nextStatus === 'enabled' ? '启用' : '禁用';
  proxy.$modal
    .confirm('确认要' + action + '该数据源吗？')
    .then(() => {
      return toggleDatasourceStatus(row.id, nextStatus);
    })
    .then(() => {
      proxy.$modal.msgSuccess('操作成功');
      getList();
    })
    .catch(() => {});
}

function handleDelete(row) {
  const id = row.id;
  proxy.$modal
    .confirm('是否确认删除该数据源？删除前将校验是否被数据资产、元数据采集任务、ETL开发任务、数据质量规则引用。')
    .then(() => removeDatasource(id))
    .then((res) => {
      const msg = (res && res.msg) || '删除成功';
      proxy.$modal.msgSuccess(msg);
      getList();
    })
    .catch((err) => {
      if (err && (err.msg || err.message)) {
        proxy.$modal.msgError(err.msg || err.message);
      }
    });
}

onMounted(() => {
  getList();
});
</script>

<style lang="scss" scoped>
.form-section-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}
.form-tip {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-left: 8px;
}
:deep(.base-label) {
  width: 200px;
  .cell-item {
    font-weight: 500;
  }
}
</style>
