<template>
  <div class="app-container">
    <guide-tip tip-id="meta/retrieve" />
    <qt-wrap :config="{ fullContent: false, actions: { show: false } }">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          @query="getList"
          @reset="getList"
          :config="{ permi: ['md:retrieve:query'] }"
        />
      </template>

      <div class="meta-list-wrap" v-loading="store.loading">
        <template v-if="store.data.length">
          <div class="meta-list">
            <div class="meta-item" v-for="meta in store.data" :key="meta.id">
              <div class="head-wrap">
                <div class="head-desc">
                  <div class="desc-item title">
                    {{ meta.name }}
                  </div>
                  <div class="desc-item tag type">
                    <div class="desc-icon" v-if="meta.type_config.icon">
                      <svg-icon :icon-class="meta.type_config.icon"></svg-icon>
                    </div>
                    {{ meta.type_config?.abbreviate }}
                  </div>
                  <div
                    :class="[
                      'desc-item',
                      'tag',
                      'status',
                      meta.status == '1' ? 'publish' : '',
                    ]"
                  >
                    {{ meta.status == "1" ? "已发布" : "未发布" }}
                  </div>
                </div>
                <div class="head-info">
                  <div
                    class="info-item"
                    v-for="info in meta.infos"
                    :key="info.key"
                    v-show="info.value != undefined && info.value != ''"
                  >
                    <div class="info-icon" v-if="info.icon">
                      <svg-icon :icon-class="info.icon"></svg-icon>
                    </div>
                    <div class="info-value">
                      {{ info.value }}{{ info.unit }}
                    </div>
                  </div>
                </div>
              </div>
              <div class="body-wrap">
                <el-form lable-width="auto" class="body-form">
                  <!-- 库 -->
                  <template v-if="meta.mdDbDO">
                    <el-form-item label="数据连接类型：">
                      <dict-tag
                        :options="toValue(dicts.datasource_type)"
                        :value="meta.mdDbDO?.dbType"
                      />
                    </el-form-item>

                    <el-form-item label="IP：">
                      {{ getFormatValue(meta.mdDbDO?.ip) }}
                    </el-form-item>

                    <el-form-item label="端口：">
                      {{ getFormatValue(meta.mdDbDO?.port) }}
                    </el-form-item>

                    <el-form-item label="账号：">
                      {{ getFormatValue(meta.mdDbDO?.username) }}
                    </el-form-item>

                    <el-form-item
                      :label="item.label + '：'"
                      v-for="item in TYPE_BASE_DETAIL"
                      :key="item.key"
                      :class="item.class"
                    >
                      {{ getFormatValue(meta.mdDbDO[item.key]) }}
                    </el-form-item>
                  </template>
                  <!-- 表 -->
                  <template v-if="meta.mdTableRespVO">
                    <el-form-item label="表注释：">
                      {{ getFormatValue(meta.mdTableRespVO?.tableComment) }}
                    </el-form-item>
                    <el-form-item label="业务域：">
                      {{
                        getDomainPath(meta.mdTableRespVO?.dbRespVO?.domainId)
                      }}
                    </el-form-item>
                    <el-form-item label="所属分层：">
                      <dict-tag
                        :options="toValue(dicts.meta_dw_layers)"
                        :value="meta.mdTableRespVO?.dbRespVO?.belongingLayer"
                      />
                    </el-form-item>
                    <el-form-item label="是否主表：">
                      <dict-tag
                        :options="toValue(dicts.table_yes_no)"
                        :value="meta.mdTableRespVO.masterFlag"
                      />
                    </el-form-item>

                    <el-form-item
                      :label="item.label + '：'"
                      v-for="item in TYPE_BASE_DETAIL"
                      :key="item.key"
                      :class="item.class"
                    >
                      {{ getFormatValue(meta.mdTableRespVO[item.key]) }}
                    </el-form-item>
                  </template>
                  <!-- 字段 -->
                  <template v-if="meta.mdColumnDO">
                    <el-form-item label="字段注释：">
                      {{ getFormatValue(meta.mdColumnDO?.columnComment) }}
                    </el-form-item>

                    <el-form-item label="业务域：">
                      {{ meta.mdColumnDO.domainName || "-" }}
                    </el-form-item>

                    <el-form-item label="是否主键：">
                      <dict-tag
                        :options="toValue(dicts.table_yes_no)"
                        :value="meta.mdColumnDO.pkFlag"
                      />
                    </el-form-item>

                    <el-form-item label="是否外键：">
                      <dict-tag
                        :options="toValue(dicts.table_yes_no)"
                        :value="meta.mdColumnDO.fkFlag"
                      />
                    </el-form-item>

                    <el-form-item
                      :label="item.label + '：'"
                      v-for="item in TYPE_BASE_DETAIL"
                      :key="item.key"
                      :class="item.class"
                    >
                      {{ getFormatValue(meta.mdColumnDO[item.key]) }}
                    </el-form-item>
                  </template>
                </el-form>
              </div>
              <div class="footer-wrap">
                <!-- 库 -->
                <template v-if="meta.mdDbDO">
                  <div
                    class="btn-item"
                    @click="go('db/detail', { id: meta.id })"
                    v-hasPermi="['md:unreleased:structured:db:detail']"
                  >
                    <el-icon color="var(--el-color-primary)">
                      <View />
                    </el-icon>
                    {{ t('common.button.details') }}
                  </div>

                  <div
                    class="btn-item"
                    @click="go('db/detail', { id: meta.id, tab: 'TableList' })"
                    v-hasPermi="['md:unreleased:structured:db:detail']"
                  >
                    <svg-icon icon-class="meta-table"></svg-icon>
                    表列表
                  </div>

                  <div
                    class="btn-item"
                    @click="
                      go('db/detail', {
                        id: meta.id,
                        tab: 'VersionManagement',
                      })
                    "
                    v-hasPermi="['md:unreleased:structured:db:detail']"
                  >
                    <svg-icon icon-class="meta-version"></svg-icon>
                    版本与变更
                  </div>
                </template>

                <!-- 表 -->
                <template v-if="meta.mdTableRespVO">
                  <div
                    class="btn-item"
                    @click="go('table/detail', { id: meta.id })"
                    v-hasPermi="['md:unreleased:structured:table:detail']"
                  >
                    <el-icon color="var(--el-color-primary)">
                      <View />
                    </el-icon>
                    {{ t('common.button.details') }}
                  </div>

                  <div
                    class="btn-item"
                    @click="
                      go('table/detail', {
                        id: meta.id,
                        tab: 'ColumnList',
                      })
                    "
                    v-hasPermi="['md:unreleased:structured:table:detail']"
                  >
                    <svg-icon icon-class="meta-column"> </svg-icon>
                    字段列表
                  </div>

                  <el-dropdown popper-class="more-dropdown">
                    <div
                      class="btn-item"
                      v-hasPermi="['md:unreleased:structured:table:detail']"
                    >
                      <svg-icon icon-class="arrow-down"> </svg-icon>
                      <span>{{ t('common.button.more') }}</span>
                    </div>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <!-- <el-dropdown-item
                          @click="
                            go('table/detail', {
                              id: meta.id,
                              tab: 'ImpactAnalysis',
                            })
                          "
                          v-hasPermi="['md:unreleased:structured:table:detail']"
                        >
                          <el-text type="primary">
                            <svg-icon icon-class="meta-impact"> </svg-icon>
                            影响分析
                          </el-text>
                        </el-dropdown-item> -->
                        <el-dropdown-item
                          @click="
                            go('table/detail', {
                              id: meta.id,
                              tab: 'LineageAnalysis',
                            })
                          "
                          v-hasPermi="['md:unreleased:structured:table:detail']"
                        >
                          <el-text type="primary">
                            <svg-icon icon-class="meta-lineage"></svg-icon>
                            血缘分析
                          </el-text>
                        </el-dropdown-item>
                        <el-dropdown-item
                          @click="
                            go('table/detail', {
                              id: meta.id,
                              tab: 'VersionManagement',
                            })
                          "
                          v-hasPermi="['md:unreleased:structured:table:detail']"
                        >
                          <el-text type="primary">
                            <svg-icon icon-class="meta-version"> </svg-icon>
                            版本与变更
                          </el-text>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </template>

                <!-- 字段 -->
                <template v-if="meta.mdColumnDO">
                  <div
                    class="btn-item"
                    @click="
                      go('column/detail', {
                        id: meta.id,
                      })
                    "
                  >
                    <el-icon color="var(--el-color-primary)">
                      <View />
                    </el-icon>
                    {{ t('common.button.details') }}
                  </div>

                  <div
                    class="btn-item"
                    @click="
                      go('column/detail', {
                        id: meta.id,
                        tab: 'LineageAnalysis',
                      })
                    "
                  >
                    <svg-icon icon-class="meta-lineage"></svg-icon>
                    血缘分析
                  </div>

                  <el-dropdown popper-class="more-dropdown">
                    <div class="btn-item">
                      <svg-icon icon-class="arrow-down"> </svg-icon>
                      <span>{{ t('common.button.more') }}</span>
                    </div>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          @click="
                            go('column/detail', {
                              id: meta.id,
                              tab: 'ImpactAnalysis',
                            })
                          "
                          v-hasPermi="[
                            'md:unreleased:structured:column:detail',
                          ]"
                        >
                          <el-text type="primary">
                            <svg-icon icon-class="meta-impact"> </svg-icon>
                            影响分析
                          </el-text>
                        </el-dropdown-item>
                        <el-dropdown-item
                          @click="
                            go('column/detail', {
                              id: meta.id,
                              tab: 'VersionManagement',
                            })
                          "
                          v-hasPermi="[
                            'md:unreleased:structured:column:detail',
                          ]"
                        >
                          <el-text type="primary">
                            <svg-icon icon-class="meta-version"> </svg-icon>
                            版本与变更
                          </el-text>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </template>
              </div>
            </div>
          </div>
          <pagination
            v-show="store.total > 0"
            :total="store.total"
            v-model:page="searchStore.params.pageNum"
            v-model:limit="searchStore.params.pageSize"
            @pagination="getList"
          />
        </template>

        <div class="emptyBg" v-else>
          <img src="@/assets/images/sys/error/no-data.png" alt="" />
          <p>{{ t('common.message.noRecord') }}</p>
        </div>
      </div>
    </qt-wrap>
  </div>
