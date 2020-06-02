package com.sy.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 在这个类中来生产Student类的动态代理类
 */
public class StudentJDKProxy {

    public static void main(String[] args) {
        //创建要被代理的对象
        Student student = new Student("Tom");

        //java.lang.Proxy
        //该方法用于创建代理对象，返回创建的动态代理对象
        //参数：
        //loader-类加载器，一个能够运行，首先要进行加载，生成的代理类和原来的被代理类需要通过同一个类加载器加载
        //此处类加载器的获取通过自己编写的任何一个类都可以获取，因为它们具有相同的类加载器
        //interfaces-要生成的代理类需要实现哪些接口，类比静态代理类中，代理类需要实现和被代理类一样的接口
        //h-在其中定义需要增强的逻辑,InvocationHandler类型，是一个接口类类型，需要自己实现
        //即实现动态代理对象中的增强逻辑
        Object obj = Proxy.newProxyInstance(Student.class.getClassLoader(), Student.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //proxy:用的不多，代表生成的动态代理对象
                //method:被代理对象中的方法对象，通过这个对象可以调用原来被代理类中的方法
                //args:在调用方法的时候传递的参数，由于方法的参数个数，类型不确定，所以使用Object数组来表示
                String methodName = method.getName();
                //method.invoke():如果对Student进行代理，则相当于调用的就是Student中的原来没有增强的方法
                Object result = null;
                if ("eat".equals(methodName)||"toString".equals(methodName)) {
                    result = method.invoke(student, args);
                } else {
                    System.out.println(methodName + "方法开始执行...");
                    result = method.invoke(student, args);
                    //在动态代理中，如果去修改当期invoke方法的返回，是可以影响方法最终的返回值的
                    result = 4;
                    System.out.println(methodName + "方法结束执行...");
                }

                return result;
            }
        });
        //对象打印时调用的是对象的toString方法，toString方法的逻辑也会在代理类中被增强
        System.out.println(obj);//com.sy.bean.Student@2f0e140b
        System.out.println(student.getClass().getName());
        System.out.println(obj.getClass().getName());//com.sy.bean.$Proxy0:这个是代理对象，不是原来的Student对象
        //因为被代理类和代理类之间没有任何父子关系，所以无法造型
        //Student $student=(Student)obj;

        //因为动态代理类也已经实现了和原来被代理类一样的接口，所以可以造型为接口类型
        IStudent $student = (IStudent) obj;
        System.out.println($student.learn());
        $student.eat();
    }
}
