package cn.enjoy.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * @Classname DataSourceProperty
 * @Description TODO
 * @Author Jack
 * Date 2020/11/26 15:00
 * Version 1.0
 */
@Data
@Accessors(chain = true)
public class DataSourceProperty {
    /**
     * 连接池名称(只是一个名称标识)</br>
     * 默认是配置文件上的名称
     */
    private String pollName;
    /**
     * 连接池类型，如果不设置自动查找 Druid > HikariCp
     */
    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;
    /**
     * jndi数据源名称(设置即表示启用)
     */
    private String jndiName;
    /**
     * Druid参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
}
