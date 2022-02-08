package cn.enjoy.autoconfig;

import cn.enjoy.aop.DynamicDataSourceAnnotationAdvisor;
import cn.enjoy.creator.DynamicDataSourceCreator;
import cn.enjoy.datasource.DynamicRoutingDataSource;
import cn.enjoy.properties.DynamicDataSourceProperties;
import cn.enjoy.provider.DynamicDataSourceProvider;
import cn.enjoy.provider.YmlDynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @Classname DynamicDataSourceAutoConfiguration
 * @Description TODO
 * @Author Jack
 * Date 2020/11/26 15:23
 * Version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(DruidDynamicDataSourceConfiguration.class)
public class DynamicDataSourceAutoConfiguration {
    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DynamicDataSourceCreator dynamicDataSourceCreator) {
        return new YmlDynamicDataSourceProvider(properties, dynamicDataSourceCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setProvider(dynamicDataSourceProvider);
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor();
        advisor.setOrder(properties.getOrder());
        return advisor;
    }
}
