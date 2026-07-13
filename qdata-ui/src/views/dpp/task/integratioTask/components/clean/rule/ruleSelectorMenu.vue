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
  <el-row>
    <el-col :span="5">
      <DeptTree
        :deptOptions="processedData"
        :leftWidth="leftWidth"
        :placeholder="td('dpp.cleanRule.inputRuleType', '请输入规则类型')"
        @node-click="handleNodeClick"
        ref="DeptTreeRef"
        :showFilter="false"
      />
    </el-col>
    <div class="divider"></div>
    <el-col :span="18" class="content-col" v-loading="loading">
      <div class="content" ref="contentWrapper">
        <el-row>
          <div class="cards-wrapper">
            <template v-if="attCleanRuleList.length">
              <div
                v-for="data in attCleanRuleList"
                :key="data.id"
                class="card-item"
                :class="{ selected: selectedCard?.id === data.id }"
                @click="cardClick(data)"
              >
                <el-card
                  class="box-card boxCard"
                  shadow="never"
                  :body-style="{ padding: '15px' }"
                >
                  <div
                    class="card-icon"
                    :class="{ 'is-disabled': data.validFlag == false }"
                  >
                    <el-icon>
                      <Document />
                    </el-icon>
                  </div>

                  <div class="card-title ellipsis">{{ data.name }}</div>
                  <div class="card-desc ellipsis-multi">
                    {{ data.description }}
                  </div>
                </el-card>
              </div>
            </template>
            <template v-else>
              <div class="empty-wrapper">
                <div class="emptyBg">
                  <img
                    src="../../../../../../../assets/images/system/no_data/empty-nodata.png"
                    alt=""
                  />
                  <p>{{ td('dpp.cleanRule.noData', '无数据') }}</p>
                </div>
              </div>
            </template>
          </div>
        </el-row>
      </div>
    </el-col>
  </el-row>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import {
  Document,
  Menu,
  DataLine,
  Files,
  Monitor,
} from "@element-plus/icons-vue";
import DeptTree from "@/components/DeptTree/tree.vue";
import { listAll } from "@/api/att/rule/cleanRule.js";
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { att_rule_clean_type } = proxy.useDict("att_rule_clean_type");
import {
  listAttCleanCat,
  getAttCleanCat,
  delAttCleanCat,
  addAttCleanCat,
  updateAttCleanCat,
} from "@/api/att/cat/cleanCat/cleanCat.js";

const contentWrapper = ref(null);
const selectedCard = ref(null);
const leftWidth = ref(250); // Initial left width
const emit = defineEmits(["card-click"]);
const props = defineProps({
  type: {
    type: String,
    default: "",
  },
});
let queryParams = ref({
  type: "",
  // validFlag: '1'
});
const processedData = ref([]);
function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  fetchRulesByDimension();
}

let attCleanRuleList = ref([]);
function getDataTree() {
  listAttCleanCat().then((response) => {
    processedData.value = [];
    const data = { id: "", name: td('dpp.cleanRule.cleanRuleTree', '清洗规则'), children: [] };
    data.children = proxy.handleTree(response.data, "id", "parentId");
    processedData.value.push(data);
  });
}
let loading = ref(false);
async function fetchRulesByDimension() {
  loading.value = true;
  const res = await listAll(queryParams.value);
  const list = res.data || [];
  console.log("🚀 ~ fetchRulesByDimension ~ list:", list);

  if (props.type == "3") {
    const disabledCodes = ["029", "039"];
    attCleanRuleList.value = list.map((item) => {
      if (disabledCodes.includes(item.code)) {
        return { ...item, validFlag: false };
      }
      return item;
    });
  } else {
    attCleanRuleList.value = list;
  }

  // Sort, put validFlag == false at the end
  attCleanRuleList.value.sort((a, b) => {
    return (b.validFlag === true) - (a.validFlag === true);
  });

  loading.value = false;
}

function cardClick(data) {
  if (data.validFlag == false) {
    return ElMessage.info(td('dpp.cleanRule.developing', '开发中'));
  }
  selectedCard.value = data;
  emit("card-click", data);
}

onMounted(() => {
  fetchRulesByDimension();
  getDataTree();
});
</script>

<style lang="less" scoped>
.main-layout {
  height: 75vh;
  overflow: hidden;
}

.left-col {
  padding-right: 0;
}

.divider {
  width: 1px;
  height: 700px;
  background-color: #dcdfe6;
}

.content-col {
  width: 100%;
  overflow: hidden;
  padding-left: 0;
}

.content {
  overflow-y: auto;
  position: relative;
}

/* Right content */
.content-col {
  height: 75vh;
  overflow: hidden;
}

.content {
  height: 75vh;
  overflow-y: auto;
  padding: 20px 10px;
}

.cards-wrapper {
  padding-left: 40px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: flex-start;
}

.card-item {
  width: 180px;
  box-sizing: border-box;
  transition: transform 0.2s;
  display: flex;
  justify-content: center;
  cursor: pointer;

  &.selected {
    transform: translateY(-2px);
    border: 2px solid #409eff;
    box-shadow: 0 0 10px rgba(64, 158, 255, 0.3);
  }

  &:hover {
    transform: translateY(-2px);
  }
}

.boxCard {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 150px;
  height: 150px;
  width: 100%;
  padding: 10px;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);

  &:hover {
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
  }

  .card-icon {
    font-size: 45px;
    color: #409eff;
    margin-bottom: 6px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .card-title {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 17ch;
    /* Limit maximum width to 8 characters */
    margin: 0 auto;
    cursor: pointer;
    text-align: center;
    width: 100%;
  }

  .ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .card-desc {
    font-size: 12px;
    color: #666;
    text-align: center;
    padding: 0 5px;
    word-break: break-word;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    /* Show 3 lines */
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.dh {
  padding: 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.menu-icon {
  font-size: 20px;
  vertical-align: middle;
}

::v-deep .el-card__body {
  padding: 0 !important;
}

.empty-wrapper {
  position: absolute;
  top: 300px;
  left: 0;
  right: 0;
  bottom: 0;

  display: flex;
  justify-content: center;
  align-items: center;

  /* text style */
  font-size: 16px;
  color: #999;
  user-select: none;
}

.card-icon {
  cursor: pointer;
  color: #409eff;
}

.card-icon.is-disabled {
  color: #ccc;
  cursor: not-allowed;
}
</style>
