<!-- 复杂详情路由模板
    {
        path: '/att/client',
        component: Layout,
        redirect: 'client',
        hidden: true,
        children: [
            {
                path: 'clientDetail',
                component: () => import('@/views/att/client/detail/index.vue'),
                name: 'tree',
                meta: { title: '应用管理详情', activeMenu: '/att/client'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ clientDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ clientDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">应用名称</div>
                <div class="infotop-row-value">
                  {{ clientDetail.name || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">应用类型</div>
                <div class="infotop-row-value">
                    <dict-tag :options="auth_app_type" :value="clientDetail.type "/>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">允许授权的url</div>
                <div class="infotop-row-value">
                  {{ clientDetail.allowUrl || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">同步地址</div>
                <div class="infotop-row-value">
                  {{ clientDetail.syncUrl || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">应用图标</div>
                <div class="infotop-row-value">
                  <image-preview :src="clientDetail.logo" :width="50" :height="50"/>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">应用描述</div>
                <div class="infotop-row-value">
                  {{ clientDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">是否公开</div>
                <div class="infotop-row-value">
                    <dict-tag :options="auth_public" :value="clientDetail.publicFlag "/>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ clientDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(clientDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ clientDetail.remark || '-' }}
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

<script setup name="Client">
  import {getClient } from "@/api/att/client/client";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/att/client/detail/componentOne.vue";
  import ComponentTwo from "@/views/att/client/detail/componentTwo.vue";

  const { proxy } = getCurrentInstance();
    const { auth_public, auth_app_type } = proxy.useDict('auth_public', 'auth_app_type');

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
            getClientDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      clientDetail: {
    },
    form: {},
  });

  const {  clientDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getClientDetailById() {
        const _id = id ;
    getClient(_id).then(response => {
        clientDetail.value = response.data;
    });
  }

  getClientDetailById();

</script>
