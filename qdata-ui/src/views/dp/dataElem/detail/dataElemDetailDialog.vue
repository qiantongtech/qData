<template>
  <div class="app-container">
    <div class="pagecont-top" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ form.name || "-" }}
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">英文名称</div>
              <div class="infotop-row-value">
                {{ form.engName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">类目编码</div>
              <div class="infotop-row-value">
                {{ form.catName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">类型</div>
              <div class="infotop-row-value">
                <dict-tag
                  :options="dp_data_elem_code_type"
                  :value="form.type"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">
                {{ form.personChargeName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">联系电话</div>
              <div class="infotop-row-value">
                {{ form.contactNumber || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">字段类型</div>
              <div class="infotop-row-value">
                <dict-tag :options="column_type" :value="form.columnType" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="sys_disable" :value="form.status" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">
                {{ parseTime(form.createTime, "{y}-{m}-{d}") }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ form.createBy || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ form.description || "-" }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 标签页部分 -->
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="关联清洗规则" name="1">
          <rule-clean-index
            :dataElemId="dataElemId"
            dataType="2"
          ></rule-clean-index>
        </el-tab-pane>
        <el-tab-pane label="关联稽查规则" name="2">
          <rule-audit-index
            :dataElemId="dataElemId"
            dataType="1"
          ></rule-audit-index>
        </el-tab-pane>
        <el-tab-pane label="关联信息" name="3">
          <dp-data-elem-asset-rel />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="dataElemDetailDialog">
import { onMounted } from "vue";

const { proxy } = getCurrentInstance();

import { getDpDataElem } from "@/api/dp/dataElem/dpDataElem";

import RuleCleanIndex from "@/views/dp/dataElem/detail/component/ruleCleanIndex";
import RuleAuditIndex from "@/views/dp/dataElem/detail/component/ruleAuditIndex";
import DpDataElemAssetRel from "@/views/dp/dataElem/detail/dpDataElemAssetRel.vue";
import { useRoute } from "vue-router";
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type"
);

const dpDataElemRuleRelList = ref([]);

const data = reactive({
  form: {},
  activeName: "1",
});
const { form, activeName } = toRefs(data);
const dataElemId = ref("");
const route = useRoute();
dataElemId.value = route.query.id;

/** 详情按钮操作 */
function getDetail() {
  const id = dataElemId.value;
  if (!id) return;
  getDpDataElem(id).then((response) => {
    form.value = response.data;
  });
}

// 页面加载时获取数据
onMounted(() => {
  getDetail();
});

// 返回列表页
function goBack() {
  router.go(-1);
}
</script>

<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;
  .pagecont-bottom {
    min-height: calc(100vh - 414px) !important;
  }
}
</style>
