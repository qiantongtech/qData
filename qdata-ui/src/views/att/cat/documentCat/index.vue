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
  <div class="app-container">
    <CatPage
      :list-func="listAttDocumentCat"
      :get-func="getAttDocumentCat"
      :del-func="delAttDocumentCat"
      :add-func="addAttDocumentCat"
      :update-func="updateAttDocumentCat"
      :name-label="td('att.common.documentCatName')"
      :title-base="td('att.common.documentCatName')"
      perm-base="att:documentCat"
    />
  </div>
</template>

<script setup name="DocumentCat">
import CatPage from "../components/CatPage.vue";
import {
  listAttDocumentCat,
  getAttDocumentCat,
  delAttDocumentCat,
  addAttDocumentCat,
  updateAttDocumentCat,
} from "@/api/att/cat/documentCat/documentCat.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
</script>
