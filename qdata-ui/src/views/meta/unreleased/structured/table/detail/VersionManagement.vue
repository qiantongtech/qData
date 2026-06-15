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
            {{ td("common.button.details") }}
          </el-button>
          <el-button link type="primary" icon="Edit">{{
            td("meta.unreleased.structured.table.detail.restore")
          }}</el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      v-model="dialog.open"
      :title="td('meta.unreleased.structured.table.detail.versionDetail')"
      width="800"
      draggable
    >
      <el-form label-width="auto">
        <el-form-item
          :label="td('meta.released.structured.table.tableName') + '：'"
        >
          <el-input
            v-model="dialog.row.tableName"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.released.structured.table.tableName'),
              })
            "
            disabled
          />
        </el-form-item>
        <el-form-item
          :label="td('meta.released.structured.table.tableComment') + '：'"
        >
          <el-input
            v-model="dialog.row.tableComment"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.released.structured.table.tableComment'),
              })
            "
            disabled
          />
        </el-form-item>
        <el-form-item
          :label="td('meta.unreleased.structured.table.detail.version') + '：'"
        >
          <el-input
            v-model="dialog.row.version"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.version'),
              })
            "
            disabled
          />
        </el-form-item>
        <el-form-item
          :label="
            td('meta.unreleased.structured.table.detail.updateType') + '：'
          "
        >
          <el-input
            v-model="dialog.row.updateType"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.updateType'),
              })
            "
            disabled
          />
        </el-form-item>
        <el-form-item
          :label="
            td('meta.unreleased.structured.table.detail.updateMsg') + '：'
          "
        >
          <el-input
            v-model="dialog.row.updateMsg"
            :placeholder="
              td('common.form.inputPlaceholder', {
                field: td('meta.unreleased.structured.table.detail.updateMsg'),
              })
            "
            disabled
            type="textarea"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>
        <el-form-item
          :label="
            td('meta.unreleased.structured.table.detail.activeVersion') + '：'
          "
        >
          <dict-tag
            :options="toValue(dicts.sys_yes_no)"
            :value="dialog.row.activeVersion"
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy') + '：'">
          <el-input
              v-model="dialog.row.name"
              :placeholder="td('meta.unreleased.structured.table.detail.createByPlaceholder')"
              disabled
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime') + '：'">
          <el-input
              v-model="dialog.row.time"
              :placeholder="td('meta.unreleased.structured.table.detail.createTimePlaceholder')"
              disabled
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialog.open = false">{{
            td("common.button.close")
          }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="VersionManagement">
import useDefaultLang from "@/composables/useDefaultLang";
import { reactive, toValue, getCurrentInstance } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict("sys_yes_no");

const tableStroe = reactive({
  columns: [
    {
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: td("meta.released.structured.table.tableName"),
      prop: "tableName",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 140,
    },
    {
      label: td("meta.released.structured.table.tableComment"),
      prop: "tableComment",
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
    },
    {
      label: td("meta.unreleased.structured.table.detail.version"),
      prop: "version",
      width: 90,
    },
    {
      label: td("meta.unreleased.structured.table.detail.updateType"),
      prop: "updateType",
      width: 90,
    },
    {
      label: td("meta.unreleased.structured.table.detail.updateMsg"),
      prop: "updateMsg",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("meta.unreleased.structured.table.detail.activeVersion"),
      prop: "activeVersion",
      slot: "active-version",
      width: 90,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 90,
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.operation"),
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
                updateType: td("common.button.add"),
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
                updateType: td("common.button.delete"),
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
                updateType: td("common.button.add"),
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
                updateType: td("common.button.add"),
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
