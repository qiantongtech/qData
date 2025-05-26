<!-- 复杂详情路由模板
    {
        path: '/da/assetColumn',
        component: Layout,
        redirect: 'assetColumn',
        hidden: true,
        children: [
            {
                path: 'daAssetColumnDetail',
                component: () => import('@/views/da/assetColumn/detail/user.vue'),
                name: 'tree',
                meta: { title: '数据资产字段详情', activeMenu: '/da/daAssetColumn'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ daAssetColumnDetail.id }}
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">ID</div>
              <div class="infotop-row-value">{{ daAssetColumnDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">资产id</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.assetId || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">中文名称/英文名称</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.columnName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">字段注释/中文名称</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.columnComment || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据类型</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.columnType || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">长度</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.columnLength || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">小数位</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.columnScale || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">是否必填</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.nullableFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">是否主键</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.pkFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">默认值</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.defaultValue || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">是否代码</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.dataElemCodeFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">代码id</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.dataElemCodeId || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">敏感等级id</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.sensitiveLevelId || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">关联数据元</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.relDataElmeFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">关联清洗规则</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.relCleanFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">关联稽查规则</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.relAuditFlag || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.description || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.createBy || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">
                {{ parseTime(daAssetColumnDetail.createTime, "{y}-{m}-{d}") }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                {{ daAssetColumnDetail.remark || "-" }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="组件一" name="1">
          <component-one></component-one>
        </el-tab-pane>
        <el-tab-pane label="组件二" name="2">
          <component-two></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DaAssetColumn">
import { getDaAssetColumn } from "@/api/da/assetColumn/daAssetColumn";
import { useRoute } from "vue-router";
import ComponentOne from "@/views/da/assetColumn/detail/componentOne.vue";
import ComponentTwo from "@/views/da/assetColumn/detail/componentTwo.vue";

const { proxy } = getCurrentInstance();

const activeName = ref("1");

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1; // 如果 id 为空，使用默认值 1
    getDaAssetColumnDetailById();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  daAssetColumnDetail: {},
  form: {},
});

const { daAssetColumnDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDaAssetColumnDetailById() {
  const _id = id;
  getDaAssetColumn(_id).then((response) => {
    daAssetColumnDetail.value = response.data;
  });
}

getDaAssetColumnDetailById();
</script>
