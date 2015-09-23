package com.ive.springtest;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class BeanTest 
{
	    static final ApplicationContext context;
	    static {
	        context = new ClassPathXmlApplicationContext("beans.xml");
	    }

	    public static void main(String[] args) throws Exception {
	      
	        final TryBean t1 = context.getBean("one", TryBean.class);
	        final TryBean t2 = context.getBean("two", TryBean.class);
	        
	        t1.hello();
	        t2.hello();
	        
	        load();
	        
	    }

		private static void load() throws Exception {
			
			ClassPathResource res = new ClassPathResource("xyz.txt");
			InputStream is = res.getInputStream();
			
			assert is != null;
			
		}
	}

