package org.throwable.config;


import org.nutz.mvc.NutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * @author zhangjinci
 * @version 2017/1/23 13:32
 * @function
 */
@Configuration
public class CustomWebMvcConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "springIocProvider")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringIocProvider springIocProvider(){
        return new SpringIocProvider(applicationContext);
    }

    @Bean(name = "nutFilter")
    @ConditionalOnBean(value = SpringIocProvider.class,name = "springIocProvider")
    public NutFilter nutFilter(){
        System.out.println("springIocProvider -->" + applicationContext.getBean("springIocProvider"));
        return new NutFilter();
    }

    @Bean(name = "nutFilterRegistrationBean")
    public FilterRegistrationBean filterRegistrationBean(NutFilter nutFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(nutFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("modules", "org.throwable.MainModule");
        return filterRegistrationBean;
    }

}
