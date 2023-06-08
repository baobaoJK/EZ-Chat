<template>
  <div class="chat">
    <div class="list-pane">
      <div class="user-pane">
        <div class="user-count">
          <h2>当前在线人数：{{ userCount }}</h2>
        </div>
        <div class="user-list">
          <div class="user" v-for="user in userList" :key="user">
            <el-image
              class="user-img"
              :src="require('@/assets/images/user.png')"
            ></el-image>
            <p class="username">{{ user }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="chat-pane">
      <div class="chat-header">
        <h2>EZ-Chat - {{ nickname }}</h2>
      </div>
      <div class="chat-message" ref="chatHistory">
        <div class="user-message" v-for="message in messages" :key="message">
          <div class="img">
            <el-image
              class="user-img"
              :src="require('@/assets/images/user.png')"
            ></el-image>
          </div>
          <div class="message">
            <div class="username">
              {{ message.name }} <span class="time">{{ message.time }}</span>
            </div>
            <div class="text user-text" v-if="nickname === message.name">
              {{ message.msg }}
            </div>
            <div class="text" v-if="nickname !== message.name">
              {{ message.msg }}
            </div>
          </div>
        </div>
      </div>
      <div class="chat-textarea">
        <el-input
          v-model="text"
          class="user-textarea"
          type="textarea"
          resize="none"
          @keydown.enter="sendButton"
        ></el-input>
        <el-button type="primary" class="send-button" @click="sendButton"
          >发送</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onActivated } from "vue";
import router from "@/router";

let nickname = ref();
let socket: WebSocket;

onActivated(() => {
  nickname.value = sessionStorage.getItem("name");
  // 查询是否设置了昵称
  if (nickname.value == null) {
    router.push("/");
    return;
  }

  // 查询浏览器是否支持 WebSocket
  if (typeof WebSocket == "undefined") {
    alert("您的浏览器不支持 WebSocket");
    router.push("/");
    return;
  }

  // 开启 WebSocket 服务
  let socketHost = "localhost";
  let socketPort = "8888";
  let socketUrl =
    "ws://" + socketHost + ":" + socketPort + "/socket/" + nickname.value;
  socket = new WebSocket(socketUrl);

  // 连接服务器
  socket.onopen = () => {
    console.log("已连接至服务器");
  };

  // 浏览器接收服务端发送的消息
  socket.onmessage = (msg) => {
    let data = JSON.parse(msg.data);
    if (data.userlist) {
      // 接收用户列表消息
      userList.value = data.userlist;
      userCount.value = data.userlist.length;
    } else {
      // 接收消息
      messages.value.push(data);

      // 获取节点
      let chatHistory = document.getElementsByClassName("chat-message")[0];
      if (chatHistory.scrollHeight >= chatHistory.clientHeight) {
        setTimeout(function () {
          //设置滚动条到最底部
          chatHistory.scrollTop = chatHistory.scrollHeight;
        }, 0);
      }
    }
  };
  // 关闭服务
  socket.onclose = () => {
    console.log("WebSocket 服务已关闭");
  };
  // 错误事件
  socket.onerror = () => {
    console.log("WebSocket 服务发生错误");
  };
});

// 日期转换
const formatTime = (date: Date) => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  return (
    [year, month, day].map(formatNumber).join("-") +
    " " +
    [hour, minute, second].map(formatNumber).join(":")
  );
};
const formatNumber = (n: number) => {
  const s = n.toString();
  return s[1] ? s : "0" + s;
};

// 用户数量
let userCount = ref(0);

// 用户列表
let userList = ref([]);

// 信息框
let text = ref("");

// 信息列表
let messages = ref([]);

// 信息
let message = {
  name: "",
  time: "",
  msg: "",
};

// 发送信息
const sendButton = (event: { preventDefault: () => void }) => {
  event.preventDefault();

  if (text.value != null && text.value !== "" && nickname.value != null) {
    message.name = nickname.value;
    message.time = formatTime(new Date());
    message.msg = text.value;
    socket.send(JSON.stringify(message));
    message.msg = "";
    text.value = "";
  }
};
</script>

<style lang="scss">
@import "../assets/css/chat";
</style>
