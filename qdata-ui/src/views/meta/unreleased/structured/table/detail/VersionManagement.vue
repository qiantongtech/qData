<template>
  <div class="version-management">
    <qt-wrap
      :columns="tableStroe.columns"
      :tableRef="tableRef"
      :config="{ fullContent: false, actions: { table: { search: false } } }"
    >
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #active-version="{ row }">
          <el-icon v-show="row.activeVersion == 'Y'"><Select /></el-icon>
        </template>
        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetailClick(row)"
          >
            {{ t('common.button.details') }}
          </el-button>
          <el-button link type="primary" icon="Edit"> 恢复 </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog v-model="dialog.open" title="版本详情" width="800" draggable>
      <el-form label-width="auto">
        <el-form-item label="表名称：">
          <el-input
            v-model="dialog.row.tableName"
            placeholder="请输入表名称"
            disabled
          />
        </el-form-item>
        <el-form-item label="表注释：">
          <el-input
            v-model="dialog.row.tableComment"
            placeholder="请输入表注释"
            disabled
          />
        </el-form-item>
        <el-form-item label="版本号：">
          <el-input
            v-model="dialog.row.version"
            placeholder="请输入版本号"
            disabled
          />
        </el-form-item>
        <el-form-item label="变更类型：">
          <el-input
            v-model="dialog.row.updateType"
            placeholder="请输入变更类型"
            disabled
          />
        </el-form-item>
        <el-form-item label="变更说明：">
          <el-input
            v-model="dialog.row.updateMsg"
            placeholder="请输入变更说明"
            disabled
            type="textarea"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
        <el-form-item label="当前版本：">
          <dict-tag
            :options="toValue(dicts.sys_yes_no)"
            :value="dialog.row.activeVersion"
          />
        </el-form-item>
        <el-form-item label="创建人：">
          <el-input
            v-model="dialog.row.name"
            placeholder="请输入修改人"
            disabled
          />
        </el-form-item>
        <el-form-item label="创建时间：">
          <el-input
            v-model="dialog.row.time"
            placeholder="请输入创建时间"
            disabled
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialog.open = false">{{ t('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="VersionManagement">
import { useI18n } from 'vue-i18n'
import { reactive, toValue, getCurrentInstance } from "vue";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict("sys_yes_no");

const tableStroe = reactive({
  columns: [
    {
      label: t('common.texts.number'),
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: "表名称",
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
    },
    {
      label: "表注释",
      prop: "tableComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
    },
    {
      label: "版本号",
      prop: "version",
      width: 90,
    },
    {
      label: "变更类型",
      prop: "updateType",
      width: 90,
    },
    {
      label: "变更说明",
      prop: "updateMsg",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "当前版本",
      prop: "activeVersion",
      slot: "active-version",
      width: 90,
    },
    {
      label: t('common.texts.createdBy'),
      prop: "createBy",
      width: 90,
    },
    {
      label: t('common.texts.createdTime'),
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: t('common.texts.operation'),
      width: 240,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: function () {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          data: {
            rows: [
              {
                id: 1,
                version: "1.0.0",
                updateType: t('common.button.add'),
                updateMsg:
                  "新增用户表字段，包括用户姓名、联系方式、注册时间等基础信息，优化数据结构设计",
                activeVersion: "N",
                createBy: "admin",
                createTime: "2021-09-01 10:00:00",
                tableName: "qdata_user",
                tableComment: "用户表",
              },
              {
                id: 2,
                version: "1.0.1",
                updateType: t('common.button.delete'),
                updateMsg:
                  "删除冗余字段，移除不再使用的email_backup和phone_backup字段，精简表结构提高性能",
                activeVersion: "N",
                createBy: "user001",
                createTime: "2021-09-02 14:30:00",
                tableName: "qdata_meta",
                tableComment: "用户表",
              },
              {
                id: 3,
                version: "1.0.2",
                updateType: t('common.button.add'),
                updateMsg:
                  "添加索引优化，为user_name和create_time字段创建复合索引，提升查询效率约30%",
                activeVersion: "N",
                createBy: "admin",
                createTime: "2021-09-03 09:15:00",
                tableName: "qdata_meta",
                tableComment: "用户表",
              },
              {
                id: 4,
                version: "1.0.3",
                updateType: "回滚",
                updateMsg:
                  "回滚至1.0.1版本，撤销1.0.2版本的索引变更，解决因索引导致的数据插入性能问题",
                activeVersion: "N",
                createBy: "dev001",
                createTime: "2021-09-04 16:45:00",
                tableName: "qdata_meta",
                tableComment: "用户表",
              },
              {
                id: 5,
                version: "1.0.4",
                updateType: t('common.button.add'),
                updateMsg:
                  "增加用户权限字段，添加role_id和permission_level字段，支持多级权限管理功能",
                activeVersion: "Y",
                createBy: "admin",
                createTime: "2021-09-05 11:20:00",
                tableName: "qdata_meta",
                tableComment: "用户表",
              },
            ],
            total: 5,
          },
        });
      }, 1000);
    });
  },
});

const dialog = reactive({
  open: false,
  row: {},
});

function handleDetailClick(row) {
  dialog.row = row;
  dialog.open = true;
}
</script>
