import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import NameView from "../views/NameView.vue";
import ChatView from "../views/ChatView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "name",
    component: NameView,
    meta: {
      keepAlive: true,
    },
  },
  {
    path: "/chat",
    name: "chat",
    component: ChatView,
    meta: {
      keepAlive: true,
    },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