</template>

<script setup name="MetaRetrieve">
import { useI18n } from 'vue-i18n'
import { getCurrentInstance, reactive, toValue } from "vue";
import { listMeta } from "@/api/mc/retrieve.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { listDomain } from "@/api/att/domain/domain.js";
import { useRouter } from "vue-router";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { t } = useI18n();
const BASE_URL = "/meta/unreleased/structured/";

const TYPE_OPTIONS = [
  {
    label: "库元数据",
    value: "1",
    abbreviate: "库",
    field: "mdDbDO",
    icon: "meta-database",
  },
  {
    label: "表元数据",
    value: "2",
    abbreviate: "表",
    field: "mdTableRespVO",
    icon: "meta-table",
  },
  {
    label: "字段元数据",
    value: "3",
    abbreviate: "字段",
    field: "mdColumnDO",
    icon: "meta-column",
  },
];

const TYPE_INFOS = {
  mdDbDO: [
    {
      label: "表数量",
      key: "mdDbDO.tableCount",
      icon: "meta-table-count",
    },
    {
      label: "字段数量",
      key: "mdDbDO.columnCount",
      icon: "meta-column-count",
    },
  ],
  mdTableRespVO: [
    {
      label: "行数量",
      key: "mdTableRespVO.columnCount",
      icon: "meta-column-count",
    },
    {
      label: "数据质量",
      key: "mdTableRespVO.dataQuality",
      unit: "分",
      icon: "meta-table-quality",
    },
    {
      label: "版本",
      key: "mdTableRespVO.version",
      icon: "meta-flag-version",
    },
    {
      label: "所属系统",
      key: "mdTableRespVO.dbRespVO.belongingSystem",
      icon: "meta-table-system",
    },
  ],
  mdColumnDO: [
    {
      label: "字段类型",
      key: "mdColumnDO.columnType",
      icon: "meta-column-type",
    },
    {
      label: "字段长度",
      key: "mdColumnDO.columnLength",
      icon: "meta-column-length",
    },
    {
      label: "字段精度",
      key: "mdColumnDO.columnPrecision",
      icon: "meta-column-precision",
    },
    {
      label: "字段小数位",
      key: "mdColumnDO.columnScale",
      icon: "meta-column-scale",
    },
  ],
};

