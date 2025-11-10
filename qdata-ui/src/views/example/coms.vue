<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please contact the official team for authorization.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <el-button @click="show1=!show1">测试基础组件</el-button>
    <el-button @click="show2=!show2">测试衍生组件</el-button>
    <dp-detail-layout v-model="show1" :config="config" :menu="menu" />

    <dp-detail-pop-res v-if="show2">
    </dp-detail-pop-res>
</template>

<script setup>
    const show1 = ref(false)
    const show2 = ref(false)

    const config = {
        container: {
            title: '水库详情',
            width: '80%',
            direction: 'horizontal'
        }
    };

    const menu = {
        data: [
            {
                label: '水情信息',
                value: 'a',
                icon: 'res/icon1.png',
                children: [
                    {
                        label: '实时过程',
                        value: 'a1'
                    },
                    {
                        label: '均值过程',
                        value: 'a2'
                    },
                    {
                        label: '同期对比',
                        value: 'a3'
                    },
                    {
                        label: '极值系列',
                        value: 'a4'
                    }
                ]
            },
            {
                label: '视频监控',
                value: 'b',
                icon: 'res/icon2.png'
            },
            {
                label: '坝体监测',
                value: 'c',
                icon: 'res/icon3.png',
                children: [
                    {
                        label: '测点位移',
                        value: 'c1'
                    },
                    {
                        label: '测压管过程线',
                        value: 'c2'
                    },
                    {
                        label: '渗压过程线',
                        value: 'c3'
                    },
                    {
                        label: '裂缝开合度过程线',
                        value: 'c4'
                    },
                    {
                        label: '应变过程线',
                        value: 'c5'
                    },
                    {
                        label: '浸润线',
                        value: 'c6'
                    },
                    {
                        label: '扬压力',
                        value: 'c7'
                    }
                ]
            },
            {
                label: '库容曲线',
                value: 'd',
                icon: 'res/icon4.png'
            },
            {
                label: '基础信息',
                value: 'e',
                icon: 'res/icon5.png'
            }
        ],
        components: shallowRef({
            a1: defineAsyncComponent(() => import('./components/view-a1.vue')),
            a2: defineAsyncComponent(() => import('./components/view-a2.vue')),
            a3: defineAsyncComponent(() => import('./components/view-a3.vue')),
            a4: defineAsyncComponent(() => import('./components/view-a4.vue')),
            b: defineAsyncComponent(() => import('./components/view-b.vue')),
            c1: defineAsyncComponent(() => import('./components/view-c1.vue')),
            c2: defineAsyncComponent(() => import('./components/view-c2.vue')),
            c3: defineAsyncComponent(() => import('./components/view-c3.vue')),
            c4: defineAsyncComponent(() => import('./components/view-c4.vue')),
            c5: defineAsyncComponent(() => import('./components/view-c5.vue')),
            c6: defineAsyncComponent(() => import('./components/view-c6.vue')),
            c7: defineAsyncComponent(() => import('./components/view-c7.vue')),
            d: defineAsyncComponent(() => import('./components/view-d.vue')),
            e: defineAsyncComponent(() => import('./components/view-e.vue'))
        }),
        defaultActive: 'a1',
        defaultOpeneds: ['a']
    };
</script>




    
