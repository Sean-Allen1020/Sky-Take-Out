package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller方法，还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是Controller方法，直接放行 (实际上就是，放行静态资源)
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // 令牌判空
        if (token == null || token.isEmpty()) {
            // 不通过，响应401状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户id：", userId);

            //2. 将userId存入线程局部存储空间
            BaseContext.setCurrentId(userId);

            //3、通过，放行
            return true;
        }
        catch (ExpiredJwtException e) {
            // token 过期
            log.info("令牌过期");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        catch (SignatureException e) {
            // 签名不对，可能被篡改，或者密钥不匹配
            log.info("签名不对，可能被篡改，或者密钥不匹配");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        catch (JwtException | IllegalArgumentException e) {
            // 其余 JWT 非法情况
            log.info("其余Jwt非法情况");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