const TYPE_BASE_DETAIL = [
  {
    label: t('common.texts.createdBy'),
    key: "createBy",
  },
  {
    label: t('common.texts.createdTime'),
    key: "createTime",
  },
  {
    label: t('common.texts.updatedBy'),
    key: "updateBy",
  },
  {
    label: t('common.texts.updatedTime'),
    key: "updateTime",
  },
  {
    label: t('common.texts.description'),
    key: "description",
    class: "row-full",
  },
];

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "meta_task_status",
  "meta_dw_layers",
  "table_yes_no"
);
const router = useRouter();

const store = reactive({
  data: [],
  total: 0,
  treeDomains: [],
  loading: false,
});

const searchStore = reactive({
  items: [
    {
      label: "元数据名称",
      prop: "keyword",
      component: {
        is: "input",
      },
    },
    {
      label: "元数据类型",
      prop: "types",
      component: {
        is: "select",
        options: [...TYPE_OPTIONS],
      },
    },
    {
      label: "数据源类型",
      prop: "dbTypes",
      component: {
        is: "select",
        options: dicts.datasource_type,
      },
    },
    {
      label: "修改时间",
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: computed(() => td("common.form.startDatePlaceholder")),
        endPlaceholder: computed(() => td("common.form.endDatePlaceholder")),
      },
    },
  ],
  params: {
    pageNum: 1,
    pageSize: 10,
    orderByColumn: "createTime",
    isAsc: "descending",
  },
});

// 格式化参数
function formatParams(params) {
  if (!params.time || !params.time.length) return params;
  const { time, ...other } = { ...params };
  other.createTimeStart = time[0];
  other.createTimeEnd = time[1];
  return other;
}

