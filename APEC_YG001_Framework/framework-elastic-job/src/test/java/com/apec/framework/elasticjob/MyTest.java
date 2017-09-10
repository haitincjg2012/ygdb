package com.apec.framework.elasticjob;

import org.apache.poi.ss.formula.functions.T;

public class MyTest
{

    public static void main(String[] args) throws Exception
    {
        Person p = new Person();
        p.setId(1);
        p.setAge(21);
        p.setName("夜华");
        System.out.println("第一位用户："+p);
        
        Person pp = Person.class.newInstance();
        pp.setId(2);
        pp.setAge(26);
        pp.setName("墨渊");
        System.out.println("第二位用户："+pp);
        
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> c = classLoader.loadClass(Person.class.getName());
        Person ppp =  (Person)c.newInstance();
        ppp.setId(3);
        ppp.setAge(23);
        ppp.setName("白浅");
        System.out.println("第三位用户："+ppp);
        
    }
    
}
