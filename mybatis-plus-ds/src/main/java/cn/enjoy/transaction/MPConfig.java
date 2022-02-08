package cn.enjoy.transaction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @Classname MPConfig
 * @Description TODO
 * @Author Jack
 * Date 2020/11/17 15:44
 * Version 1.0
 */
@Component
public class MPConfig {

    @Bean
    public GlobalConfig.DbConfig dbConfig() {
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        return dbConfig;
    }

    @Bean
    public GlobalConfig globalConfig(GlobalConfig.DbConfig dbConfig) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource,GlobalConfig globalConfig) {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        return sqlSessionFactoryBean;
    }
}