// 查找数据
function getNestedValue(obj, path) {
  return path.split(".").reduce((current, key) => {
    return current && current[key] !== undefined ? current[key] : "";
  }, obj);
}

// 获取业务域列表
function getDomains() {
  return listDomain().then((res) => {
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.treeDomains.push(domains);
    return res;
  });
}

// 获取业务域路径
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.treeDomains, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

// 获取列表
function getList() {
  const params = formatParams(searchStore.params);
  store.loading = true;
  listMeta(params).then((res) => {
    for (let i = 0; i < res.data.rows.length; i++) {
      const item = res.data.rows[i];
      if (item.mdDbDO?.datasource?.datasourceConfig) {
        const datasourceConfig = JSON.parse(
          item.mdDbDO.datasource.datasourceConfig
        );
        item.mdDbDO.username = datasourceConfig.username;
      }

      const type_config = TYPE_OPTIONS.find((e) => item.type == e.value);
      item.type_config = type_config;
      if (!TYPE_INFOS[type_config.field]) continue;
      const infos = [...TYPE_INFOS[type_config.field]];
      item.infos = infos.map((info) => {
        info.value = getNestedValue(item, info.key);
        if (info.key.includes("version")) {
          info.value = proxy.formatVersion(info.value);
        }
        return info;
      });
    }
    store.data = res.data.rows;
    store.total = res.data.total;
    store.loading = false;
  });
}

// go
function go(path, query) {
  router.push({
    path: BASE_URL + path,
    query,
  });
}

getList();
// getDomains();
</script>

<style lang="scss" scoped>
::v-deep(.qt-wrap--content) {
  background-color: transparent;
}

::v-deep(.pagination-container) {
  height: 60px;
  background: #fff;
  border-radius: 2px;
  margin: 15px 0 0;
  padding: 14px 20px !important;
  .el-pagination {
    right: 20px;
  }
}

.emptyBg {
  background-color: #fff;
}

.meta-item {
  padding: 18px 18px 14px;
  background: #fff;
  margin-bottom: 14px;
  border-radius: 2px;
}

.head-wrap {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #d3d8e0;
  line-height: 24px;
  padding-bottom: 10px;
  margin-bottom: 8px;
  .head-info,
  .head-desc {
    display: flex;
  }

  .info-item {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 10px;
    position: relative;
    font-size: 13px;
    color: #3d446e;
    &:last-child {
      padding-right: 0;
    }
    &::before {
      content: "";
      position: absolute;
      width: 1px;
      height: 12px;
      top: 50%;
      transform: translateY(-50%);
      right: 0;
      background-color: #c9cfd8;
    }

    &:last-child::before {
      display: none;
    }
  }

  .info-icon {
    margin-right: 4px;
    .svg-icon {
      font-size: 16px;
      display: block;
    }
  }

  .desc-item {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 8px;
    border-radius: 2px;
  }

  .desc-item.title {
    font-weight: 700;
    font-size: 16px;
    color: #3d446e;
    margin-right: 16px;
  }

  .desc-item.tag {
    padding: 0 8px;
    height: 24px;
    font-weight: 400;
    font-size: 12px;
    color: #fff;
  }

  .desc-item.type {
    background: #ecf9ff;
    border: 1px solid #91d5ff;
    color: var(--el-color-primary);
  }
  .desc-item.status {
    background: #ff9800;
  }
  .desc-item.status.publish {
    background: #0baa84;
  }

  .desc-icon {
    margin-right: 4px;
    .svg-icon {
      font-size: 13px;
    }
  }
}

.body-wrap {
  padding-bottom: 8px;
  .body-form {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    .row-full {
      grid-column: span 4;
    }
    ::v-deep(.el-form-item) {
      margin-bottom: 0;
      .el-form-item__label {
        font-size: 14px;
        color: #8c8c8c;
        padding-right: 0;
      }
      .el-form-item__content {
        font-size: 14px;
        color: #262626;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

.footer-wrap {
  display: flex;
  justify-content: flex-end;
  .btn-item {
    margin-right: 10px;
    cursor: pointer;
    height: 24px;
    background: #e8f1ff;
    border-radius: 2px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3d446e;
    font-weight: 400;
    font-size: 12px;
    width: fit-content;
    padding: 0 12px;
    &:last-child {
      margin-right: 0;
    }
    &.active {
      background: #e1f9fc;
      color: #039792;
    }
    .el-icon {
      margin-right: 2px;
      font-size: 14px;
    }
    .svg-icon {
      margin-right: 4px;
      font-size: 13px;
    }
  }
}

.more-dropdown {
  .el-text {
    .svg-icon,
    .el-icon {
      font-size: 14px;
    }
  }
}
</style>
