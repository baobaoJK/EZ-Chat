package com.chat.component;

import com.alibaba.fastjson.JSON;
import com.chat.entity.Message;
import com.chat.entity.UserList;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KSaMar
 * @version 1.0
 * WebSocket 服务
 */
@Component
@ServerEndpoint("/socket/{username}")
public class WebSocketServer {
    /**
     * 存储对象 map
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /***
     * WebSocket 建立连接事件
     * 1.把登录的用户存到 sessionMap 中
     * 2.发送给所有人当前登录人员信息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {

        // 搜索名称是否存在
        boolean isExist = sessionMap.containsKey(username);
        if (!isExist) {
            System.out.println(username + "加入了聊天室");
            sessionMap.put(username, session);
            sendAllMessage(setUserList());
            showUserList();
        }
    }

    /**
     * WebSocket 关闭连接事件
     * 1.把登出的用户从 sessionMap 中剃除
     * 2.发送给所有人当前登录人员信息
     */
    @OnClose
    public void onClose(@PathParam("username") String username) {
        if (username != null) {
            System.out.println(username + "退出了聊天室");
            sessionMap.remove(username);
            sendAllMessage(setUserList());
            showUserList();
        }
    }

    /**
     * WebSocket 接受信息事件
     * 接收处理客户端发来的数据
     * @param message 信息
     */
    @OnMessage
    public void onMessage(String message) {
        Message msg = JSON.parseObject(message, Message.class);
        sendAllMessage(JSON.toJSONString(msg));
    }

    /**
     * WebSocket 错误事件
     * @param session 用户 Session
     * @param error 错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 显示在线用户
     */
    private void showUserList() {
        System.out.println("------------------------------------------");
        System.out.println("当前在线用户");
        System.out.println("------------------------------------------");
        for (String username : sessionMap.keySet()) {
            System.out.println(username);
        }
        System.out.println("------------------------------------------");
        System.out.println();
    }

    /**
     * 设置接收消息的用户列表
     * @return 用户列表
     */
    private String setUserList(){
        ArrayList<String> list = new ArrayList<>(sessionMap.keySet());
        UserList userList = new UserList();
        userList.setUserlist(list);
        return JSON.toJSONString(userList);
    }

    /**
     * 发送消息到所有用户种
     * @param message 消息
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
