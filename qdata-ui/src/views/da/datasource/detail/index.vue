<!-- 复杂详情路由模板
    {
        path: '/da/datasource',
        component: Layout,
        redirect: 'datasource',
        hidden: true,
        children: [
            {
                path: 'daDatasourceDetail',
                component: () => import('@/views/da/datasource/detail/user.vue'),
                name: 'tree',
                meta: { title: '数据源详情', activeMenu: '/da/daDatasource'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ daDatasourceDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ daDatasourceDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源名称</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.datasourceName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源类型</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.datasourceType || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源配置(json字符串)</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.datasourceConfig || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">IP</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.ip || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">端口号</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.port || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据库表数</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.listCount || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">同步记录数</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.syncCount || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">同步数据量大小</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.dataSize || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">描述</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(daDatasourceDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ daDatasourceDetail.remark || '-' }}
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

<script setup name="DaDatasource">
  import {getDaDatasource } from "@/api/da/datasource/daDatasource";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/da/datasource/detail/componentOne.vue";
  import ComponentTwo from "@/views/da/datasource/detail/componentTwo.vue";

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
            getDaDatasourceDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      daDatasourceDetail: {
    },
    form: {},
  });

  const {  daDatasourceDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getDaDatasourceDetailById() {
        const _id = id ;
    getDaDatasource(_id).then(response => {
        daDatasourceDetail.value = response.data;
    });
  }

  getDaDatasourceDetailById();

</script>
