package org.throwable.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.nutz.ioc.aop.Aop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhangjinci
 * @version 2017/1/24 17:14
 * @function
 */
@Component
@Aspect
public class NutzAopAdapterAspect {


    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut(value = "@annotation(org.nutz.ioc.aop.Aop)")
    public void nutzAopPointCut() {
    }


    @Around(value = "nutzAopPointCut()")
    public Object executeNutzAopAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object target = joinPoint.getTarget();  //目标类
//        Class<?> targetClass = target.getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); //目标方法签名
        Method targetMethod = methodSignature.getMethod();
        Aop aop = targetMethod.getAnnotation(Aop.class);
        if (null == aop) {
            return joinPoint.proceed();
        } else {
            String[] value = aop.value();
            if (value.length == 0) {
                return joinPoint.proceed();
            } else {
                for (String name : value) {
                    Object executeBean = applicationContext.getBean(name);  //根据aop的string数组循环获取bean,然后调用execute方法
                    if (null != executeBean) {
                        Class<?> executeBeanClass = executeBean.getClass();
                        Method method = executeBeanClass.getMethod("execute", ProceedingJoinPoint.class);
                        return method.invoke(executeBean, joinPoint);
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
