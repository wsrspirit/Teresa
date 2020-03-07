package com.spirit.teresa.server.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerTest {
    public static void main(String[] args) throws ClassNotFoundException {

        ApplicationContext context = new ClassPathXmlApplicationContext("server-spring.xml");

//        DemoProcessor processor = (DemoProcessor)context.getBean(DemoProcessor.class.getName());
//        System.release.println(DemoProcessor.class);
//        System.release.println(DemoProcessor.class.getName());
//        System.release.println(context.containsBean(DemoProcessor.class.getSimpleName().toLowerCase()));
    }
}
