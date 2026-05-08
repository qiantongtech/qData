<template>
  <div class="app-container" v-loading="store.loading">
    <div class="pagecont-top-wrap">
      <div class="infotop">
        <div class="infotop-title mb15 clearfixs">
          <div class="task-item">
            <div class="task-id">
              {{ getFormatValue(form.id) }}
            </div>
            <div class="task-name">
              {{ getFormatValue(form.dbName) }}
            </div>
            <div>
              <dict-tag
                :options="toValue(dicts.meta_task_status)"
                :value="form.status"
              />
            </div>
          </div>
          <div class="btn-style">
            <el-button
              type="primary"
              plain
              class="fh_btn"
              @mousedown="(e) => e.preventDefault()"
              @click="router.back"
            >
              <svg-icon iconClass="fhs" />返回
            </el-button>
          </div>
        </div>

        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">业务域</div>
              <div class="infotop-row-value">
                {{ getDomainPath(form.domainId) }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据库类型</div>
              <div class="infotop-row-value">
                <dict-tag
                  :options="toValue(dicts.datasource_type)"
                  :value="form.dbType"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">版本号</div>
              <div class="infotop-row-value">
                {{ formatVersion(form.version) }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="store.tab" @tab-change="handleTabChange">
        <el-tab-pane
          v-for="tab in tabData"
          :label="tab.label"
          :name="tab.key"
          :key="tab.key"
        />
      </el-tabs>

      <component
        v-if="!store.loading"
        :is="tabComponent[store.tab]"
        :detail="store.form"
      />
    </div>
  </div>
</template>
<script setup name="DatabaseDetail">
import { computed, getCurrentInstance, reactive, toValue } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getDb } from "@/api/mc/unreleased/db.js";
import { listDomain } from "@/api/att/domain/domain.js";
import { getParentLabelPath } from "@/utils/anivia.js";

const tabData = [
  {
    key: "BaseInfo",
    label: "基本信息",
  },
  {
    key: "TableList",
    label: "表列表",
  },
  {
    key: "VersionManagement",
    label: "版本与变更",
  },
];
const tabComponent = {
  BaseInfo: defineAsyncComponent(() => import("./BaseInfo.vue")),
  TableList: defineAsyncComponent(() => import("./TableList.vue")),
  VersionManagement: defineAsyncComponent(() =>
    import("./VersionManagement.vue")
  ),
};

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode",
  "meta_task_status"
);

const router = useRouter();
const route = useRoute();
if (!route.query.id) router.go(-1);

const store = reactive({
  loading: false,
  form: {},
  treeDomains: [],
  domains: [],
  tab: route.query.tab || "BaseInfo",
});

const form = computed(() => store.form);

// 获取详情
function getDetail() {
  store.loading = true;
  getDb(route.query.id).then((res) => {
    const datasource = res.data?.datasource;
    if (datasource !== null) {
      if (datasource.datasourceConfig) {
        datasource.datasourceConfig = JSON.parse(datasource.datasourceConfig);
      }
      res.data.username = datasource?.datasourceConfig?.username;
    }
    store.form = res.data;
    store.loading = false;
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

// 获取业务域列表
function getDomains() {
  listDomain().then((res) => {
    store.domains = [...res.data];
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.treeDomains.push(domains);
  });
}

// 切换tab
function handleTabChange(tab) {
  router.push({
    query: {
      ...route.query,
      tab,
    },
  });
}

// getDomains();
getDetail();
</script>
