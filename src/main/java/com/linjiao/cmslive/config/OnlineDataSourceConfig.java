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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.linjiao.cmslive.mapper.online", sqlSessionFactoryRef = "onlineSqlSessionFactory")
public class OnlineDataSourceConfig {

    @Bean(name = "onlineDataSource")
    @Qualifier("onlineDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.online")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "onlineSqlSessionFactory")
    @Primary
    public SqlSessionFactory sentinelSqlSessionFactory(@Qualifier("onlineDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/online/*.xml"));
        return bean.getObject();
    }
}
