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
  <div class="app-container" ref="app-container">
    <DetailInfo
      :show="showSearch"
      :data="clientDetail"
      :header="{
        nameKey: 'name',
      }"
      :items="detailItems"
      mode="free"
    />

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane v-for="item in tabList" :key="item.name" :label="item.label" :name="item.name">
          <component :is="item.component" :clientDetail="clientDetail"></component>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script setup name="Client">
import { ref, reactive, watch, toRefs, computed, getCurrentInstance } from 'vue';
import useDefaultLang from "@/composables/useDefaultLang";
import { getClient } from "@/api/ds/client/client";
import { useRoute } from 'vue-router';
import info from "@/views/ds/client/detail/info.vue";
import api from "@/views/ds/client/detail/api.vue";

const { td } = useDefaultLang();
const noDataImg = new URL('@/assets/images/system/D.png', import.meta.url).href

const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict('auth_public', 'auth_app_type');
const tabList = ref([
    {
        label: td('ds.client.details.apiAuth'),
        name: '1',
        component: api
    },
    {
        label: td('ds.client.details.detailInfo'),
        name: '2',
        component: info
    }
  ]);
const activeName = ref('1')

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const showSearch = ref(true);

const detailItems = computed(() => [
  { label: td('common.texts.number'), key: 'id' },
  { label: td('ds.client.details.appSecret'), key: 'secret' },
  {
    label: td('ds.client.details.appSecret'),
    key: 'logo',
    type: 'image',
    imageFallback: noDataImg,
    width: 50,
    height: 50
  },
  { label: td('common.texts.description'), key: 'description', span: 24, ellipsisClass: 'ellipsis-2' , className: "mt2 mb2",},
]);

const route = useRoute();
let id = route.query.id || 1;
// Monitor id changes
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;  // If id is empty, the default value 1 is used
    getClientDetailById();

  },
  { immediate: true }  // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
const data = reactive({
  clientDetail: {
  },
  form: {},
});

const { clientDetail, rules } = toRefs(data);

/** Form query at the top of the complex details page */
function getClientDetailById() {
  const _id = id;
  getClient(_id).then(response => {
    clientDetail.value = response.data;
  });
}
// Save without code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // Remove component based on component ID
  }
  drawer.value = false;
};
getClientDetailById();

</script>
<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 343px) !important;

  }
}
</style>