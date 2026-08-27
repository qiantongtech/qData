<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="ds-selector" v-loading="loading">
    <div class="ds-selector__sidebar">
      <DeptTree
        preset="simple"
        :deptOptions="processedData"
        :leftWidth="leftWidth"
        :placeholder="td('dpp.cleanRule.inputRuleType', 'Please enter rule type')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
        :showFilter="false"
      >
        <template #label="{ data }">
          <span class="tree-label-with-count">
            <span class="label-text">{{ data.name }}</span>
            <span class="count-text" v-if="data.id === ''">({{ allRulesCount }})</span>
            <span class="count-text" v-else>({{ getRulesCountByDimension(data.code || data.id) }})</span>
          </span>
        </template>
      </DeptTree>
    </div>
    <div class="ds-selector__main" ref="contentWrapper">
      <div class="ds-search">
        <el-input
          v-model="searchKeyword"
          :placeholder="td('dpp.cleanRule.search', 'Please enter what you want to search')"
          clearable
          class="ds-search__input"
          :prefix-icon="Search"
        />
      </div>

      <div v-if="visibleCategories.length > 0">
        <div
          v-for="(cat, index) in visibleCategories"
          :key="cat.id || cat.code"
          class="category-group"
          :class="{ 'is-first': index === 0 }"
        >
          <div :id="`category-${cat.id || cat.code}`" class="category-group__header">
            <span class="category-group__title">{{ cat.name }}</span>
          </div>
          <div class="ds-grid">
            <div
              v-for="data in getRulesByCategoryGroup(cat)"
              :key="data.id"
              class="ds-card"
              :class="{ 'ds-card--selected': selectedCard?.id === data.id, 'is-disabled': data.validFlag == false }"
              @click="cardClick(data)"
            >
              <div class="ds-card__check" v-if="selectedCard?.id === data.id">
                <el-icon><CircleCheckFilled /></el-icon>
              </div>
              <div class="ds-card__header">
                <div class="ds-card__icon" :class="{ 'is-disabled': data.validFlag == false }">
                <template v-if="data.validFlag == false">
                  <div v-if="getIcon(data.code)" class="rule-icon-mask" :style="{ '--icon-url': `url('${getIcon(data.code)}')` }"></div>
                  <el-icon v-else class="rule-icon-fallback">
                    <Document />
                  </el-icon>
                </template>
                <template v-else>
                  <img v-if="getIcon(data.code)" :src="getIcon(data.code)" class="rule-icon" />
                  <el-icon v-else>
                    <Document />
                  </el-icon>
                </template>
              </div>
                <div class="ds-card__title" :title="data.name">{{ data.name }}</div>
              </div>
              <div class="ds-card__desc" :title="data.description">{{ data.description }}</div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="visibleCategories.length === 0" class="empty-wrapper">
        <div class="emptyBg">
          <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
          <p>{{ td('dpp.cleanRule.noData', 'No Data') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { Document, CircleCheckFilled, Search } from "@element-plus/icons-vue";
import DeptTree from "@/components/DeptTree";
import { listAll } from "@/api/att/rule/cleanRule.js";
import { listAttCleanCat } from "@/api/att/cat/cleanCat/cleanCat.js";
import { ref, computed, onMounted, getCurrentInstance, toRefs } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const emit = defineEmits(["card-click"]);
const props = defineProps({
  type: {
    type: String,
    default: "",
  },
});

// 状态定义 (State)
const loading = ref(false);
const contentWrapper = ref(null);
const selectedCard = ref(null);
const leftWidth = ref(308); // Restore to 308 to match CSS
const processedData = ref([]);
const activeCategory = ref("");
const allRules = ref([]);
const searchKeyword = ref("");

// 静态资源与映射配置 (Static Resources & Mapping)
const iconModules = import.meta.glob("@/assets/images/dpp/etl/rule/**/*.svg", { eager: true, import: "default" });

const ruleIconMap = [
  { code: "001", name: "数值边界调整", icon: "numeric-boundary-adjustment.svg" },
  { code: "002", name: "字段值替换", icon: "field-value-replacement.svg" },
  { code: "003", name: "字符串转数值", icon: "string-to-number.svg" },
  { code: "004", name: "字符串转日期", icon: "string-to-date.svg" },
  { code: "005", name: "任意类型转布尔值", icon: "any-to-boolean.svg" },
  { code: "006", name: "手机号格式统一", icon: "phone-format-unification.svg" },
  { code: "007", name: "日期格式统一", icon: "date-format-unification.svg" },
  { code: "008", name: "小数位统一", icon: "decimal-places-unification.svg" },
  { code: "009", name: "去除字段空格", icon: "remove-field-spaces.svg" },
  { code: "010", name: "字段前缀/后缀统一", icon: "prefix-suffix-unification.svg" },
  { code: "011", name: "正则表达式替换", icon: "regex-replacement.svg" },
  { code: "012", name: "超长字段截断", icon: "truncate-long-fields.svg" },
  { code: "013", name: "字段补零", icon: "field-pad-zero.svg" },
  { code: "014", name: "数值空值填充", icon: "numeric-empty-fill.svg" },
  { code: "015", name: "字符串空值填充", icon: "string-empty-fill.svg" },
  { code: "016", name: "布尔空值填充", icon: "boolean-empty-fill.svg" },
  { code: "017", name: "日期空值填充", icon: "date-empty-fill.svg" },
  { code: "018", name: "全字段为空删除", icon: "delete-all-empty-fields.svg" },
  { code: "019", name: "组合字段为空删除", icon: "delete-composite-empty-fields.svg" },
  { code: "020", name: "单位标准化类", icon: "unit-standardization.svg" },
  { code: "021", name: "字段值转大写", icon: "field-value-to-uppercase.svg" },
  { code: "022", name: "字段值转小写", icon: "field-value-to-lowercase.svg" },
  { code: "023", name: "字段值首字母大写", icon: "field-value-capitalize.svg" },
  { code: "024", name: "枚举值映射标准化", icon: "enum-mapping-standardization.svg" },
  { code: "025", name: "按主键去重（保留首条）", icon: "dedup-by-pk-first.svg" },
  { code: "026", name: "按主键去重（按时间字段保留最新）", icon: "dedup-by-pk-time.svg" },
  { code: "027", name: "按优先级字段保留记录", icon: "keep-by-priority-field.svg" },
  { code: "028", name: "按非空字段优先保留记录", icon: "keep-non-empty-field-first.svg" },
  { code: "029", name: "按组合字段去重（保留最新或首条）", icon: "dedup-by-composite-fields.svg" },
  { code: "030", name: "替换无效数值", icon: "replace-invalid-numeric.svg" },
  { code: "031", name: "替换非法枚举值", icon: "replace-invalid-enum.svg" },
  { code: "032", name: "替换无效时间格式", icon: "replace-invalid-time-format.svg" },
  { code: "033", name: "替换非法布尔值", icon: "replace-invalid-boolean.svg" },
  { code: "034", name: "关键词脱敏", icon: "keyword-desensitization.svg" },
  { code: "035", name: "敏感词替换", icon: "sensitive-word-replacement.svg" },
  { code: "036", name: "修正未来时间值", icon: "fix-future-time.svg" },
  { code: "037", name: "校正超前时间差值", icon: "correct-advanced-time-difference.svg" },
  { code: "038", name: "填补空时间为当前时间", icon: "fill-empty-time-with-current.svg" },
  { code: "039", name: "清理过期记录", icon: "clean-expired-records.svg" },
  { code: "040", name: "修正历史时间边界", icon: "fix-history-time-boundary.svg" }
];

/**
 * 获取对应规则的图标
 */
const getIcon = (code) => {
  const rule = ruleIconMap.find(item => item.code === code);
  if (!rule || !rule.icon) return null;
  const path = Object.keys(iconModules).find(p => p.endsWith(rule.icon));
  return path ? iconModules[path] : null;
};

// 树形节点数据处理逻辑 (Tree Node Processing)

/**
 * Recursively find the node with the specified ID in the tree structure
 */
function findNode(nodes, id) {
  for (const node of nodes) {
    if ((node.code || node.id) === id) return node;
    if (node.children) {
      const found = findNode(node.children, id);
      if (found) return found;
    }
  }
  return null;
}

/**
 * Recursively collect codes of the current node and all its children
 */
function collectCodes(node) {
  let codes = [node.code || node.id];
  if (node.children) {
    for (const child of node.children) {
      codes = codes.concat(collectCodes(child));
    }
  }
  return codes;
}

/**
 * 获取指定维度节点及其所有子节点的code集合
 */
function getCategoryCodes(dimId) {
  if (!dimId) return [];
  const node = findNode(processedData.value, dimId);
  if (!node) return [dimId];
  return collectCodes(node);
}

/**
 * 获取第二层级的分类节点集合
 */
function getSecondLevelCategories(nodes) {
  let result = [];
  if (!nodes || nodes.length === 0) return result;
  const rootNode = nodes[0];
  if (rootNode && rootNode.children) {
    for (const child of rootNode.children) {
      result.push(child);
    }
  }
  return result;
}

// 规则计算与过滤逻辑 (Rules Computed & Filtering)

/**
 * Rule list filtered by search keyword
 */
const filteredRules = computed(() => {
  const keyword = searchKeyword.value?.toLowerCase() || '';
  if (!keyword) return allRules.value;
  return allRules.value.filter(rule => 
    rule.name?.toLowerCase().includes(keyword) || 
    rule.description?.toLowerCase().includes(keyword)
  );
});

/**
 * 规则总数
 */
const allRulesCount = computed(() => allRules.value.length);

/**
 * 获取指定维度（包含子节点）下的总规则数
 */
function getRulesCountByDimension(dimId) {
  if (!dimId) return allRulesCount.value;
  const codes = getCategoryCodes(dimId);
  return allRules.value.filter((rule) => codes.includes(rule.catCode) || codes.includes(rule.type)).length;
}

/**
 * Get the number of filtered rules for a category (including children)
 */
function getFilteredCategoryGroupCount(cat) {
  const codes = getCategoryCodes(cat.code || cat.id);
  return filteredRules.value.filter((rule) => codes.includes(rule.catCode) || codes.includes(rule.type)).length;
}

/**
 * Get the specific filtered rule list for a category (including children)
 */
function getRulesByCategoryGroup(cat) {
  const codes = getCategoryCodes(cat.code || cat.id);
  return filteredRules.value.filter((rule) => codes.includes(rule.catCode) || codes.includes(rule.type));
}

/**
 * Calculate the list of second-level categories that should be displayed currently
 */
const visibleCategories = computed(() => {
  if (!processedData.value.length) return [];
  const secondLevelDims = getSecondLevelCategories(processedData.value);
    
  if (activeCategory.value === "") {
    return secondLevelDims.filter(cat => getFilteredCategoryGroupCount(cat) > 0);
  } else {
    const activeCodes = getCategoryCodes(activeCategory.value);
    return secondLevelDims.filter(cat => {
      const catCodes = getCategoryCodes(cat.code || cat.id);
      const hasIntersection = catCodes.some(code => activeCodes.includes(code));
      return hasIntersection && getFilteredCategoryGroupCount(cat) > 0;
    });
  }
});

// 事件处理 (Event Handlers)

/**
 * 左侧树节点点击事件
 */
function handleNodeClick(data) {
  const raw = data && data.payload ? data.payload : data;
  const targetId = raw?.code ?? raw?.id ?? "";
  activeCategory.value = targetId;
  
  if (contentWrapper.value) {
    contentWrapper.value.scrollTop = 0;
  }
}

/**
 * Right rule card click event
 */
function cardClick(data) {
  if (data.validFlag == false) {
    return ElMessage.info(td('dpp.cleanRule.developing', 'Under Development'));
  }
  selectedCard.value = data;
  emit("card-click", data);
}

// 数据初始化 (Data Initialization)

/**
 * Get the left tree category data
 */
function getDataTree() {
  listAttCleanCat().then((response) => {
    processedData.value = [];
    const data = { id: "", name: td('dpp.cleanRule.cleanRuleTree', 'Clean Rule'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    processedData.value.push(data);
  });
}

/**
 * Fetch and process all rule data
 */
async function fetchRulesByDimension() {
  loading.value = true;
  const res = await listAll({ type: "" });
  const list = res.data || [];
  
  if (props.type == "3") {
    const disabledCodes = ["029", "039"];
    const processedList = list.map((item) => {
      if (disabledCodes.includes(item.code)) {
        return { ...item, validFlag: false };
      }
      return item;
    });
    allRules.value = processedList.sort((a, b) => {
      return (b.validFlag === true) - (a.validFlag === true);
    });
  } else {
    allRules.value = list.sort((a, b) => {
      return (b.validFlag === true) - (a.validFlag === true);
    });
  }

  loading.value = false;
}

onMounted(() => {
  fetchRulesByDimension();
  getDataTree();
});
</script>

<style lang="scss" scoped>
@import "@/assets/system/styles/components/option-card.scss";
@import "@/assets/system/styles/components/ds-selector.scss";
</style>
