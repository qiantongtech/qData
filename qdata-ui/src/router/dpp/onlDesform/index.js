/* Layout */
import Layout from "@/layout/index.vue";

// 数据自动填报公共路由
export default [
  {
    path: "/dpp/onlDesform/edit",
    component: Layout,
    hidden: true,
    redirect: "noredirect",
    children: [
      {
        path: "",
        component: () => import("@/views/dpp/onlDesform/components/designForm.vue"),
        name: "设计表单",
        meta: { title: '设计表单', activeMenu: '/dpp/onlDesform' }

      },
    ],
  },
  {
    path: "/dpp/onlDesform/data",
    component: Layout,
    hidden: true,
    redirect: "noredirect",
    children: [
      {
        path: "list/:desformCode",
        component: () => import("@/views/dpp/onlDesform/components/list/index.vue"),
        name: "设计表单列表",
        meta: { title: "设计表单列表" },
      },
    ],
  },
  {
    path: "/dpp/onlDesform/data/add/:desformCode",
    component: () => import("@/views/dpp/onlDesform/add/index.vue"),
    meta: { title: "设计表单添加" },
    hidden: true,
  },
  {
    path: "/dpp/onlDesform/data/edit/:desformCode/:dataId",
    component: () => import("@/views/dpp/onlDesform/edit/index.vue"),
    meta: { title: "设计表单编辑" },
    hidden: true,
  },
  {
    path: "/dpp/onlDesform/data/details/:desformCode/:dataId",
    component: () => import("@/views/dpp/onlDesform/detail/index.vue"),
    meta: { title: "设计表单详情" },
    hidden: true,
  },
  {
    path: "/dpp/onlDesform/data/success",
    component: () => import("@/views/dpp/onlDesform/components/success/index.vue"),
    meta: { title: "提交成功" },
    hidden: true,
  },
];
