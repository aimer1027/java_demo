package org.kylin.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class HelloBean
{
    private String helloWorld ;

    public String getHelloWorld ()
    {
        return helloWorld ;
    }

    public void setHelloWorld ( String helloWorld )
    {
        this.helloWorld = helloWorld ;
    }

    public static void main ( String [] args )
    {
       /* ApplicationContext context =
                new ClassPathXmlApplicationContext("beanConfig.xml")   ;

        HelloBean helloBean = (HelloBean)context.getBean("helloBean") ;

        System.out.println (helloBean.getHelloWorld()) ;
*/

    System.out.println (   System.getProperty("java.class.path")) ;
    }
}


