<!-- 复杂详情路由模板
    {
        path: '/da/sensitiveLevel',
        component: Layout,
        redirect: 'sensitiveLevel',
        hidden: true,
        children: [
            {
                path: 'daSensitiveLevelDetail',
                component: () => import('@/views/da/sensitiveLevel/detail/user.vue'),
                name: 'tree',
                meta: { title: '敏感等级详情', activeMenu: '/da/daSensitiveLevel'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ daSensitiveLevelDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ daSensitiveLevelDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">敏感级别</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.sensitiveLevel || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">敏感规则</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.sensitiveRule || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">起始字符位置</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.startCharLoc || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">截止字符位置</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.endCharLoc || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">遮盖字符</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.maskCharacter || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">在线状态</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.onlineFlag || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">描述</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(daSensitiveLevelDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ daSensitiveLevelDetail.remark || '-' }}
                </div>
              </div>
            </el-col>
        </el-row>

      </div>
    </div>

    <div  class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="组件一" name="1">
          <component-one ></component-one>
        </el-tab-pane>
        <el-tab-pane label="组件二" name="2">
          <component-two ></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="DaSensitiveLevel">
  import {getDaSensitiveLevel } from "@/api/da/sensitiveLevel/daSensitiveLevel";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/da/sensitiveLevel/detail/componentOne.vue";
  import ComponentTwo from "@/views/da/sensitiveLevel/detail/componentTwo.vue";

  const { proxy } = getCurrentInstance();

  const activeName = ref('1')

  const handleClick = (tab, event) => {
    console.log(tab, event)
  }

  const showSearch = ref(true);
  const route = useRoute();
  let id = route.query.id || 1;
  // 监听 id 变化
  watch(
          () => route.query.id,
          (newId) => {
            id = newId || 1;  // 如果 id 为空，使用默认值 1
            getDaSensitiveLevelDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      daSensitiveLevelDetail: {
    },
    form: {},
  });

  const {  daSensitiveLevelDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getDaSensitiveLevelDetailById() {
        const _id = id ;
    getDaSensitiveLevel(_id).then(response => {
        daSensitiveLevelDetail.value = response.data;
    });
  }

  getDaSensitiveLevelDetailById();

</script>
