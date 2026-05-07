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
              {{ getFormatValue(form.name) }}
            </div>
            <div>
              <dict-tag
                :options="toValue(dicts.mc_task_status)"
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
              <div class="infotop-row-lable">来源系统</div>
              <div class="infotop-row-value">
                {{ form?.sourceSystemName || "--" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">{{ form.personChargeName }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人电话</div>
              <div class="infotop-row-value">{{ form.leaderPhone }}</div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ form.description }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">最近执行时间</div>
              <div class="infotop-row-value">{{ form.lastExecuteTime }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">下次执行时间</div>
              <div class="infotop-row-value">{{ form.createTime }}</div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0 0">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">{{ form.remark }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="store.tab">
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
<script setup name="Detail">
import { computed, getCurrentInstance, reactive, toValue } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getTask, sourceSystemTree } from "@/api/mc/task/task";
import { getParentLabelPath } from "@/utils/anivia.js";

const tabData = [
  {
    key: "BaseInfo",
    label: "基本信息",
  },
  {
    key: "CollectInstance",
    label: "采集实例",
  },
];
const tabComponent = {
  BaseInfo: defineAsyncComponent(() => import("./BaseInfo.vue")),
  CollectInstance: defineAsyncComponent(() => import("./CollectInstance.vue")),
};

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode",
  "mc_task_status"
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
  getTask(route.query.id).then((res) => {
    store.form = res.data;
    store.loading = false;
  });
}

// 获取来源系统路径
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

// 获取来源系统列表
function getSourceSystemTreeData() {
  sourceSystemTree().then((res) => {
    store.treeDomains = res.data;
  });
}

getSourceSystemTreeData();
getDetail();
</script>
