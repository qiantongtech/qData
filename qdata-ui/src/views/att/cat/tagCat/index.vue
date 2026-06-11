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
-->

<template>
  <div class="app-container">
    <CatPage
      :list-func="listAttTagCat"
      :get-func="getAttTagCat"
      :del-func="delAttTagCat"
      :add-func="addAttTagCat"
      :update-func="updateAttTagCat"
      :name-label="td('att.common.tagCatName')"
      title-base="标签管理类目"
      perm-base="att:tagCat"
    />
  </div>
</template>

<script setup name="TagCat">
import CatPage from "../components/CatPage.vue";
import {
  listAttTagCat,
  getAttTagCat,
  delAttTagCat,
  addAttTagCat,
  updateAttTagCat,
} from "@/api/att/cat/tagCat/tagCat";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
</script>
