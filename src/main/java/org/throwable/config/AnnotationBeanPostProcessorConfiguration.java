package org.throwable.config;

import org.nutz.ioc.loader.annotation.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zjc
 * @version 2017/1/23 1:07
 * @description 注解转换配置
 */
@Configuration
public class AnnotationBeanPostProcessorConfiguration {


	@Bean
	public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() throws ClassNotFoundException {
		AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
		Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);
		autowiredAnnotationTypes.add(Autowired.class);
		autowiredAnnotationTypes.add(Value.class);
		autowiredAnnotationTypes.add(Inject.class);   //使nutz的Inject注解具有自动注入功能
		processor.setAutowiredAnnotationTypes(autowiredAnnotationTypes);
		return processor;
	}

	@Bean
	public QualifierAnnotationAutowireCandidateResolver qualifierAnnotationAutowireCandidateResolver() {
		QualifierAnnotationAutowireCandidateResolver resolver = new QualifierAnnotationAutowireCandidateResolver();
		resolver.addQualifierType(Inject.class); //使nutz的Inject注解具有按bean名称注入功能,满足多实例注入
		return resolver;
	}


}
