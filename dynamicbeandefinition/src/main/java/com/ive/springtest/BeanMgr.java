package com.ive.springtest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class BeanMgr implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>{

	private ApplicationContext context;
    public void register(Class<?> beanClass, String beanId, String... moreBeanIds) {
        final BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getAutowireCapableBeanFactory();
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setAutowireCandidate(true);
        registry.registerBeanDefinition(beanId, beanDefinition);
        for(String each : moreBeanIds) {
	        registry.registerBeanDefinition(each, beanDefinition);
        }
    }

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
	}

	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		register(TryBean.class, "one", "two");
		register(TryBean.class, "three");
	}
}
