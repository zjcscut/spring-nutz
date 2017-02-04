package org.throwable.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.nutz.dao.impl.NutDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author zhangjinci
 * @version 2017/1/24 17:21
 * @function
 */
@Configuration
public class DataSourceConfiguration {

    @Bean(value = "dataSource")
    @ConditionalOnMissingBean
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/nutz");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Bean(value = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dao")
    public NutDao nutDao(DataSource dataSource){
        return new NutDao(dataSource);
    }
}
