package cn.enjoy.properties;

import cn.enjoy.datasource.DruidConfig1;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Classname DynamicDataSourceProperties
 * @Description TODO
 * @Author Jack
 * Date 2020/11/26 14:59
 * Version 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {
    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 每一个数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
//    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;
    /**
     * aop切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * 是否使用p6spy输出，默认不输出
     */
    private Boolean p6spy = false;
    /**
     * Druid全局参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig1 druid = new DruidConfig1();
}
