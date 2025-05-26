<template>
  <div class="c-bottom">
    <div class="box-wrap bottom">
      <div class="box-title">
        <div class="box-name">自治区监测数据月到报率汇总</div>
        <div class="box-tabs">
          <div :class="['tab', { act: tabAct == item.value }]" @click="boxTabChange(item, index)"
            v-for="(item, index) in tabsData" :key="index">{{ item.label }}</div>
        </div>
      </div>
      <div class="box-con">
        <el-carousel height="240px" :autoplay="false" arrow="always" indicator-position="always">
          <el-carousel-item :class="['bot-wrap']" v-for="(item, index) in carouselData" :key="index"
            indicator-position="outside">
            <div class="bot-item" v-for="(bot, key) in bottomData(index)" :key="key">
              <div class="bot-left">{{ bot.label }} - {{ moon }}</div>
              <div class="bot-right">
                <div class="b-tabs" v-if="bot.tabs && bot.tabs.length > 0">
                  <div :class="['tab', { act: bot.tabsAct == item.value }]" @click="bot.tabsAct = item.value"
                    v-for="(item, index) in bot.tabs" :key="index">{{ item.label }}</div>
                </div>
                <div class="b-table">
                  <div class="b-thead">
                    <div class="tr">
                      <div :class="['td']" :style="{ width: item.width }"
                        v-for="(item, index) in bot.tableColumn || bot.tabs[bot.tabsAct].tableColumn" :key="index">{{
                          item.label }}</div>
                    </div>
                  </div>
                  <div class="b-tbody"
                    :style="{ height: bot.tabs && bot.tabs.length > 0 ? 'calc(170px - 54px)' : '170px' }">
                    <div class="tr" v-for="(item, index) in bot.data || bot.tabs[bot.tabsAct].data"
                      :key="'thead' + index">
                      <div class="td ellipsis" :style="{ width: key.width }"
                        v-for="(key, keyIndex) in bot.tableColumn || bot.tabs[bot.tabsAct].tableColumn"
                        :key="'tbody' + keyIndex" :title="item[key.value]">
                        <span
                          :class="['odds', { warn: item[key.value] >= 20 && item[key.value] <= 50, error: item[key.value] < 20 }]"
                          v-if="key.value == 'odds'">{{ item[key.value] }}%</span>
                        <span v-else>{{ item[key.value] || "-" }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script setup name="CockpitBottom">
import moment from "moment";
const moon = ref(moment().format("MM月"));
const tabAct = ref(0);
const tabsData = ref([
  {
    label: "水资源",
    value: 0,
    children: [
      {
        label: "取水口到报率",
        value: 0,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "11",
            val3: "8",
            odds: Math.round((8 / 11) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "10",
            val3: "7",
            odds: Math.round((7 / 10) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "40",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "34",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "60",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
      {
        label: "机电井到报率",
        value: 1,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "31",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "100",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "54",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "50",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "76",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },

      {
        label: "总体到报率",
        value: 0,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "34",
            val2: "19",
            val3: "3",
            odds: Math.round((3 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "43",
            val2: "36",
            val3: "25",
            odds: Math.round((25 / 36) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "52",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "45",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "71",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "53",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
  {
    label: "水文监测",
    value: 1,
    children: [
      {
        label: "河道水情到报率",
        value: 0,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "34",
            val2: "19",
            val3: "3",
            odds: Math.round((3 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "43",
            val2: "36",
            val3: "25",
            odds: Math.round((25 / 36) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "52",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "45",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "71",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "53",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
      {
        label: "水库水情到报率",
        value: 1,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "11",
            val3: "8",
            odds: Math.round((8 / 11) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "10",
            val3: "7",
            odds: Math.round((7 / 10) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "40",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "34",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "60",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],        // tabsAct: 0,
        // tabs: [
        //   {
        //     label: "水文局水库水情",
        //     value: 0,
        //     tableColumn: [
        //       {
        //         label: "地州",
        //         value: "val0",
        //         width: "25%",
        //       },
        //       {
        //         label: "备案数",
        //         value: "val1",
        //         width: "25%",
        //       },
        //       {
        //         label: "应到报数",
        //         value: "val2",
        //         width: "25%",
        //       },
        //       {
        //         label: "实到报数",
        //         value: "val3",
        //         width: "25%",
        //       },
        //       {
        //         label: "到报率",
        //         value: "odds",
        //         width: "25%",
        //       },
        //     ],
        //     data: [
        //       {
        //         val0: "和田地区",
        //         val1: "12",
        //         val2: "10",
        //         val3: "99",
        //         odds: Math.round((10 / 99) * 100),
        //       },
        //       {
        //         val0: "乌鲁木齐市",
        //         val1: "15",
        //         val2: "67",
        //         val3: "85",
        //         odds: Math.round((67 / 85) * 100),
        //       },
        //       {
        //         val0: "克拉玛依市",
        //         val1: "16",
        //         val2: "68",
        //         val3: "30",
        //         odds: Math.round((68 / 30) * 100),
        //       },
        //       {
        //         val0: "吐鲁番地区",
        //         val1: "12",
        //         val2: "91",
        //         val3: "32",
        //         odds: Math.round((91 / 32) * 100),
        //       },
        //     ],
        //   },
        //   {
        //     label: "地州推送水库水情",
        //     value: 1,
        //     tableColumn: [
        //       {
        //         label: "地州",
        //         value: "val0",
        //         width: "25%",
        //       },
        //       {
        //         label: "备案数",
        //         value: "val1",
        //         width: "25%",
        //       },
        //       {
        //         label: "应到报数",
        //         value: "val2",
        //         width: "25%",
        //       },
        //       {
        //         label: "实到报数",
        //         value: "val3",
        //         width: "25%",
        //       },
        //       {
        //         label: "到报率",
        //         value: "odds",
        //         width: "25%",
        //       },
        //     ],
        //     data: [
        //       {
        //         val0: "和田地区",
        //         val1: "12",
        //         val2: "10",
        //         val3: "99",
        //         odds: Math.round((10 / 99) * 100),
        //       },
        //       {
        //         val0: "乌鲁木齐市",
        //         val1: "15",
        //         val2: "67",
        //         val3: "85",
        //         odds: Math.round((67 / 85) * 100),
        //       },
        //       {
        //         val0: "克拉玛依市",
        //         val1: "16",
        //         val2: "68",
        //         val3: "30",
        //         odds: Math.round((68 / 30) * 100),
        //       },
        //       {
        //         val0: "吐鲁番地区",
        //         val1: "12",
        //         val2: "91",
        //         val3: "32",
        //         odds: Math.round((91 / 32) * 100),
        //       },
        //     ],
        //   },
        // ],
      },
      {
        label: "墒情到报率",
        value: 2,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "31",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "100",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "54",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "50",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "76",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
      {
        label: "雨情到报率",
        value: 3,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "31",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "100",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "54",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "50",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "76",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
      {
        label: "地下水生态监测到报率",
        value: 4,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "11",
            val3: "8",
            odds: Math.round((8 / 11) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "10",
            val3: "7",
            odds: Math.round((7 / 10) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "40",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "34",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "60",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
  {
    label: "水旱灾害防御",
    value: 2,
    children: [
      {
        label: "河道水情",
        value: 0,
        tabsAct: 0,
        tabs: [
          {
            label: "水位站到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "应到报数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "99",
                val2: "88",
                val3: "56",
                odds: Math.round((56 / 88) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "90",
                val2: "87",
                val3: "67",
                odds: Math.round((67 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "16",
                val2: "68",
                val3: "30",
                odds: Math.round((15 / 30) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "12",
                val2: "91",
                val3: "32",
                odds: Math.round((20 / 30) * 100),
              },
            ],
          },
          {
            label: "河道水文站到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "应到报数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "雨情 雨量站到报率",
        value: 1,
        tableColumn: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "25%",
          },
          {
            label: "应到报数",
            value: "val2",
            width: "25%",
          },
          {
            label: "实到报数",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "11",
            val3: "8",
            odds: Math.round((8 / 11) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "10",
            val3: "7",
            odds: Math.round((7 / 10) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "40",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "34",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "60",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
  {
    label: "水库大坝安全监测",
    value: 3,
    children: [
      {
        label: "渗流监测",
        value: 0,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "渗流监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "渗流监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装渗流点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报渗流点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "渗压监测",
        value: 1,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "渗压监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "34",
                val2: "19",
                val3: "3",
                odds: Math.round((3 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "43",
                val2: "36",
                val3: "25",
                odds: Math.round((25 / 36) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "52",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "45",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "71",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "53",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "渗压监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装渗压点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报渗压点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "垂直位移监测",
        value: 2,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "垂直位移监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "垂直位移监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装垂直位移监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报垂直位移监测点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "水平位移监测",
        value: 3,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水平位移监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水平位移监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装水平位移监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报水平位移监测点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "硬力监测",
        value: 4,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "硬力监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "34",
                val2: "19",
                val3: "3",
                odds: Math.round((3 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "43",
                val2: "36",
                val3: "25",
                odds: Math.round((25 / 36) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "52",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "45",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "71",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "53",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "硬力监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装硬力　　监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报硬力　　监测点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "补压力监测",
        value: 5,
        tabsAct: 0,
        tabs: [
          {
            label: "水库到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "补压力监测",
                value: "val2",
                width: "25%",
              },
              {
                label: "到报水库",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "补压力监测点到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水库数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装补压力 监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报补压力 监测点",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
    ],
  },
  {
    label: "农村饮水安全",
    value: 4,
    children: [
      {
        label: "进水口水量监测",
        value: 0,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂进水口水量监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装进水口 水量监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "出水口水量监测",
        value: 1,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "34",
                val2: "19",
                val3: "3",
                odds: Math.round((3 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "43",
                val2: "36",
                val3: "25",
                odds: Math.round((25 / 36) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "52",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "45",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "71",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "53",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂出水口水量监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装出水口 水量监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "蓄水池水质",
        value: 2,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂蓄水池水质监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装蓄水池 水质监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "34",
                val2: "19",
                val3: "3",
                odds: Math.round((3 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "43",
                val2: "36",
                val3: "25",
                odds: Math.round((25 / 36) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "52",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "45",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "71",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "53",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "进水口水质",
        value: 3,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "34",
                val2: "19",
                val3: "3",
                odds: Math.round((3 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "43",
                val2: "36",
                val3: "25",
                odds: Math.round((25 / 36) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "52",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "45",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "71",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "53",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂进水口水质监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装进水口 水质监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "出水口水质",
        value: 4,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂出水口水质监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装出水口 水质监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "进水口压力监测",
        value: 5,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂进水口压力监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装进水口 压力监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "出水口压力监测",
        value: 6,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂出水口压力监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装出水口 压力监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
      {
        label: "输水管压力监测",
        value: 7,
        tabsAct: 0,
        tabs: [
          {
            label: "水厂到报率",
            value: 0,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "备案数",
                value: "val1",
                width: "25%",
              },
              {
                label: "水厂座数",
                value: "val2",
                width: "25%",
              },
              {
                label: "实到报数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "31",
                val2: "19",
                val3: "18",
                odds: Math.round((18 / 19) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "100",
                val2: "87",
                val3: "85",
                odds: Math.round((85 / 87) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "54",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "50",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "76",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
          {
            label: "水厂输水管压力监测点位到报率",
            value: 1,
            tableColumn: [
              {
                label: "地州",
                value: "val0",
                width: "25%",
              },
              {
                label: "水厂数量",
                value: "val1",
                width: "25%",
              },
              {
                label: "安装输水管 压力监测点",
                value: "val2",
                width: "25%",
              },
              {
                label: "实报点数",
                value: "val3",
                width: "25%",
              },
              {
                label: "到报率",
                value: "odds",
                width: "25%",
              },
            ],
            data: [
              {
                val0: "和田地区",
                val1: "12",
                val2: "11",
                val3: "8",
                odds: Math.round((8 / 11) * 100),
              },
              {
                val0: "乌鲁木齐市",
                val1: "15",
                val2: "10",
                val3: "7",
                odds: Math.round((7 / 10) * 100),
              },
              {
                val0: "克拉玛依市",
                val1: "40",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "吐鲁番地区",
                val1: "34",
                val2: "31",
                val3: "31",
                odds: Math.round((31 / 31) * 100),
              },
              {
                val0: "塔城地区",
                val1: "60",
                val2: "38",
                val3: "30",
                odds: Math.round((30 / 38) * 100),
              },
              {
                val0: "哈密市",
                val1: "42",
                val2: "41",
                val3: "32",
                odds: Math.round((32 / 41) * 100),
              },
            ],
          },
        ],
      },
    ],
  },
  {
    label: "小水电生态监管",
    value: 5,
    children: [
      {
        label: "小水电生态监管",
        value: 0,
        tableColumn: [
          {
            label: "名称",
            value: "val0",
            width: "25%",
          },
          {
            label: "生态指标",
            value: "val1",
            width: "25%",
          },
          {
            label: "当月到报率",
            value: "odds",
            width: "25%",
          },
          {
            label: "流量达标天数",
            value: "val3",
            width: "25%",
          },
          {
            label: "实际到报天数",
            value: "val2",
            width: "25%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "11",
            val3: "8",
            odds: Math.round((8 / 11) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "10",
            val3: "7",
            odds: Math.round((7 / 10) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "40",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "34",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "60",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "42",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
]);
const boxTabChange = (item, index) => {
  tabAct.value = index;
  // 循环把botTabAct设置为0
  if (tabsData.value[index].tabs && tabsData.value[index].tabs.length > 0) {
    for (let i = 0; i < tabsData.value[index].tabs.length; i++) {
      tabsData.value[index].tabs[i].tabsAct = 0;
    }
  }
};
const carouselData = computed(() => {
  return Math.ceil(tabsData.value[tabAct.value].children.length / 3);
});
const bottomData = (index) => {
  return tabsData.value[tabAct.value].children.slice(index * 3, (index + 1) * 3);
};
</script>

<style scoped lang="scss">
.c-bottom {
  // position: absolute;
  // left: 10px;
  // right: 20px;
  // bottom: 20px;
  margin: 0 auto;
  padding: 10px 20px 0 10px;

  .bottom {
    background: url("@/assets/cockpit/box (1).png") no-repeat;
    background-size: 100% 100%;
    width: 100%;
    height: 317px;

    .box-tabs {
      display: flex;
      justify-content: space-between;
      padding-top: 6px;

      .tab {
        user-select: none;
        cursor: pointer;
        margin-left: 1px;
        width: 150px;
        height: 32px;
        line-height: 32px;
        text-align: center;
        font-family: PingFangSC;
        font-weight: 500;
        font-size: 14px;
        color: #fdffff;
        background: url("@/assets/cockpit/bottom-tab.png") no-repeat;
        background-size: 100% 100%;

        &.act {
          background: url("@/assets/cockpit/bottom-tab-act.png") no-repeat;
          background-size: 100% 100%;
        }
      }
    }

    .box-con {
      padding: 0 0 0 24px;

      :deep(.el-carousel) {
        padding-right: 40px;

        .el-carousel__indicators--horizontal {
          bottom: -15px;
        }

        .el-carousel__arrow--left {
          left: auto;
          right: -4px;
          top: 35%;
          background: url("@/assets/cockpit/arrow-l.png") no-repeat;
          background-size: 100% 100%;
          width: 70px;
          height: 70px;

          .el-icon {
            display: none;
          }
        }

        .el-carousel__arrow--right {
          right: -4px;
          top: 65%;
          background: url("@/assets/cockpit/arrow-r.png") no-repeat;
          background-size: 100% 100%;
          width: 70px;
          height: 70px;

          .el-icon {
            display: none;
          }
        }
      }
    }

    .bot-wrap {
      display: flex;
      justify-content: space-around;
      align-items: center;

      .bot-item {
        // width: 33%;
        height: 100%;
        // margin-right: 40px;
        display: flex;
        align-items: center;

        .bot-left {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 34px;
          height: 238px;
          font-family: PingFang SC;
          font-weight: 400;
          text-align: center;
          font-size: 16px;
          color: #ffffff;
          line-height: 20px;
          padding: 0 10px;
          background: url("@/assets/cockpit/bot-left.png") no-repeat;
          background-size: 100% 100%;
          margin-right: 20px;
        }

        .bot-right {
          .b-tabs {
            width: fit-content;
            max-width: 500px;
            height: 38px;
            padding: 0 8px;
            margin: 0 auto 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: url("@/assets/cockpit/tabs.png") no-repeat;
            background-size: 100% 100%;

            .tab {
              user-select: none;
              cursor: pointer;
              padding: 0 20px;
              min-width: 130px;
              height: 37px;
              line-height: 37px;
              text-align: center;
              font-family: PingFangSC;
              font-weight: 500;
              font-size: 14px;
              color: #fdffff;
              text-shadow: 4px 0px 6px #02293e;
              letter-spacing: 1px;

              &.act {
                background: url("@/assets/cockpit/tab.png") no-repeat;
                background-size: 100% 100%;
              }
            }
          }

          .b-table {
            width: 504px;

            .tr {
              display: flex;
              justify-content: space-between;
              align-items: center;
              height: 45px;
              padding: 0 5px 0 10px;

              .td {
                padding: 0 6px;
                text-align: center;
              }
            }

            .b-thead {
              width: 100%;
              background: url("@/assets/cockpit/bthead.png") no-repeat;
              background-size: 101% 100%;
              margin-left: -4px;
              margin-bottom: 5px;

              .td {
                font-family: sharp;
                font-weight: bold;
                font-size: 14px;
                color: #ffffff;
                text-shadow: 2px 0px 8px #051c37;
              }
            }

            .b-tbody {
              height: 170px;
              overflow: hidden auto;

              &::-webkit-scrollbar {
                width: 0px;
              }

              .tr {
                width: 500px;
                height: 34px;
                background: url("@/assets/cockpit/btbody.png") no-repeat;
                background-size: 100% 100%;
                margin-bottom: 10px;
                padding: 0 0;

                &:last-child {
                  margin-bottom: 0;
                }

                .td {
                  font-family: PingFangSC;
                  font-weight: 400;
                  font-size: 14px;
                  color: #cae5ff;
                }
              }
            }

            .odds {
              &.success {
                color: #2af267;
              }

              &.error {
                color: #ff2626;
              }

              &.warn {
                color: #e7c125;
              }
            }
          }
        }
      }
    }
  }
}
</style>
