<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div :class="{ 'show': show }" class="header-search">
    <!--    <svg-icon class-name="search-icon" icon-class="search_new_icon" @click.stop="click" />-->
    <i class="iconfont icon-a-chaxunxianxing" style="font-size: 20px" @click.stop="click"></i>
    <el-select ref="headerSearchSelectRef" v-model="search" :remote-method="querySearch" filterable default-first-option
      remote placeholder="请输入菜单内容" class="header-search-select" @change="change">
      <el-option v-for="option in options" :key="option.item.path" :value="option.item"
        :label="option.item.title.join(' > ')" />
    </el-select>
  </div>
</template>

<script setup>
import Fuse from 'fuse.js'
import { getNormalPath } from '@/utils/anivia.js'
import { isHttp } from '@/utils/validate'
import usePermissionStore from '@/store/system/permission'

const search = ref('');
const options = ref([]);
const searchPool = ref([]);
const show = ref(false);
const fuse = ref(undefined);
const headerSearchSelectRef = ref(null);
const router = useRouter();
const routes = computed(() => usePermissionStore().routes);

function click() {
  show.value = !show.value
  if (show.value) {
    headerSearchSelectRef.value && headerSearchSelectRef.value.focus()
  }
};
function close() {
  headerSearchSelectRef.value && headerSearchSelectRef.value.blur()
  options.value = []
  show.value = false
}
function change(val) {
  const path = val.path;
  const query = val.query;
  if (isHttp(path)) {
    // http(s):// 路径新窗口打开
    const pindex = path.indexOf("http");
    window.open(path.substr(pindex, path.length), "_blank");
  } else {
    if (query) {
      router.push({ path: path, query: JSON.parse(query) });
    } else {
      router.push(path)
    }
  }

  search.value = ''
  options.value = []
  nextTick(() => {
    show.value = false
  })
}
function initFuse(list) {
  fuse.value = new Fuse(list, {
    shouldSort: true,
    threshold: 0.4,
    location: 0,
    distance: 100,
    minMatchCharLength: 1,
    keys: [{
      name: 'title',
      weight: 0.7
    }, {
      name: 'path',
      weight: 0.3
    }]
  })
}
// Filter out the routes that can be displayed in the sidebar
// And generate the internationalized title
function generateRoutes(routes, basePath = '', prefixTitle = []) {
  let res = []

  for (const r of routes) {
    // skip hidden router
    if (r.hidden) { continue }
    const p = r.path.length > 0 && r.path[0] === '/' ? r.path : '/' + r.path;
    const data = {
      path: !isHttp(r.path) ? getNormalPath(basePath + p) : r.path,
      title: [...prefixTitle]
    }

    if (r.meta && r.meta.title) {
      data.title = [...data.title, r.meta.title]

      if (r.redirect !== 'noRedirect') {
        // only push the routes with title
        // special case: need to exclude parent router without redirect
        res.push(data)
      }
    }
    if (r.query) {
      data.query = r.query
    }

    // recursive child routes
    if (r.children) {
      const tempRoutes = generateRoutes(r.children, data.path, data.title)
      if (tempRoutes.length >= 1) {
        res = [...res, ...tempRoutes]
      }
    }
  }
  return res
}
function querySearch(query) {
  if (query !== '') {
    options.value = fuse.value.search(query)
  } else {
    options.value = []
  }
}

onMounted(() => {
  searchPool.value = generateRoutes(routes.value);
})

watchEffect(() => {
  searchPool.value = generateRoutes(routes.value)
})

watch(show, (value) => {
  if (value) {
    document.body.addEventListener('click', close)
  } else {
    document.body.removeEventListener('click', close)
  }
})

watch(searchPool, (list) => {
  initFuse(list)
})
</script>

<style lang='scss' scoped>
.header-search {
  //font-size: 0 !important;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    vertical-align: middle;
  }

  .header-search-select {
    font-size: 18px;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 0;
    display: inline-block;
    vertical-align: middle;


    :deep(.el-input__inner) {
      // border-radius: 0;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      border-bottom: 1px solid #d9d9d9;
      vertical-align: middle;

    }
  }

  &.show {
    .header-search-select {
      width: 210px;
      margin-left: 10px;
    }
  }

  ::v-deep {
    .el-select__wrapper {
      border-radius: 2px;
    }
  }

}
</style>
