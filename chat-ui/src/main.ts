import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

const app = createApp(App);

//Element Plus
import ElementPlus from "element-plus"; // 导入 element-plus
import "element-plus/theme-chalk/index.css"; // 根据 node_modules 文件夹选择对应的 theme-chalk/index.css 文件
import zhCn from "element-plus/es/locale/lang/zh-cn"; // 设置 element-plus 语言
app.use(ElementPlus, { locale: zhCn }); // 设置

app.use(store).use(router).mount("#app");
