package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.constant.WechatLoginVerifyConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 私有方法，用于获取openid
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        //调用微信接口服务，获得当前微信用户openid
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String jsonResult = HttpClientUtil.doGet(WechatLoginVerifyConstant.WX_LOGIN_URL, paramMap);

        //将jsonResult转换为json对象
        JSONObject jsonObject = JSON.parseObject(jsonResult);
        //从json对象中获取openid
        String openid = jsonObject.getString("openid");
        return openid;
    }

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //获取openid
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenid(openid);
        //判断用户对于应用是否为新用户
        if (user == null) {
            //如果是新用户，则自动注册用户信息
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
