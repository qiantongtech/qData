<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :span="24" class="card-box">
        <el-card>
          <template #header><Monitor style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ td('sys.monitor.cache.basicInfo') }}</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.redisVersion') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_version }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.runMode') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_mode == "standalone" ? td('sys.monitor.cache.standalone') : td('sys.monitor.cache.cluster') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.port') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.tcp_port }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.clientCount') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.connected_clients }}</div></td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.runDays') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.uptime_in_days }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.usedMemory') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.used_memory_human }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.usedCpu') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.memoryConfig') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.maxmemory_human }}</div></td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.aofEnabled') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.aof_enabled == "0" ? td('sys.monitor.cache.no') : td('sys.monitor.cache.yes') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.rdbSuccess') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.rdb_last_bgsave_status }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.keyCount') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.dbSize">{{ cache.dbSize }} </div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">{{ td('sys.monitor.cache.network') }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.instantaneous_input_kbps }}kps/{{cache.info.instantaneous_output_kbps}}kps</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box bottom">
        <el-card>
          <template #header><PieChart style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ td('sys.monitor.cache.commandStats') }}</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="commandstats" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box bottom">
        <el-card>
          <template #header><Odometer style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ td('sys.monitor.cache.memoryInfo') }}</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="usedmemory" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Cache">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { getCache } from '@/api/system/monitor/cache.js';
import * as echarts from 'echarts';
import useDefaultLang from "@/composables/useDefaultLang";

const { td, locale } = useDefaultLang();

const cache = ref([]);
const commandstats = ref(null);
const usedmemory = ref(null);
const { proxy } = getCurrentInstance();

let commandstatsIntance = null;
let usedmemoryInstance = null;

function getList() {
  proxy.$modal.loading(td('sys.monitor.cache.loadingMsg'));
  getCache().then(response => {
    proxy.$modal.closeLoading();
    cache.value = response.data;

    initCharts(response.data);

    window.addEventListener("resize", () => {
      commandstatsIntance?.resize();
      usedmemoryInstance?.resize();
    });
  })
}

function initCharts(data) {
  // Destroy old instance
  if (commandstatsIntance) {
    commandstatsIntance.dispose();
    commandstatsIntance = null;
  }
  if (usedmemoryInstance) {
    usedmemoryInstance.dispose();
    usedmemoryInstance = null;
  }

  commandstatsIntance = echarts.init(commandstats.value, "macarons");
  commandstatsIntance.setOption({
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    series: [
      {
        name: td('sys.monitor.cache.echarts.command'),
        type: "pie",
        roseType: "radius",
        radius: [15, 95],
        center: ["50%", "38%"],
        data: data.commandStats,
        animationEasing: "cubicInOut",
        animationDuration: 1000
      }
    ]
  });

  usedmemoryInstance = echarts.init(usedmemory.value, "macarons");
  usedmemoryInstance.setOption({
    tooltip: {
      formatter: "{b} <br/>{a} : " + cache.value.info.used_memory_human
    },
    series: [
      {
        name: td('sys.monitor.cache.echarts.peak'),
        type: "gauge",
        min: 0,
        max: 1000,
        detail: {
          formatter: cache.value.info.used_memory_human
        },
        data: [
          {
            value: parseFloat(cache.value.info.used_memory_human),
            name: td('sys.monitor.cache.echarts.memoryConsumption')
          }
        ]
      }
    ]
  });
}

// Monitor language changes and re-render charts
watch(locale, () => {
  if (cache.value && cache.value.info) {
    initCharts(cache.value);
  }
});

getList();
</script>
<style lang="scss" scoped>
.bottom {
  margin-bottom: 0;
}
</style>
