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
      :list-func="listAttTaskCat"
      :get-func="getAttTaskCat"
      :del-func="delAttTaskCat"
      :add-func="addAttTaskCat"
      :update-func="updateAttTaskCat"
      :status-change-check-func="checkStatusChange"
      :before-submit="beforeFormSubmit"
      :rules="rules"
      show-remark
      :name-label="td('dpp.setting.taskCat.taskCatName')"
      :title-base="td('dpp.setting.taskCat.taskCatName')"
      perm-base="att:taskCat"
      check-project-params
      status-change-pass-row
    />
  </div>
</template>

<script setup name="TaskCat">
import { ref, getCurrentInstance } from "vue";
import CatPage from "@/views/att/cat/components/CatPage.vue";
import {
  listAttTaskCat,
  getAttTaskCat,
  delAttTaskCat,
  addAttTaskCat,
  updateAttTaskCat,
  hasIntegrationTask,
  isTaskCatNameUsed,
  getTaskCatIntegrationTaskCount,
} from "@/api/att/cat/taskCat/taskCat";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const rules = {
  name: [
    {
      required: true,
      message: td("dpp.setting.taskCat.nameRequired"),
      trigger: "blur",
    },
    {
      validator: async (_rule, value, callback) => {
        if (!value) {
          callback();
          return;
        }
        try {
          const response = await isTaskCatNameUsed({
            name: value.trim(),
          }, { hideErrorMessage: true });
          if (response.data === true) {
            callback(new Error(td("dpp.setting.taskCat.nameUsed", "名称已被使用")));
            return;
          }
          callback();
        } catch (error) {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

/** 状态变更前的业务校验 */
async function checkStatusChange(row) {
  if (row.validFlag === false) {
    const response = await hasIntegrationTask(row.id, { hideErrorMessage: true });
    if (response.data === true) {
      proxy.$modal.msgWarning(td('dpp.setting.taskCat.integrationTaskExistsCannotDisable', '存在数据集成任务，不允许禁用'));
      return false;
    }
  }
  return true;
}

/** 提交前的业务校验（如重命名确认） */
async function beforeFormSubmit(formData) {
  if (formData.id) {
    try {
      const oldData = await getAttTaskCat(formData.id, { hideErrorMessage: true });
      const originalName = oldData.data.name || "";
      if ((formData.name || "").trim() !== originalName.trim()) {
        const response = await getTaskCatIntegrationTaskCount(formData.id, { hideErrorMessage: true });
        await proxy.$modal.confirm(
          td(
            "dpp.setting.taskCat.confirmRenameWithTaskCount",
            "该类目已被 {count} 个数据集成任务使用，修改名称后任务归属显示将同步变化。",
            { count: response.data || 0 }
          )
        );
      }
    } catch (error) {
      if (error === 'cancel' || error === 'close') return false;
    }
  }
  return true;
}
</script>
