<template>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component"></component>
    </keep-alive>
  </router-view>
</template>

<script lang="ts" setup>
const handleBeforeUnload = () => {
  // 刷新页面，删除 session
  let name = sessionStorage.getItem("name");
  if (name != null || name != "") {
    sessionStorage.removeItem("name");
    sessionStorage.clear();
  }
};
window.addEventListener("beforeunload", handleBeforeUnload);
</script>

<style lang="scss">
html,
body,
#app {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;

  --el-border-radius-base: 0px;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
