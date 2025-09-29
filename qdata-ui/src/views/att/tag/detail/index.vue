<!-- 复杂详情路由模板
    {
        path: '/att/Tag',
        component: Layout,
        redirect: 'Tag',
        hidden: true,
        children: [
            {
                path: 'AttTagDetail',
                component: () => import('@/views/att/Tag/detail/ruleBase.vue'),
                name: 'tree',
                meta: { title: '标签管理详情', activeMenu: '/att/AttTag'  }
            }
        ]
    }
 -->


<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ AttTagDetail.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">{{ AttTagDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">标签类目</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.catName || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">资产数量</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.aeestCount || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.description || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">近义词</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.nearSynonyms || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">同义词</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.synonyms || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="dp_model_status" :value="AttTagDetail.status" />
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.createBy || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">{{ parseTime(AttTagDetail.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">更新时间</div>
              <div class="infotop-row-value">{{ parseTime(AttTagDetail.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                {{ AttTagDetail.remark || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="资产信息" name="1">
          <asset :ids="AttTagDetail"></asset>
        </el-tab-pane>
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="Tag">
import { getAttTag } from "@/api/att/tag/tag.js";
import { useRoute } from 'vue-router';
import asset from "@/views/att/tag/detail/asset.vue";

const { proxy } = getCurrentInstance();
const { dp_model_status, } = proxy.useDict(
  "dp_model_status",
);
const activeName = ref('0')

const handleClick = (tab, event) => {
  console.log(AttTagDetail.value)
}

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;  // 如果 id 为空，使用默认值 1
    activeName.value = '0';
    getAttTagDetailById();

  },
  { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  AttTagDetail: {},
  form: {},
});

const { AttTagDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getAttTagDetailById() {
  const _id = id;
  getAttTag(_id).then(response => {
    activeName.value = '1';
    AttTagDetail.value = response.data;
  });
}

// getAttTagDetailById();

</script>
