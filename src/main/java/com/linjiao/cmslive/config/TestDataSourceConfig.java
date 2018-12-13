package com.linjiao.cmslive.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.linjiao.cmslive.mapper.test", sqlSessionFactoryRef = "testSqlSessionFactory")
public class TestDataSourceConfig {

    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory sentinelSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/test/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "testDataSource")
    @Qualifier("testDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
