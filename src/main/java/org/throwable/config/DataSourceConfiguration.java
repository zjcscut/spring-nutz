package org.throwable.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.nutz.dao.impl.NutDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.throwable.config.prop.DruidConfigProperties;

import javax.sql.DataSource;

/**
 * @author zhangjinci
 * @version 2017/1/24 17:21
 * @function
 */
@Configuration
@EnableConfigurationProperties(value = DruidConfigProperties.class)
public class DataSourceConfiguration {

	private final DruidConfigProperties druidConfigProperties;

	public DataSourceConfiguration(DruidConfigProperties druidConfigProperties) {
		this.druidConfigProperties = druidConfigProperties;
	}

	@Bean(value = "dataSource")
    @ConditionalOnMissingBean
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(druidConfigProperties.getUrl());
        dataSource.setUsername(druidConfigProperties.getUsername());
        dataSource.setPassword(druidConfigProperties.getPassword());
        dataSource.setDriverClassName(druidConfigProperties.getDriverClassName());
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
