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
        :placeholder="td('da.qualityTask.ruleBase.ruleTypePlaceholder', 'Please enter rule type')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
        :showFilter="false"
      >
        <template #label="{ data }">
          <span class="tree-label-with-count">
            <span class="label-text">{{ data.name }}</span>
            <span class="count-text" v-if="data.id === ''">({{ allRulesCount }})</span>
            <span class="count-text" v-else>({{ getRulesCountByDimension(data.id) }})</span>
          </span>
        </template>
      </DeptTree>
    </div>
    <div class="ds-selector__main" ref="contentWrapper">
      <div class="ds-search">
        <el-input
          v-model="searchKeyword"
          :placeholder="td('da.qualityTask.ruleBase.search', '请输入你要搜索的内容')"
          clearable
          class="ds-search__input"
          :prefix-icon="Search"
        />
      </div>

      <div v-if="visibleCategories.length > 0">
        <div
          v-for="(cat, index) in visibleCategories"
          :key="cat.id"
          class="category-group"
          :class="{ 'is-first': index === 0 }"
        >
          <div :id="`category-${cat.id}`" class="category-group__header">
            <span class="category-group__title">{{ cat.name }}</span>
          </div>
          <div class="ds-grid">
            <div
              v-for="data in getRulesByDimension(cat.id)"
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
                    <div v-if="data.iconPath" class="rule-icon-mask" :style="{ '--icon-url': `url('${getIconByName(data.iconPath)}')` }"></div>
                    <div v-else-if="getIcon(data.id)" class="rule-icon-mask" :style="{ '--icon-url': `url('${getIcon(data.id)}')` }"></div>
                    <el-icon v-else class="rule-icon-fallback">
                      <Document />
                    </el-icon>
                  </template>
                  <template v-else>
                    <img v-if="data.iconPath" :src="getIconByName(data.iconPath)" class="rule-icon" />
                    <img v-else-if="getIcon(data.id)" :src="getIcon(data.id)" class="rule-icon" />
                    <el-icon v-else>
                      <Document />
                    </el-icon>
                  </template>
                </div>
                <div class="ds-card__title" :title="data.name">{{ data.name }}</div>
              </div>
              <div class="ds-card__desc" :title="data.useCase">{{ data.useCase }}</div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="visibleCategories.length === 0" class="empty-wrapper">
        <div class="emptyBg">
          <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
          <p>{{ td('common.noData', 'No Data') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { Document, CircleCheckFilled, Search } from "@element-plus/icons-vue";
import { listAttAuditRule } from "@/api/att/rule/auditRule.js";
import DeptTree from "@/components/DeptTree";
import { ref, computed, onMounted, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { att_rule_audit_q_dimension } = proxy.useDict("att_rule_audit_q_dimension");

const loading = ref(false);
const contentWrapper = ref(null);
const selectedCard = ref(null);
const leftWidth = ref(308);
const emit = defineEmits(["card-click"]);
const props = defineProps({
  type: {
    type: String,
    default: "",
  },
});

const activeCategory = ref("");
const allRules = ref([]);
const searchKeyword = ref("");

// 静态资源与映射配置 (Static Resources & Mapping)
const iconModules = import.meta.glob("@/assets/images/da/qualityTask/rule/**/*.svg", { eager: true, import: "default" });

const ruleIconMap = [
  { id: "107", icon: "character-type-validation.svg" },
  { id: "108", icon: "length-range-validation.svg" },
  { id: "109", icon: "datetime-format-validation.svg" },
  { id: "110", icon: "decimal-precision-validation.svg" },
  { id: "111", icon: "enum-validation.svg" },
  { id: "112", icon: "master-data-ref-validation.svg" },
  { id: "113", icon: "logic-model-consistency-validation.svg" },
  { id: "114", icon: "field-meta-info-completeness-validation.svg" },
  { id: "115", icon: "time-sequence-logic-validation.svg" },
  { id: "116", icon: "numeric-logic-relation-validation.svg" },
  { id: "117", icon: "status-dependency-validation.svg" },
  { id: "118", icon: "mutually-exclusive-field-conflict.svg" },
  { id: "119", icon: "sensitive-field-desensitization-validation.svg" },
  { id: "203", icon: "required-not-null-validation.svg" },
  { id: "204", icon: "invalid-placeholder-identification.svg" },
  { id: "205", icon: "group-field-completeness.svg" },
  { id: "206", icon: "whole-record-missing-check-validation.svg" },
  { id: "207", icon: "conditional-field-completeness-validation.svg" },
  { id: "306", icon: "numeric-range-validation.svg" },
  { id: "307", icon: "numeric-logic-relation-validation.svg" },
  { id: "308", icon: "numeric-outlier-identification.svg" },
  { id: "309", icon: "dirty-data-identification.svg" },
  { id: "310", icon: "single-field-uniqueness-validation.svg" },
  { id: "311", icon: "composite-uniqueness-validation.svg" },
  { id: "403", icon: "cross-table-consistency-validation.svg" },
  { id: "404", icon: "foreign-key-validity-validation.svg" },
  { id: "405", icon: "status-consistency-validation.svg" },
  { id: "504", icon: "time-order-validation.svg" }
];

/**
 * Get the icon for the corresponding rule
 */
const getIcon = (id) => {
  const rule = ruleIconMap.find(item => String(item.id) === String(id));
  if (!rule || !rule.icon) return null;
  const path = Object.keys(iconModules).find(p => p.endsWith(rule.icon));
  return path ? iconModules[path] : null;
};

/**
 * Get the icon by filename
 */
const getIconByName = (filename) => {
  if (!filename) return null;
  const rule = ruleIconMap.find(item => item.icon === filename);
  const mappedName = rule ? rule.icon : filename;
  const path = Object.keys(iconModules).find(p => p.endsWith(mappedName));
  return path ? iconModules[path] : null;
};

/**
 * Filter rules by keyword
 */
const filteredRules = computed(() => {
  const keyword = searchKeyword.value?.toLowerCase() || '';
  if (!keyword) return allRules.value;
  return allRules.value.filter(rule => 
    rule.name?.toLowerCase().includes(keyword) || 
    rule.useCase?.toLowerCase().includes(keyword)
  );
});

/**
 * 处理左侧树的分类数据
 */
const processedData = computed(() => {
  return [
    {
      id: "",
      name: td('da.qualityTask.ruleBase.qualityDimension', 'Quality Dimension'),
      children: Array.isArray(att_rule_audit_q_dimension.value)
        ? att_rule_audit_q_dimension.value.map((item) => ({
            name: item.label,
            id: item.value,
          }))
        : [],
    },
  ];
});

/**
 * Total number of all rules
 */
const allRulesCount = computed(() => allRules.value.length);

/**
 * Get rule count by dimension
 */
function getRulesCountByDimension(dimId) {
  if (!dimId) return allRulesCount.value;
  return allRules.value.filter((rule) => rule.qualityDim === dimId).length;
}

/**
 * Get filtered rule count by dimension
 */
function getFilteredRulesCountByDimension(dimId) {
  if (!dimId) return filteredRules.value.length;
  return filteredRules.value.filter((rule) => rule.qualityDim === dimId).length;
}

/**
 * Get rule list by dimension
 */
function getRulesByDimension(dimId) {
  if (!dimId) return filteredRules.value;
  return filteredRules.value.filter((rule) => rule.qualityDim === dimId);
}

/**
 * Actually visible category groups on the right
 */
const visibleCategories = computed(() => {
  const allDims = Array.isArray(att_rule_audit_q_dimension.value)
    ? att_rule_audit_q_dimension.value.map(item => ({ id: item.value, name: item.label }))
    : [];
    
  if (activeCategory.value === "") {
    return allDims.filter(cat => getFilteredRulesCountByDimension(cat.id) > 0);
  } else {
    return allDims.filter(cat => cat.id === activeCategory.value && getFilteredRulesCountByDimension(cat.id) > 0);
  }
});

/**
 * Handle left tree node click event
 */
function handleNodeClick(data) {
  const raw = data && data.payload ? data.payload : data;
  const targetId = raw?.id ?? "";
  activeCategory.value = targetId;
  
  if (contentWrapper.value) {
    contentWrapper.value.scrollTop = 0;
  }
}

/**
 * Fetch all rule data and process validity
 */
async function fetchRulesByDimension() {
  loading.value = true;
  const res = await listAttAuditRule({ pageNum: 1, pageSize: 999 });
  const list = res.data.rows || [];
  
  if (props.type == "3") {
    const disabledCodes = [
      "TIME_ORDER_VALIDATION",
      "COMPOSITE_UNIQUENESS_VALIDATION",
    ];
    const processedList = list.map((item) => {
      if (disabledCodes.includes(item.strategyKey)) {
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

/**
 * Handle rule card click event
 */
function cardClick(data) {
  if (data.validFlag == false) {
    return proxy.$message.info(td('da.qualityTask.ruleBase.developing', 'Under Development'));
  }
  selectedCard.value = data;
  emit("card-click", data);
}

onMounted(() => {
  fetchRulesByDimension();
});
</script>

<style lang="scss" scoped>
@import "@/assets/system/styles/components/option-card.scss";
@import "@/assets/system/styles/components/ds-selector.scss";
</style>
