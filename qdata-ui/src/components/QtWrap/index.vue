<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qData Data Middle Platform (Commercial Edition)
  Software Copyright Registration No. 16069171
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qData 数据中台（商业版） | 软著登字第16069171号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div class="qt-wrap">
    <div
      :class="[config.search ? '' : 'qt-wrap--search']"
      v-if="$slots.search"
      v-show="store.search"
    >
      <slot name="search"></slot>
    </div>
    <div :class="['qt-wrap--content', config.fullContent ? 'full' : '']">
      <div class="qt-wrap--actions" v-if="config.actions.show">
        <div class="data-actions">
          <slot name="actions-data"></slot>
        </div>
        <div class="table-actions" v-if="config.actions.table.show">
          <el-tooltip effect="dark" content="隐藏搜索" placement="top">
            <el-button
              circle
              @click="store.search = !store.search"
              v-show="config.actions.table.search"
            >
              <i class="iconfont icon-a-chaxunxianxing"></i>
            </el-button>
          </el-tooltip>

          <el-tooltip effect="dark" content="刷新" placement="top">
            <el-button
              circle
              v-show="config.actions.table.refresh"
              @click="handleRefreshClick"
            >
              <i class="iconfont icon-a-shuaxinxianxing"></i>
            </el-button>
          </el-tooltip>

          <el-tooltip effect="dark" content="显隐列" placement="top">
            <el-dropdown
              trigger="click"
              :hide-on-click="false"
              v-show="config.actions.table.columns"
              popper-class="columns-popper"
            >
              <el-button circle icon="Menu" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="item in props.columns"
                    :key="item.prop"
                  >
                    <el-checkbox
                      v-show="item?.type != 'selection'"
                      :checked="!item.hide"
                      :label="item.label"
                      @change="handleCheckboxChange($event, item)"
                    />
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>
        </div>
      </div>
      <div class="qt-wrap--main" v-if="$slots.default">
        <slot name="default" />
      </div>
    </div>
  </div>
</template>

<script setup name="QtWrap">
import { computed, reactive } from "vue";
import { merge } from "lodash-es";

const DEFAULT_CONFIG = {
  fullContent: true,
  actions: {
    show: true,
    table: {
      show: true,
      search: true,
      refresh: true,
      columns: true,
    },
  },
};

const props = defineProps({
  config: {
    type: Object,
    default: () => {
      return {};
    },
  },
  columns: {
    type: Array,
    default: () => {
      return [];
    },
  },
  tableRef: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const config = computed(() => {
  return merge({}, DEFAULT_CONFIG, props.config);
});

const store = reactive({
  search: true,
});

// 刷新
function handleRefreshClick() {
  props.tableRef.getList();
}

// 显隐列
function handleCheckboxChange(checked, item) {
  item.hide = !checked;
}
</script>

<style lang="scss" scoped>
.qt-wrap {
  width: 100%;
  height: 100%;
}
.qt-wrap--search {
  padding: 15px 15px 1px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 5px 8px #8091a51a;
  margin-bottom: 15px;
}

.qt-wrap--content {
  background-color: #ffffff;
  border-radius: 2px;
}

.qt-wrap--content.full {
  flex: 1;
  padding: 13px 15px;
  min-height: calc(100vh - 250px);
  box-shadow: 0 5px 8px rgba(128, 145, 165, 0.1);
}

.qt-wrap--actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.data-actions {
  ::v-deep(.el-button) {
    height: 28px;
    padding: 8px 11px;
    font-size: 12px;
  }
}

.data-actions,
.table-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  ::v-deep(.el-button + .el-button) {
    margin-left: 0;
  }
}
</style>

<style lang="scss">
.columns-popper {
  .el-dropdown-menu__item {
    line-height: 30px;
    padding: 0px 17px;
  }
}
</style>
