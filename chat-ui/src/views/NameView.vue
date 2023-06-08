<template>
  <div class="name">
    <el-form
      class="name-form"
      ref="nameFormRef"
      :model="nameForm"
      :rules="nameRules"
      @submit.prevent
    >
      <el-form-item>
        <h1>EZ-Chat</h1>
      </el-form-item>
      <el-form-item class="name-input" prop="nickname">
        <el-input
          class="nickname"
          v-model="nameForm.nickname"
          @keydown.enter="submitButton(nameFormRef)"
          placeholder="输入一个昵称"
        ></el-input>
        <el-button
          class="submit"
          type="primary"
          @click="submitButton(nameFormRef)"
          >进入</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import router from "@/router";
import axios from "axios";

// 昵称表单
const nameFormRef = ref<FormInstance>();

// 昵称表单
const nameForm = reactive({
  nickname: "",
});

// 表单校验
const nameRules = reactive<FormRules>({
  nickname: [{ required: true, message: "请输入一个昵称", trigger: "blur" }],
});

// 登录操作
const submitButton = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      axios
        .get("http://localhost:8888/list/" + nameForm.nickname)
        .then((resp) => {
          const data = resp.data;

          // 判断用户名是否存在
          if (!data.isExist) {
            sessionStorage.setItem("name", nameForm.nickname);
            router.push("/chat");
          } else {
            ElMessage({
              message: "该用户名已存在，请更换",
              grouping: true,
              type: "error",
            });
          }
        });
    } else {
      ElMessage({
        message: "请输入一个昵称",
        grouping: true,
        type: "error",
      });
      return false;
    }
  });
};
</script>
<style lang="scss">
@import "../assets/css/name";
</style>
