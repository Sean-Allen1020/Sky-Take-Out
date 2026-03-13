package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointcutAutoFill() {
    }

    @Before("pointcutAutoFill()")
    public void autoFill(JoinPoint jp) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //1.获取被拦截到的mapper方法的数据库操作类型 insert or update ？
        Method method = ((MethodSignature) jp.getSignature()).getMethod();  //获取方法签名后获取方法元信息
        OperationType operationType = method.getAnnotation(AutoFill.class).value(); //获取注解的值

        //2.获取mapper方法的运行时参数 -- 实体对象
        Object[] enitys = jp.getArgs();
        //非空判定
        if(enitys.length == 0){
            return;
        }
        //获取实体
        Object enity = enitys[0];
        //3.通过反射，获取成员方法
        Method setCreateTime = enity.getClass().getMethod("setCreateTime", LocalDateTime.class);
        Method setUpdateTime = enity.getClass().getMethod("setUpdateTime", LocalDateTime.class);
        Method setCreateUser = enity.getClass().getMethod("setCreateUser", Long.class);
        Method setUpdateUser = enity.getClass().getMethod("setUpdateUser", Long.class);

        //4.通过反射，对获取的实体对象的公共属性，根据不同操作类型，用反射进行赋值
        if (operationType == OperationType.INSERT) {
            setCreateTime.invoke(enity, LocalDateTime.now());
            setUpdateTime.invoke(enity, LocalDateTime.now());
            setCreateUser.invoke(enity, BaseContext.getCurrentId());
            setUpdateUser.invoke(enity, BaseContext.getCurrentId());
        }
        if (operationType == OperationType.UPDATE) {
            setUpdateTime.invoke(enity, LocalDateTime.now());
            setUpdateUser.invoke(enity, BaseContext.getCurrentId());
        }

    }

}
