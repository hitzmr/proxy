package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Client {
    public static void main(String[] args) {
        IActor iActor1 = new ActorImpl();
        iActor1.basicAct(100f);
        iActor1.dangrtAct(300f);

        /**
         * 动态代理：
         *      作用：在不改变源码的基础上，对已有方法加强。（它是AOP思想的实现技术）
         *      分类：
         *          1、基于接口的动态代理
         *              要求：被代理类至少实现了一个接口
         *              提供者：JDK
         *              涉及的类：Proxy
         *              创建代理对象的方法：newProxyInstance(Classloader, Class[], InvocationHandler)
         *                          参数含义：ClassLoader。类加载器。和被代理对象使用相同的加载器。一般都是固定写法。
         *                                  Class[]。字节码数组。被代理类实现的接口（要求被代理类和代理类有相同的行为）。一般都是固定写法。
         *                            InvocationHandler。它是一个接口，用于我们提供增强代码的。我们一般都是写一个该接口的实现类。实现类可以是匿名内部类。
         *                                              在此处，使用了策略模式（数据已经有了，要达成一个明确的目标，而达成目标的过程就是一种策略）
         *          2、基于子类的动态代理
         *              要求：被代理类不能是最终类(final)
         *              提供者：CGLIB
         *              涉及的类：Enhancer
         *              创建代理对象的方法：create(Class, Callback)
         *                      Class:被代理对象的字节码
         *                      Callback：如何代理。它和InvocationHandler作用是一样的。它也是一个接口，我们一般使用该接口的子接口MethodInterceptor
         *                                 在使用时我们也是创建该接口的匿名内部类
         */
        IActor iActor2 = (IActor) Proxy.newProxyInstance(iActor1.getClass().getClassLoader(), iActor1.getClass().getInterfaces(), new CustomInvocationHandler(iActor1));
        iActor2.basicAct(200f);
        iActor2.dangrtAct(500f);

        IActor iActor3 = (IActor) Proxy.newProxyInstance(iActor1.getClass().getClassLoader(),
                iActor1.getClass().getInterfaces(),
                new InvocationHandler() {
                    private Object retValue = null;

                    /**
                     *
                     * @param proxy
                     * @param method
                     * @param args
                     * @return
                     * @throws Throwable
                     * 执行被代理对象的任何方法都会经过这个方法，此方法有拦截功能。
                     *  方法的参数：
                     *      Object proxy：代理对象的引用。不一定每次都会有
                     *      Method method：当前执行的方法
                     *      Object[] args：当前执行方法所需的参数
                     *   返回值：
                     *      当前执行方法的返回值
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        float money = (float) args[0];

                        if ("basicAct".equals(method.getName())) {
                            if (money > 1000) {
                                retValue = method.invoke(iActor1, money);

                            }
                        } else if ("dangrtAct".equals(method.getName())) {
                            if (money > 3000) {
                                retValue = method.invoke(iActor1, money);
                            }
                        }
                        return retValue;
                    }
                });

        iActor3.basicAct(1000f);
        iActor3.dangrtAct(3000f);
        iActor3.basicAct(2000f);
        iActor3.dangrtAct(4000f);
    }
}

class CustomInvocationHandler implements InvocationHandler {
    private Object handler;

    public CustomInvocationHandler(Object handler) {
        this.handler = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(handler, args);
        return null;
    }
}
