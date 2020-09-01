package com.vailter.standard.learn.proxy.dynamic;

import com.vailter.standard.learn.proxy.dynamic.cglib.CglibDynamicProxy;
import com.vailter.standard.learn.proxy.dynamic.jdk.HelloService;
import com.vailter.standard.learn.proxy.dynamic.jdk.HelloServiceImpl;
import com.vailter.standard.learn.proxy.dynamic.jdk.JdkDynamicProxy;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * jdk动态代理不需要引入第三方包，需要实现InvocationHandler接口，要求被代理对象必须实现接口
 * cglib动态代理需要引入第三方包，需要实现MethodInterceptor接口，被代理对象可以实现接口，也可以不实现接口
 */
public class ProxyTest {
    public static void main(String[] args) {

        JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy(new HelloServiceImpl());
        Object target = jdkDynamicProxy.getTarget();
        HelloService proxy = (HelloService) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), jdkDynamicProxy);
        proxy.sayHi("小王");
        proxy.sayHi();

        CglibDynamicProxy cglibDynamicProxy = new CglibDynamicProxy(new HelloServiceImpl());
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(cglibDynamicProxy.getTarget().getClass().getInterfaces());
        enhancer.setCallback(cglibDynamicProxy);
        HelloService proxy1 = (HelloService) enhancer.create();
        String res = proxy1.sayHi("小王");
        System.out.println(res);
    }
}
