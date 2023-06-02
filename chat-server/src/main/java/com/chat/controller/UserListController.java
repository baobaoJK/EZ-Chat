package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.chat.component.WebSocketServer.sessionMap;

/**
 * @author KSaMar
 * @version 1.0
 * 用户列表控制器
 */
@RestController
@RequestMapping("/list")
public class UserListController {

    /**
     * 判断用户名是否存在与聊天室
     * @param username 用户名
     * @return json
     */
    @GetMapping("/{username}")
    public JSONObject getUsername(@PathVariable("username") String username) {
        JSONObject jsonObject = new JSONObject();
        boolean isEmpty = sessionMap.isEmpty();
        jsonObject.put("isEmpty", isEmpty);
        jsonObject.put("isExist", false);
        if (!isEmpty) {
            boolean isExist = sessionMap.containsKey(username);
            jsonObject.replace("isExist", isExist);
        }
        return jsonObject;
    }

}
