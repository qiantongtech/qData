<template>
  <div class="base-info">
    <div class="module-head">
      {{ td("meta.unreleased.structured.table.detail.basicInfo") }}
    </div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.belongingLayer") }}
            </div>
            <div class="infotop-row-value">
              <dict-tag
                :options="toValue(dicts.meta_dw_layers)"
                :value="detail.dbRespVO?.belongingLayer"
              />
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{
                td("meta.unreleased.structured.table.detail.belongingSystem")
              }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.belongingSystem) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("common.texts.description") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.description) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("common.texts.updatedBy") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.updateBy) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("common.texts.updatedTime") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.updateTime) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("common.texts.createdBy") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.createBy) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("common.texts.createdTime") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.createTime) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td("common.texts.remark") }}</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.remark) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="module-head">
      {{ td("meta.unreleased.structured.table.detail.techInfo") }}
    </div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.datasourceName") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbName) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.ip") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.ip) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.port") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.port) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.username") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.username) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.storageType") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.storageType) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.storageSize") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.storageSize) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ td("meta.unreleased.structured.table.detail.techLeader") }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.techLeaderName) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{
                td("meta.unreleased.structured.table.detail.techLeaderPhone")
              }}
            </div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.techLeaderPhone) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="module-head">{{ td("meta.unreleased.structured.table.detail.businessInfo") }}</div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <!-- <el-col :span="8">
                    <div class="infotop-row border-top">
                        <div class="infotop-row-lable">Security level</div>
                        <div class="infotop-row-value">
                            {{ getFormatValue(detail.dbRespVO?.safetyLevelName) }}
                        </div>
                    </div>
                </el-col> -->

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td("meta.unreleased.structured.table.detail.isMasterTable") }}</div>
            <div class="infotop-row-value">
              <dict-tag
                :options="toValue(dicts.table_yes_no)"
                :value="detail.masterFlag"
              />
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td("meta.unreleased.structured.table.detail.tempFlag") }}</div>
            <div class="infotop-row-value">
              <dict-tag
                :options="toValue(dicts.table_yes_no)"
                :value="detail.tempFlag"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td("meta.unreleased.structured.table.detail.businessLeader") }}</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.businessLeaderName) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">{{ td("meta.unreleased.structured.table.detail.businessLeaderPhone") }}</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.businessLeaderPhone) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup name="SynchronizeTaskBaseInfo">
import useDefaultLang from "@/composables/useDefaultLang";
import { getCurrentInstance, toValue } from "vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "meta_dw_layers",
  "table_yes_no"
);

const props = defineProps({
  detail: {
    type: Object,
    required: true,
  },
});
</script>
