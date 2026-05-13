<template>
  <div class="base-info">
    <div class="module-head">基础信息</div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">所属分层</div>
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
            <div class="infotop-row-lable">所属系统</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.belongingSystem) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">描述</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.description) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">更新人</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.updateBy) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">更新时间</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.updateTime) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">创建人</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.createBy) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">创建时间</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.createTime) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">备注</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.remark) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="module-head">技术信息</div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">数据源名称</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbName) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">IP</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.ip) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">端口号</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.dbRespVO?.port) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">账号</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.username) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">存储类型</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.storageType) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">存储大小</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.storageSize) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">技术负责人</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.techLeaderName) }}
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">技术负责人电话</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.techLeaderPhone) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="module-head">业务信息</div>
    <div class="module-body infotop">
      <el-row :gutter="2">
        <!-- <el-col :span="8">
                    <div class="infotop-row border-top">
                        <div class="infotop-row-lable">安全等级</div>
                        <div class="infotop-row-value">
                            {{ getFormatValue(detail.dbRespVO?.safetyLevelName) }}
                        </div>
                    </div>
                </el-col> -->

        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">是否主表</div>
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
            <div class="infotop-row-lable">是否临时表</div>
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
            <div class="infotop-row-lable">业务负责人</div>
            <div class="infotop-row-value">
              {{ getFormatValue(detail.businessLeaderName) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="2">
        <el-col :span="8">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">业务负责人电话</div>
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
import { getCurrentInstance, toValue } from "vue";
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
