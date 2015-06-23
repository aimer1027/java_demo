package kylin.zhang.

public class SpringDemo {
    //    public static void main(String[] args) {
//        Resource rs =
//                new FileSystemResource("beans-config.xml");
//        BeanFactory factory =
//                new XmlBeanFactory(rs);
//
//        HelloBean hello =
//                (HelloBean) factory.getBean("helloBean");
//        System.out.println(hello.getHelloWord());
//    }
    public static void main(String args[]){
//        ApplicationContext context = new FileSystemXmlApplicationContext("beans-config.xml");
        ApplicationContext context = new ClassPathXmlApplicationContext("beans-config.xml");
        HelloBean hello = (HelloBean)context.getBean("helloBean");
        System.out.println(hello.getHelloWord());
    }
}