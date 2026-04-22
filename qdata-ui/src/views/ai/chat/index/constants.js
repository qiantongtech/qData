import { Histogram, Opportunity } from "@element-plus/icons-vue";

export const CHAT_TYPES = [
  {
    value: "chart",
    label: "智能图表",
    icon: Histogram,
    disabled: false,
  },
  {
    value: "smart",
    label: "智能问答",
    icon: Opportunity,
    disabled: true,
  },
];
