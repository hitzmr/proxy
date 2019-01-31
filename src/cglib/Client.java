package cglib;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Client {
    public static void main(String[] args) {
        Actor actor = new Actor();
        Actor actor1 = (Actor) Enhancer.create(actor.getClass(), new MethodInterceptor() {
            Object retValue = null;
            /**
             *
             * @param o
             * @param method
             * @param objects
             * @param methodProxy
             * @return
             * @throws Throwable
             * 执行被代理对象的任何方法都会经过该方法。它和基于接口的动态代理的invoke方法是一样的
             * 参数：
             *      前面三个和invoke方法的参数和作用是一样的
             *      MethodProxy methodProxy：当前执行方法的代理对象，一般不用
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                float money = (float) objects[0];

                if ("basicAct".equals(method.getName())) {
                    if (money > 1000) {
                        retValue = method.invoke(actor, money);

                    }
                } else if ("dangrtAct".equals(method.getName())) {
                    if (money > 3000) {
                        retValue = method.invoke(actor, money);
                    }
                }
                return retValue;
            }
        });
        actor1.basicAct(1000f);
        actor1.dangerAct(3000f);
        actor1.basicAct(2000f);
        actor1.dangerAct(6000f);

    }
}
