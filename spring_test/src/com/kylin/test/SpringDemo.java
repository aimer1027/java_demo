package com.kylin.test;

import java.lang.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kylin.HelloBean.HelloBean ;



public class SpringDemo {
    public static void main(String[] args)
    {
       try {
          // ApplicationContext contex = new ClassPathXmlApplicationContext("beans-config.xml");

           //HelloBean hello = (HelloBean) contex.getBean("helloBean");


           //System.out.println(hello.getHelloWorld());
       }
       catch (Exception e )
       {
           e.printStackTrace();
       }


    }
}
