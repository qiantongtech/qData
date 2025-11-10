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
    <dp-main>
        <dp-shrink width="260px" placement="left">
            <template #flex>
                <el-table class="video-list" :data="videoList">
                    <el-table-column label="监控名称" prop="name" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column label="状态" align="center" width="80">
                        <template #default="scope">
                            <el-tag type="primary" v-if="scope.row.isOnline == '1'"> 在线 </el-tag>
                            <el-tag type="danger" v-else>离线</el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </template>

            <div class="video-main">
                <dp-grid @change="handleGridChange">
                    <div class="video-main--warpper" v-show="showVideoContent">
                        <dp-video-wrap
                            v-for="(item, index) in activeList"
                            class="dp-grid--item"
                            :ref="'video' + index"
                            :key="index"
                            :index="index"
                            :id="item.id"
                        >
                        </dp-video-wrap>
                    </div>
                </dp-grid>
            </div>
        </dp-shrink>
    </dp-main>
</template>

<script setup name="DetailPopResViewB">
    const videoList = ref([
        {
            id: '20eed7584c8d4cb992b531c762695345',
            pid: 'HP0016540280000028',
            objCode: '20eed7584c8d4cb992b531c762695345',
            name: '吉林台一级厂房尾水水位球机',
            isOnline: '1',
            objLvl: '4',
            objType: 'OBJ_WMST'
        },
        {
            id: '7103bcf6e6bc45a099103d86de3f145b',
            pid: 'HP0016540280000028',
            objCode: '7103bcf6e6bc45a099103d86de3f145b',
            name: '吉林台一级坝后全景球机',
            isOnline: '1',
            objLvl: '4',
            objType: 'OBJ_WMST'
        },
        {
            id: '85b968ef23c3451d88c3808003091c6c',
            pid: 'HP0016540280000028',
            objCode: '85b968ef23c3451d88c3808003091c6c',
            name: '吉一级水库大坝区域',
            isOnline: '1',
            objLvl: '4',
            objType: 'OBJ_WMST'
        },
        {
            id: '88a94712d64f4d288df4a4152539dc7f',
            pid: 'HP0016540280000028',
            objCode: '88a94712d64f4d288df4a4152539dc7f',
            name: '吉林台一级深表孔泄洪口球机',
            isOnline: '1',
            objLvl: '4',
            objType: 'OBJ_WMST'
        },
        {
            id: 'c11d84e354e74a96ae31e192f750e118',
            pid: 'HP0016540280000028',
            objCode: 'c11d84e354e74a96ae31e192f750e118',
            name: '吉一级大坝区域',
            isOnline: '1',
            objLvl: '4',
            objType: 'OBJ_WMST'
        }
    ]);
    const activeList = ref([]);
    const showVideoContent = ref(false);
    function handleGridChange(e) {
        activeList.value = [];
        showVideoContent.value = false;
        nextTick(() => {
            activeList.value = videoList.value.slice(0, e);
            showVideoContent.value = true;
        });
    }

    onMounted(() => {
        const rows = document.querySelectorAll('.video-list .el-table__body .el-table__row');
        rows.forEach((e, index) => {
            e.setAttribute('draggable', true);
            e.addEventListener('dragstart', (event) => {
                event.dataTransfer.setData('text/plain', videoList.value[index].id);
            });
        });
    });
</script>

<style lang="scss" scoped>
    .video-main {
        height: 100%;
        padding: var(--dp-main-gap);
        box-sizing: border-box;

        ::v-deep .dp-grid {
            height: 100%;
            .dp-grid--mode {
                justify-content: flex-end;
            }
        }

        .video-main--warpper {
            height: 100%;

            .video-wrap {
                background-color: #000;
            }
        }
    }
</style>
