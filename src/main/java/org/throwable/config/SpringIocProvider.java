package org.throwable.config;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocException;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.lang.Strings;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * 简单实现Nutz.IoC-Spring桥
 * Need Spring 2.0 or later
 */

public class SpringIocProvider implements IocProvider, Ioc {

    public SpringIocProvider() {
    }

    public SpringIocProvider(ApplicationContext applicationContext) {
        SpringIocProvider.applicationContext = applicationContext;
    }


    private static ApplicationContext applicationContext;

    @Override
    public Ioc create(NutConfig config, String[] args) {
        //Ignore
        return this;
    }

    @Override
    public void depose() {
        if (applicationContext != null) {
            applicationContext.publishEvent(new ContextClosedEvent(applicationContext));
            applicationContext = null;
        }
    }

    @Override
    public <T> T get(Class<T> type, String name) {
        return applicationContext.getBean(name, type);
    }

    @Override
    public String[] getNames() {
        return applicationContext.getBeanDefinitionNames();
    }

    @Override
    public boolean has(String name) {
        return applicationContext.containsBean(name);
    }

    @Override
    public void reset() {
        applicationContext.publishEvent(new ContextRefreshedEvent(applicationContext));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> classZ) throws IocException {
        InjectName injectName = classZ.getAnnotation(InjectName.class);
        if (injectName != null && !Strings.isBlank(injectName.value()))
            return (T) applicationContext.getBean(injectName.value());
        return (T) applicationContext.getBean(applicationContext.getBeanNamesForType(classZ)[0]);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.nutz.ioc.Ioc#getNamesByType(java.lang.Class)
     */
    @Override
    public String[] getNamesByType(Class<?> klass) {

        return applicationContext.getBeanDefinitionNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.nutz.ioc.Ioc#getByType(java.lang.Class)
     */
    @Override
    public <K> K getByType(Class<K> klass) {
        return applicationContext.getBean(klass);
    }
}